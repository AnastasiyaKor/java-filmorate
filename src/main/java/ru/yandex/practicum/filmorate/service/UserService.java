package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    //добавление в друзья
    public List<Long> addFriends(User user, User userOther) {
        user.getFriends().add(userOther.getId());
        userOther.getFriends().add(user.getId());
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
        inMemoryUserStorage.getUser(id).getFriends().stream()
                .filter(inMemoryUserStorage.getUser(id).getFriends()::contains)
                .forEach(x -> friends.add(inMemoryUserStorage.getUser(x)));
        return friends;
    }

    //вывод списка общих друзей
    public List<User> getListMutualFriends(long id, long otherId) {
        List<User> listMutualFriends = new ArrayList<>();
        inMemoryUserStorage.getUser(id).getFriends().stream()
                .filter(inMemoryUserStorage.getUser(otherId).getFriends()::contains)
                .forEach(x -> listMutualFriends.add(inMemoryUserStorage.getUser(x)));
        return listMutualFriends;
    }
}
