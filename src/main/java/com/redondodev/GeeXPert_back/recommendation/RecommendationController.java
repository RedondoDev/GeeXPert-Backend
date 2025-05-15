package com.redondodev.GeeXPert_back.recommendation;

import com.redondodev.GeeXPert_back.game.GameApiService;
import com.redondodev.GeeXPert_back.game.GameDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recommendations")
@CrossOrigin(origins = "http://localhost:4200")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final GameApiService gameApiService;

    public RecommendationController(RecommendationService recommendationService, GameApiService gameApiService) {
        this.recommendationService = recommendationService;
        this.gameApiService = gameApiService;
    }

    @Value("${ollama.model}")
    private String model;

    @Value("${ollama.prompt}")
    private String prompt;

    @GetMapping("/ai-assistant/{userId}")
    public ResponseEntity<List<GameDTO>> getRecommendations(@PathVariable Integer userId) {
        String[] recommendations = recommendationService.getGameRecommendations(model, prompt, userId);

        List<GameDTO> gameDetails = new ArrayList<>();
        for (String gameName : recommendations) {
            if (gameDetails.size() == 3) {
                break;
            }
            List<GameDTO> games = gameApiService.getGamesByName(gameName);
            if (!games.isEmpty()) {
                gameDetails.add(games.getFirst());
            }
        }

        return ResponseEntity.ok(gameDetails);
    }

}