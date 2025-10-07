package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Service
public class GenreService {
    @Autowired
    private GenreDbStorage genreDbStorage;

    public Collection<Genre> findAllGenre() {
        return genreDbStorage.findAllGenre();
    }

    public Optional<Genre> getGenreById(long id) {
        if (genreDbStorage.findById(id).isPresent()) {
            return genreDbStorage.findById(id);
        } else {
            throw new NotFoundException("Жанр не найден.");
        }
    }
}
