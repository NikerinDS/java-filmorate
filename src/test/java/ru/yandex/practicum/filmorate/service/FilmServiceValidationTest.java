package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceValidationTest {
    private final FilmService filmService;
    private Film testFilm;

    public FilmServiceValidationTest() {
        IdGenerator idGen = new IdGenerator();
        FilmStorage filmStorage = new InMemoryFilmStorage(idGen);
        UserStorage userStorage = new InMemoryUserStorage(idGen);
        this.filmService = new FilmService(filmStorage, userStorage);
    }

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
        assertDoesNotThrow(() -> filmService.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenFilmNameIsBlank() {
        testFilm.setName("  ");
        assertThrows(ValidationException.class, () -> filmService.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenFilmNameIsNull() {
        testFilm.setName(null);
        assertThrows(ValidationException.class, () -> filmService.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenFilmDescriptionTooLong() {
        testFilm.setDescription("A".repeat(201));
        assertThrows(ValidationException.class, () -> filmService.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenReleaseDateIsNull() {
        testFilm.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> filmService.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenReleaseDateIsTooEarly() {
        testFilm.setReleaseDate(LocalDate.of(1895, 1, 1));
        assertThrows(ValidationException.class, () -> filmService.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenDurationIsZero() {
        testFilm.setDuration(0);
        assertThrows(ValidationException.class, () -> filmService.createFilm(testFilm));
    }

    @Test
    void createFilmShouldThrowValidationExceptionWhenDurationIsNegative() {
        testFilm.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmService.createFilm(testFilm));
    }

    @Test
    void updateFilmShouldNotThrowWhenCorrectFilm() {
        filmService.createFilm(testFilm);
        testFilm.setDescription("better description");
        assertDoesNotThrow(() -> filmService.updateFilm(testFilm));
    }

    @Test
    void updateFilmShouldThrowNotFoundExceptionWhenIdIsIncorrect() {
        filmService.createFilm(testFilm);
        testFilm.setId(null);
        assertThrows(NotFoundException.class, () -> filmService.updateFilm(testFilm));
    }
}