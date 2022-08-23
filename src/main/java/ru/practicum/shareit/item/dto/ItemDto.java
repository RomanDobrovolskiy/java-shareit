package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.ForItemBookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * // TODO .
 */
@Getter
@Setter
@Builder
public class ItemDto {
    private Long id;
    @NotBlank (message = "Имя не может быть пустым")
    private String name;
    @NotBlank (message = "Описание не может быть пустым")
    private String description;
    @NotNull
    private Boolean available;
    private ForItemBookingDto lastBooking;
    private ForItemBookingDto nextBooking;
    private List<CommentDto> comments;
}
