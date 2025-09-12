package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLike(User user, Film film) {
        film.getLike().add(user.getId());
        return filmStorage.update(film);
    }

    public Film deleteLike(User user, Film film) {
        film.getLike().remove(user.getId());
        return filmStorage.update(film);
    }

    public Collection<Film> getAllLikes(int count) {
        Comparator<Film> safeComparator = (f1, f2) -> {
            int size1 = f1.getLike() != null ? f1.getLike().size() : 0;
            int size2 = f2.getLike() != null ? f2.getLike().size() : 0;
            return Integer.compare(size1, size2);
        };
        Set<Film> actualFilms = new TreeSet<>(safeComparator.reversed());
        actualFilms.addAll(filmStorage.findAll());
        return actualFilms.stream()
                .limit(count)
                .toList();
    }
}
