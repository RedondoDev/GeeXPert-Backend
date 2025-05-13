package com.redondodev.GeeXPert_back.recommendation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommendations")
@CrossOrigin(origins = "http://localhost:4200")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Value("${ollama.model}")
    private String model;

    @Value("${ollama.prompt}")
    private String prompt;


    @GetMapping("/ai-assistant/{userId}")
    public ResponseEntity<String[]> getRecommendations(@PathVariable Integer userId) {
        String[] recommendations = recommendationService.getGameRecommendations(model, prompt, userId);
        System.out.println(String.join(", ", recommendations));
        return ResponseEntity.ok(recommendations);
    }
}