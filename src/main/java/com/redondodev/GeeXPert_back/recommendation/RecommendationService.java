package com.redondodev.GeeXPert_back.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redondodev.GeeXPert_back.user.User;
import com.redondodev.GeeXPert_back.user.UserRepository;
import com.redondodev.GeeXPert_back.user_games.UserGame;
import com.redondodev.GeeXPert_back.user_games.UserGameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
@AllArgsConstructor
public class RecommendationService {

    private final ObjectMapper objectMapper;
    private final UserGameRepository userGameRepository;
    private final UserRepository userRepository;

    public String[] getGameRecommendations(String model, String prompt, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserGame> userGames = userGameRepository.findAllByUser(user);

        String gameNames = userGames.stream()
                .map(userGame -> userGame.getGame().getName())
                .reduce((name1, name2) -> name1 + ", " + name2)
                .orElse("");

        String updatedPrompt = prompt.replace("<>", gameNames);

        try {
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            var requestBody = new RecommendationRequest(model, updatedPrompt, false);

            try (OutputStream os = connection.getOutputStream()) {
                objectMapper.writeValue(os, requestBody);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                RecommendationResponse response = objectMapper.readValue(br, RecommendationResponse.class);
                String fullResponse = response.getResponse();
                fullResponse = fullResponse.substring(fullResponse.lastIndexOf("\n") + 1);
                return fullResponse.split(", ");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating recommendations", e);
        }
    }

}