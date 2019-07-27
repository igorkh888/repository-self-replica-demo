package com.igorkhromov.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

public class ObjectMapperUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public static <T> T json2Object(String jsonString, Class<T> clazz)
            throws IOException {
        return OBJECT_MAPPER.readValue(jsonString, clazz);
    }
}
