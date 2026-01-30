package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    User deleteUser(User user);

    Collection<User> getAllUsers();

    Optional<User> getUserById(Integer id);

    void addFriendToUser(Integer userId, Integer friendId);

    void removeFriendFromUser(Integer userId, Integer friendId);

    Collection<User> getFriendsByUserId(Integer id);

    Collection<User> getMutualFriendsByUsersId(Integer id1, Integer id2);
}




