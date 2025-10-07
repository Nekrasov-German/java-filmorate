package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public Collection<User> getAllCommonFriends(Long userId, Long friendId) {
        Collection<User> userIdFriends = getAllFriends(userId);
        Collection<User> friendIdFriends = getAllFriends(friendId);

        return userIdFriends.stream()
                .filter(friendIdFriends::contains)
                .collect(Collectors.toList());
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void delete(User user) {
        userStorage.delete(user);
    }

    public Optional<User> findById(Long id) {
        return userStorage.findById(id);
    }

    public User addFriend(Long userId, Long friendId) {
        validateUsersExist(userId, friendId);

        userStorage.addFriend(userId, friendId);

        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.getFriends().add(friendId);
        return user;
    }

    public User deleteFriend(Long userId, Long friendId) {
        validateUsersExist(userId, friendId);

        userStorage.deleteFriend(userId, friendId);

        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.getFriends().remove(friendId);
        return user;
    }

    public Collection<Long> getFriendIds(Long userId) {
        return userStorage.getFriendIds(userId);
    }

    public Collection<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    private void validateUsersExist(Long userId, Long friendId) {
        if (!userStorage.findById(userId).isPresent() ||
                !userStorage.findById(friendId).isPresent()) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
