package ru.practicum.shareit.user.model;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }

    public static Collection<UserDto> toDtoList(Collection<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
