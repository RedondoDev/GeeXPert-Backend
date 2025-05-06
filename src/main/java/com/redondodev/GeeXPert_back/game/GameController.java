package com.redondodev.GeeXPert_back.game;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class GameController {

    private final GameApiService gameApiService;

    @GetMapping("/top")
    public List<GameDTO> getTopGames() {
        return gameApiService.getTopRatedGames();
    }

    @GetMapping("/trending")
    public List<GameDTO> getTrendingGames(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return gameApiService.getTrendingGames(page, size);
    }

    @GetMapping("/explore")
    public List<GameDTO> getExploreGames(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return gameApiService.getTrendingGames(page, size);
    }

    @GetMapping("/search")
    public List<GameDTO> searchGames(@RequestParam String name) {
        return gameApiService.getGamesByName(name);
    }

    @GetMapping("/{id}")
    public GameDTO getGameById(@PathVariable Integer id) {
        return gameApiService.getGameById(id);
    }

}
