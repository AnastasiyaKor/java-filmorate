package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.MpaDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaService {
    private final MpaDao mpaDao;


    public List<Mpa> findAllMpa() {
        return mpaDao.findAllMpa();
    }

    public Mpa getMpaById(int id) {
        if (id <= 0) {
            throw new MpaDoesNotExistException("такого жанра нет");
        }
        return mpaDao.getMpaById(id);
    }
}
