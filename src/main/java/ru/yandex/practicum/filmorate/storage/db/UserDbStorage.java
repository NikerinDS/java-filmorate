package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class UserDbStorage extends BaseDbStorage implements UserStorage {
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_USER_QUERY = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends(userId, friendId) VALUES (?, ?)";
    private static final String REMOVE_FRIEND_QUERY = "DELETE FROM friends WHERE userId = ? AND friendId = ?";
    private static final String GET_FRIENDS_BY_USER_ID_QUERY = "SELECT users.* FROM friends join users ON friends.friendId = users.id WHERE friends.userId = ?";
    private static final String GET_MUTUAL_FRIENDS_QUERY = "SELECT * FROM users WHERE id IN ( " +
            "SELECT id1 FROM " +
            "(SELECT friendId as id1 FROM friends WHERE userId = ?) as f1 " +
            "JOIN (SELECT friendId as id2 FROM friends WHERE userId = ?) as f2 ON f1.id1 = f2.id2 " +
            ")";

    private final RowMapper<User> userMapper;

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> userMapper) {
        super(jdbc);
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(User user) {
        Integer id = insert(INSERT_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday())
        );
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(UPDATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );
        return user;
    }

    @Override
    public User deleteUser(User user) {
        delete(DELETE_USER_QUERY, user.getId());
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return findMany(GET_ALL_USERS_QUERY, userMapper);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return findOne(GET_USER_BY_ID_QUERY, userMapper, id);
    }

    @Override
    public void addFriendToUser(Integer userId, Integer friendId) {
        insertWithoutReturningKey(ADD_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public void removeFriendFromUser(Integer userId, Integer friendId) {
        delete(REMOVE_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public Collection<User> getFriendsByUserId(Integer id) {
        return findMany(GET_FRIENDS_BY_USER_ID_QUERY, userMapper, id);
    }

    @Override
    public Collection<User> getMutualFriendsByUsersId(Integer id1, Integer id2) {
        return findMany(GET_MUTUAL_FRIENDS_QUERY, userMapper, id1, id2);
    }
}
