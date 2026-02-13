package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService {
    final RatingStorage ratingStorage;

    public Collection<Rating> getAllRatings() {
        return ratingStorage.getAllRatings();
    }

    public Rating getRatingById(Integer id) {
        Optional<Rating> rating = ratingStorage.getRatingById(id);
        if (rating.isEmpty()) {
            log.warn("Ошибка при получении рейтинга: Не найден рейтинг с id:{}", id);
            throw new NotFoundException("Не найден рейтинг с id=" + id);
        }
        return rating.get();
    }
}
