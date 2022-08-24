package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    //создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST: создание пользователя");
        setTheName(user);
        userService.create(user);
        return user;
    }

    //обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT: обновление пользователя");
        setTheName(user);
        userService.update(user);
        return user;
    }

    //получение пользователя по id
    @GetMapping("/{id}")
    public User getUser(@RequestBody @PathVariable long id) {
        log.debug("Получен запрос GET: получить пользователя по id");
        return userService.getUserById(id);
    }

    //получение всех пользователей
    @GetMapping
    public List<User> findAll() {
        log.debug("Получен запрос GET: получить всех пользователей");
        return userService.findAllUser();
    }

    // добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public List<Long> addFriends(@RequestBody @PathVariable long id, @PathVariable long friendId) {
        log.debug("Получен запрос PUT на добавление в друзья");
        return userService.addFriends(userService.getUserById(id), userService.getUserById(friendId));
    }

    //удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public List<Long> deleteFriends(@RequestBody @PathVariable long id, @PathVariable long friendId) {
        log.debug("Получен запрос DELETE  на удаление из друзей");
        return userService.deleteFriends(userService.getUserById(id), userService.getUserById(friendId));
    }

    //возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@RequestBody @PathVariable long id) {
        log.debug("Получен запрос GET:получить список друзей");
        return userService.getUserFriends(id);
    }

    //список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListMutualFriends(@RequestBody @PathVariable long id, @PathVariable long otherId) {
        log.debug("Получен запрос GET: получить список общих друзей");
        return userService.getListMutualFriends(id, otherId);
    }

    private void setTheName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
