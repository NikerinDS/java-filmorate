package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;


@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    final FilmService filmService;

    @GetMapping
    public ResponseEntity<Collection<FilmDto>> getAllFilms() {
        log.info("Запрос на получения списка фильмов");
        return new ResponseEntity<>(filmService.getAllFilms(), HttpStatus.OK);
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable Integer filmId) {
        FilmDto film = filmService.getFilmById(filmId);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@RequestBody FilmDto film) {
        log.info("Запрос на создание фильма:{}", film);
        FilmDto createdFilm = filmService.createFilm(film);
        return new ResponseEntity<>(createdFilm, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<FilmDto> updateFilm(@RequestBody FilmDto film) {
        log.info("Запрос на обновление фильма:{}", film);
        FilmDto updatedFilm = filmService.updateFilm(film);
        return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("Запрос на отметку понравившегося фильма:{} от пользователя:{}", filmId, userId);
        filmService.addLikeToFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("Запрос на удаление отметки понравившегося фильма:{} от пользователя:{}", filmId, userId);
        filmService.removeLikeFromFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<FilmDto>> getPopularFilms(@RequestParam int count) {
        log.info("Запрос на получение {} наиболее популярных фильмов", count);
        return new ResponseEntity<>(filmService.getMostPopularFilms(count), HttpStatus.OK);
    }
}
