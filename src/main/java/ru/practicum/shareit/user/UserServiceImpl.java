package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User addUser(User user) {
        return repository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
    }

    @Override
    public Collection<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User updateUser(User updateUser, Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }
        if (updateUser.getEmail() != null) {
            user.setEmail(updateUser.getEmail());
        }
        return repository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }
}
