package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {

    public ItemDto create(ItemDto itemDto, User user);

    public ItemDto update(Item item, int userId);

    public ItemDto updateById(Item item, int userId, int itemId);

    public ItemDto getById(int id);

    public List<ItemDto> getAll(int userId);
}
