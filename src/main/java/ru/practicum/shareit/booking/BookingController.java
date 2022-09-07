package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;


    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @Valid @RequestBody BookingRequestDto requestBookingDto) {
        Booking booking = BookingMapper.fromInputDto(requestBookingDto);
        Long itemId = requestBookingDto.getItemId();
        return BookingMapper.toOutputDto(bookingService.createBooking(booking, userId, itemId));
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                           @PathVariable Long bookingId,
                                           @RequestParam Boolean approved) {
        return BookingMapper.toOutputDto(bookingService.approveBooking(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(USER_ID_HEADER) Long userId,
                                           @PathVariable Long bookingId) {
        return BookingMapper.toOutputDto(bookingService.getBookingById(userId, bookingId));
    }

    @GetMapping
    public List<BookingResponseDto> getAllUserBookings(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") State state,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return BookingMapper.toOutputDtoList(bookingService.getAllUserBookings(userId, state, from, size));
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllUserItemsBookings(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") State state,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return BookingMapper.toOutputDtoList(bookingService.getAllUserItemsBookings(userId, state, from, size));
    }
}
