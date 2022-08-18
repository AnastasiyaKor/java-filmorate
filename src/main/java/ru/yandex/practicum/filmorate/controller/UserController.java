package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
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
    LocalDate moment = LocalDate.now();

    private int idUser() {
        return ++idUser;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /user");
        validatorUser(user);
        if (user.getName().isBlank()) {
            log.debug("Имя не задано. В качестве имени будет использован логин");
            log.debug("создание пользователя без имени");
            user.setId(idUser);
            idUser();
            user.setName(user.getLogin());
            users.put(user.getId(), user);
        } else {
            log.debug("создание пользователя");
            user.setId(idUser);
            idUser();
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!(users.containsKey(user.getId()))) {
            log.debug("Обработка исключения: неверный идентификатор");
            throw new ValidationException("неверный идентификатор");
        }
        log.debug("Получен запрос PUT /users");
        validatorUser(user);
        if (user.getName().isBlank()) {
            log.debug("Имя не задано. В качестве имени будет использован логин");
            log.debug("обновление пользователя");
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            }
        } else {
            log.debug("обновление пользователя");
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            } else if (user.getId() > 0) {
                create(user);
            }
        }
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    private void validatorUser(User user) {
        if (user.getEmail().isBlank() || (!(user.getEmail().contains("@")))) {
            log.debug("Обработка исключения: строка для электронной почты не заполнена, или отсутсвует @");
            throw new ValidationException("электронная почта не может быть пустой, " +
                    "в электронной почте должен быть символ @");
        } else if (user.getLogin().isBlank()) {
            log.debug("Обработка исключения: логин пустой или содержит пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(moment)) {
            log.debug("Обработка исключения: дата рождения указана позже текущей даты");
            throw new ValidationException("дата рождения не может быть в будущем.");
        }
    }
}
