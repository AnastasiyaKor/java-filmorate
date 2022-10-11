package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class FilmDbStorage implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final GenreDao genreDao;
    private static final String GET_ALL =
            "SELECT films.id, films.name, films.description, films.duration, films.release_date, rate, mpa, mr.name " +
                    "FROM films " +
                    "JOIN mpa_rating AS mr ON films.mpa = mr.id";
    private static final String GET_BY_ID = "SELECT " +
            "films.id, films.name, films.description, films.duration, films.release_date, rate, mpa, mr.name   " +
            "FROM films INNER JOIN mpa_rating AS mr ON films.mpa = mr.id " +
            "WHERE films.id =?";
    private static final String CREATE = "INSERT INTO " +
            "films (name, description, release_date, duration, rate, mpa) " +
            "VALUES (?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa = ? " +
            "WHERE id =?";
    private static final String DELETE = "DELETE FROM films WHERE id = ?";
    private static final String DELETE_ALL = "DELETE FROM films";
    private static final String BATCH_INSERT = "INSERT INTO film_genre VALUES (?,?)";
    private static final String LIST_GENRE = "SELECT * FROM genres " +
            "WHERE id IN(SELECT genre_id FROM film_genre WHERE film_id = ?) ORDER BY id";


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmMapper filmMapper, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMapper = filmMapper;
        this.genreDao = genreDao;
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRate());
            ps.setInt(6, film.getMpa().getId());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        film.setId(key.longValue());
        if (film.getGenres() != null) {
            List<Genre> genreFilm = film.getGenres();
            filmBatchUpdate(film.getId(), new HashSet<>(genreFilm));
        }
        return film;
    }

    public void filmBatchUpdate(Long filmId, Set<Genre> genres) {
        List<Object[]> batch = new ArrayList<>();
        for (Genre genre : genres) {
            Long[] values = new Long[]{filmId, (long) genre.getId()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(BATCH_INSERT, batch);
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public List<Film> findAllFilms() {
        List<Film> films = jdbcTemplate.query(GET_ALL, filmMapper);
        for (Film film : films) {
            List<Genre> genres = new ArrayList<>(genreList(film.getId()));
            film.setGenres(genres);
        }
        return films;
    }

    private List<Genre> genreList(long id) {
        return jdbcTemplate.query(LIST_GENRE, new BeanPropertyRowMapper<>(Genre.class), id);
    }


    @Override
    public Optional<Film> getFilmById(long id) {
        return jdbcTemplate.query(GET_BY_ID, filmMapper, id).stream().findAny();
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
