package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    public UserDto createUser(UserDto userDto);

    public UserDto getUserById(int userId);

    public List<UserDto> getUsers();

    public UserDto updateUser(UserDto userDto);

    public UserDto updateById(int id, UserDto userDto);

    public void deleteUserById(int id);

    public Map<Integer, User> getAll();
}
