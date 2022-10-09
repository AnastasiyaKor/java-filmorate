package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.GenreDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreDao;

    public Genre getGenreById(int id) {
        return genreDao.getGenreById(id).orElseThrow(() -> new GenreDoesNotExistException("жанр не найден"));
    }

    public List<Genre> findAllGenres() {
        return genreDao.findAllGenres();
    }
}
