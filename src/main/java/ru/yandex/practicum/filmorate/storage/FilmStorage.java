package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film); //добавление фильма

    Film update(Film film);  // обновление фильма

    Collection<Film> findAllFilms(); //получение всех фильмов

    Film getFilm(long id); //получение фильма по id
}
