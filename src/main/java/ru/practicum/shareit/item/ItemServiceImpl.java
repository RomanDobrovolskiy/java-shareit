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
public class ItemServiceImpl implements ItemService {

    private int id = 1;
    private InMemoryItemRepository itemRepository = new InMemoryItemRepository();

    @Override
    public ItemDto create(ItemDto itemDto, User user) {
        itemRepository.getItems().put(id, ItemMapper.toItem(itemDto, id, user));
        id++;
        return ItemMapper.toItemDto(itemRepository.getItems().get(id - 1));
    }

    @Override
    public ItemDto update(Item item, int userId) {
        for (int i : itemRepository.getItems().keySet()) {
            if (itemRepository.getItems().get(i).getOwner().getId() == userId) {
                itemRepository.getItems().get(i).setName(item.getName());
                itemRepository.getItems().get(i).setDescription(item.getDescription());
                return ItemMapper.toItemDto(item);
            }
        }
        log.info("Передан неверный id");
        return null;
    }


    @Override
    public ItemDto updateById(Item item, int userId, int itemId) {
        Item itemById = itemRepository.getItems().get(itemId);
        if (itemById.getOwner().getId() != userId) {
            throw new UserIdNotValidException("Неверный идентификатор пользователя.");
        }

        if (!itemRepository.getItems().containsKey(itemId)) {
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
        return ItemMapper.toItemDto(itemRepository.getItems().get(id));
    }

    @Override
    public List<ItemDto> getAll(int userId) {
        List<ItemDto> userItem = new ArrayList<>();
        for (int i : itemRepository.getItems().keySet()) {
            if (itemRepository.getItems().get(i).getOwner().getId() == userId) {
                userItem.add(ItemMapper.toItemDto(itemRepository.getItems().get(i)));
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
