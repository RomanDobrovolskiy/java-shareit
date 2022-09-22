package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleNotFound(final ru.practicum.shareit.exceptions.NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleItemOwnerIsNotSet(final ru.practicum.shareit.exceptions.ItemOwnerIsNotSetException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUserIsNotOwner(final ru.practicum.shareit.exceptions.UserIsNotOwnerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleDuplicateEmail(final ru.practicum.shareit.exceptions.DuplicateEmailException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleItemIsBooked(final ru.practicum.shareit.exceptions.ItemIsBookedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUserHasNotBookedItem(final ru.practicum.shareit.exceptions.UserHasNotBookedItem e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleInvalidValidationPageParams(final ConstraintViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleWrongArgument(final ru.practicum.shareit.exceptions.IncorrectStateException e) {
        Map<String,String> resp = new HashMap<>();
        resp.put("error", e.getMessage());
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
}
