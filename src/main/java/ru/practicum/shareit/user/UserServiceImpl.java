package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dao.InMemoryUserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();
    private int id = 1;

    @Override
    public UserDto createUser(@RequestBody UserDto userDto) {
        emailValidation(userDto);
        userDto.setId(id);
        id++;
        userRepository.getUsers().put(userDto.getId(), UserMapper.toUser(userDto));
        return userDto;
    }

    @Override
    public UserDto getUserById(int userId) {
        if (userRepository.getUsers().get(userId) == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return UserMapper.toUserDto(userRepository.getUsers().get(userId));
    }

    @Override
    public List<UserDto> getUsers() {
        return List.copyOf(userRepository.getUsers().values().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList()));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User newUser = new User();
        newUser.setId(userDto.getId());
        newUser.setName(userDto.getName() == null ? userRepository.getUsers().get(userDto.getId()).getName() : userDto.getName());
        newUser.setEmail(userDto.getEmail() == null ? userRepository.getUsers().get(userDto.getId()).getEmail() : userDto.getEmail());

        for (int i : userRepository.getUsers().keySet()) {
            if (userRepository.getUsers().get(i).getEmail().equals(userDto.getEmail()) && userRepository.getUsers().get(i).getId() != userDto.getId()) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }

        userRepository.getUsers().put(userDto.getId(), newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto updateById(int id, UserDto userDto) {
        User newUser = new User();
        newUser.setId(id);
        newUser.setName(userDto.getName() == null ? getUserById(id).getName() : userDto.getName());
        newUser.setEmail(userDto.getEmail() == null ? getUserById(id).getEmail() : userDto.getEmail());

        for (int i : userRepository.getUsers().keySet()) {
            if (userRepository.getUsers().get(i).getEmail().equals(userDto.getEmail()) && userRepository.getUsers().get(i).getId() != userDto.getId()) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }

        userRepository.getUsers().put(id, newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.getUsers().remove(id);
    }

    private void emailValidation(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new NullPointerException("Нужно указать email");
        }

        if (!userDto.getEmail().matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}")) {
            throw new NullPointerException("E-mail неверен");
        }

        for (int i : userRepository.getUsers().keySet()) {
            if (userRepository.getUsers().get(i).getEmail().equals(userDto.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }
    }

    @Override
    public Map<Integer, User> getAll() {
        return userRepository.getUsers();
    }
}
