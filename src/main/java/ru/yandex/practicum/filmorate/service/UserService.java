package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //добавление в друзья
    public List<Long> addFriends(User user, User userOther) {
        if (userOther.getId() < 0) {
            throw new UserDoesNotExistException("Пользователь не существует");
        } else if (user.getFriends().contains(userOther.getId())) {
            throw new UserAlreadyExistException("Пользователь уже добавлен в друзья");
        } else {
            user.getFriends().add(userOther.getId());
            userOther.getFriends().add(user.getId());
        }
        return user.getFriends();
    }

    //удаление из друзей
    public List<Long> deleteFriends(User user, User userOther) {
        if (user.getFriends().contains(userOther.getId())) {
            user.getFriends().remove(userOther.getId());
            userOther.getFriends().remove(user.getId());
        } else {
            throw new UserDoesNotExistException("Пользователь с идентификатором: "
                    + userOther.getId() + " не существует");
        }
        return user.getFriends();
    }

    //получение списка друзей
    public List<User> getUserFriends(long id) {
        List<User> friends = new ArrayList<>();
        getUserById(id).getFriends().stream()
                .filter(getUserById(id).getFriends()::contains)
                .forEach(x -> friends.add(getUserById(x)));
        return friends;
    }

    //вывод списка общих друзей
    public List<User> getListMutualFriends(long id, long otherId) {
        List<User> listMutualFriends = new ArrayList<>();
        getUserById(id).getFriends().stream()
                .filter(getUserById(otherId).getFriends()::contains)
                .forEach(x -> listMutualFriends.add(getUserById(x)));
        return listMutualFriends;
    }

    //получение пользователя по id
    public User getUserById(long id) {
        if (id > 0) {
            return userStorage.getUserById(id).orElseThrow(() ->
                    new UserDoesNotExistException("пользователь не найден"));
        }
        throw new UserDoesNotExistException("Id пользователя должен быть больше 0");
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        if (userStorage.getUsers().containsKey(user.getId())) {
            return userStorage.update(user);
        } else {
            throw new UserDoesNotExistException("Пользователь: " + user + " не существует");
        }
    }

    public List<User> findAllUser() {
        return userStorage.findAllUser();
    }
}
