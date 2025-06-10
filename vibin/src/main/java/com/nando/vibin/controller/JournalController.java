package com.nando.vibin.controller;

import com.nando.vibin.model.Journal;
import com.nando.vibin.model.User;
import com.nando.vibin.payload.DeleteEntryRequest;
import com.nando.vibin.payload.JournalRequest;
import com.nando.vibin.repository.JournalRepository;
import com.nando.vibin.repository.UserRepository;
import com.nando.vibin.service.JournalService;
import com.nando.vibin.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

    private final JournalService journalService;
    private final UserRepository userRepository;
    private final JournalRepository journalRepository;


    private final JwtService jwtService;


    public JournalController(
            JournalService journalService,
            UserRepository userRepository, JournalRepository journalRepository, JwtService jwtService
    ) {
        this.journalService = journalService;
        this.userRepository = userRepository;
        this.journalRepository = journalRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/entry")
    public ResponseEntity<?> createEntry(@RequestBody JournalRequest requestBody, HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);


        Optional<User> user = userRepository.findByEmail(email);

        Journal journal = new Journal();
        journal.setTitle(requestBody.getTitle());
        journal.setBody(requestBody.getBody());
        journal.setUserId(user.get().getId());
        journal.setDate(new Date());
        Journal saved = journalService.createAndEditEntry(journal);

        // 3) Return 201 CREATED (no body; front‐end can redirect to journal entry)
        return ResponseEntity
                .status(201)
                .body(Map.of("message", "Entry created successfully"));
    }


    @DeleteMapping("/entry")
    public ResponseEntity<?> deleteEntry(@RequestBody DeleteEntryRequest requestBody, HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);


        Optional<User> user = userRepository.findByEmail(email);
        Long userId = user.get().getId();

        Optional<Journal> entry = journalRepository.findById(requestBody.getId());

        if (entry.isPresent() && entry.get().getUserId().equals(userId)) {

            journalRepository.deleteById(requestBody.getId());

            return ResponseEntity
                    .status(200)
                    .body(Map.of("message", "Entry deleted successfully"));
        }

        // 3) Return 201 CREATED (no body; front‐end can redirect to journal entry)
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this entry.");
    }

    @GetMapping("/entry")
    public ResponseEntity<?> getEntry(@RequestBody DeleteEntryRequest requestBody, HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);

        Optional<User> user = userRepository.findByEmail(email);
        Long userId = user.get().getId();

        Optional<Journal> entry = journalRepository.findById(requestBody.getId());

        if (entry.isPresent() && entry.get().getUserId().equals(userId)) {

            Optional<Journal> journal = journalRepository.findById(requestBody.getId());

            return ResponseEntity
                    .status(200)
                    .body(journal);
        }

        // 3) Return 201 CREATED (no body; front‐end can redirect to journal entry)
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this entry.");
    }

    @GetMapping("/entries")
    public ResponseEntity<?> getAllEntries(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);

        Optional<User> user = userRepository.findByEmail(email);

        List<Journal> journals =  journalRepository.findAllByUserId(user.get().getId());

        return ResponseEntity
                .status(200)
                .body(journals);
    }

}
