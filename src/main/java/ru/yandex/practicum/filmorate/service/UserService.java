package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserStorage {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long userId, Long friendId) {
        userStorage.findById(userId).getFriends().add(friendId);
        userStorage.findById(friendId).getFriends().add(userId);
        return userStorage.findById(userId);
    }

    public User deleteFriend(Long userId, Long friendId) {
        userStorage.findById(userId).getFriends().remove(friendId);
        userStorage.findById(friendId).getFriends().remove(userId);
        return userStorage.findById(userId);
    }

    public Collection<User> getAllFriends(Long userId) {
        User user = userStorage.findById(userId);
        return userStorage.findAll()
                .stream()
                .filter(u -> user.getFriends().contains(u.getId()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Collection<User> getAllCommonFriends(Long userId, Long friendId) {
        return userStorage.findAll()
                .stream()
                .filter(u -> userStorage.findById(userId).getFriends().contains(u.getId()))
                .filter(u -> userStorage.findById(friendId).getFriends().contains(u.getId()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public void delete(User user) {
        userStorage.delete(user);
    }

    @Override
    public User findById(Long id) {
        return userStorage.findById(id);
    }
}
