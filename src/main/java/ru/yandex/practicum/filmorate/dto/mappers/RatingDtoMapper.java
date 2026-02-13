package ru.yandex.practicum.filmorate.dto.mappers;

import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.model.Rating;

public class RatingDtoMapper {
    public static RatingDto ratingToDto(Rating rating) {
        return new RatingDto(rating.getId(), rating.getName());
    }

    public static Rating ratingFromDto(RatingDto ratingDto) {
        return new Rating(ratingDto.getId(), ratingDto.getName());
    }
}
