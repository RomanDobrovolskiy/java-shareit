package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {

    User addUser(User user);

    User getUserById(Long userId);

    Collection<User> getAllUsers();

    User updateUser(User user, Long userId);

    void deleteUser(Long userId);
}
