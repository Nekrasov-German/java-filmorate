package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
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
        return userStorage.findById(userId)
                .getFriends()
                .stream()
                .map(userStorage::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Collection<User> getAllCommonFriends(Long userId, Long friendId) {
        Set<User> friendsUserId = userStorage.findById(userId)
                .getFriends()
                .stream()
                .map(userStorage::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<User> friendsFriendsId = userStorage.findById(friendId)
                .getFriends()
                .stream()
                .map(userStorage::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        friendsUserId.retainAll(friendsFriendsId);
        return friendsUserId;
    }
}
