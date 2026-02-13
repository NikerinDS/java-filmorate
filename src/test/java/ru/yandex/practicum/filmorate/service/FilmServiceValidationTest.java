package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.memory.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceValidationTest {
    private final FilmService filmService;
    private FilmDto testFilm;

    public FilmServiceValidationTest() {
        IdGenerator idGen = new IdGenerator();
        FilmStorage filmStorage = new InMemoryFilmStorage(idGen);
        UserStorage userStorage = new InMemoryUserStorage(idGen);
        RatingStorage ratingStorage = new InMemoryRatingStorage();
        GenreStorage genreStorage = new InMemoryGenreStorage();
        this.filmService = new FilmService(filmStorage, userStorage, ratingStorage, genreStorage);
    }

    @BeforeEach
    void beforeEach() {
        testFilm = new FilmDto(0,
                "Фильм",
                "Описание",
                LocalDate.of(2025, 1, 1),
                100,
                new RatingDto(1, ""),
                List.of(new GenreDto(1, ""), new GenreDto(2, "")),
                0);
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
        testFilm = filmService.createFilm(testFilm);
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