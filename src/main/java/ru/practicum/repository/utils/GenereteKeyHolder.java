package ru.practicum.repository.utils;

public class GenereteKeyHolder implements KeyHolder {
    private Number key;

    @Override
    public Number getKey() {
        return key;
    }

    @Override
    public void setKey(Number key) {
        this.key = key;
    }
}
