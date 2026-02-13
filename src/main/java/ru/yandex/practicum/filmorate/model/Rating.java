package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Rating {
    private final int id;
    private final String name;
}
