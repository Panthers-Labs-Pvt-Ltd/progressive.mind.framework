package com.progressive.minds.chimera.common.kvstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progressive.minds.chimera.common.tags.annotation.Private;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Serializer used to translate between app-defined types and the disk-based stores.
 *
 * <p>
 * The serializer is based on Jackson, so values are written as JSON. It also allows "naked strings"
 * and integers to be written as values directly, which will be written as UTF-8 strings.
 * </p>
 */
@Private
public class KVStoreSerializer {

    /**
     * Object mapper used to process app-specific types. If an application requires a specific
     * configuration of the mapper, it can subclass this serializer and add custom configuration
     * to this object.
     */
    protected final ObjectMapper mapper;

    public KVStoreSerializer() {
        this.mapper = new ObjectMapper();
    }

    public byte[] serialize(Object o) throws Exception {
        if (o instanceof String str) {
            return str.getBytes(UTF_8);
        } else {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (GZIPOutputStream out = new GZIPOutputStream(bytes)) {
                mapper.writeValue(out, o);
            }
            return bytes.toByteArray();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] data, Class<T> klass) throws Exception {
        if (klass.equals(String.class)) {
            return (T) new String(data, UTF_8);
        } else {
            try (GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(data))) {
                return mapper.readValue(in, klass);
            }
        }
    }

    final byte[] serialize(long value) {
        return String.valueOf(value).getBytes(UTF_8);
    }

    final long deserializeLong(byte[] data) {
        return Long.parseLong(new String(data, UTF_8));
    }

}
