package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.dao.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.util.Optional;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate") // Добавляем сканирование компонентов
@Import({UserDbStorage.class, FilmDbStorage.class, UserMapper.class, FilmMapper.class}) // Импортируем маппер
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmDbStorage;
	private final UserMapper userMapper;
	private final FilmMapper filmMapper;

	private User testUser;
	private Film testFilm;

	@BeforeEach
	public void setUp() {
		MPA mpaTest = new MPA();
		mpaTest.setId(1L);

		testUser = new User();
		testUser.setEmail("test@example.com");
		testUser.setLogin("testlogin");
		testUser.setName("TestUser");
		testUser.setBirthday(LocalDate.now().minusYears(18));

		testFilm = new Film();
		testFilm.setName("test");
		testFilm.setDescription("description");
		testFilm.setReleaseDate(LocalDate.of(1900, 1, 1));
		testFilm.setDuration(Duration.ofMinutes(100));
		testFilm.setMpa(mpaTest);
	}

	@Test
	public void testFindUserById() throws InterruptedException {
		userStorage.create(testUser);

		Optional<User> userOptional = userStorage.findById(1L);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);
	}

	@Test
	public void testFindFilmById() throws InterruptedException {
		filmDbStorage.create(testFilm);
		Optional<Film> userOptional = filmDbStorage.findById(1L);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);
	}
}
