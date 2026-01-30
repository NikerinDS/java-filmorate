package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    final FilmStorage filmStorage;
    final UserStorage userStorage;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film updateFilm(Film film) {
        validate(film);
        Film updatedFilm = filmStorage.updateFilm(film);
        if (updatedFilm == null) {
            log.warn("Ошибка при обновлении фильма: Не найден фильм с id:{}", film.getId());
            throw new NotFoundException("Не найден фильм с id=" + film.getId());
        }
        log.info("Обновлен фильм:{}", updatedFilm);
        return updatedFilm;
    }

    public Film createFilm(Film film) {
        validate(film);
        Film createdFilm = filmStorage.createFilm(film);
        log.info("Создан фильм:{}", createdFilm);
        return createdFilm;
    }


    public void addLikeToFilm(Integer filmId, Integer userId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            log.warn("Ошибка при установке like: Не найден фильм с id:{}", filmId);
            throw new NotFoundException("Не найден фильм с id=" + userId);
        }
        if (userStorage.getUserById(userId).isEmpty()) {
            log.warn("Ошибка при установке like: Не найден пользователь с id:{}", userId);
            throw new NotFoundException("Не найден пользователь с id=" + userId);
        }
        filmStorage.addLikeToFilm(filmId, userId);
    }

    public void removeLikeFromFilm(Integer filmId, Integer userId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            log.warn("Ошибка при удалении like: Не найден фильм с id:{}", filmId);
            throw new NotFoundException("Не найден фильм с id=" + userId);
        }
        if (userStorage.getUserById(userId).isEmpty()) {
            log.warn("Ошибка при удалении like: Не найден пользователь с id:{}", userId);
            throw new NotFoundException("Не найден пользователь с id=" + userId);
        }
        filmStorage.removeLikeFromFilm(filmId, userId);
    }

    public Collection<Film> getMostPopularFilms(int maxNumber) {
        return filmStorage.getFilmsByPopularity(maxNumber);
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
