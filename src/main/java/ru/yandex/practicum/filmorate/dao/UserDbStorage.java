package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@Qualifier("userDbStorage")
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private final String findAll = "SELECT * FROM users;";
    private final String findById = "SELECT * FROM users WHERE id = ?;";
    private final String createUser = "INSERT INTO users (email, login, name, birthday) " +
            "VALUES (?,?,?,?);";
    private final String updateUser = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private final String deleteUser = "DELETE users WHERE id = ?";
    private final String addFriend = "INSERT INTO user_friends (user_id, friend_id) VALUES (?,?);";
    private final String deleteFriend = "DELETE user_friends WHERE user_id = ? AND friend_id = ?;";
    private final String getFriendsById = "SELECT u.* FROM users u " +
            "JOIN user_friends uf ON uf.friend_id = u.id " +
            "WHERE uf.user_id = ?;";
    private final String getFriendIds = "SELECT friend_id FROM user_friends WHERE user_id = ?;";
    private final String getUsersLikeFilm = "SELECT u.* FROM users u JOIN film_likes fl ON u.id = fl.user_id " +
            "WHERE fl.film_id = ?;";

    private final UserMapper userMapper;
    private final RowMapper<Long> friendIdMapper;

    public UserDbStorage(JdbcTemplate jdbc, UserMapper userMapper) {
        super(jdbc, userMapper);
        this.userMapper = userMapper;
        this.friendIdMapper = userMapper::mapFriendId;
    }

    public Collection<User> findUsersByLike(Long id) {
        return findMany(getUsersLikeFilm, id);
    }

    @Override
    public Collection<User> findAll() {
        return findMany(findAll);
    }

    @Override
    public User create(User user) {
        long id = insert(
                createUser,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        if (findOne(findById, user.getId()).isPresent()) {
            update(
                    updateUser,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId()
            );
            return user;
        } else {
            throw new NotFoundException("пользователь не найден");
        }
    }

    @Override
    public void delete(User user) {
        delete(deleteUser, user.getId());
    }

    @Override
    public Optional<User> findById(Long id) {
        return findOne(findById, id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        update(addFriend, userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        delete(deleteFriend, userId, friendId);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        if (findById(id).isEmpty()) {
            throw new NotFoundException("Такого пользователя не существует.");
        }
        return findMany(getFriendsById, id);
    }

    @Override
    public Collection<Long> getFriendIds(Long userId) {
        return jdbc.query(getFriendIds, friendIdMapper, userId);
    }
}
