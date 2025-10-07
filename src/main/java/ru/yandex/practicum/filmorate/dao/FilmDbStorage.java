package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    @Autowired
    private MPADbStorage mpaDbStorage;
    @Autowired
    private GenreDbStorage genreDbStorage;
    @Autowired
    private UserDbStorage userDbStorage;

    private final String requestFindAllFilms = "SELECT * FROM films;";
    private final String findByIdFilms = "SELECT * FROM films WHERE id = ?;";
    private final String requestCreateFilm = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
            "VALUES (?,?,?,?,?);";
    private final String updatefilm = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
            "WHERE id = ?";
    private final String deletefilm = "DELETE films WHERE id = ?";
    private final String insertgenre = "INSERT INTO film_genres (film_id, genre_id) VALUES (?,?);";
    private final String deletegenres = "DELETE FROM film_genres WHERE film_id = ?;";
    private final String insertLike = "INSERT INTO film_likes (film_id, user_id) VALUES (?,?);";
    private final String deleteLike = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?;";
    private final String findFilmsByRating = "SELECT \n" +
            "    f.id,\n" +
            "    f.name,\n" +
            "    f.description,\n" +
            "    f.release_date,\n" +
            "    f.duration,\n" +
            "    f.mpa_id,\n" +
            "    COUNT(fl.user_id) as likes_count\n" +
            "FROM films f\n" +
            "LEFT JOIN film_likes fl ON f.id = fl.film_id\n" +
            "GROUP BY f.id, f.name, f.description, f.release_date, f.duration\n" +
            "ORDER BY likes_count DESC\n" +
            "LIMIT ?;";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Film> findFilmsByLike(int count) {
        return findMany(findFilmsByRating,count);
    }

    public void addLike(Long filmId, Long userId) {
        update(insertLike, filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        delete(deleteLike, filmId, userId);
    }

    public Set<Genre> getGenreById(Long id) {
        return new HashSet<>(genreDbStorage.findGenreByFilmId(id));
    }

    @Override
    public void addGenres(Long filmId, Set<Genre> genreId) {
        genreId.forEach(g -> update(insertgenre,filmId,g.getId()));
    }

    @Override
    public void removeGenres(Long filmId) {
        delete(deletegenres,filmId);
    }

    @Override
    public Collection<Film> findAll() {
        return findMany(requestFindAllFilms);
    }

    @Override
    public Film create(Film film) {
        Long idMPA = film.getMpa().getId();
        Optional<MPA> mpa = mpaDbStorage.findById(idMPA);
        if (mpa.isEmpty()) {
            throw new NotFoundException("Такого рейтинга не найдено");
        }
        Set<Genre> genres = film.getGenres();
        for (Genre genre : genres) {
            Optional<Genre> result = genreDbStorage.findById(genre.getId());
            if (result.isEmpty()) {
                throw new NotFoundException("Такого жанра не существует.");
            }
        }
        long id = insert(
                requestCreateFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                mpa.get().getId()
        );
        film.setId(id);
        addGenres(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (findOne(findByIdFilms, film.getId()).isPresent()) {
            Long idMPA = film.getMpa().getId();
            Optional<MPA> mpa = mpaDbStorage.findById(idMPA);
            if (mpa.isEmpty()) {
                throw new NotFoundException("Такого рейтинга не найдено");
            }
            Set<Genre> genres = film.getGenres();
            for (Genre genre : genres) {
                Optional<Genre> result = genreDbStorage.findById(genre.getId());
                if (result.isEmpty()) {
                    throw new NotFoundException("Такого жанра не существует.");
                }
            }
            update(
                    updatefilm,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration().toMinutes(),
                    mpa.get().getId(),
                    film.getId()
            );
            addGenres(film.getId(), film.getGenres());
            return film;
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    @Override
    public void delete(Film film) {
        removeGenres(film.getId());
        delete(deletefilm, film.getId());
    }

    @Override
    public Optional<Film> findById(Long id) {
        Optional<Film> film = findOne(findByIdFilms,id);
        film.get().setGenres(getGenreById(id));
        Set<Long> userIds = userDbStorage.findUsersByLike(id).stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        film.get().setLikes(userIds);
        return film;
    }
}
