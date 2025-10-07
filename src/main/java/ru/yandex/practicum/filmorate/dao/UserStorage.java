package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> findAll();

    User create(User user);

    User update(User user);

    void delete(User user);

    Optional<User> findById(Long id);

    void addFriend(Long user_id, Long friend_id);

    void deleteFriend(Long userId, Long friendId);

    Collection<User> getFriends(Long id);

    Collection<Long> getFriendIds(Long id);

}
