package ru.practicum.servlet.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JsonMapper {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private JsonMapper() {
    }

    public static <T> T parseRequestBody(HttpServletRequest req, Class<T> clazz) throws IOException {
        return mapper.readValue(req.getReader(), clazz);
    }

    public static <T> String parseToJson(T obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }
}
