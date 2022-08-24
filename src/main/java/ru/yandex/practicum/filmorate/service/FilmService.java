package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private int like = 1;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    private void addLike(Film film) {
        film.setRate(like);
        ++like;
    }

    private void deleteLike(Film film) {
        film.setRate(like);
        --like;
    }

    //добавление лайка
    public List<Long> addLikeFilm(Film film, long userId) {
        if (!(film.getLikes().contains(userService.getUserById(userId).getId()))) {
            addLike(film);
            film.getLikes().add(userService.getUserById(userId).getId());
        } else {
            throw new UserAlreadyExistException("Пользователь уже поставил лайк");
        }
        return film.getLikes();
    }

    //удаление лайка
    public List<Long> deleteLikeFilm(Film film, long userId) {
        if ((film.getLikes().contains(userService.getUserById(userId).getId()))) {
            deleteLike(film);
            film.getLikes().remove(userService.getUserById(userId).getId());
        } else {
            throw new UserDoesNotExistException("Пользователь с идентификатором: " + userId + " не ставил лайк.");
        }
        return film.getLikes();
    }

    //вывод 10 наиболее популярных фильмов по количеству лайков
    public List<Film> getListPopularFilms(int count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparingInt(x -> -x.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(long id) {
        if (id > 0) {
            return filmStorage.getFilmById(id).orElseThrow(() -> new FilmDoesNotExistException("фильм не найден"));
        }
        throw new FilmDoesNotExistException("id фильма должен быть положительным");
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (filmStorage.getFilms().containsKey(film.getId())) {
            return filmStorage.update(film);
        } else {
            throw new FilmDoesNotExistException("неверный идентификатор");
        }
    }

    //получение всех фильмов

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }
}
