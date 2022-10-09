package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    private int id;
    @NotBlank
    private String name;

    public Mpa(int id) {
        this.id = id;
    }
}
