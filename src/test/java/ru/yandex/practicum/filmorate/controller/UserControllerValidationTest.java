package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerValidationTest {
    private final UserController controller = new UserController();
    private User testUser;

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
        assertDoesNotThrow(() -> controller.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenEmailIsNull() {
        testUser.setEmail(null);
        assertThrows(ValidationException.class, () -> controller.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenEmailIsIncorrect() {
        testUser.setEmail("wrongEmail");
        assertThrows(ValidationException.class, () -> controller.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenLoginIsNull() {
        testUser.setLogin(null);
        assertThrows(ValidationException.class, () -> controller.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenLoginIsBlank() {
        testUser.setLogin(" ");
        assertThrows(ValidationException.class, () -> controller.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenBirthDateIsNull() {
        testUser.setBirthday(null);
        assertThrows(ValidationException.class, () -> controller.createUser(testUser));
    }

    @Test
    void createUserShouldThrowValidationExceptionWhenBirthDateIsInFuture() {
        testUser.setBirthday(LocalDate.of(2500, 1, 1));
        assertThrows(ValidationException.class, () -> controller.createUser(testUser));
    }

    @Test
    void updateUserShouldNotThrowWhenCorrectFilm() {
        controller.createUser(testUser);
        testUser.setName("new name");
        assertDoesNotThrow(() -> controller.updateUser(testUser));
    }

    @Test
    void updateUserShouldThrowNotFoundExceptionWhenIdIsIncorrect() {
        controller.createUser(testUser);
        testUser.setId(null);
        assertThrows(NotFoundException.class, () -> controller.updateUser(testUser));
    }
}
