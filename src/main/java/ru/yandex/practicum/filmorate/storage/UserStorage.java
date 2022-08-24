package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {

    User create(User user); //создание пользователя

    User update(User user); //обновление пользователя

    List<User> findAllUser(); //получение списка всех пользователей

    Optional<User> getUserById(long id); // получение пользователя по id

    Map<Long, User> getUsers(); //получение мапы с пользователями
}
