package com.redondodev.GeeXPert_back.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class GameDTO {

    private Integer id;
    private String name;
    private Date releaseDate;
    private String genresIds;
    private String platformsIds;
    private String coverId;
    private double rating;

}
