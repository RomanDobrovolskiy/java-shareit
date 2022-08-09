package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserIdNotValidException;
import ru.practicum.shareit.item.dao.InMemoryItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Service
@Slf4j
public class ItemService {
    private InMemoryItemRepository itemRepository = new InMemoryItemRepository();


    public ItemDto create(ItemDto itemDto, User user) {
        return itemRepository.create(itemDto, user);
    }


    public ItemDto update(Item item, int userId) {
        return itemRepository.update(item, userId);
    }


    public ItemDto updateById(Item item, int userId, int itemId) {
        return itemRepository.updateById(item, userId, itemId);
    }


    public ItemDto getById(int id) {
        return itemRepository.getById(id);
    }


    public List<ItemDto> getAll(int userId) {
        return itemRepository.getAll(userId);
    }

    public List<ItemDto> search(String text) {
        List<ItemDto> searchItems = new ArrayList<>();

        if (Objects.equals(text, "") || text == null) {
            return searchItems;
        }

        for (int i : itemRepository.getItems().keySet()) {
            if (itemRepository.getItems().get(i).getName().toLowerCase().contains(text.toLowerCase()) ||
                    itemRepository.getItems().get(i).getDescription().toLowerCase().contains(text.toLowerCase()) &&
                            itemRepository.getItems().get(i).getAvailable()) {
                searchItems.add(ItemMapper.toItemDto(itemRepository.getItems().get(i)));
            }
        }

        return searchItems;
    }
}
