package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Optional<Film> create(Film film); //добавление фильма

    Optional<Film> update(Film film);  // обновление фильма

    List<Film> findAllFilms(); //получение всех фильмов

    Optional<Film> getFilmById(long id); //получение фильма по id

    void delete(Long id); //удаление пользователя по id

    void deleteAll(); //удаление всех пользователей
}
