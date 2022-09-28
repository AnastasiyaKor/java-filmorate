package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.GenreDBStorage;
import ru.yandex.practicum.filmorate.exception.GenreDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreServise {
    private final GenreDBStorage genreDBStorage;

@Autowired
    public GenreServise(GenreDBStorage genreDBStorage) {
        this.genreDBStorage = genreDBStorage;
    }

    public Genre getGenreById(int id) {
        return genreDBStorage.getGenreById(id).orElseThrow(() -> new GenreDoesNotExistException("жанр не найден"));
    }

    public List<Genre> findAllGenres() {
        return genreDBStorage.findAllGenres();
    }
}
