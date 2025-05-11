package com.redondodev.GeeXPert_back.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@AllArgsConstructor
public class RecommendationService {

    private final ObjectMapper objectMapper;

    public String[] getGameRecommendations(String model, String prompt) {
        String fullResponse = "";

        try {
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            var requestBody = new RecommendationRequest(model, prompt, false);

            try (OutputStream os = connection.getOutputStream()) {
                objectMapper.writeValue(os, requestBody);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                RecommendationResponse response = objectMapper.readValue(br, RecommendationResponse.class);
                fullResponse = response.getResponse();
            }

            fullResponse = fullResponse.substring(fullResponse.lastIndexOf("\n") + 1);

            return fullResponse.split(", ");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating recommendations", e);
        }
    }

}