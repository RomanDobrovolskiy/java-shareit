package ru.practicum.shareit.exception;

public class UserIdNotValidException extends RuntimeException {
    public UserIdNotValidException(String message) {
        super(message);
    }
}
