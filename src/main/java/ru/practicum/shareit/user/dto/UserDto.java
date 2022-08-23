package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Email(message = "Неверный формат почты")
    @NotBlank (message = "Email не может быть пустым")
    private String email;
}
