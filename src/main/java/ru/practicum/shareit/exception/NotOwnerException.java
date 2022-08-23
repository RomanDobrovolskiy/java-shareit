package ru.practicum.shareit.exception;

public class NotOwnerException extends IllegalArgumentException{
    public NotOwnerException(String s) {
        super(s);
    }
}
