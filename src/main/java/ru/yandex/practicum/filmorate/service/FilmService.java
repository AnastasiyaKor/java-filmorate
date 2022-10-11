package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmService {
    private final UserService userService;
    private final FilmDao filmDao;
    private final FilmMapper filmMapper;
    private final GenreDao genreDao;
    private final LikeDao likeDao;
    private static final LocalDate DATE_MIN = LocalDate.of(1895, 12, 28);

    //добавление лайка
    public List<Long> addLikeFilm(long id, long userId) {
        Film film = getFilmById(id);
        if (!(film.getLikes().contains(userService.getUserById(userId).getId()))) {
            likeDao.addLikeFilm(id, userId);
        } else {
            throw new UserAlreadyExistException("Пользователь уже поставил лайк");
        }
        return film.getLikes();
    }

    //удаление лайка
    public List<Long> deleteLikeFilm(long id, long userId) {
        Film film = getFilmById(id);
        if ((film.getLikes().contains(userService.getUserById(userId).getId()))) {
            likeDao.deleteLikeFilm(id, userId);
        } else {
            throw new UserDoesNotExistException("Пользователь с идентификатором: " + userId + " еще не ставил лайк.");
        }
        return film.getLikes();
    }

    //вывод 10 наиболее популярных фильмов по количеству лайков
    public List<Film> getListPopularFilms(int count) {

        return likeDao.getListPopularFilms(count);
    }

    public Film getFilmById(long id) {
        Film getFilm = filmDao.getFilmById(id).orElseThrow(() -> new FilmDoesNotExistException("фильм не найден"));
        return setGenreFilm(getFilm);
    }

    public Film create(Film film) {
        validatorFilm(film);
        Film createFilm = filmDao.create(film);
        return setGenreFilm(createFilm);
    }

    public Optional<Film> update(Film film) {
        filmDao.getFilmById(film.getId())
                .orElseThrow(() -> new FilmDoesNotExistException("фильм не найден"));
        validatorFilm(film);
        List<Genre> filmGenres = film.getGenres();
        Film updateFilm = setGenreFilm(filmDao.update(film));
        genreDao.deleteGenre(film.getId());
        if (filmGenres != null && !filmGenres.isEmpty()) {
            filmDao.filmBatchUpdate(updateFilm.getId(), new HashSet<>(filmGenres));
        }
        return Optional.of(setGenreFilm(updateFilm));
    }

    //получение всех фильмов
    public List<Film> findAllFilms() {
        return filmDao.findAllFilms();
    }

    //удаление фильма
    public void delete(Long id) {
        filmDao.delete(id);
    }

    //удаление всех фильмов
    public void deleteAll() {
        filmDao.deleteAll();
    }

    public Film setGenreFilm(Film film) {
        film.setGenres(filmMapper.genreMapper(film.getId()));
        return film;
    }
    private void validatorFilm(Film film) {
        if (film.getReleaseDate().isBefore(DATE_MIN)) {
            throw new ValidationException("дата релиза не должна быть раньше 28 декабря 1895 года");
        }
    }

}
