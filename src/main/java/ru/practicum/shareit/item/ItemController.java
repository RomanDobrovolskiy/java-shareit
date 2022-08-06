package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserIdNotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    private void ItemValidation(ItemDto itemDto, int userId) { // добавили валидационный метод
        if (!userService.getAll().containsKey((userId))) {
            throw new UserIdNotValidException("Пользователь с id " + userId + " не найден");
        }

        if (itemDto.getName() == "" || itemDto.getName() == null ||
                itemDto.getDescription() == null || itemDto.getDescription() == "") {
            throw new NotFoundException("Не указано название предмета");
        }

        if (itemDto.getAvailable() == null) {
            throw new NotFoundException("Укажите доступность предмета");
        }
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        ItemValidation(itemDto, userId);
        return itemService.create(itemDto, UserMapper.toUser(userService.getUserById((userId))));
    }

    @PatchMapping
    public ItemDto update(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.update(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateById(@RequestBody Item item,
                              @RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId) {

        return itemService.updateById(item, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable int itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
