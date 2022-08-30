package ru.practicum.shareit.exception;

public class UserHasNotBookedItem extends RuntimeException {
    public UserHasNotBookedItem(String message) {
        super(message);
    }
}
