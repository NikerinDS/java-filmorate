package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class GenreDbStorage extends BaseDbStorage implements GenreStorage {
    private static final String GET_ALL_GENRES_QUERY = "SELECT * FROM genres";
    private static final String GET_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";

    private final RowMapper<Genre> genreMapper;

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc);
        this.genreMapper = mapper;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return findMany(GET_ALL_GENRES_QUERY, genreMapper);
    }

    @Override
    public Optional<Genre> getGenreById(Integer genreId) {
        return findOne(GET_GENRE_BY_ID_QUERY, genreMapper, genreId);
    }
}
