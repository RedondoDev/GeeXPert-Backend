package com.redondodev.GeeXPert_back.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class GameDTO {

    private Integer id;
    private String name;
    private LocalDate releaseDate;
    private String genresIds;
    private String platformsIds;
    private String coverId;
    private double rating;

}
