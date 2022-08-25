package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films")
public class FilmController {
    private static final LocalDate DATE_MIN = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;

    //создание фильма
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST на создание фильма");
        validatorFilm(film);
        filmService.create(film);
        return film;
    }

    // обновление фильма
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT на обновление фильма");
        if (film != null) {
            validatorFilm(film);
            filmService.update(film);
        } else {
            throw new FilmDoesNotExistException("неверный идентификатор");
        }
        return film;
    }

    //получение фильма по id
    @GetMapping("/{id}")
    public Film getFilm(@RequestBody @PathVariable long id) {
        log.debug("Получен запрос GET: получить фильм по id");
        return filmService.getFilmById(id);
    }

    //получение всех фильмов
    @GetMapping
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    //пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public List<Long> addLikeFilm(@RequestBody @PathVariable long id, @PathVariable long userId) {
        log.debug("Получен запрос PUT: поставить лайк фильму");
        return filmService.addLikeFilm(id, userId);
    }

    //пользователь удаляет лайк
    @DeleteMapping("/{id}/like/{userId}")
    public List<Long> deleteLikeFilm(@RequestBody @PathVariable long id, @PathVariable long userId) {
        log.debug("Получен запрос DELETE: удалить лайк");
        return filmService.deleteLikeFilm(id, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков
    @GetMapping("/popular")
    public List<Film> getListPopularFilms(@RequestParam(required = false, defaultValue = "10")
                                          @Valid @Positive int count) {
        log.debug("Получен запрос GET: получить список популярных фильмов");
        return filmService.getListPopularFilms(count);
    }

    private void validatorFilm(Film film) {
        if (film.getReleaseDate().isBefore(DATE_MIN)) {
            throw new ValidationException("дата релиза не должна быть раньше 28 декабря 1895 года");
        }
    }
}
