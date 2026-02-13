package ru.yandex.practicum.filmorate.storage.memory;

import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class InMemoryRatingStorage implements RatingStorage {
    private final Map<Integer, Rating> ratingMap;

    public InMemoryRatingStorage() {
        ratingMap = Map.of(
                1,new Rating(1, "G"),
                2,new Rating(2, "PG"),
                3,new Rating(3, "PG-13"),
                4,new Rating(4, "R"),
                5,new Rating(5, "NC-17")
        );
    }

    @Override
    public Collection<Rating> getAllRatings() {
        return ratingMap.values();
    }

    @Override
    public Optional<Rating> getRatingById(Integer ratingId) {
        return Optional.of(ratingMap.get(ratingId));
    }
}
