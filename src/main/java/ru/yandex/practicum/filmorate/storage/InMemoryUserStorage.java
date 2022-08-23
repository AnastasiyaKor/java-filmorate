package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private int idUser = 1;

    //создание пользователя
    @Override
    public User create(User user) {
        setTheName(user);
        user.setId(idUser);
        ++idUser;
        users.put(user.getId(), user);
        return user;
    }

    //обновление пользователя
    @Override
    public User update(User user) {
        if (!(users.containsKey(user.getId()))) {
            throw new UserDoesNotExistException("Пользователь: " + user + " не существует");
        }
        setTheName(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
        return user;
    }

    private void setTheName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    //получение пользователя по id
    @Override
    public User getUser(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new UserDoesNotExistException("Пользователь с идентификатором: " + id + " не существует");
        }
    }

    //получение всех пользователей
    @Override
    public Collection<User> findAllUser() {
        return users.values();
    }


}
