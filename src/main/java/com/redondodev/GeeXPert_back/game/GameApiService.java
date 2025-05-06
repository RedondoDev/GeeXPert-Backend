package com.redondodev.GeeXPert_back.game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameApiService {

    private final RestTemplate restTemplate;

    @Value("${igbd.client.id}")
    private String clientId;

    @Value("${igbd.api.key}")
    private String apiKey;

    public GameApiService(RestTemplate restTemplate, GameRepository gameRepository) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("topRatedGames")
    public List<GameDTO> getTopRatedGames() {
        String url = "https://api.igdb.com/v4/games";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields id, cover.url, first_release_date, name, rating, genres.name, platforms.name; " +
                "sort rating desc; where rating_count > 100; limit 10;";

        return delayHttpRequest(url, headers, body);
    }

    @Cacheable("trendingGames")
    public List<GameDTO> getTrendingGames(int page, int size) {
        String url = "https://api.igdb.com/v4/games";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        int offset = page * size;
        String body = "fields id, cover.url, first_release_date, name, rating, rating_count, genres.name, platforms.name; " +
                "sort rating_count desc; limit " + size + "; offset " + offset + ";";

        return delayHttpRequest(url, headers, body);
    }

    public List<GameDTO> getGamesByName(String name) {

        String url = "https://api.igdb.com/v4/games";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields id, cover.url, name, rating, genres.name, platforms.name, first_release_date; " +
                "search \"" + name + "\"; limit 30;";

        return delayHttpRequest(url, headers, body);
    }

    private List<GameDTO> getGameDTOS(String url, HttpHeaders headers, String body) {

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

            List<GameDTO> games = new ArrayList<>();

            for (JsonNode gameNode : jsonResponse) {
                GameDTO gameDTO = new GameDTO();

                gameDTO.setId(gameNode.path("id").asInt());
                gameDTO.setName(gameNode.path("name").asText(null));
                gameDTO.setRating(gameNode.path("rating").asDouble(0));
                gameDTO.setFirst_release_date(gameNode.path("first_release_date").asLong(0));

                if (gameNode.has("cover")) {
                    gameDTO.setCover(gameNode.path("cover").path("url").asText(null)
                            .replace("thumb", "cover_big"));
                }

                if (gameNode.has("genres")) {
                    List<String> genres = new ArrayList<>();
                    for (JsonNode genre : gameNode.path("genres")) {
                        genres.add(genre.path("name").asText());
                    }
                    gameDTO.setGenres(genres);
                }

                if (gameNode.has("platforms")) {
                    List<String> platforms = new ArrayList<>();
                    for (JsonNode platform : gameNode.path("platforms")) {
                        platforms.add(platform.path("name").asText());
                    }
                    gameDTO.setPlatforms(platforms);
                }

                games.add(gameDTO);
            }

            return games;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing games response", e);
        }
    }


    private List<GameDTO> delayHttpRequest(String url, HttpHeaders headers, String body) {
        for (int i = 1; i <= 3; i++) {
            try {
                return getGameDTOS(url, headers, body);
            } catch (HttpClientErrorException.TooManyRequests e) {
                if (i == 3) {
                    throw new RuntimeException("Max retries reached. Please try again later.", e);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", interruptedException);
                }
            }
        }
        throw new RuntimeException("Unexpected error occurred.");
    }

    @Cacheable("gameById")
    public GameDTO getGameById(Integer id) {
        String url = "https://api.igdb.com/v4/games";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields id, cover.url, first_release_date, name, rating, rating_count, genres.name, platforms.name; " +
                "where id = " + id + ";";

        return getGameDTOS(url, headers, body).getFirst();
    }

}
