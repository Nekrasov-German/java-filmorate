package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class InMemoryFilmStorage  implements FilmStorage {
    private static final AtomicInteger AUTO_ID = new AtomicInteger(1);
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895,12,28);
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    @Override
    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        film.setId(AUTO_ID.getAndIncrement());
        films.put(film.getId(),film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(),film);
        } else {
            throw new NotFoundException("Фильм не найден.");
        }
        return film;
    }

    @Override
    public void delete(Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
        } else {
            throw new NotFoundException("Фильм не найден.");
        }
    }
}
