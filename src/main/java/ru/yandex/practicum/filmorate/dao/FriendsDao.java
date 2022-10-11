package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {
    void addFriends(long id, long friendId); //добавление в друзья

    List<Long> addFriendsList(long id); //получение id друзей

    List<Long> deleteFriends(long id, long friendId); //удаление из друзей

    List<User> getUserFriends(long id); //получение списка друзей

    List<User> getListMutualFriends(long id, long otherId); //вывод списка общих друзей
}
