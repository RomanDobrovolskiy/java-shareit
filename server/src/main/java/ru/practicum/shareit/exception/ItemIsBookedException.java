package ru.practicum.shareit.exception;

public class ItemIsBookedException extends RuntimeException {
    public ItemIsBookedException(String message) {
        super(message);
    }
}
