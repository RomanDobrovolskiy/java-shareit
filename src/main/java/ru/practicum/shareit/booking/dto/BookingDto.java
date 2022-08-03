package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * // TODO .
 */
@Data
public class BookingDto {
    private LocalDate start;
    private LocalDate end;
    private int itemId;

    public BookingDto(LocalDate start, LocalDate end, int itemId) {
        this.start = start;
        this.end = end;
        this.itemId = itemId;
    }
}