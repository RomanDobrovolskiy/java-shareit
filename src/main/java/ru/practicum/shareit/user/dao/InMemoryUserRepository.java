package ru.practicum.shareit.user.dao;

import lombok.Getter;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    @Getter
    private Map<Integer, User> users = new HashMap<>();
}
