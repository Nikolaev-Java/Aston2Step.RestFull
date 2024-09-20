package ru.practicum.servlet.utils;

public enum ErrorMessage {
    EMPTY_BODY("Invalid request. Body is empty."),
    EMPTY_PATH_VARIABLE("Invalid request. PathVariable is empty.");

    private final String message;

    ErrorMessage(String s) {
        this.message = s;
    }

    public String msg() {
        return message;
    }
}
