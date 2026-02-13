package ru.yandex.practicum.filmorate.storage.memory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();
    private final IdGenerator idGenerator;

    @Override
    public Film createFilm(Film film) {
        film.setId(idGenerator.getNextId());
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            return null;
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        if (films.containsKey(film.getId())) {
            likes.remove(film.getId());
            return films.remove(film.getId());
        }
        return null;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public void addLikeToFilm(Integer filmId, Integer userId) {
        likes.get(filmId).add(userId);
    }

    @Override
    public void removeLikeFromFilm(Integer filmId, Integer userId) {
        likes.get(filmId).remove(userId);
    }

    @Override
    public Collection<Film> getFilmsByPopularity(int maxNumber) {
        return likes.entrySet().stream()
                //.sorted(Comparator.comparingInt(e -> e.getValue().size()))
                .sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size())
                .map(e -> films.get(e.getKey()))
                .limit(maxNumber)
                .toList();
    }

    @Override
    public Integer getLikesByFilmId(Integer filmId) {
        return likes.containsKey(filmId) ? likes.get(filmId).size() : 0;
    }
}
