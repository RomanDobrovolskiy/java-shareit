package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.UserIdNotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryItemRepository implements ItemRepository {

    private Map<Integer, Item> items = new HashMap<>();

    private int id = 1;

    public Map<Integer, Item> getItems() {
        return items;
    }

    @Override
    public ItemDto create(ItemDto itemDto, User user) {
        items.put(id, ItemMapper.toItem(itemDto, id, user));
        id++;
        return ItemMapper.toItemDto(items.get(id - 1));
    }

    @Override
    public ItemDto update(Item item, int userId) {
        for (int i : items.keySet()) {
            if (items.get(i).getOwner().getId() == userId) {
                items.get(i).setName(item.getName());
                items.get(i).setDescription(item.getDescription());
                return ItemMapper.toItemDto(item);
            }
        }
        log.info("Передан неверный id");
        return null;
    }


    @Override
    public ItemDto updateById(Item item, int userId, int itemId) {
        Item itemById = items.get(itemId);
        if (itemById.getOwner().getId() != userId) {
            throw new UserIdNotValidException("Неверный идентификатор пользователя.");
        }

        if (!items.containsKey(itemId)) {
            throw new NullPointerException("Предмет с id " + itemId + " не найден.");
        }

        itemById
                .setName(item.getName() == null ?
                        itemById.getName() :
                        item.getName());
        itemById
                .setDescription(item.getDescription() == null ?
                        itemById.getDescription() :
                        item.getDescription());
        itemById
                .setAvailable(item.getAvailable() == null ?
                        itemById.getAvailable() :
                        item.getAvailable());

        return ItemMapper.toItemDto(itemById);
    }

    @Override
    public ItemDto getById(int id) {
        return ItemMapper.toItemDto(items.get(id));
    }

    @Override
    public List<ItemDto> getAll(int userId) {
        List<ItemDto> userItem = new ArrayList<>();
        for (int i : items.keySet()) {
            if (items.get(i).getOwner().getId() == userId) {
                userItem.add(ItemMapper.toItemDto(items.get(i)));
            }
        }
        return userItem;
    }
}
