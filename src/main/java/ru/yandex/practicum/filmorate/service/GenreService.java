package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    final GenreStorage genreStorage;

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(Integer id) {
        Optional<Genre> genre = genreStorage.getGenreById(id);
        if (genre.isEmpty()) {
            log.warn("Ошибка при получении жанра: Не найден жанр с id:{}", id);
            throw new NotFoundException("Не найден жанр с id=" + id);
        }
        return genre.get();
    }
}
