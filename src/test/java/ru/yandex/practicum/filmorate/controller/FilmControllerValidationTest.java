package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerValidationTest {
    private final FilmController controller = new FilmController();
    private Film testFilm;

    @BeforeEach
    void beforeEach() {
        testFilm = new Film(0,
                "Фильм",
                "Описание",
                LocalDate.of(2025, 1, 1),
                100);
    }

    @Test
    void createFilmShouldNotThrowWhenCorrectFilm() {
        assertDoesNotThrow(() -> controller.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenFilmNameIsBlank() {
        testFilm.setName("  ");
        assertThrows(ValidationException.class, () -> controller.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenFilmNameIsNull() {
        testFilm.setName(null);
        assertThrows(ValidationException.class, () -> controller.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenFilmDescriptionTooLong() {
        testFilm.setDescription("A".repeat(201));
        assertThrows(ValidationException.class, () -> controller.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenReleaseDateIsNull() {
        testFilm.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> controller.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenReleaseDateIsTooEarly() {
        testFilm.setReleaseDate(LocalDate.of(1895, 1, 1));
        assertThrows(ValidationException.class, () -> controller.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenDurationIsZero() {
        testFilm.setDuration(0);
        assertThrows(ValidationException.class, () -> controller.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenDurationIsNegative() {
        testFilm.setDuration(-1);
        assertThrows(ValidationException.class, () -> controller.createFilm(testFilm));
    }

    @Test
    void updateFilmShouldNotThrowWhenCorrectFilm() {
        controller.createFilm(testFilm);
        testFilm.setDescription("better description");
        assertDoesNotThrow(() -> controller.updateFilm(testFilm));
    }

    @Test
    void updateFilmShouldThrowNotFoundExceptionWhenIdIsIncorrect() {
        controller.createFilm(testFilm);
        testFilm.setId(null);
        assertThrows(NotFoundException.class, () -> controller.updateFilm(testFilm));
    }
}