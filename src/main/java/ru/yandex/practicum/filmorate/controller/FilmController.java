package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films")
public class FilmController {

    private final FilmService filmService;

    //создание фильма
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST на создание фильма");
        filmService.create(film);
        return film;
    }

    //обновление фильма
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT на обновление фильма");
        filmService.update(film);
        return film;
    }

    //удаление фильма
    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        log.debug("Получен запрос DELETE: удалить фильм по id");
        filmService.delete(id);
    }

    //удаление всех фильмов
    @DeleteMapping
    public void deleteAll() {
        log.debug("Получен запрос DELETE: удалить все фильмы");
        filmService.deleteAll();
    }

    //получение фильма по id
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        log.debug("Получен запрос GET: получить фильм по id");
        return filmService.getFilmById(id);
    }

    //получение списка фильмов
    @GetMapping
    public List<Film> findAllFilms() {
        log.debug("Получен запрос DET: получить все фильмы");
        return filmService.findAllFilms();
    }

    //добавление лайка фильму
    @PutMapping("/{id}/like/{userId}")
    public List<Long> addLikeFilm(@PathVariable long id, @PathVariable long userId) {
        log.debug("Получен запрос PUT: поставить лайк фильму");
        return filmService.addLikeFilm(id, userId);
    }

    //удаление лайка у фильма
    @DeleteMapping("/{id}/like/{userId}")
    public List<Long> deleteLikeFilm(@PathVariable long id, @PathVariable long userId) {
        log.debug("Получен запрос DELETE: удалить лайк");
        return filmService.deleteLikeFilm(id, userId);
    }

    //получение популярных фильмов
    @GetMapping("/popular")
    public List<Film> getListPopularFilms(@RequestParam(required = false, defaultValue = "10")
                                          @Positive int count) {
        log.debug("Получен запрос GET: получить список популярных фильмов");
        return filmService.getListPopularFilms(count);
    }


}
