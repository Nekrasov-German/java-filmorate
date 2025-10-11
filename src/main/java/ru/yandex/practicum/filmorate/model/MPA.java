package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MPA {
    @NotNull
    private Long id;
    private String name;
    private int ageLimit;
}
