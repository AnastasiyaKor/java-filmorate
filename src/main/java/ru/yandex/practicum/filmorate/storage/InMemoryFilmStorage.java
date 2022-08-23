package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    private int idFilms = 1;
    private static final LocalDate dateMin = LocalDate.of(1895, 12, 28);

    //добавление фильма
    @Override
    public Film create(Film film) {
        validatorFilm(film);
        film.setId(idFilms);
        ++idFilms;
        films.put(film.getId(), film);
        return film;
    }

    // обновление фильма
    @Override
    public Film update(Film film) {
        if (!(films.containsKey(film.getId()))) {
            throw new FilmDoesNotExistException("неверный идентификатор");
        }
        validatorFilm(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        }
        return film;
    }

    //получение всех фильмов
    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    //получение фильма по id
    @Override
    public Film getFilm(long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new FilmDoesNotExistException("Фильм с идентификатором: " + id + " не существует");
        }
    }

    private void validatorFilm(Film film) {
        if (film.getReleaseDate().isBefore(dateMin)) {
            throw new ValidationException("дата релиза не должна быть раньше 28 декабря 1895 года");
        }
    }

}
