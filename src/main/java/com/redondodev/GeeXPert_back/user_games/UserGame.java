package com.redondodev.GeeXPert_back.user_games;

import com.redondodev.GeeXPert_back.game.Game;
import com.redondodev.GeeXPert_back.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_games")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_game_id")
    private Integer user_game_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id",nullable = false)
    private Game game;

    @Column(name = "state", nullable = false)
    private State state;

    public enum State {
        PENDING,
        PLAYING,
        COMPLETED
    }

}
