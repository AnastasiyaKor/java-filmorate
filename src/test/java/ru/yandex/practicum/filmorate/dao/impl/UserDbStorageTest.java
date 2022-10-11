package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(scripts = "classpath:schema.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    void create() {
        User user = new User("Анастасия", "nastya-16.94@mail.ru", "StasyKor",
                LocalDate.of(1994, 03, 17));
        Optional<User> user1 = userDbStorage.create(user);
        assertEquals(1, user1.get().getId());
        List<User> users = userDbStorage.findAllUser();
        assertEquals(1, users.size());
    }

    @Test
    void update() {
        User user = new User("Анастасия", "nastya-16.94@mail.ru", "StasyKor",
                LocalDate.of(1994, 03, 17));
        userDbStorage.create(user);
        userDbStorage.update(new User("StasyKor", "nastya-16.94@mail.ru", "StasyKor",
                LocalDate.of(1994, 03, 17)));
        assertThat(userDbStorage.getUserById(1).get().getName().equals("StasyKor"));
    }

    @Test
    void findAllUser() {
        User user = new User("Анастасия", "nastya-16.94@mail.ru", "StasyKor",
                LocalDate.of(1994, 03, 17));
        User user2 = new User("Елена", "elena@mail.ru", "Lenka",
                LocalDate.of(1996, 07, 15));
        userDbStorage.create(user);
        userDbStorage.create(user2);
        List<User> users = userDbStorage.findAllUser();
        assertEquals(2, users.size());
    }

    @Test
    void getUserById() {
        User user1 = new User("Анастасия", "nastya-16.94@mail.ru", "StasyKor",
                LocalDate.of(1994, 03, 17));
        userDbStorage.create(user1);
        Optional<User> userOptional = userDbStorage.getUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }
}