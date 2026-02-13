package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.memory.IdGenerator;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceValidationTest {
    private final UserService userService;
    private User testUser;

    public UserServiceValidationTest() {
        IdGenerator idGen = new IdGenerator();
        UserStorage userStorage = new InMemoryUserStorage(idGen);
        this.userService = new UserService(userStorage);
    }

    @BeforeEach
    void beforeEach() {
        testUser = new User(0,
                "example@test.ru",
                "nik",
                "nikname",
                LocalDate.of(200, 1, 1));
    }

    @Test
    void createUserShouldNotThrowWhenCorrectFilm() {
        assertDoesNotThrow(() -> userService.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenEmailIsNull() {
        testUser.setEmail(null);
        assertThrows(ValidationException.class, () -> userService.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenEmailIsIncorrect() {
        testUser.setEmail("wrongEmail");
        assertThrows(ValidationException.class, () -> userService.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenLoginIsNull() {
        testUser.setLogin(null);
        assertThrows(ValidationException.class, () -> userService.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenLoginIsBlank() {
        testUser.setLogin(" ");
        assertThrows(ValidationException.class, () -> userService.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenBirthDateIsNull() {
        testUser.setBirthday(null);
        assertThrows(ValidationException.class, () -> userService.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenBirthDateIsInFuture() {
        testUser.setBirthday(LocalDate.of(2500, 1, 1));
        assertThrows(ValidationException.class, () -> userService.createUser(testUser));
    }

    @Test
    void updateUserShouldNotThrowWhenCorrectFilm() {
        userService.createUser(testUser);
        testUser.setName("new name");
        assertDoesNotThrow(() -> userService.updateUser(testUser));
    }

    @Test
    void updateUserShouldThrowNotFoundExceptionWhenIdIsIncorrect() {
        userService.createUser(testUser);
        testUser.setId(null);
        assertThrows(NotFoundException.class, () -> userService.updateUser(testUser));
    }
}
