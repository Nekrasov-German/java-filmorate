package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895,12,28);
    @Autowired
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    @Autowired
    private GenreDbStorage genreStorage;

    private final Comparator<Film> saveComparator = (f1, f2) -> {
        int size1 = f1.getLikes() != null ? f1.getLikes().size() : 0;
        int size2 = f2.getLikes() != null ? f2.getLikes().size() : 0;
        return Integer.compare(size1, size2);
    };

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLike(User user, Film film) {
        filmStorage.addLike(film.getId(), user.getId());
        return filmStorage.findById(film.getId()).get();
    }

    public Film deleteLike(User user, Film film) {
        filmStorage.deleteLike(film.getId(), user.getId());
        return filmStorage.findById(film.getId()).get();
    }

    public Collection<Film> getAllLikes(int count) {
        return filmStorage.findFilmsByLike(count);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        return filmStorage.update(film);
    }

    public void delete(Film film) {
        filmStorage.delete(film);
    }

    public Optional<Film> findById(Long id) {
        return filmStorage.findById(id);
    }
}
