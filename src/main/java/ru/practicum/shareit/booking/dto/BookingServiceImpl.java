package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.exception.IncorrectStateException;
import ru.practicum.shareit.exception.ItemAlreadyBookedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking addBooking(Booking booking, Long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Предмет с id %d не найден", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Предмет с id %d не найден", itemId)));
        if (!item.getAvailable()) {
            throw new ItemAlreadyBookedException("Предмет уже забронирован");
        }
        if (item.getOwnerId().equals(userId)) {
            throw new NotFoundException("Владелец не может забронировать предмет");
        }
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approve(Long userId, Long bookingId, Boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с id %d не найдено", bookingId)));
        if (!booking.getItem().getOwnerId().equals(userId)) {
            throw new NotOwnerException("Только владелец может подтвердить бронирование");
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ItemAlreadyBookedException("Статус можно изменить только для предметов с ожиданием");
        }
        booking.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с id %d не найдено", bookingId)));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwnerId().equals(userId)) {
            return booking;
        } else {
            throw new NotOwnerException("Только владелец может посмотреть информацию о бронировании");
        }
    }

    @Override
    public List<Booking> getAllUsersBookings(Long userId, State state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc
                        (userId, now, now);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(userId, now);
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(userId,
                        now);
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStartIsAfterAndStatusIsOrderByStartDesc(userId,
                        now, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(userId, Status.REJECTED);
            default:
                throw new IncorrectStateException("Неизвестный статус");
        }
    }

    @Override
    public List<Booking> getAllUsersItemsBookings(Long userId, State state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                return bookingRepository.getAllUsersItemsBookings(userId);
            case CURRENT:
                return bookingRepository.getCurrentUsersItemsBookings(userId, now);
            case PAST:
                return bookingRepository.getPastUsersItemsBookings(userId, now);
            case FUTURE:
                return bookingRepository.getFutureUsersItemsBookings(userId, now, Status.APPROVED);
            case WAITING:
                return bookingRepository.getWaitingUsersItemsBookings(userId, now, Status.WAITING);
            case REJECTED:
                return bookingRepository.getRejectedUsersItemsBookings(userId, Status.REJECTED);
            default:
                throw new IncorrectStateException("Неизвестный статус");
        }
    }
}
