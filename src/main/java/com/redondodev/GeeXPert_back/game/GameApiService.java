package com.redondodev.GeeXPert_back.game;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<GameDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                GameDTO[].class
        );

        return Arrays.asList(Objects.requireNonNull(response.getBody()));

    }

    public List<GameDTO> getTrendingGames() {

        String url = "https://api.igdb.com/v4/games";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields id, cover, first_release_date, name, rating, rating_count, genres, platforms; " +
                "sort rating_count desc; limit 10;";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<GameDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                GameDTO[].class
        );

        return Arrays.asList(Objects.requireNonNull(response.getBody()));

    }

    public List<GameDTO> getGamesByName(String name) {

        String url = "https://api.igdb.com/v4/games";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.TEXT_PLAIN);

        String body = "fields id, name, rating, genres, platforms, first_release_date; " +
                "search \"" + name + "\"; limit 20;";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<GameDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                GameDTO[].class
        );

        return Arrays.asList(Objects.requireNonNull(response.getBody()));

    }

}
