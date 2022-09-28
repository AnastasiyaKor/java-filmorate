package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(scripts = "classpath:data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    void create() {
        Film film = new Film("Фильм 1", "описание фильма 1",
                LocalDate.of(2020, 05, 15), 120, 4, new Mpa(1));
        filmDbStorage.create(film);
        assertEquals(1, film.getId());
    }

    @Test
    void update() {
        Film film = new Film("Фильм 1", "описание фильма 1",
                LocalDate.of(2020, 05, 15), 120, 4, new Mpa(1));
        filmDbStorage.create(film);
        filmDbStorage.update(new Film("Новый Фильм 1", "описание фильма 1",
                LocalDate.of(2020, 05, 15), 120, 4, new Mpa(1)));
        assertThat(filmDbStorage.getFilmById(1).get().getName().equals("Новый Фильм 1"));
    }

    @Test
    void findAllFilms() {
        Film film1 = new Film("Фильм 1", "описание фильма 1",
                LocalDate.of(2020, 05, 15), 120, 4, new Mpa(1));
        Film film2 = new Film("Фильм 2", "описание фильма 2",
                LocalDate.of(2020, 11, 17), 140, 8, new Mpa(3));
        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        List<Film> films = filmDbStorage.findAllFilms();
        assertEquals(2, films.size());
    }

    @Test
    void getFilmById() {
        Film film1 = new Film("Фильм", "описание фильма 1",
                LocalDate.of(2020, 05, 15), 120, 4, new Mpa(1));
        filmDbStorage.create(film1);
        assertThat(filmDbStorage.getFilmById(1))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }
}