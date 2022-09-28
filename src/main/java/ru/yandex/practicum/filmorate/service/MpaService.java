package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.MpaDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

@Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> findAllMpa(){
        return mpaDbStorage.findAllMpa();
    }

    public Mpa getMpaById(int id){
    if(id <= 0){
        throw new MpaDoesNotExistException("такого жанра нет");
    }
    return mpaDbStorage.getMpaById(id);
    }
}
