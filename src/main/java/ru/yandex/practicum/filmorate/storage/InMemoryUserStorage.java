package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private int idUser = 1;

    //создание пользователя
    @Override
    public User create(User user) {
        user.setId(idUser);
        ++idUser;
        users.put(user.getId(), user);
        return user;
    }

    //обновление пользователя
    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    //получение фильма по id
    @Override
    public Optional<User> getUserById(long id) {
        return Optional.of(users.get(id));
    }

    //получение всех пользователей
    @Override
    public List<User> findAllUser() {
        return new ArrayList<>(users.values());
    }

    //получение мапы с пользователями
    @Override
    public Map<Long, User> getUsers() {
        return users;
    }
}
