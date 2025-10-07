package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class MPADbStorage extends BaseDbStorage<MPA> {
    private final String FIND_ALL = "SELECT * FROM mpa;";
    private final String FIND_BY_ID = "SELECT * FROM mpa WHERE id = ?;";

    public MPADbStorage(JdbcTemplate jdbc, RowMapper<MPA> mapper) {
        super(jdbc, mapper);
    }

    public List<MPA> findAllMPA() {
        return findMany(FIND_ALL);
    }

    public Optional<MPA> findById(Long id) {
        return findOne(FIND_BY_ID,id);
    }
}
