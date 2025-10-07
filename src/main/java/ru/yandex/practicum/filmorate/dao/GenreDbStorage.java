package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class GenreDbStorage extends BaseDbStorage<Genre> {
    private final String FIND_ALL = "SELECT * FROM genre ORDER BY id;";
    private final String FIND_BY_ID = "SELECT * FROM genre WHERE id = ?;";
    private final String FIND_BY_FILM_ID = "SELECT g.* FROM genre g JOIN film_genres fg ON g.id = fg.genre_id " +
            "WHERE fg.film_id = ?;";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAllGenre() {
        return findMany(FIND_ALL);
    }

    public Optional<Genre> findById(Long id) {
        return findOne(FIND_BY_ID,id);
    }

    public List<Genre> findGenreByFilmId(Long filmId) {
        return findMany(FIND_BY_FILM_ID, filmId);
    }
}
