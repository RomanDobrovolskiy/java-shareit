package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    public static Booking fromInputDto(BookingRequestDto bookingRequestDto) {
        return Booking.builder()
                .start(bookingRequestDto.getStart())
                .end(bookingRequestDto.getEnd())
                .build();
    }

    public static BookingResponseDto toOutputDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toDto(booking.getItem()))
                .booker(UserMapper.toDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public static ForItemBookingDto toItemBookingDto(Booking booking) {
        return new ForItemBookingDto(booking.getId(), booking.getBooker().getId(),
                booking.getStart(), booking.getEnd());
    }

    public static List<BookingResponseDto> toOutputDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toOutputDto)
                .collect(Collectors.toList());
    }
}
