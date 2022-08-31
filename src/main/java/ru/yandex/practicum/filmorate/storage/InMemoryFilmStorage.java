package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private int idFilms = 1;

    //создание фильма
    @Override
    public Film create(Film film) {
        film.setId(idFilms);
        ++idFilms;
        films.put(film.getId(), film);
        return film;
    }

    // обновление фильма
    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    //получение всех фильмов
    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    //получение фильма по id
    @Override
    public Optional<Film> getFilmById(long id) {
        return Optional.ofNullable(films.get(id));
    }

}
