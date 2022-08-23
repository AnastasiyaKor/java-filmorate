package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User create(User user); //создание пользователя

    User update(User user); //обновление пользователя

    Collection<User> findAllUser(); //получение списка всех пользователей

    User getUser(long id); // получение пользователя по id
}
