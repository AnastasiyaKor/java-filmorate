package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController();
    User user;

    @Test
    void missingCharacterInTheEmailField() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            user = new User("email", "login", "name", LocalDate.of(1980,
                    12, 12));
            userController.update(user);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }

    @Test
    void theEmailFieldIsNotFilledIn() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            user = new User(" ", "login", "name", LocalDate.of(1980,
                    12, 12));
            userController.update(user);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }

    @Test
    void theLoginFieldIsNotFilledIn() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            user = new User("email@mail.ru", "", "name", LocalDate.of(1980,
                    12, 12));
            userController.update(user);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }

    @Test
    void checkinTheDateOfBirth() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            user = new User("email@mail.ru", "login", "name", LocalDate.of(2023,
                    12, 12));
            userController.update(user);
        }, "ожидалась ошибка ValidationException");
        assertNotNull(thrown.getMessage());
    }
}