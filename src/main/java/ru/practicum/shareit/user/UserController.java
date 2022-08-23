package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * // TODO .
 */
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.UserMapper;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userService.addUser(user));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return UserMapper.toUserDto(userService.getUserById(userId));
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        return UserMapper.toDtoList(userService.getAllUsers());
    }

    @PatchMapping("/{userId}")
    public UserDto editUser(@RequestBody UserDto userDto,
                            @PathVariable Long userId) {
        User updateUser = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userService.updateUser(updateUser, userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}