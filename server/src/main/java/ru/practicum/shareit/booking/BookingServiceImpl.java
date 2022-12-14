package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking createBooking(@Valid Booking booking, Long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", itemId)));
        if (!item.getAvailable()) {
            throw new ItemIsBookedException("Item is already booked");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Owner can't book his item");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())
                || booking.getEnd().isBefore(LocalDateTime.now())
                || booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException();
        }
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveBooking(Long userId, Long bookingId, Boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id %d not found", bookingId)));
        if (!Objects.equals(userId, booking.getItem().getOwner().getId())) {
            throw new UserIsNotOwnerException("Only owner of the item can approve booking");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new IncorrectStateException("You can change status only for waiting bookings");
        }
        if (isApproved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id %d not found", bookingId)));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return booking;
        } else {
            throw new UserIsNotOwnerException("Only owner of the item or booker can view information about booking");
        }
    }

    @Override
    public List<Booking> getAllUserBookings(Long userId, BookingState state, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        validationPage(from, size);
        LocalDateTime now = LocalDateTime.now();
        Pageable page = PageRequest.of(from / size, size);
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page);
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(userId, now,
                        now, page);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(userId, now, page);
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(userId, now, page);
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStartIsAfterAndStatusIsOrderByStartDesc(userId,
                        now, BookingStatus.WAITING, page);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(userId,
                        BookingStatus.REJECTED, page);
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }

    @Override
    public List<Booking> getAllUserItemsBookings(Long userId, BookingState state, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        validationPage(from, size);
        LocalDateTime now = LocalDateTime.now();
        Pageable page = PageRequest.of(from / size, size);
        switch (state) {
            case ALL:
                return bookingRepository.getAllUsersItemsBookings(userId, page);
            case CURRENT:
                return bookingRepository.getCurrentUsersItemsBookings(userId, now, page);
            case PAST:
                return bookingRepository.getPastUsersItemsBookings(userId, now, page);
            case FUTURE:
                return bookingRepository.getFutureUsersItemsBookings(userId, now, BookingStatus.APPROVED, page);
            case WAITING:
                return bookingRepository.getWaitingUsersItemsBookings(userId, now, BookingStatus.WAITING, page);
            case REJECTED:
                return bookingRepository.getRejectedUsersItemsBookings(userId, BookingStatus.REJECTED, page);
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }

    public void validationPage(int from, int size) {
        if (from < 0) {
            throw new javax.validation.ValidationException("From cannot be less that 0");
        }
        if (size <= 0) {
            throw new javax.validation.ValidationException("Size cannot be less or equals 0");
        }
    }
}

