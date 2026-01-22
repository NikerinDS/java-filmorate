package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 0;

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        log.info("Запрос на получения списка фильмов");
        return new ResponseEntity<>(films.values(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        log.info("Запрос на создание фильма:{}", film);
        validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Создан фильм:{}", film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        log.info("Запрос на обновление фильма:{}", film);
        if (!films.containsKey(film.getId())) {
            log.warn("Не найден фильм с id:{}", film.getId());
            throw new NotFoundException("Не найден фильм с id=" + film.getId());
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Обновлен фильм:{}", film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    private int getNextId() {
        return ++nextId;
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Ошибка валидации: name не определено");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Ошибка валидации: description длиннее 200 символов");
            throw new ValidationException("Описание фильма должно быть короче 200 символов");
        }
        if (film.getReleaseDate() == null
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Ошибка валидации: releaseDate не определена или раньше 28.12.1895");
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года;");
        }
        if (film.getDuration() <= 0) {
            log.warn("Ошибка валидации: duration <= 0 ");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
