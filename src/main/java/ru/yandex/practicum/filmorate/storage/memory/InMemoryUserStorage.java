package ru.yandex.practicum.filmorate.storage.memory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();
    private final IdGenerator idGenerator;

    @Override
    public User createUser(User user) {
        user.setId(idGenerator.getNextId());
        users.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            return null;
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            friends.remove(user.getId());
            return users.remove(user.getId());
        }
        return null;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void addFriendToUser(Integer userId, Integer friendId) {
        friends.get(userId).add(friendId);
    }

    @Override
    public void removeFriendFromUser(Integer userId, Integer friendId) {
        friends.get(userId).remove(friendId);
    }

    @Override
    public Collection<User> getFriendsByUserId(Integer id) {
        return friends.get(id).stream().map(users::get).toList();
    }

    @Override
    public Collection<User> getMutualFriendsByUsersId(Integer id1, Integer id2) {
        Set<Integer> fiendsId = new HashSet<>(friends.get(id1));
        fiendsId.retainAll(friends.get(id2));
        return fiendsId.stream().map(users::get).toList();
    }
}
