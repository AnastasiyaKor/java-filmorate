package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int idFilms = 1;
    private static final LocalDate dateMin = LocalDate.of(1895, 12, 28);

    //добавление фильма
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST /films");
        validatorFilm(film);
        log.debug("создание фильма");
        film.setId(idFilms);
        ++idFilms;
        films.put(film.getId(), film);
        return film;
    }

    // обновление фильма
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!(films.containsKey(film.getId()))) {
            log.debug("Обработка исключения: неверный идентификатор");
            throw new ValidationException("неверный идентификатор");
        }
        log.debug("Получен запрос PUT /films");
        validatorFilm(film);
        log.debug("обновление фильма");
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        }
        return film;
    }

    //получение всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    private void validatorFilm(Film film) {
        if (film.getReleaseDate().isBefore(dateMin)) {
            log.debug("Обработка исключения: дата релиза неверная");
            throw new ValidationException("дата релиза не должна быть раньше 28 декабря 1895 года");
        }
    }
}
