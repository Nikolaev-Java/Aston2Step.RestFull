package ru.practicum.service;

import java.util.Objects;
import java.util.function.Consumer;

public class ServiceUtils {
    private ServiceUtils() {
    }

    public static <E> void updateEntity(E param, Consumer<E> consumer) {
        if (Objects.nonNull(param)) {
            consumer.accept(param);
        }
    }
}
