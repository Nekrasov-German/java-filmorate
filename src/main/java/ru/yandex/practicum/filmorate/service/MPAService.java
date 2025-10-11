package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MPADbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.Optional;

@Service
public class MPAService {
    @Autowired
    private MPADbStorage mpaDbStorage;

    public Collection<MPA> findAllMPA() {
        return mpaDbStorage.findAllMPA();
    }

    public Optional<MPA> getMPAById(Long id) {
        if (mpaDbStorage.findById(id).isPresent()) {
            return mpaDbStorage.findById(id);
        } else {
            throw new NotFoundException("MPA не найден.");
        }
    }
}
