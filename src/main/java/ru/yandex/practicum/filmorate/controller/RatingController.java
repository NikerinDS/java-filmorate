package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class RatingController {
    final RatingService ratingService;

    @GetMapping
    public ResponseEntity<Collection<Rating>> getAllGenres() {
        log.info("Запрос на получения списка рейтингов");
        return new ResponseEntity<>(ratingService.getAllRatings(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getGenreBuId(@PathVariable Integer id) {
        log.info("Запрос на получения рейтинга с id={}", id);
        return new ResponseEntity<>(ratingService.getRatingById(id), HttpStatus.OK);
    }
}
