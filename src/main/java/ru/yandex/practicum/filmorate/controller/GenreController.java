package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {
    final GenreService genreService;

    @GetMapping
    public ResponseEntity<Collection<Genre>> getAllGenres() {
        log.info("Запрос на получения списка жанров");
        return new ResponseEntity<>(genreService.getAllGenres(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreBuId(@PathVariable Integer id) {
        log.info("Запрос на получения жанра с id={}", id);
        return new ResponseEntity<>(genreService.getGenreById(id), HttpStatus.OK);
    }
}
