package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    //добавление лайка
    public List<Long> addLikeFilm(Film film, long userId) {
        if (!(film.getLikes().contains(userStorage.getUser(userId).getId()))) {
            film.getLikes().add(userStorage.getUser(userId).getId());
        } else {
            throw new UserAlreadyExistException("Пользователь уже поставил лайк");
        }
        return film.getLikes();
    }

    //удаление лайка
    public List<Long> deleteLikeFilm(Film film, long userId) {
        if (film.getLikes().contains(userStorage.getUser(userId).getId())) {
            film.getLikes().remove(userStorage.getUser(userId).getId());
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
}
