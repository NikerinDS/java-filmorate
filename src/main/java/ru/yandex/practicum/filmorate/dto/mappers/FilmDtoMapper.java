package ru.yandex.practicum.filmorate.dto.mappers;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmDtoMapper {
    public static FilmDto filmToDto(Film film, Integer likes) {
        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                RatingDtoMapper.ratingToDto(film.getRating()),
                film.getGenres().stream().map(GenreDtoMapper::genreToDto).toList(),
                likes
        );
    }

    public static Film filmFromDto(FilmDto filmDto) {
        return new Film(
                filmDto.getId(),
                filmDto.getName(),
                filmDto.getDescription(),
                filmDto.getReleaseDate(),
                filmDto.getDuration(),
                (filmDto.getMpa() == null) ? null : RatingDtoMapper.ratingFromDto(filmDto.getMpa()),
                filmDto.getGenres().stream().map(GenreDtoMapper::genreFromDto).toList()
        );
    }
}
