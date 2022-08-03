package ru.practicum.shareit.exceptions;

public class UserIdNotValidException extends RuntimeException {
    public UserIdNotValidException(String message) {
        super(message);
    }
}
