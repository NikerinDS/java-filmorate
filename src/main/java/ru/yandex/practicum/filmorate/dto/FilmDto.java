package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class FilmDto {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private RatingDto mpa;
    private List<GenreDto> genres;
    private Integer likes;
}
