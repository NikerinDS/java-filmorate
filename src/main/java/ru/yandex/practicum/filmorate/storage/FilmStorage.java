package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm(Film film);

    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(Integer filmId);

    void addLikeToFilm(Integer filmId, Integer userId);

    void removeLikeFromFilm(Integer filmId, Integer userId);

    Collection<Film> getFilmsByPopularity(int maxNumber);

    Integer getLikesByFilmId(Integer filmId);
}
