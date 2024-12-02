package com.progressive.minds.chimera.foundational.chimeraUtils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ChimeraUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts the given block of code to a JSON string using a Jackson JsonGenerator.
     *
     * @param block The block of code that writes JSON using the generator.
     * @return A JSON string produced by the block.
     * @throws IOException If an I/O error occurs while writing the JSON.
     */
    public static String toJsonString(JsonGeneratorBlock block) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            JsonGenerator generator = mapper.createGenerator(baos, JsonEncoding.UTF8);
            block.write(generator);
            generator.close();
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Returns the class loader associated with this class.
     *
     * @return The class loader of the ChimeraUtils class.
     */
    public static ClassLoader getSparkClassLoader() {
        return ChimeraUtils.class.getClassLoader();
    }

    /**
     * Functional interface used for passing the JsonGenerator to a block of code.
     */
    @FunctionalInterface
    public interface JsonGeneratorBlock {
        void write(JsonGenerator generator) throws IOException;
    }
}
