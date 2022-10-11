package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> create(User user); //создание пользователя

    User update(User user); //обновление пользователя

    List<User> findAllUser(); //получение списка всех пользователей

    Optional<User> getUserById(long id); // получение пользователя по id

    void delete(Long id); //удаление пользователя по id

    void deleteAll(); //удаление всех пользователей
}
