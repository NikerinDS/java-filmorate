package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 0;

    private int getNextId() {
        return ++nextId;
    }

    private void validate(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации: login пустой или содержит пробелы");
            throw new ValidationException("login пользователя не может быть пустым или содержать пробелы");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации: birthDate не указана или позже текущей даты");
            throw new ValidationException("Дата рождения пользователя должна быть указана и не быть в будущем");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Ошибка валидации: email некорректен");
            throw new ValidationException("Email пользователя должен быть корректным");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("пустое поле name перезаписано значением login");
            user.setName(user.getLogin());
        }
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.info("Запрос на получения списка пользователей");
        return new ResponseEntity<>(users.values(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Запрос на создание пользователя:{}", user);
        validate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан пользователь:{}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("Запрос на обновление пользователя:{}", user);
        if (!users.containsKey(user.getId())) {
            log.warn("Не найден пользователь с id:{}", user.getId());
            throw new NotFoundException("Не найден пользователь с id=" + user.getId());
        }
        validate(user);
        users.put(user.getId(), user);
        log.info("Обновлен пользователь:{}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
