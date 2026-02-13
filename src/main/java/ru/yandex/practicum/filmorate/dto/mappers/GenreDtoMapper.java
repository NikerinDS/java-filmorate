package ru.yandex.practicum.filmorate.dto.mappers;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreDtoMapper {
    public static GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public static Genre genreFromDto(GenreDto genreDto) {
        return new Genre(genreDto.getId(), genreDto.getName());
    }
}
