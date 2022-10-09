package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FriendsDbStorage implements FriendsDao {
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
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //добавление в друзья
    @Override
    public void addFriends(long id, long friendId) {
        jdbcTemplate.update(ADD_FRIENDS, id, friendId);

    }

    //получение id друзей
    @Override
    public List<Long> addFriendsList(long id) {
        return jdbcTemplate.query(ADD_FRIENDS_LIST, rs -> {
            List<Long> friends = new ArrayList<>();
            while (rs.next()) {
                friends.add(rs.getLong("friend_id"));
            }
            return friends;
        }, id);
    }

    //удаление из друзей
    @Override
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
    @Override
    public List<User> getUserFriends(long id) {
        return jdbcTemplate.query(GET_FRIENDS, new UserMapper(), id);
    }

    //вывод списка общих друзей
    @Override
    public List<User> getListMutualFriends(long id, long otherId) {
        return jdbcTemplate.query(LIST_MUTUAL_FRIENDS, new UserMapper(), id, otherId);
    }
}
