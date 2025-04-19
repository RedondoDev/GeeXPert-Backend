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
    @GeneratedValue
    @Column(name = "game_id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "platforms_ids")
    private String platformsIds;

    @Column(name = "genres_ids")
    private String genresIds;

    @Column(name = "cover_id")
    private String coverId;

    @Column(name = "rating")
    private double rating;

}
