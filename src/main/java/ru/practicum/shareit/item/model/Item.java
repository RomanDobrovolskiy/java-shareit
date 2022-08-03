package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * // TODO .
 */
@Data
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;

    public Item(int id, String name, String description, Boolean available, User user, int requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = user;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestId);
        this.request = itemRequest;
    }
}
