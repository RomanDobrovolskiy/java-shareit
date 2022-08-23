package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.UserDoesNotHaveBookedItems;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemDto addItem(ItemDto itemDto, Long userId) {
        userService.getUserById(userId); // проверка владельца
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwnerId(userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Предмет с id %d не найден", itemId)));
        if (item.getOwnerId().equals(userId)) {
            setLastAndNextBooking(item);
        }
        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setComments(CommentMapper.toDtoList(commentRepository.findAllByItemId(item.getId())));
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllUsersItems(Long userId) {
        List<ItemDto> items = itemRepository.getAllByOwnerId(userId).stream()
                .map(this::setLastAndNextBooking)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        for (ItemDto itemDto : items) {
            itemDto.setComments(CommentMapper.toDtoList(commentRepository.findAllByItemId(itemDto.getId())));
        }
        return items;
    }

    @Override
    public ItemDto editItem(ItemDto itemDto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Предмет с id %d не найден", itemId)));
        if (item.getOwnerId().equals(userId)) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            return ItemMapper.toItemDto(itemRepository.save(item));
        } else {
            throw new NotOwnerException(String.format("Пользователь с id %d не является владельцем предмета", userId));
        }
    }

    public List<ItemDto> searchAvailableItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.searchAvailableItems(text);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }


    private Item setLastAndNextBooking(Item item) {
        LocalDateTime now = LocalDateTime.now();
        bookingRepository.getLastItemBooking(item.getId(), now)
                .ifPresent(booking -> item.setLastBooking(BookingMapper.toItemBookingDto(booking)));
        bookingRepository.getNextItemBooking(item.getId(), now)
                .ifPresent(booking -> item.setNextBooking(BookingMapper.toItemBookingDto(booking)));
        return item;
    }

    @Override
    public Comment addComment(Comment comment, Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Предмет с id %d не найден", itemId)));
        User user = userService.getUserById(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository
                .findAllByItemAndBookerIdAndStatusIsAndEndIsBefore(item, userId, Status.APPROVED, now);
        if (bookings.isEmpty()) {
            throw new UserDoesNotHaveBookedItems("Нужно завершить хотя бы одно бронирование, чтобы оставлять комментарии");
        }
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(now);
        return commentRepository.save(comment);
    }
}
