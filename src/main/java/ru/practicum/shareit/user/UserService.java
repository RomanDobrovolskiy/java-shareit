package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import ru.practicum.shareit.user.dao.InMemoryUserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;


@Service
public class UserService {

    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();

    public UserDto createUser(@RequestBody UserDto userDto) {
        return userRepository.createUser(userDto);
    }


    public UserDto getUserById(int userId) {
        return userRepository.getUserById(userId);
    }


    public List<UserDto> getUsers() {
        return userRepository.getUsers();
    }


    public UserDto updateUser(UserDto userDto) {
        return userRepository.updateUser(userDto);
    }


    public UserDto updateById(int id, UserDto userDto) {
        return userRepository.updateById(id, userDto);
    }


    public void deleteUserById(int id) {
        userRepository.deleteUserById(id);
    }

    public Map<Integer, User> getAll() {
        return userRepository.getAll();
    }
}
