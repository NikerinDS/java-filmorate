package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.info("Запрос на получения списка пользователей");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Запрос на создание пользователя:{}", user);
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("Запрос на обновление пользователя:{}", user);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Collection<User>> getUserFriends(@PathVariable Integer id) {
        log.info("Запрос на получения списка друзей пользователя");
        return new ResponseEntity<>(userService.getFriendsByUserId(id), HttpStatus.OK);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriendToUser(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("Запрос на добавление в список друзей. userId:{}, friendId:{}", userId, friendId);
        userService.addFriends(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriendFromUser(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("Запрос на удаление из списка друзей. userId:{}, friendId:{}", userId, friendId);
        userService.removeFriends(userId, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<User>> getUserFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Запрос на получение списка общих друзей. id:{}, otherId:{}", id, otherId);
        return new ResponseEntity<>(userService.getMutualFriends(id, otherId), HttpStatus.OK);
    }
}
