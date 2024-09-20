package ru.practicum.service;

import java.util.Objects;
import java.util.function.Consumer;

public class ServiceUtils {
    private ServiceUtils() {
    }

    public static <T, E> void updateEntity(T entity, E param, Consumer<T> consumer) {
        if (Objects.nonNull(param)) {
            consumer.accept(entity);
        }
    }
}
