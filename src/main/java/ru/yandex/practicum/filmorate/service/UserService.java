package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FriendsDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService {
    private final UserDbStorage userDbStorage;
    private final FriendsDbStorage friendsDbStorage;

    //получение пользователя по id
    public User getUserById(long id) {
        return userDbStorage.getUserById(id).orElseThrow(() ->
                new UserDoesNotExistException("пользователь не найден"));
    }

    public Optional<User> create(User user) {
        return userDbStorage.create(user);
    }

    public User update(User user) {
        getUserById(user.getId());
        return userDbStorage.update(user);
    }

    public List<User> findAllUser() {
        return userDbStorage.findAllUser();
    }

    public void delete(Long id) {
        userDbStorage.delete(id);
    }

    public void deleteAll() {
        userDbStorage.deleteAll();
    }

    //добавление в друзья
    public List<Long> addFriends(long id, long friendId) {
        getUserById(id);
        getUserById(friendId);
        friendsDbStorage.addFriends(id, friendId);
        return friendsDbStorage.addFriendsList(id);
    }

    //удаление из друзей
    public List<Long> deleteFriends(long id, long friendId) {
        return friendsDbStorage.deleteFriends(id, friendId);
    }

    //получение списка друзей
    public List<User> getUserFriends(long id) {

        return friendsDbStorage.getUserFriends(id);
    }

    //вывод списка общих друзей
    public List<User> getListMutualFriends(long id, long otherId) {
        return friendsDbStorage.getListMutualFriends(id, otherId);
    }
}
