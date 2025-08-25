package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;
import ru.yandex.practicum.filmorate.util.DurationDeserializer;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private long id;
    @NotNull(message = "Название не может быть пустым")
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotNull(message = "Описание не может быть пустым")
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    private LocalDate releaseDate;
    @JsonSerialize(as = Long.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    // геттер для сериализации в минуты
    @JsonIgnore
    public Duration getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public long getDurationInMinutes() {
        return duration.toMinutes();
    }
}
