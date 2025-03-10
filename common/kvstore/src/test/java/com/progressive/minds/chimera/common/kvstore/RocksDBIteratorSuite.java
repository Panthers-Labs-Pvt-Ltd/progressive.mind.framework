package com.progressive.minds.chimera.common.kvstore;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;

public class RocksDBIteratorSuite extends DBIteratorSuite {

    private static File dbpath;
    private static RocksDB db;

    @AfterAll
    public static void cleanup() throws Exception {
        if (db != null) {
            db.close();
        }
        if (dbpath != null) {
            FileUtils.deleteQuietly(dbpath);
        }
    }

    @Override
    protected KVStore createStore() throws Exception {
        dbpath = File.createTempFile("test.", ".rdb");
        dbpath.delete();
        db = new RocksDB(dbpath);
        return db;
    }

}
