package com.redondodev.GeeXPert_back.game;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {

    Optional<Game> findByName(String name);

}
