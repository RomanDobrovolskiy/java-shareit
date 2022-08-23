package ru.practicum.shareit.exception;

public class UserDoesNotHaveBookedItems extends RuntimeException{
    public UserDoesNotHaveBookedItems(String message) {
        super(message);
    }
}
