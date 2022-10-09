package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmDao {
    Film create(Film film); //добавление фильма

    Film update(Film film);  // обновление фильма

    List<Film> findAllFilms(); //получение всех фильмов

    Optional<Film> getFilmById(long id); //получение фильма по id

    void delete(Long id); //удаление пользователя по id

    void deleteAll(); //удаление всех пользователей

    void filmBatchUpdate(Long filmId, Set<Genre> genres); //запрос в базу данных
}
