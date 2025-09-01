package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class FilmControllerTest {
    private MockMvc mockMvc;
    private FilmController filmController;
    private ObjectMapper objectMapper;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        mockMvc = MockMvcBuilders.standaloneSetup(filmController)
                .build();

        objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        testFilm = new Film();
        testFilm.setName("test");
        testFilm.setDescription("description");
        testFilm.setReleaseDate(LocalDate.of(1900,1,1));
        testFilm.setDuration(Duration.ofMinutes(100));
    }

    @Test
    void testFindAll_WhenNoFilms_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void testCreate_ValidFilm_ReturnsCreatedFilm() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testFilm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("test"))
                .andExpect(jsonPath("description").value("description"))
                .andExpect(jsonPath("releaseDate[0]").value(1900))
                .andExpect(jsonPath("releaseDate[1]").value(1))
                .andExpect(jsonPath("releaseDate[2]").value(1))
                .andExpect(jsonPath("duration").value(100));
    }

    @Test
    void testCreate_InvalidFilm_ReturnsBadRequest() throws Exception {
        Film invalidFilm = new Film();
        invalidFilm.setDuration(Duration.ofMinutes(-10));

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidFilm)))
                .andExpect(status().isBadRequest());
    }
}
