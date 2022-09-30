package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final UserService userService;
    private static final String CREATE = "INSERT INTO film_likes (film_id, user_id) VALUES (?,?)";
    private static final String DELETE = "DELETE FROM film_likes WHERE film_id =? AND user_id =?";
    private static final String POPULAR = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa, mr.name " +
            "FROM film_likes AS fl RIGHT JOIN films AS f ON fl.film_id = f.id LEFT JOIN mpa_rating AS mr ON f.mpa = mr.id " +
            "GROUP BY f.id ORDER BY COUNT(fl.film_id) DESC LIMIT ?";
        /*"SELECT f.id, f.name, f.description, f.release_date, f.duration, mr.name " +
            "FROM film_likes AS fl RIGHT JOIN films  AS f ON fl.film_id = f.id " +
            "LEFT JOIN mpa_rating AS mr ON f.mpa = mr.id GROUP BY f.id ORDER BY COUNT(fl.film_id) DESC LIMIT ?";*/

    @Autowired
    public FilmService(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage, UserService userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = filmDbStorage;
        this.userService = userService;
    }

    private void updateRate(long filmId) {
        String sqlQuery = "update films f " +
                "set rate = (select count(l.user_id) from film_likes l where l.film_id = f.id) where f.id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    //добавление лайка
    public List<Long> addLikeFilm(long id, long userId) {
        Film film = getFilmById(id);
        if (!(film.getLikes().contains(userService.getUserById(userId).getId()))) {
            jdbcTemplate.update(CREATE, id, userId);
            updateRate(film.getId());
        } else {
            throw new UserAlreadyExistException("Пользователь уже поставил лайк");
        }
        return film.getLikes();
    }

    //удаление лайка
    public List<Long> deleteLikeFilm(long id, long userId) {
        Film film = getFilmById(id);
        if ((film.getLikes().contains(userService.getUserById(userId).getId()))) {
            jdbcTemplate.update(DELETE, id, userId);
            updateRate(film.getId());
        } else {
            throw new UserDoesNotExistException("Пользователь с идентификатором: " + userId + " еще не ставил лайк.");
        }
        return film.getLikes();
    }

    //вывод 10 наиболее популярных фильмов по количеству лайков
    public List<Film> getListPopularFilms(int count) {
        List<Film> filmList = new ArrayList<>();
        return jdbcTemplate.query(POPULAR, rs -> {
            while (rs.next()) {
                Film film = new Film().toBuilder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .duration(rs.getInt("duration"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .mpa(filmDbStorage.mpaMapping(rs.getInt("MPA")))
                        .genres(filmDbStorage.genreMapper(rs.getInt("id")))
                        .likes(filmDbStorage.likesMapper(rs.getInt("id")))
                        .build();
                filmList.add(film);
            }
            return filmList;
        }, count);
    }

    public Film getFilmById(long id) {
        return filmDbStorage.getFilmById(id).orElseThrow(() -> new FilmDoesNotExistException("фильм не найден"));
    }

    public Optional<Film> create(Film film) {
        return filmDbStorage.create(film);
    }

    public Optional<Film> update(Film film) {
        getFilmById(film.getId());
        return filmDbStorage.update(film);
    }

    //получение всех фильмов
    public List<Film> findAllFilms() {
        return filmDbStorage.findAllFilms();
    }

    //удаление фильма
    public void delete(Long id) {
        filmDbStorage.delete(id);
    }

    //удаление всех фильмов
    public void deleteAll() {
        filmDbStorage.deleteAll();
    }
}
