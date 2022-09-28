package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpa")
public class MpaController {
    private final MpaService mpaService;

    //получение рейтинга по id
    @GetMapping("/{id}")
    public Mpa getMpaById(@RequestBody @PathVariable int id) {
        log.debug("Получен запрос GET: получить рейтинг по id");
        return mpaService.getMpaById(id);
    }

    //получение всех рейтингов
    @GetMapping
    public List<Mpa> findAllMpa() {
        log.debug("Получен запрос GET: получить все рейтинги");
        return mpaService.findAllMpa();
    }
}
