package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dao.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class UserDbStorage implements UserDao {
    private static JdbcTemplate jdbcTemplate;
    private static final String GET_ALL = "SELECT * FROM users";
    private static final String GET_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String CREATE = "INSERT INTO users(name, email, login, birthday) VALUES(?,?,?,?)";
    private static final String UPDATE = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? WHERE id =?";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";
    private static final String DELETE_ALL = "DELETE FROM users";

@Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2,user.getEmail());
            ps.setString(3,user.getLogin());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        user.setId(key.longValue());
        return getUserById(user.getId());
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(UPDATE,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        return new User();
    }

    @Override
    public List<User> findAllUser() {
        return jdbcTemplate.query(GET_ALL, new UserMapper());
    }

    @Override
    public Optional<User> getUserById(long id) {
    return jdbcTemplate.query(GET_BY_ID, new UserMapper(), id).stream().findAny();
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL);
    }
}
