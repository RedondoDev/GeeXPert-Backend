package com.redondodev.GeeXPert_back.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {

    private Integer id;
    private String cover;
    private Long first_release_date;
    private List<String> genres;
    private String name;
    private List<String> platforms;
    private double rating;

}