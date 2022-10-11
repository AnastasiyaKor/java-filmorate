package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    List<Genre> findAllGenres(); //получение всех жанров

    Optional<Genre> getGenreById(int id); //получение жанра по id

    void deleteGenre(long filmId); // удаление жанров
}
