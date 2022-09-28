package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FilmDbStorage implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private static final String GET_ALL =
            "SELECT films.id, films.name, films.description, films.duration, films.release_date, rate, mpa " +
                    "FROM films " +
                    "JOIN mpa_rating ON films.mpa = mpa_rating.id";
    private static final String GET_BY_ID = "SELECT " +
            "films.id, films.name, films.description, films.duration, films.release_date, rate, mpa  " +
            "FROM films INNER JOIN mpa_rating ON films.mpa = mpa_rating.id " +
            "WHERE films.id =?";
    private static final String CREATE = "INSERT INTO " +
            "films (name, description, release_date, duration, rate, mpa) " +
            "VALUES (?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa = ? " +
            "WHERE id =?";
    private static final String DELETE = "DELETE FROM films WHERE id = ?";
    private static final String DELETE_ALL = "DELETE FROM films";
    private static final String GENRE_MAPPER = "SELECT genres.id, genres.name " +
            "FROM genres INNER JOIN film_genre ON genres.id = film_genre.genre_id WHERE film_id =?";
    private static final String MPA_MAPPER = " SELECT * FROM mpa_rating WHERE id =?";
    private static final String LIKE_MAPPER = "SELECT user_id FROM film_likes WHERE film_id =?";


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRate());
            ps.setInt(6, film.getMpa().getId());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        film.setId(key.longValue());
        if (film.getGenres() != null) {
            film.getGenres()
                    .forEach(x -> jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)",
                            film.getId(), x.getId()));
        }
        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        jdbcTemplate.update(UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        String sqlDelete = "DELETE FROM film_genre WHERE film_id =?";
        jdbcTemplate.update(sqlDelete, film.getId());
        if (!film.getGenres().isEmpty()) {
            String sqlInsert = "INSERT INTO film_genre VALUES (?,?)";
            List<Genre> uniqueGenre = film.getGenres().stream()
                    .distinct()
                    .collect(Collectors.toList());
            uniqueGenre.forEach(x -> jdbcTemplate.update(sqlInsert, film.getId(), x.getId()));
            film.setGenres(uniqueGenre);
        }
        return Optional.of(new Film());
    }

    @Override
    public List<Film> findAllFilms() {
        return jdbcTemplate.query(GET_ALL, (rs, rowNum) -> new Film().toBuilder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(mpaMapping(rs.getInt("MPA")))
                .genres(genreMapper(rs.getInt("id")))
                .likes(likesMapper(rs.getInt("id")))
                .build());
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        return jdbcTemplate.query(GET_BY_ID, (rs, rowNum) -> new Film().toBuilder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(mpaMapping(rs.getInt("MPA")))
                .genres(genreMapper(rs.getInt("id")))
                .likes(likesMapper(rs.getLong("id")))
                .build(), id).stream().findAny();
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL);
    }

    public List<Genre> genreMapper(int id) {
        return jdbcTemplate.query(GENRE_MAPPER, new BeanPropertyRowMapper<>(Genre.class), id);
    }

    public Mpa mpaMapping(int id) {
        return jdbcTemplate.query(MPA_MAPPER, new BeanPropertyRowMapper<>(Mpa.class), id)
                .stream().findFirst().orElse(null);
    }

    public List<Long> likesMapper(long id) {
        return jdbcTemplate.queryForList(LIKE_MAPPER, Long.TYPE, id);
    }
}
