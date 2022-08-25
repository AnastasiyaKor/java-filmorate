package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //добавление в друзья
    public List<Long> addFriends(long id, long friendId) {
        User user = getUserById(id);
        User userOther = getUserById(friendId);
        if (user.getFriends().contains(userOther.getId())) {
            throw new UserAlreadyExistException("Пользователь уже добавлен в друзья");
        } else {
            user.getFriends().add(userOther.getId());
            userOther.getFriends().add(user.getId());
        }
        return user.getFriends();
    }

    //удаление из друзей
    public List<Long> deleteFriends(long id, long friendId) {
        User user = getUserById(id);
        User userOther = getUserById(friendId);
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
        return getUserById(id).getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    //вывод списка общих друзей
    public List<User> getListMutualFriends(long id, long otherId) {
        return getUserById(id).getFriends().stream()
                .filter(getUserById(otherId).getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    //получение пользователя по id
    public User getUserById(long id) {
        return userStorage.getUserById(id).orElseThrow(() ->
                new UserDoesNotExistException("пользователь не найден"));
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        getUserById(user.getId());
        return userStorage.update(user);
    }

    public List<User> findAllUser() {
        return userStorage.findAllUser();
    }
}
