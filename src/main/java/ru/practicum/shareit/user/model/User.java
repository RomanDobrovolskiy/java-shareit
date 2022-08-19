package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * // TODO .
 */
@Data
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
