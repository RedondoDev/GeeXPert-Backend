package com.redondodev.GeeXPert_back.game;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {

    private final GameApiService gameApiService;

    @GetMapping("/top")
    public List<GameDTO> getTopGames() {
        return gameApiService.getTopRatedGames();
    }

    @GetMapping("/trending")
    public List<GameDTO> getTrendingGames() {
        return gameApiService.getTrendingGames();
    }

    @GetMapping("/search")
    public List<GameDTO> searchGames(@RequestParam String name) {
        return gameApiService.getGamesByName(name);
    }

}
