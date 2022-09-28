package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class GenreDBStorage implements GenreDao {

    private final JdbcTemplate jdbcTemplate;
    private static final String GET_ALL = "SELECT * FROM genres";
    private static final String GET_BY_ID = "SELECT * FROM genres WHERE id =?";

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate
                .query(GET_ALL, new BeanPropertyRowMapper<>(Genre.class));
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return jdbcTemplate.query(GET_BY_ID, new BeanPropertyRowMapper<>(Genre.class), id).stream().findAny();
    }
}
