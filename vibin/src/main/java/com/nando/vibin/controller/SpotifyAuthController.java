package com.nando.vibin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Base64;
import java.util.Map;

@Controller
public class SpotifyAuthController {
    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";

    @GetMapping("/api/spotify/login")
    public RedirectView login() {
        String scopes = "user-read-private";  // adjust as needed
        String url = AUTH_URL +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&scope=" + scopes +
                "&redirect_uri=" + redirectUri;
        return new RedirectView(url);
    }

    @GetMapping("/api/spotify/callback")
    public ResponseEntity<?> callback(@RequestParam String code) {
        // Exchange code for access token
        RestTemplate rest = new RestTemplate();
        String tokenUrl = "https://accounts.spotify.com/api/token";

        // Basic auth header
        String creds = clientId + ":" + clientSecret;
        String basic = Base64.getEncoder().encodeToString(creds.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + basic);

        // form data
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String,String>> req = new HttpEntity<>(form, headers);
        Map<String, Object> resp = rest.postForObject(tokenUrl, req, Map.class);

        // You now have: resp.get("access_token"), resp.get("refresh_token"), etc.
        return ResponseEntity.ok(resp);
    }
}

