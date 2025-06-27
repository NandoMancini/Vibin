package com.nando.vibin.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 3) Recommendation service: call Spotify Web API using obtained token
 */
@Service
public class SpotifyService {

    private final RestTemplate rest = new RestTemplate();

    @Setter
    private String accessToken;

    @Value("${spotify.market:US}")
    private String market;

    /**
     * Map emotion to seed genre and fetch a single track recommendation
     */
    public Map<String, Object> recommendByEmotion(String emotion) {
        // 1) map emotion to seed genre
        String seed = switch (emotion.toLowerCase()) {
            case "happy" -> "pop";
            case "sad"   -> "acoustic";
            case "angry" -> "rock";
            case "fear"  -> "ambient";
            default       -> "classical";
        };

        // 2) build search URL for tracks of that genre
        String url = UriComponentsBuilder
                .fromUriString("https://api.spotify.com/v1/search")
                .queryParam("q", "genre:" + seed)
                .queryParam("type", "track")
                .queryParam("market", market)
                .queryParam("limit", 10)
                .build()
                .toUriString();

        // 3) set up headers with Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // 4) call the Spotify Search API
        ResponseEntity<Map> response = rest.exchange(url, HttpMethod.GET, request, Map.class);
        Map<?,?> body = response.getBody();

        // 5) drill into tracks.items
        List<?> items = ((Map<?,?>) body.get("tracks")).get("items") instanceof List ?
                (List<?>) ((Map<?,?>) body.get("tracks")).get("items") : List.of();

        // 6) pick a random track
        if (items.isEmpty()) {
            throw new IllegalStateException("No tracks found for genre: " + seed);
        }
        Map<?,?> pick = (Map<?,?>) items.get(new Random().nextInt(items.size()));

        // 7) extract minimal fields
        String name = (String) pick.get("name");
        String uri = (String) pick.get("uri");
        String previewUrl = (String) pick.get("preview_url");

        @SuppressWarnings("unchecked")
        List<Map<String,Object>> artists = (List<Map<String,Object>>) pick.get("artists");
        String artistNames = artists.stream()
                .map(a -> (String) a.get("name"))
                .collect(Collectors.joining(", "));

        @SuppressWarnings("unchecked")
        List<Map<String,Object>> images = (List<Map<String,Object>>) ((Map<?,?>) pick.get("album")).get("images");
        String albumArtUrl = images.isEmpty() ? null : (String) images.get(0).get("url");

        // 8) return only what the client needs
        assert albumArtUrl != null;
        return Map.of(
                "name", name,
                "artist", artistNames,
                "uri", uri,
                "previewUrl", previewUrl,
                "albumArtUrl", albumArtUrl
        );
    }
}

