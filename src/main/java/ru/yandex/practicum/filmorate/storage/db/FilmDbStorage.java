package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class FilmDbStorage extends BaseDbStorage implements FilmStorage {
    private static final String GET_ALL_FILMS_QUERY = "SELECT * FROM films";
    private static final String GET_FILM_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String GET_GENRES_BY_FILM_ID_QUERY = "SELECT g.id, g.name FROM films_genres as f " +
            "JOIN genres as g ON f.genreId = g.id WHERE filmId = ?";
    private static final String GET_RATING_BY_ID = "SELECT * FROM ratings WHERE id = ?";
    private static final String INSERT_FILM_QUERY = "INSERT INTO films(name, description, releaseDate, duration," +
            " ratingId) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_GENRES_FOR_FILM_QUERY = "INSERT INTO films_genres(filmId, genreId) VALUES (?, ?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?," +
            " duration = ?, ratingId = ? WHERE id = ?";
    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String DELETE_GENRES_BY_FILM_ID_QUERY = "DELETE FROM films_genres WHERE filmId = ?";
    private static final String ADD_LIKE_QUERY = "INSERT INTO likes(filmId, userId) VALUES (?, ?)";
    private static final String REMOVE_LIKE_QUERY = "DELETE FROM likes WHERE filmId = ? AND userId = ?";
    private static final String GET_POPULAR_FILMS_QUERY = "SELECT * FROM films AS f " +
            "LEFT join(SELECT filmId,count(*) AS popularity FROM likes GROUP BY filmId)" +
            " AS l ON f.id = l.filmId ORDER BY l.popularity DESC LIMIT ?";
    private static final String GET_LIKES_COUNT_FOR_FILM_QUERY = "SELECT count(*) FROM likes WHERE filmId = ?";

    private final RowMapper<Film> filmMapper;
    private final RowMapper<Genre> genreMapper;
    private final RowMapper<Rating> ratingMapper;

    public FilmDbStorage(JdbcTemplate jdbc,
                         RowMapper<Film> filmMapper,
                         RowMapper<Genre> genreMapper,
                         RowMapper<Rating> ratingMapper) {
        super(jdbc);
        this.filmMapper = filmMapper;
        this.genreMapper = genreMapper;
        this.ratingMapper = ratingMapper;
    }

    @Override
    public Film createFilm(Film film) {
        Integer filmId = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                (film.getRating() == null) ? null : film.getRating().getId()
        );
        film.setId(filmId);
        for (Genre genreId : film.getGenres()) {
            insertWithoutReturningKey(INSERT_GENRES_FOR_FILM_QUERY, filmId, genreId.getId());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                (film.getRating() == null) ? null : film.getRating().getId(),
                film.getId()
        );
        delete(DELETE_GENRES_BY_FILM_ID_QUERY, film.getId());
        for (Genre genreId : film.getGenres()) {
            insertWithoutReturningKey(INSERT_GENRES_FOR_FILM_QUERY, film.getId(), genreId.getId());
        }
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        delete(DELETE_FILM_QUERY, film.getId());
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        List<Film> films = findMany(GET_ALL_FILMS_QUERY, filmMapper);
        for (Film film : films) {
            film.setGenres(findMany(GET_GENRES_BY_FILM_ID_QUERY, genreMapper, film.getId()));
            film.setRating(findOne(GET_RATING_BY_ID, ratingMapper, film.getRating().getId())
                    .orElse(null));
        }
        return films;
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        Optional<Film> optionalFilm = findOne(GET_FILM_BY_ID_QUERY, filmMapper, filmId);
        if (optionalFilm.isPresent()) {
            optionalFilm.get().setGenres(findMany(GET_GENRES_BY_FILM_ID_QUERY, genreMapper, optionalFilm.get().getId()));
            optionalFilm.get().setRating(findOne(GET_RATING_BY_ID, ratingMapper, optionalFilm.get().getRating().getId())
                    .orElse(null));
        }
        return optionalFilm;
    }

    @Override
    public void addLikeToFilm(Integer filmId, Integer userId) {
        insertWithoutReturningKey(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void removeLikeFromFilm(Integer filmId, Integer userId) {
        delete(REMOVE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public Collection<Film> getFilmsByPopularity(int maxNumber) {
        List<Film> films = findMany(GET_POPULAR_FILMS_QUERY, filmMapper, maxNumber);
        for (Film film : films) {
            film.setGenres(findMany(GET_GENRES_BY_FILM_ID_QUERY, genreMapper, film.getId()));
            film.setRating(findOne(GET_RATING_BY_ID, ratingMapper, film.getRating().getId())
                    .orElse(null));
        }
        return films;
    }

    @Override
    public Integer getLikesByFilmId(Integer filmId) {
        return jdbc.queryForObject(GET_LIKES_COUNT_FOR_FILM_QUERY, Integer.class, filmId);
    }
}
