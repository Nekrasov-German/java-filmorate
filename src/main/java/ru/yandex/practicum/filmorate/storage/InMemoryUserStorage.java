package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static final AtomicInteger AUTO_ID = new AtomicInteger(1);
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    @Override
    public User create(User user) {
        user.setName(user.getDisplayName());
        user.setId(AUTO_ID.getAndIncrement());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new NotFoundException("Пользователь не найден.");
        }
        return user;
    }

    @Override
    public void delete(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
