package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes = {FilmDbStorage.class, FilmRowMapper.class, GenreRowMapper.class, RatingRowMapper.class,
        UserDbStorage.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    void createFilmTest() {
        Film film = new Film(
                0,
                "name",
                "description",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        Film createdFilm = filmDbStorage.createFilm(film);
        assertAll(
                () -> assertNotEquals(0, createdFilm.getId()),
                () -> assertEquals(film.getName(), createdFilm.getName()),
                () -> assertEquals(film.getDescription(), createdFilm.getDescription()),
                () -> assertEquals(film.getReleaseDate(), createdFilm.getReleaseDate()),
                () -> assertEquals(film.getDuration(), createdFilm.getDuration()),
                () -> assertEquals(film.getRating().getId(), createdFilm.getRating().getId()),
                () -> assertTrue(createdFilm.getGenres().stream().map(Genre::getId).toList().containsAll(List.of(3, 4)))
        );
    }

    @Test
    void getFilmByIdTest() {
        Film film = new Film(
                0,
                "name",
                "description",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        Film createdFilm = filmDbStorage.createFilm(film);
        Optional<Film> optionalFilm = filmDbStorage.getFilmById(createdFilm.getId());
        assertTrue(optionalFilm.isPresent());
        assertAll(
                () -> assertEquals(film.getName(), optionalFilm.get().getName()),
                () -> assertEquals(film.getDescription(), optionalFilm.get().getDescription()),
                () -> assertEquals(film.getReleaseDate(), optionalFilm.get().getReleaseDate()),
                () -> assertEquals(film.getDuration(), optionalFilm.get().getDuration()),
                () -> assertEquals(film.getRating().getId(), optionalFilm.get().getRating().getId()),
                () -> assertTrue(optionalFilm.get().getGenres().stream().map(Genre::getId).toList().containsAll(List.of(3, 4)))
        );
    }

    @Test
    void getAllFilmsTest() {
        Film film1 = new Film(
                0,
                "name1",
                "description1",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        Film film2 = new Film(
                0,
                "name2",
                "description2",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        Film film3 = new Film(
                0,
                "name3",
                "description3",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        filmDbStorage.createFilm(film1);
        filmDbStorage.createFilm(film2);
        filmDbStorage.createFilm(film3);
        List<Film> films = filmDbStorage.getAllFilms().stream().toList();
        assertEquals(3, films.size());
    }

    @Test
    void updateFilmTest() {
        Film film = new Film(
                0,
                "name",
                "description",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        Film createdFilm = filmDbStorage.createFilm(film);
        createdFilm.setRating(new Rating(2,""));
        createdFilm.setName("newName");
        createdFilm.setDescription("newDescription");
        Film updatedFilm = filmDbStorage.updateFilm(createdFilm);
        assertAll(
                () -> assertEquals(createdFilm.getName(), updatedFilm.getName()),
                () -> assertEquals(createdFilm.getDescription(), updatedFilm.getDescription()),
                () -> assertEquals(createdFilm.getReleaseDate(), updatedFilm.getReleaseDate()),
                () -> assertEquals(createdFilm.getDuration(), updatedFilm.getDuration()),
                () -> assertEquals(createdFilm.getRating().getId(), updatedFilm.getRating().getId()),
                () -> assertTrue(updatedFilm.getGenres().stream().map(Genre::getId).toList().containsAll(List.of(3, 4)))
        );
    }

    @Test
    void deleteFilmTest() {
        Film film = new Film(
                0,
                "name",
                "description",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        Film createdFilm = filmDbStorage.createFilm(film);
        filmDbStorage.deleteFilm(createdFilm);
        Optional<Film> optionalFilm = filmDbStorage.getFilmById(createdFilm.getId());
        assertTrue(optionalFilm.isEmpty());
    }

    @Test
    void likesTest() {
        Film film = new Film(
                0,
                "name",
                "description",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        film = filmDbStorage.createFilm(film);
        User user1 = new User(0,
                "test1@box",
                "nik1",
                "name1",
                LocalDate.of(1980, 1, 1));
        User user2 = new User(0,
                "test2@box",
                "nik2",
                "name2",
                LocalDate.of(1980, 1, 1));
        user1 = userDbStorage.createUser(user1);
        user2 = userDbStorage.createUser(user2);
        filmDbStorage.addLikeToFilm(film.getId(), user1.getId());
        filmDbStorage.addLikeToFilm(film.getId(), user2.getId());
        assertEquals(2, filmDbStorage.getLikesByFilmId(film.getId()));

        filmDbStorage.removeLikeFromFilm(film.getId(), user2.getId());
        assertEquals(1, filmDbStorage.getLikesByFilmId(film.getId()));
    }

    @Test
    void getFilmsByPopularityTest() {
        Film film1 = new Film(
                0,
                "name1",
                "description1",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        Film film2 = new Film(
                0,
                "name2",
                "description2",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        Film film3 = new Film(
                0,
                "name3",
                "description3",
                LocalDate.of(2020, 1, 1),
                100,
                new Rating(5, ""),
                List.of(new Genre(3, ""), new Genre(4, ""))
        );
        film1 = filmDbStorage.createFilm(film1);
        film2 = filmDbStorage.createFilm(film2);
        film3 = filmDbStorage.createFilm(film3);
        User user1 = new User(0,
                "test1@box",
                "nik1",
                "name1",
                LocalDate.of(1980, 1, 1));
        User user2 = new User(0,
                "test2@box",
                "nik2",
                "name2",
                LocalDate.of(1980, 1, 1));
        user1 = userDbStorage.createUser(user1);
        user2 = userDbStorage.createUser(user2);
        filmDbStorage.addLikeToFilm(film3.getId(), user1.getId());
        filmDbStorage.addLikeToFilm(film3.getId(), user2.getId());
        filmDbStorage.addLikeToFilm(film1.getId(), user1.getId());

        Collection<Film> popularFilms = filmDbStorage.getFilmsByPopularity(2);
        assertEquals(2, popularFilms.size());
        assertIterableEquals(List.of(film3.getId(), film1.getId()),
                popularFilms.stream().map(Film::getId).toList());
    }
}