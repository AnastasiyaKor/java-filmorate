package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDbStorage implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_ALL = "SELECT id, name FROM mpa_rating";
    private static final String GET_ID = "SELECT id, name FROM mpa_rating WHERE id =?";

    @Override
    public List<Mpa> findAllMpa() {
        return jdbcTemplate.query(GET_ALL, new BeanPropertyRowMapper<>(Mpa.class));
    }

    @Override
    public Mpa getMpaById(int id) {
        return jdbcTemplate.query(GET_ID, new BeanPropertyRowMapper<>(Mpa.class), id)
                .stream().findAny().orElse(null);
    }
}
