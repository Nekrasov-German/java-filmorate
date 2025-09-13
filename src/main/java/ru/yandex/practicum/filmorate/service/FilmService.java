package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
public class FilmService implements FilmStorage {
    private final FilmStorage filmStorage;
    private final Comparator<Film> saveComparator = (f1, f2) -> {
        int size1 = f1.getLike() != null ? f1.getLike().size() : 0;
        int size2 = f2.getLike() != null ? f2.getLike().size() : 0;
        return Integer.compare(size1, size2);
    };

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
        Set<Film> actualFilms = new TreeSet<>(saveComparator.reversed());
        actualFilms.addAll(filmStorage.findAll());
        return actualFilms.stream()
                .limit(count)
                .toList();
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public void delete(Film film) {
        filmStorage.delete(film);
    }

    @Override
    public Film findById(Long id) {
        return filmStorage.findById(id);
    }
}
