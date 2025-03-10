package com.progressive.minds.chimera.common.kvstore;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.rocksdb.RocksIterator;

class RocksDBIterator<T> implements KVStoreIterator<T> {

    private static final Cleaner CLEANER = Cleaner.create();
    private final RocksDB db;
    private final boolean ascending;
    private final RocksIterator rocksIterator;
    private final Class<T> type;
    private final RocksDBTypeInfo ti;
    private final RocksDBTypeInfo.Index index;
    private final byte[] indexKeyPrefix;
    private final byte[] end;
    private final long max;
    private final Cleaner.Cleanable cleanable;
    private final RocksDBIterator.ResourceCleaner resourceCleaner;

    private boolean checkedNext;
    private byte[] next;
    private boolean closed;
    private long count;

    RocksDBIterator(Class<T> type, RocksDB db, KVStoreView<T> params) throws Exception {
        this.db = db;
        this.ascending = params.ascending;
        this.rocksIterator = db.db().newIterator();
        this.type = type;
        this.ti = db.getTypeInfo(type);
        this.index = ti.index(params.index);
        this.max = params.max;
        this.resourceCleaner = new RocksDBIterator.ResourceCleaner(rocksIterator, db);
        this.cleanable = CLEANER.register(this, resourceCleaner);

        Preconditions.checkArgument(!index.isChild() || params.parent != null,
                "Cannot iterate over child index %s without parent value.", params.index);
        byte[] parent = index.isChild() ? index.parent().childPrefix(params.parent) : null;

        this.indexKeyPrefix = index.keyPrefix(parent);
        rocksIterator.seek(getFirstKey(params, parent));
        this.end = getEndKey(params, parent);

        if (params.skip > 0) {
            skip(params.skip);
        }
    }

    private byte[] getEndKey(KVStoreView<T> params, byte[] parent) {
        byte[] end = null;
        if (ascending) {
            end = params.last != null ? index.end(parent, params.last) : index.end(parent);
        } else {
            if (params.last != null) {
                end = index.start(parent, params.last);
            }
            if (!rocksIterator.isValid()) {
                throw new NoSuchElementException();
            }
            if (compare(rocksIterator.key(), indexKeyPrefix) > 0) {
                rocksIterator.prev();
            }
        }
        return end;
    }

    private byte[] getFirstKey(KVStoreView<T> params, byte[] parent) {
        if (params.first != null) {
            return ascending ? index.start(parent, params.first) : index.end(parent, params.first);
        } else {
            return ascending ? index.keyPrefix(parent) : index.end(parent);
        }
    }

    @Override
    public boolean hasNext() {
        if (!checkedNext && !closed) {
            next = loadNext();
            checkedNext = true;
        }
        if (!closed && next == null) {
            try {
                close();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return next != null;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        checkedNext = false;

        try {
            T ret;
            if (index == null || index.isCopy()) {
                ret = db.serializer.deserialize(next, type);
            } else {
                byte[] key = ti.buildKey(false, ti.naturalIndex().keyPrefix(null), next);
                ret = db.get(key, type);
            }
            next = null;
            return ret;
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> next(int max) {
        List<T> list = new ArrayList<>(max);
        while (hasNext() && list.size() < max) {
            list.add(next());
        }
        return list;
    }

    @Override
    public boolean skip(long n) {
        if (closed) return false;

        long skipped = 0;
        while (skipped < n) {
            if (next != null) {
                checkedNext = false;
                next = null;
                skipped++;
                continue;
            }

            if (!rocksIterator.isValid()) {
                checkedNext = true;
                return false;
            }

            if (!isEndMarker(rocksIterator.key())) {
                skipped++;
            }
            if (ascending) {
                rocksIterator.next();
            } else {
                rocksIterator.prev();
            }
        }

        return hasNext();
    }

    @Override
    public synchronized void close() throws IOException {
        db.notifyIteratorClosed(rocksIterator);
        if (!closed) {
            try {
                rocksIterator.close();
            } finally {
                closed = true;
                next = null;
                cancelResourceClean();
            }
        }
    }

    /**
     * Prevent ResourceCleaner from actually releasing resources after close it.
     */
    private void cancelResourceClean() {
        this.resourceCleaner.setStartedToFalse();
        this.cleanable.clean();
    }

    @VisibleForTesting
    ResourceCleaner getResourceCleaner() {
        return resourceCleaner;
    }

    RocksIterator internalIterator() {
        return rocksIterator;
    }

    private byte[] loadNext() {
        if (count >= max) {
            return null;
        }

        while (rocksIterator.isValid()) {
            Map.Entry<byte[], byte[]> nextEntry = new AbstractMap.SimpleEntry<>(rocksIterator.key(), rocksIterator.value());

            byte[] nextKey = nextEntry.getKey();
            // Next key is not part of the index, stop.
            if (!startsWith(nextKey, indexKeyPrefix)) {
                return null;
            }

            // If the next key is an end marker, then skip it.
            if (isEndMarker(nextKey)) {
                moveIterator();
                continue;
            }

            // If there's a known end key and iteration has gone past it, stop.
            if (end != null) {
                int comp = compare(nextKey, end) * (ascending ? 1 : -1);
                if (comp > 0) {
                    return null;
                }
            }

            count++;
            moveIterator();

            // Next element is part of the iteration, return it.
            return nextEntry.getValue();
        }
        return null;
    }

    private void moveIterator() {
        if (ascending) {
            rocksIterator.next();
        } else {
            rocksIterator.prev();
        }
    }

    @VisibleForTesting
    static boolean startsWith(byte[] key, byte[] prefix) {
        if (key.length < prefix.length) {
            return false;
        }

        for (int i = 0; i < prefix.length; i++) {
            if (key[i] != prefix[i]) {
                return false;
            }
        }

        return true;
    }

    private boolean isEndMarker(byte[] key) {
        return (key.length > 2 &&
                key[key.length - 2] == RocksDBTypeInfo.KEY_SEPARATOR &&
                key[key.length - 1] == RocksDBTypeInfo.END_MARKER[0]);
    }

    static int compare(byte[] a, byte[] b) {
        int diff = 0;
        int minLen = Math.min(a.length, b.length);
        for (int i = 0; i < minLen; i++) {
            diff += (a[i] - b[i]);
            if (diff != 0) {
                return diff;
            }
        }

        return a.length - b.length;
    }

    static class ResourceCleaner implements Runnable {

        private final RocksIterator rocksIterator;
        private final RocksDB rocksDB;
        private final AtomicBoolean started = new AtomicBoolean(true);

        ResourceCleaner(RocksIterator rocksIterator, RocksDB rocksDB) {
            this.rocksIterator = rocksIterator;
            this.rocksDB = rocksDB;
        }

        @Override
        public void run() {
            if (started.compareAndSet(true, false)) {
                rocksDB.closeIterator(rocksIterator);
            }
        }

        void setStartedToFalse() {
            started.set(false);
        }

        @VisibleForTesting
        boolean isCompleted() {
            return !started.get();
        }
    }
}
