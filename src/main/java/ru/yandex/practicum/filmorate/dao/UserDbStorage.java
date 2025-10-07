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
    private final String FIND_ALL = "SELECT * FROM users;";
    private final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?;";
    private final String CREATE_USER = "INSERT INTO users (email, login, name, birthday) " +
            "VALUES (?,?,?,?);";
    private final String UPDATE_USER = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private final String DELETE_USER = "DELETE users WHERE id = ?";
    private final String ADD_FRIEND = "INSERT INTO user_friends (user_id, friend_id) VALUES (?,?);";
    private final String DELETE_FRIEND = "DELETE user_friends WHERE user_id = ? AND friend_id = ?;";
    private final String GET_FRIENDS_BY_ID = "SELECT u.* FROM users u " +
            "JOIN user_friends uf ON uf.friend_id = u.id " +
            "WHERE uf.user_id = ?;";
    private final String GET_FRIEND_IDS = "SELECT friend_id FROM user_friends WHERE user_id = ?;";
    private final String GET_USERS_LIKE_FILM = "SELECT u.* FROM users u JOIN film_likes fl ON u.id = fl.user_id " +
            "WHERE fl.film_id = ?;";

    private final UserMapper userMapper;
    private final RowMapper<Long> friendIdMapper;

    public UserDbStorage(JdbcTemplate jdbc, UserMapper userMapper) {
        super(jdbc, userMapper);
        this.userMapper = userMapper;
        this.friendIdMapper = userMapper::mapFriendId;
    }

    public Collection<User> findUsersByLike(Long id) {
        return findMany(GET_USERS_LIKE_FILM, id);
    }

    @Override
    public Collection<User> findAll() {
        return findMany(FIND_ALL);
    }

    @Override
    public User create(User user) {
        long id = insert(
                CREATE_USER,
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
        if (findOne(FIND_BY_ID, user.getId()).isPresent()) {
            update(
                    UPDATE_USER,
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
        delete(DELETE_USER, user.getId());
    }

    @Override
    public Optional<User> findById(Long id) {
        return findOne(FIND_BY_ID,id);
    }

    @Override
    public void addFriend(Long user_id, Long friend_id) {
        update(ADD_FRIEND,user_id,friend_id);
    }

    public void deleteFriend(Long userId, Long friendId) {
        delete(DELETE_FRIEND,userId,friendId);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        if (findById(id).isEmpty()) {
            throw new NotFoundException("Такого пользователя не существует.");
        }
        return findMany(GET_FRIENDS_BY_ID,id);
    }

    @Override
    public Collection<Long> getFriendIds(Long userId) {
        return jdbc.query(GET_FRIEND_IDS, friendIdMapper, userId);
    }
}
