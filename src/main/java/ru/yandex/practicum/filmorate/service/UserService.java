package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        validate(user);
        User createdUser = userStorage.createUser(user);
        log.info("Создан пользователь:{}", createdUser);
        return createdUser;
    }

    public User updateUser(User user) {
        validate(user);
        if (userStorage.getUserById(user.getId()).isEmpty()) {
            log.warn("Ошибка при обновлении пользователя: Не найден пользователь с id:{}", user.getId());
            throw new NotFoundException("Не найден пользователь с id=" + user.getId());
        }
        User updatedUser = userStorage.updateUser(user);
        log.info("Обновлен пользователь:{}", updatedUser);
        return updatedUser;
    }

    public void addFriends(Integer userId1, Integer userId2) {
        if (userStorage.getUserById(userId1).isEmpty()) {
            log.warn("Ошибка при добавлении в друзья: Не найден пользователь с id:{}", userId1);
            throw new NotFoundException("Не найден пользователь с id=" + userId1);
        }
        if (userStorage.getUserById(userId2).isEmpty()) {
            log.warn("Ошибка при добавлении в друзья: Не найден пользователь с id:{}", userId2);
            throw new NotFoundException("Не найден пользователь с id=" + userId2);
        }
        userStorage.addFriendToUser(userId1, userId2);
    }

    public void removeFriends(Integer userId1, Integer userId2) {
        if (userStorage.getUserById(userId1).isEmpty()) {
            log.warn("Ошибка при удалении друзей: Не найден пользователь с id:{}", userId1);
            throw new NotFoundException("Не найден пользователь с id=" + userId1);
        }
        if (userStorage.getUserById(userId2).isEmpty()) {
            log.warn("Ошибка при удалении друзей: Не найден пользователь с id:{}", userId2);
            throw new NotFoundException("Не найден пользователь с id=" + userId2);
        }
        try {
            userStorage.removeFriendFromUser(userId1, userId2);
        } catch (InternalServerException e) {
            log.warn("Пользователи не друзья id1={} , id2={}", userId1, userId2);
        }
    }

    public Collection<User> getMutualFriends(Integer userId1, Integer userId2) {
        if (userStorage.getUserById(userId1).isEmpty()) {
            log.warn("Ошибка при получении списка общих друзей: Не найден пользователь с id:{}", userId1);
            throw new NotFoundException("Не найден пользователь с id=" + userId1);
        }
        if (userStorage.getUserById(userId2).isEmpty()) {
            log.warn("Ошибка при получении списка общих друзей: Не найден пользователь с id:{}", userId2);
            throw new NotFoundException("Не найден пользователь с id=" + userId2);
        }
        return userStorage.getMutualFriendsByUsersId(userId1, userId2);
    }

    public Collection<User> getFriendsByUserId(Integer userId) {
        if (userStorage.getUserById(userId).isEmpty()) {
            log.warn("Ошибка при получении списка друзей: Не найден пользователь с id:{}", userId);
            throw new NotFoundException("Не найден пользователь с id=" + userId);
        }
        return userStorage.getFriendsByUserId(userId);
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
}
