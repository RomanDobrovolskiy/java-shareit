package ru.practicum.shareit.exception;

public class ItemOwnerDidNotSetException extends IllegalArgumentException{
    public ItemOwnerDidNotSetException(String s) {
        super(s);
    }
}
