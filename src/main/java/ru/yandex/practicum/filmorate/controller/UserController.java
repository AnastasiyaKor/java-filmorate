package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    UserStorage userStorage;
    UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    //создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST: создание пользователя");
        userStorage.create(user);
        return user;
    }

    //обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT: обновление пользователя");
        userStorage.update(user);
        return user;
    }

    //получение пользователя по id
    @GetMapping("/{id}")
    public User getUser(@RequestBody @Valid @PathVariable long id) {
        log.debug("Получен запрос GET: получить пользователя по id");
        return userStorage.getUser(id);
    }

    //получение всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        log.debug("Получен запрос GET: получить всех пользователей");
        return userStorage.findAllUser();
    }

    // добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public List<Long> addFriends(@Valid @RequestBody @PathVariable long id, @PathVariable long friendId) {
        log.debug("Получен запрос PUT на добавление в друзья");
        return userService.addFriends(userStorage.getUser(id), userStorage.getUser(friendId));
    }

    //удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public List<Long> deleteFriends(@Valid @RequestBody @PathVariable long id, @PathVariable long friendId) {
        log.debug("Получен запрос DELETE  на удаление из друзей");
        return userService.deleteFriends(userStorage.getUser(id), userStorage.getUser(friendId));
    }

    //возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@RequestBody @Valid @PathVariable long id) {
        log.debug("Получен запрос GET:получить список друзей");
        return userService.getUserFriends(id);
    }

    //список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListMutualFriends(@RequestBody @Valid @PathVariable long id, @PathVariable long otherId) {
        log.debug("Получен запрос GET: получить список общих друзей");
        return userService.getListMutualFriends(id, otherId);
    }

}
