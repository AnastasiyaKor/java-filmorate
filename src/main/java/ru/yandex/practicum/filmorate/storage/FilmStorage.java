package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film); //добавление фильма

    Film update(Film film);  // обновление фильма

    List<Film> findAllFilms(); //получение всех фильмов

    Optional<Film> getFilmById(long id); //получение фильма по id

}
