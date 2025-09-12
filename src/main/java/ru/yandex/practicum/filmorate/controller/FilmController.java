package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final UserStorage userStorage;

    public FilmController(FilmStorage filmStorage, FilmService filmService, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/popular")
    public Collection<Film> findPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        if (count <= 0) {
            throw new ValidationException("Количество фильмов должно быть больше ноля");
        }
        return filmService.getAllLikes(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putLikeFilm(@PathVariable long id, @PathVariable long userId) {
        return filmService.addLike(userStorage.findById(userId), filmStorage.findById(id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeFilm(@PathVariable long id, @PathVariable long userId) {
        return filmService.deleteLike(userStorage.findById(userId), filmStorage.findById(id));
    }

    @DeleteMapping
    public void delete(Film film) {
        filmStorage.delete(film);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        log.error("Ошибка валидации при создании фильма", ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
            log.error("Ошибка в поле {}: {}", error.getField(), error.getDefaultMessage());
        });

        throw new jakarta.validation.ValidationException(errors.toString());
    }
}
