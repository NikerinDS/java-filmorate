package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes = {UserDbStorage.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    void createUserTest() {
        User user = new User(0,
                "test@box",
                "nik",
                "name",
                LocalDate.of(1980, 1, 1));
        User createdUser = userStorage.createUser(user);
        assertAll(
                () -> assertNotEquals(0, createdUser.getId()),
                () -> assertEquals(user.getName(), createdUser.getName()),
                () -> assertEquals(user.getEmail(), createdUser.getEmail()),
                () -> assertEquals(user.getLogin(), createdUser.getLogin()),
                () -> assertEquals(user.getBirthday(), createdUser.getBirthday())
        );
    }

    @Test
    void getUserByIdTest() {
        User user = new User(0,
                "test@box",
                "nik",
                "name",
                LocalDate.of(1980, 1, 1));
        User createdUser = userStorage.createUser(user);
        Optional<User> userById = userStorage.getUserById(createdUser.getId());
        assertTrue(userById.isPresent());
        assertAll(
                () -> assertEquals(user.getName(), userById.get().getName()),
                () -> assertEquals(user.getEmail(), userById.get().getEmail()),
                () -> assertEquals(user.getLogin(), userById.get().getLogin()),
                () -> assertEquals(user.getBirthday(), userById.get().getBirthday())
        );
    }

    @Test
    void getAllUsersTest() {
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
        User user3 = new User(0,
                "test3@box",
                "nik3",
                "name3",
                LocalDate.of(1980, 1, 1));
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);
        Collection<User> users = userStorage.getAllUsers();
        assertEquals(3, users.size());
    }

    @Test
    void updateUserTest() {
        User user = new User(0,
                "test@box",
                "nik",
                "name",
                LocalDate.of(1980, 1, 1));
        User createdUser = userStorage.createUser(user);
        createdUser.setName("newName");
        createdUser.setLogin("newLogin");
        createdUser.setEmail("newEmail@box");
        createdUser.setBirthday(LocalDate.of(2000,1,1));
        User updatedUser = userStorage.updateUser(createdUser);
        assertAll(
                () -> assertEquals(createdUser.getName(), updatedUser.getName()),
                () -> assertEquals(createdUser.getEmail(), updatedUser.getEmail()),
                () -> assertEquals(createdUser.getLogin(), updatedUser.getLogin()),
                () -> assertEquals(createdUser.getBirthday(), updatedUser.getBirthday())
        );
    }

    @Test
    void deleteUserTest() {
        User user = new User(0,
                "test@box",
                "nik",
                "name",
                LocalDate.of(1980, 1, 1));
        User createdUser = userStorage.createUser(user);
        userStorage.deleteUser(createdUser);
        Optional<User> optionalUser = userStorage.getUserById(createdUser.getId());
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    void getFriendsByUserIdTest() {
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
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        userStorage.addFriendToUser(user1.getId(), user2.getId());
        List<User> friends =  userStorage.getFriendsByUserId(user1.getId()).stream().toList();
        assertEquals(1, friends.size());
        User friend = friends.getFirst();
        assertEquals(user2.getId(), friend.getId());
    }

    @Test
    void getMutualFriendsByUsersIdTest() {
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
        User user3 = new User(0,
                "test3@box",
                "nik3",
                "name3",
                LocalDate.of(1980, 1, 1));
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        user3 = userStorage.createUser(user3);
        userStorage.addFriendToUser(user1.getId(), user2.getId());
        userStorage.addFriendToUser(user1.getId(), user3.getId());
        userStorage.addFriendToUser(user2.getId(), user3.getId());
        List<User> mutualFriends = userStorage.getMutualFriendsByUsersId(user1.getId(), user2.getId()).stream().toList();
        assertEquals(1, mutualFriends.size());
        User friend = mutualFriends.getFirst();
        assertEquals(user3.getId(), friend.getId());
    }
}