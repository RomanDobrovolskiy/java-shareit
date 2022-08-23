package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    Long id;
    @NotBlank (message = "Имя не может быть пустым")
    String name;
    @Email (message = "Неверный формат e-mail")
    @NotBlank (message = "E-mail не может быть пустым")
    String email;
}

