package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.mappers.FilmDtoMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    final FilmStorage filmStorage;
    final UserStorage userStorage;
    final RatingStorage ratingStorage;
    final GenreStorage genreStorage;

    public Collection<FilmDto> getAllFilms() {
        return filmStorage.getAllFilms().stream()
                .map(film -> FilmDtoMapper.filmToDto(film, filmStorage.getLikesByFilmId(film.getId())))
                .toList();
    }

    public FilmDto getFilmById(Integer id) {
        if (filmStorage.getFilmById(id).isEmpty()) {
            log.warn("Ошибка при получении фильма: Не найден фильм с id:{}", id);
            throw new NotFoundException("Не найден фильм с id=" + id);
        }
        return FilmDtoMapper.filmToDto(filmStorage.getFilmById(id).get(), filmStorage.getLikesByFilmId(id));
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        if (filmStorage.getFilmById(filmDto.getId()).isEmpty()) {
            log.warn("Ошибка при обновлении фильма: Не найден фильм с id:{}", filmDto.getId());
            throw new NotFoundException("Не найден фильм с id=" + filmDto.getId());
        }
        validate(filmDto);
        if (filmDto.getMpa() != null && ratingStorage.getRatingById(filmDto.getMpa().getId()).isEmpty()) {
            log.warn("Ошибка при обновлении фильма: Не найден рейтинг с id:{}", filmDto.getMpa().getId());
            throw new NotFoundException("Не найден рейтинг с id=" + filmDto.getMpa().getId());
        }
        if (filmDto.getGenres() == null) {
            filmDto.setGenres(List.of());
        }
        filmDto.setGenres(filmDto.getGenres().stream().distinct().toList());
        for (GenreDto genre : filmDto.getGenres()) {
            if (genreStorage.getGenreById(genre.getId()).isEmpty()) {
                log.warn("Ошибка при обновлении фильма: Не найден жанр с id:{}", filmDto.getMpa().getId());
                throw new NotFoundException("Не найден жанр с id=" + filmDto.getMpa().getId());
            }
        }
        Film updatedFilm = filmStorage.updateFilm(FilmDtoMapper.filmFromDto(filmDto));
        log.info("Обновлен фильм:{}", updatedFilm);
        return FilmDtoMapper.filmToDto(updatedFilm, filmStorage.getLikesByFilmId(updatedFilm.getId()));
    }

    public FilmDto createFilm(FilmDto filmDto) {
        validate(filmDto);
        if (filmDto.getMpa() != null && ratingStorage.getRatingById(filmDto.getMpa().getId()).isEmpty()) {
            log.warn("Ошибка при создании фильма: Не найден рейтинг с id:{}", filmDto.getMpa().getId());
            throw new NotFoundException("Не найден рейтинг с id=" + filmDto.getMpa().getId());
        }
        if (filmDto.getGenres() == null) {
            filmDto.setGenres(List.of());
        }
        filmDto.setGenres(filmDto.getGenres().stream().distinct().toList());
        for (GenreDto genre : filmDto.getGenres()) {
            if (genreStorage.getGenreById(genre.getId()).isEmpty()) {
                log.warn("Ошибка при создании фильма: Не найден жанр с id:{}", filmDto.getMpa().getId());
                throw new NotFoundException("Не найден жанр с id=" + filmDto.getMpa().getId());
            }
        }
        Film createdFilm = filmStorage.createFilm(FilmDtoMapper.filmFromDto(filmDto));
        log.info("Создан фильм:{}", createdFilm);
        return FilmDtoMapper.filmToDto(createdFilm, filmStorage.getLikesByFilmId(createdFilm.getId()));
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

    public Collection<FilmDto> getMostPopularFilms(int maxNumber) {
        return filmStorage.getFilmsByPopularity(maxNumber).stream()
                .map(film -> FilmDtoMapper.filmToDto(film, filmStorage.getLikesByFilmId(film.getId())))
                .toList();
    }

    private void validate(FilmDto film) {
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
