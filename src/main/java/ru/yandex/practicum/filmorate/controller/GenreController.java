package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServise;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/genres")
public class GenreController {
    private final GenreServise genreServise;

    //получение жанра по id
    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        log.debug("Получен запрос GET: получить жанр по id");
        return genreServise.getGenreById(id);
    }

    //получение списка жанров
    @GetMapping
    public List<Genre> findAllGenres() {
        log.debug("Получен запрос DET: получить все жанры");
        return genreServise.findAllGenres();
    }
}
