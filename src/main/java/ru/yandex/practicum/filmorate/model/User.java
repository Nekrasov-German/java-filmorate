package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private long id;
    @Email(message = "Некорректный email")
    private String email;
    @NotNull(message = "Login не может быть пустым")
    @NotBlank(message = "Login не может быть пустым")
    private String login;
    private String name;
    @NotNull(message = "Дата рождения не может быть пустой")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public String getDisplayName() {
        return (name != null && !name.isEmpty()) ? name : login;
    }
}
