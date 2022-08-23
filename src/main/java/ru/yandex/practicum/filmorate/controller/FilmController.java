package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films")
public class FilmController {
    FilmStorage filmStorage;
    UserStorage userStorage;
    FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, UserStorage userStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmService = filmService;
    }

    //создание фильма
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST на создание фильма");
        filmStorage.create(film);
        return film;
    }

    // обновление фильма
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT на обновление фильма");
        filmStorage.update(film);
        return film;
    }

    //получение фильма по id
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        log.debug("Получен запрос GET: получить фильм по id");
        return filmStorage.getFilm(id);
    }

    //получение всех фильмов
    @GetMapping
    public Collection<Film> findAllFilms() {

        return filmStorage.findAllFilms();
    }

    //пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public Collection<Long> addLikeFilm(@Valid @RequestBody @PathVariable long id, @PathVariable long userId) {
        log.debug("Получен запрос PUT: поставить лайк фильму");
        filmService.addLikeFilm(filmStorage.getFilm(id), userStorage.getUser(userId).getId());
        return filmStorage.getFilm(id).getLikes();
    }

    //пользователь удаляет лайк
    @DeleteMapping("/{id}/like/{userId}")
    public Collection<Long> deleteLikeFilm(@Valid @RequestBody @PathVariable long id, @PathVariable long userId) {
        log.debug("Получен запрос DELETE: удалить лайк");
        filmService.deleteLikeFilm(filmStorage.getFilm(id), userStorage.getUser(userId).getId());
        return filmStorage.getFilm(id).getLikes();
    }

    //возвращает список из первых count фильмов по количеству лайков
    @GetMapping("/popular")
    public List<Film> getListPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        log.debug("Получен запрос GET: получить список популярных фильмов");
        return filmService.getListPopularFilms(count);
    }
}
