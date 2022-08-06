package ru.practicum.shareit.item.dao;

import lombok.Getter;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

public class InMemoryItemRepository implements ItemRepository {

    @Getter
    private Map<Integer, Item> items = new HashMap<>();

}
