package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MPAService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class FilmController {
    private final FilmService filmService;
    private final UserService userService;
    private final GenreService genreService;
    private final MPAService mpaService;

    @GetMapping("/mpa")
    public Collection<MPA> findAllMPA() {
        return mpaService.findAllMPA();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMPAById(@PathVariable long id) {
        return mpaService.getMPAById(id).get();
    }

    @GetMapping("/genres")
    public Collection<Genre> findAllGenre() {
        return genreService.findAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable long id) {
        return genreService.getGenreById(id).get();
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.findById(id).get();
    }

    @GetMapping("/films/popular")
    public Collection<Film> findPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        if (count <= 0) {
            throw new ValidationException("Количество фильмов должно быть больше ноля");
        }
        return filmService.getAllLikes(count);
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film putLikeFilm(@PathVariable long id, @PathVariable long userId) {
        return filmService.addLike(userService.findById(userId).get(), filmService.findById(id).get());
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLikeFilm(@PathVariable long id, @PathVariable long userId) {
        return filmService.deleteLike(userService.findById(userId).get(), filmService.findById(id).get());
    }

    @DeleteMapping("/films")
    public void delete(Film film) {
        filmService.delete(film);
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
