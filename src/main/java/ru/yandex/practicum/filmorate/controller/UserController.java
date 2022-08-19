package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int idUser = 1;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /user");
        setTheName(user);
        log.debug("создание пользователя");
        user.setId(idUser);
        ++idUser;
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT /users");
        if (!(users.containsKey(user.getId()))) {
            log.debug("Обработка исключения: неверный идентификатор");
            throw new ValidationException("неверный идентификатор");
        }
        setTheName(user);
        log.debug("обновление пользователя");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
        return user;
    }

    private void setTheName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя не задано. В качестве имени будет использован логин");
            user.setName(user.getLogin());
        }
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

}
