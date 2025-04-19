package com.redondodev.GeeXPert_back.user_games;

import com.redondodev.GeeXPert_back.game.Game;
import com.redondodev.GeeXPert_back.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGameRepository extends JpaRepository<UserGame, Integer> {

    boolean existsByUserAndGame(User user, Game game);

}
