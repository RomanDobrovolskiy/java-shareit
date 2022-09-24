package ru.practicum.shareit.exception;

public class UserIsNotOwnerException extends IllegalArgumentException {
    public UserIsNotOwnerException(String s) {
        super(s);
    }
}
