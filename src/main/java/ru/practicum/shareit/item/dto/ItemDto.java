package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * // TODO .
 */
@Data
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private int requestId;

    public ItemDto(int id, String name, String description, Boolean available, int requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}
