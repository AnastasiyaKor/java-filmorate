package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserMapper;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;
    private static final String ADD_FRIENDS = "INSERT INTO friends (user_id, friend_id) VALUES (?,?)";
    private static final String ADD_FRIENDS_LIST = "SELECT friend_id FROM friends WHERE user_id =?";
    private static final String DELETE_FRIENDS = "DELETE FROM friends WHERE user_id =? AND friend_id =?";
    private static final String DELETE_FRIENDS_LIST = "SELECT friend_id FROM friends WHERE user_id =?";
    private static final String GET_FRIENDS = "SELECT * FROM users " +
            "WHERE id IN (SELECT friend_id FROM friends WHERE user_id =?)";
    private static final String LIST_MUTUAL_FRIENDS = "SELECT * FROM users" +
            " WHERE id IN (SELECT friend_id FROM friends WHERE user_id =?) " +
            "AND id IN (SELECT friend_id FROM friends WHERE user_id =?)";

    @Autowired
    public UserService(UserDbStorage userDbStorage, JdbcTemplate jdbcTemplate) {
        this.userDbStorage = userDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    //добавление в друзья
    public List<Long> addFriends(long id, long friendId) {
        User user = getUserById(id);
        User userOther = getUserById(friendId);
        if (user.getFriends().contains(userOther.getId())) {
            throw new UserAlreadyExistException("Пользователь уже добавлен в друзья");
        } else {
            jdbcTemplate.update(ADD_FRIENDS, id, friendId);
        }
        return jdbcTemplate.query(ADD_FRIENDS_LIST, rs -> {
            List<Long> friends = new ArrayList<>();
            while (rs.next()) {
                friends.add(rs.getLong("friend_id"));
            }
            return friends;
        }, id);
    }

    //удаление из друзей
    public List<Long> deleteFriends(long id, long friendId) {
        jdbcTemplate.update(DELETE_FRIENDS, id, friendId);
        return jdbcTemplate.query(DELETE_FRIENDS_LIST, rs -> {
            List<Long> friends = new ArrayList<>();
            while (rs.next()) {
                friends.add(rs.getLong("friend_id"));
            }
            return friends;
        }, id);
    }

    //получение списка друзей
    public List<User> getUserFriends(long id) {
        return jdbcTemplate.query(GET_FRIENDS, new UserMapper(), id);
    }

    //вывод списка общих друзей
    public List<User> getListMutualFriends(long id, long otherId) {
        return jdbcTemplate.query(LIST_MUTUAL_FRIENDS, new UserMapper(), id, otherId);
    }

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
}
