package com.redondodev.GeeXPert_back.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "games")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @Column(name = "game_id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "platforms")
    private String platformsIds;

    @Column(name = "genres")
    private String genresIds;

    @Column(name = "cover")
    private String cover;

    @Column(name = "rating")
    private double rating;

    @Column(name = "first_release_date")
    private Long firstReleaseDate;

}
