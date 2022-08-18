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
    private final LocalDate dateMin = LocalDate.of(1895, 12, 28);

    //добавление фильма
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film != null) {
            log.debug("Получен запрос POST /films");
            validatorFilm(film);
            log.debug("создание фильма");
            film.setId(idFilms);
            ++idFilms;
            films.put(film.getId(), film);
        }
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
        if (film.getName().isBlank()) {
            log.debug("Обработка исключения: Название фильма пустое");
            throw new ValidationException("Название фильма пустое");
        } else if (film.getDescription().length() > 200) {
            log.debug("Обработка исключения: Длина описания фильма неверная");
            throw new ValidationException("Длина описания фильма превышает 200 символов");
        } else if (film.getReleaseDate().isBefore(dateMin)) {
            log.debug("Обработка исключения: дата релиза неверная");
            throw new ValidationException("дата релиза не должна быть раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            log.debug("Обработка исключения: Продолжительность фильма неверная");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
