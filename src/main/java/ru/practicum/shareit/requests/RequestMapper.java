package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

public class RequestMapper {
    public static ItemRequestDto toRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getDescription(), itemRequest.getCreated());
    }
}
