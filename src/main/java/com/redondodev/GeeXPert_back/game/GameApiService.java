package com.redondodev.GeeXPert_back.game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GameApiService {

    private final RestTemplate restTemplate;

    @Value("${igbd.client.id}")
    private String clientId;

    @Value("${igbd.api.key}")
    private String apiKey;

    public GameApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<GameDTO> getTopRatedGames() {

        String url = "https://api.igdb.com/v4/games";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields id, cover, first_release_date, name, rating, genres, platforms; " +
                "sort rating desc; where rating_count > 100; limit 10;";

        return getGameDTOS(url, headers, body);

    }

    public List<GameDTO> getTrendingGames() {

        String url = "https://api.igdb.com/v4/games";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields id, cover, first_release_date, name, rating, rating_count, genres, platforms; " +
                "sort rating_count desc; limit 10;";

        return getGameDTOS(url, headers, body);
    }

    public List<GameDTO> getGamesByName(String name) {

        String url = "https://api.igdb.com/v4/games";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields id, cover, name, rating, genres, platforms, first_release_date; " +
                "search \"" + name + "\"; limit 20;";

        return getGameDTOS(url, headers, body);
    }

    private List<GameDTO> getGameDTOS(String url, HttpHeaders headers, String body) {
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<GameDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                GameDTO[].class
        );

        List<GameDTO> games = Arrays.asList(Objects.requireNonNull(response.getBody()));

        for (GameDTO game : games) {
            if (game.getCover() != null) {
                String coverUrl = getCoverUrl(game.getCover());
                game.setCover(coverUrl);
            }
            if (game.getPlatforms() != null) {
                List<String> platformNames = getPlatformNames(game.getPlatforms());
                game.setPlatforms(platformNames);
            }
            if (game.getGenres() != null) {
                List<String> genresNames = getGenresNames(game.getGenres());
                game.setGenres(genresNames);
            }
        }

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public String getCoverUrl(String cover) {
        String url = "https://api.igdb.com/v4/covers";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields url; where id = " + cover + ";";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            if (jsonResponse.isArray() && !jsonResponse.isEmpty()) {
                return jsonResponse.get(0).get("url").asText().replace("thumb", "cover_big");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getPlatformNames(List<String> platformIds) {
        String url = "https://api.igdb.com/v4/platforms";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields name; where id = (" + platformIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")) + ");";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            List<String> platformNames = new ArrayList<>();
            for (JsonNode node : jsonResponse) {
                platformNames.add(node.get("name").asText());
            }
            return platformNames;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public List<String> getGenresNames(List<String> genreIds) {
        String url = "https://api.igdb.com/v4/genres";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields name; where id = (" + genreIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")) + ");";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            List<String> genreNames = new ArrayList<>();
            for (JsonNode node : jsonResponse) {
                genreNames.add(node.get("name").asText());
            }
            return genreNames;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

}
