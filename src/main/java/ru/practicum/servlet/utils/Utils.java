package ru.practicum.servlet.utils;

import jakarta.servlet.http.HttpServletResponse;
import ru.practicum.exception.ApiError;

import java.io.IOException;

public class Utils {
    private Utils() {
    }

    public static long parsePathVariableToId(String pathVar) {
        return Long.parseLong(pathVar.replace("/", ""));
    }

    public static void setErrorResponse(HttpServletResponse resp, int code, String msg) throws IOException {
        settingResponse(resp);
        resp.setStatus(code);
        resp.getWriter().println(JsonMapper.parseToJson(ApiError.create(msg, code)));
    }

    public static void settingResponse(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }
}
