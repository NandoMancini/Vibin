package com.nando.vibin.controller;

import com.nando.vibin.model.Journal;
import com.nando.vibin.payload.EmotionRequest;
import com.nando.vibin.repository.JournalRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/emotion")
public class EmotionController {

    private final JournalRepository journalRepository;


    private final RestTemplate restTemplate = new RestTemplate();

    public EmotionController(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @PostMapping
    public ResponseEntity<?> analyzeEmotion(@RequestBody EmotionRequest request) {
        String flaskUrl = "http://localhost:5005/analyze";

        Journal entry = journalRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Journal entry not found"));

        // 2) Extract its body
        String bodyText = entry.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String,String> payload = Collections.singletonMap("text", bodyText);
        HttpEntity<Map<String,String>> httpEntity = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, httpEntity, Map.class);

        assert response.getBody() != null;
        entry.setEmotion(response.getBody().get("emotion").toString());
        journalRepository.save(entry);
        return ResponseEntity
                .status(200)
                .body(Map.of("message", "Entry successfully analysed"));
    }
}