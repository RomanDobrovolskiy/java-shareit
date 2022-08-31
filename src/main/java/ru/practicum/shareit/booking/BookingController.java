package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        Booking booking = BookingMapper.fromInputDto(bookingRequestDto);
        Long itemId = bookingRequestDto.getItemId();
        return BookingMapper.toOutputDto(bookingService.addBooking(booking, userId, itemId));
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(@RequestHeader(USER_ID_HEADER) Long userId,
                                      @PathVariable Long bookingId,
                                      @RequestParam Boolean approved) {
        return BookingMapper.toOutputDto(bookingService.approve(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @PathVariable Long bookingId) {
        return BookingMapper.toOutputDto(bookingService.getBooking(userId, bookingId));
    }

    @GetMapping
    public List<BookingResponseDto> getAllUsersBookings(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") State state) { // при передаче некорректного параметра state идёт
                                                        // обработка MethodArgumentTypeMismatchException в ErrorHandler
        return BookingMapper.toOutputDtoList(bookingService.getAllUsersBookings(userId, state));
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllUsersItemsBookings(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") State state) { // при передаче некорректного параметра state идёт
                                                        // обработка MethodArgumentTypeMismatchException в ErrorHandler
        return BookingMapper.toOutputDtoList(bookingService.getAllUsersItemsBookings(userId, state));
    }
}
