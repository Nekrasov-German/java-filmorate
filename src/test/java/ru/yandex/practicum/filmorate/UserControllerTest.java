package ru.yandex.practicum.filmorate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserController userController;
    private ObjectMapper objectMapper;
    private User testUser;
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final UserService userService = new UserService(userStorage);

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();

        objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setLogin("testlogin");
        testUser.setName("TestUser");
        testUser.setBirthday(LocalDate.now().minusYears(18));
    }

    @Test
    void testFindAll_WhenNoUsers_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void testCreate_ValidUser_ReturnsCreatedUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("test@example.com"))
                .andExpect(jsonPath("login").value("testlogin"))
                .andExpect(jsonPath("name").value("TestUser"))
                .andExpect(jsonPath("birthday").exists());
    }

    @Test
    void testCreate_InvalidEmail_ReturnsBadRequest() throws Exception {
        User invalidUser = new User();
        invalidUser.setEmail("invalid-email");
        invalidUser.setLogin("testlogin");
        invalidUser.setName("TestUser");
        invalidUser.setBirthday(LocalDate.now().minusYears(18));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreate_MissingLogin_ReturnsBadRequest() throws Exception {
        User invalidUser = new User();
        invalidUser.setEmail("test@example.com");
        invalidUser.setName("TestUser");
        invalidUser.setBirthday(LocalDate.now().minusYears(18));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreate_FutureBirthday_ReturnsBadRequest() throws Exception {
        User invalidUser = new User();
        invalidUser.setEmail("test@example.com");
        invalidUser.setLogin("testlogin");
        invalidUser.setName("TestUser");
        invalidUser.setBirthday(LocalDate.now().plusYears(1));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }
}

