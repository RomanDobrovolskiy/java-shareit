package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public UserDto createUser(@RequestBody UserDto userDto) {
        emailValidation(userDto);
        userDto.setId(id);
        id++;
        users.put(userDto.getId(), UserMapper.toUser(userDto));
        return userDto;
    }

    @Override
    public UserDto getUserById(int userId) {
        if (users.get(userId) == null) {
            throw new NullPointerException("Пользователь с id " + userId + " не найден");
        }
        return UserMapper.toUserDto(users.get(userId));
    }

    @Override
    public List<UserDto> getUsers() {
        return List.copyOf(users.values().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList()));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User newUser = new User();
        newUser.setId(userDto.getId());
        newUser.setName(userDto.getName() == null ? users.get(userDto.getId()).getName() : userDto.getName());
        newUser.setEmail(userDto.getEmail() == null ? users.get(userDto.getId()).getEmail() : userDto.getEmail());

        for (int i : users.keySet()) {
            if (users.get(i).getEmail().equals(userDto.getEmail()) && users.get(i).getId() != userDto.getId()) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }

        users.put(userDto.getId(), newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto updateById(int id, UserDto userDto) {
        User newUser = new User();
        newUser.setId(id);
        newUser.setName(userDto.getName() == null ? getUserById(id).getName() : userDto.getName());
        newUser.setEmail(userDto.getEmail() == null ? getUserById(id).getEmail() : userDto.getEmail());

        for (int i : users.keySet()) {
            if (users.get(i).getEmail().equals(userDto.getEmail()) && users.get(i).getId() != userDto.getId()) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }

        users.put(id, newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public void deleteUserById(int id) {
        users.remove(id);
    }

    private void emailValidation(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new NullPointerException("Нужно указать email");
        }

        if (!userDto.getEmail().matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}")) {
            throw new NullPointerException("E-mail неверен");
        }

        for (int i : users.keySet()) {
            if (users.get(i).getEmail().equals(userDto.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }
    }

    @Override
    public Map<Integer, User> getAll() {
        return users;
    }
}
