package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeDao {
    void addLikeFilm(long id, long userId); //добавление лайка

    void deleteLikeFilm(long id, long userId); //удаление лайка

    List<Film> getListPopularFilms(int count); //вывод 10 наиболее популярных фильмов по количеству лайков
}
