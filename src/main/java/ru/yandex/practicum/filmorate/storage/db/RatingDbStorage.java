package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class RatingDbStorage extends BaseDbStorage implements RatingStorage {
    private static final String GET_ALL_GENRES_QUERY = "SELECT * FROM ratings";
    private static final String GET_GENRE_BY_ID_QUERY = "SELECT * FROM ratings WHERE id = ?";

    private final RowMapper<Rating> ratingMapper;

    public RatingDbStorage(JdbcTemplate jdbc, RowMapper<Rating> ratingMapper) {
        super(jdbc);
        this.ratingMapper = ratingMapper;
    }

    @Override
    public Collection<Rating> getAllRatings() {
        return findMany(GET_ALL_GENRES_QUERY, ratingMapper);
    }

    @Override
    public Optional<Rating> getRatingById(Integer genreId) {
        return findOne(GET_GENRE_BY_ID_QUERY, ratingMapper, genreId);
    }
}