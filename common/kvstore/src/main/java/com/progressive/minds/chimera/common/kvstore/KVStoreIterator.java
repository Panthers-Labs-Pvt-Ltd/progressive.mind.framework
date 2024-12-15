package com.progressive.minds.chimera.common.kvstore;

import com.progressive.minds.chimera.common.tags.annotation.Private;

import java.io.Closeable;
import java.util.Iterator;
import java.util.List;

/**
 * An iterator for KVStore.
 *
 * <p>
 * Iterators may keep references to resources that need to be closed. It's recommended that users
 * explicitly close iterators after they're used.
 * </p>
 */
@Private
public interface KVStoreIterator<T> extends Iterator<T>, Closeable {

    /**
     * Retrieve multiple elements from the store.
     *
     * @param max Maximum number of elements to retrieve.
     */
    List<T> next(int max);

    /**
     * Skip in the iterator.
     *
     * @return Whether there are items left after skipping.
     */
    boolean skip(long n);

}
