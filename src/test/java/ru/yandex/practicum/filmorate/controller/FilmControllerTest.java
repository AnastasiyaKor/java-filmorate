package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController = new FilmController();
    Film film;

    @Test
    void shouldFalseFilmNameIsBlank() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film(" ", "описание фильма",
                    LocalDate.of(2022, 9, 1), 156);
            filmController.update(film);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }

    @Test
    void shouldFalseFilmNameIsBlankBorder() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("", "описание фильма",
                    LocalDate.of(2022, 9, 1), 156);
            filmController.update(film);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }

    @Test
    void shouldFalseFilmLength() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("фильм 1", "описание этого фильма превышает двести " +
                    "символов, а потому, должно быть выброшено исключение ValidationException. " +
                    "Описание этого фильма превышает двести " +
                    "\"символов, а потому, должно быть выброшено исключение ValidationException.",
                    LocalDate.of(2022, 9, 1), 156);
            filmController.update(film);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }

    @Test
    void shouldFalseFilmLengthBorder() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("фильм 1", "описание этого фильма превышает двести символов, " +
                    "а потому, должно быть выброшено исключение ValidationException. " +
                    "Описание этого фильма превышает двести символов, а потому, должно быть выброшено исключе",
                    LocalDate.of(2022, 9, 1), 156);
            filmController.update(film);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }

    @Test
    void shouldFalseFilmDateRelease() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("фильм 1", "описание фильма",
                    LocalDate.of(1800, 3, 25), 156);
            filmController.create(film);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }

    @Test
    void shouldFalseFilmDateReleaseBorder() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("фильм 1", "описание фильма",
                    LocalDate.of(1895, 12, 28), 156);
            filmController.update(film);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }

    @Test
    void shouldFalseFilmDuration() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("фильм 1", "описание фильма",
                    LocalDate.of(2023, 9, 1), -1);
            filmController.update(film);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }

    @Test
    void shouldFalseFilmDurationBorder() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("фильм 1", "описание фильма",
                    LocalDate.of(2023, 9, 1), 0);
            filmController.update(film);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }
}