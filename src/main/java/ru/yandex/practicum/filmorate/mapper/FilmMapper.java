package ru.yandex.practicum.filmorate.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbcTemplate;
    private static final String GENRE_MAPPER = "SELECT genres.id, genres.name " +
            "FROM genres INNER JOIN film_genre ON genres.id = film_genre.genre_id WHERE film_id =?";
    private static final String MPA_MAPPER = " SELECT * FROM mpa_rating WHERE id =?";
    private static final String LIKE_MAPPER = "SELECT user_id FROM film_likes WHERE film_id =?";

    @Autowired
    public FilmMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film().toBuilder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(mpaMapping(rs.getInt("MPA")))
                .likes(likesMapper(rs.getInt("id")))
                .build();
    }

    public List<Genre> genreMapper(long id) {
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
