package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorage implements LikeDao {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    String UPDATE_RATE = "update films f " +
            "set rate = (select count(l.user_id) from film_likes l where l.film_id = f.id) where f.id = ?";
    private static final String CREATE_LIKES = "INSERT INTO film_likes (film_id, user_id) VALUES (?,?)";
    private static final String DELETE_LIKES = "DELETE FROM film_likes WHERE film_id =? AND user_id =?";
    private static final String POPULAR = "SELECT f.id, f.name, f.description, f.release_date, " +
            "f.duration, f.mpa, mr.name FROM film_likes AS fl RIGHT JOIN films AS f ON fl.film_id = f.id " +
            "LEFT JOIN mpa_rating AS mr ON f.mpa = mr.id " +
            "GROUP BY f.id ORDER BY COUNT(fl.film_id) DESC LIMIT ?";

    private void updateRate(long filmId) {
        jdbcTemplate.update(UPDATE_RATE, filmId);
    }

    //добавление лайка
    @Override
    public void addLikeFilm(long id, long userId) {
        jdbcTemplate.update(CREATE_LIKES, id, userId);
        updateRate(id);
    }

    //удаление лайка
    @Override
    public void deleteLikeFilm(long id, long userId) {
        jdbcTemplate.update(DELETE_LIKES, id, userId);
        updateRate(id);
    }

    //вывод 10 наиболее популярных фильмов по количеству лайков
    @Override
    public List<Film> getListPopularFilms(int count) {
        List<Film> filmList = new ArrayList<>();
        return jdbcTemplate.query(POPULAR, rs -> {
            while (rs.next()) {
                Film film = new Film().toBuilder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .duration(rs.getInt("duration"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .mpa(filmMapper.mpaMapping(rs.getInt("MPA")))
                        .genres(filmMapper.genreMapper(rs.getInt("id")))
                        .likes(filmMapper.likesMapper(rs.getInt("id")))
                        .build();
                filmList.add(film);
            }
            return filmList;
        }, count);
    }
}
