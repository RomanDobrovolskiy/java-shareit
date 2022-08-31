package ru.practicum.shareit.exception;

public class ItemOwnerIsNotSetException extends IllegalArgumentException {
    public ItemOwnerIsNotSetException(String s) {
        super(s);
    }
}
