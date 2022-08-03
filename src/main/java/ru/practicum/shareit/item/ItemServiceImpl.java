package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserIdNotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    private int id = 1;
    private Map<Integer, Item> items = new HashMap<>();

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
        return null;
    }


    @Override
    public ItemDto updateById(Item item, int userId, int itemId) {
        if (items.get(itemId).getOwner().getId() != userId) {
            throw new UserIdNotValidException("Неверный идентификатор пользователя.");
        }

        if (!items.containsKey(itemId)) {
            throw new NullPointerException("Предмет с id " + itemId + " не найден.");
        }

        items.get(itemId)
                .setName(item.getName() == null ?
                        items.get(itemId).getName() :
                        item.getName());
        items.get(itemId)
                .setDescription(item.getDescription() == null ?
                        items.get(itemId).getDescription() :
                        item.getDescription());
        items.get(itemId)
                .setAvailable(item.getAvailable() == null ?
                        items.get(itemId).getAvailable() :
                        item.getAvailable());

        return ItemMapper.toItemDto(items.get(itemId));
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

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> searchItems = new ArrayList<>();

        if (Objects.equals(text, "") || text == null) {
            return searchItems;
        }

        for (int i : items.keySet()) {
            if (items.get(i).getName().toLowerCase().contains(text.toLowerCase()) ||
                    items.get(i).getDescription().toLowerCase().contains(text.toLowerCase()) &&
                            items.get(i).getAvailable()) {
                searchItems.add(ItemMapper.toItemDto(items.get(i)));
            }
        }

        return searchItems;
    }
}
