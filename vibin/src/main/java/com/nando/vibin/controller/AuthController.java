package com.nando.vibin.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.nando.vibin.model.User;
import com.nando.vibin.payload.JwtResponse;
import com.nando.vibin.payload.LoginRequest;
import com.nando.vibin.payload.RegisterRequest;
import com.nando.vibin.repository.UserRepository;
import com.nando.vibin.service.UserService;
import com.nando.vibin.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserService userService,
            UserRepository userRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Local registration. Client posts JSON { "username": "...", "email": "...", "password": "..." }.
     * We create a new User(authProvider=LOCAL) and return 201 Created with no body (or a short success JSON).
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        // 1) Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Email already in use"));
        }

        // 2) Create new User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        User saved = userService.registerUser(user);

        // 3) Return 201 CREATED (no body; front‐end can redirect to /login)
        return ResponseEntity
                .status(201)
                .body(Map.of("message", "User registered successfully"));
    }

    /**
     * Local login. Client posts { "email": "...", "password": "..." }. We authenticate and
     * return { "token":"<JWT>" } on success, or 401 on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        // 1) Perform authentication; if fails, Spring will throw an exception (401)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2) Generate JWT for this user
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(principal);

        // 3) Return token in body
        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));
    }

    /**
     * This endpoint is the “success handler” for OAuth2 logins. After Google redirects back,
     * Spring Security will populate an OAuth2AuthenticationToken. We grab it, create/lookup
     * the User, issue a JWT, and return it as JSON.
     *
     * In SecurityConfig (below), you will configure:
     *   .oauth2Login(oauth2 -> oauth2
     *       .successHandler(new SimpleUrlAuthenticationSuccessHandler("/api/auth/oauth2/success"))
     *       ...
     *   )
     *
     * So Google→Spring→/api/auth/oauth2/success will hit this method with a valid OAuth2 token.
     */
    @GetMapping("/oauth2/success")
    public ResponseEntity<?> oauth2Success(OAuth2AuthenticationToken authToken) {
        OAuth2User oAuth2User = authToken.getPrincipal();

        // 1) Create or fetch existing User in DB
        User user = userService.processOAuthPostLogin(oAuth2User);

        // 2) Manually build a UsernamePasswordAuthenticationToken so we can treat them as “authenticated”
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        userRepository.findByEmail(user.getEmail())
                                .map(u -> org.springframework.security.core.userdetails.User
                                        .builder()
                                        .username(u.getEmail())         // must match the “username” you used in UserDetailsService
                                        .password(u.getPassword())
                                        .authorities("ROLE_USER")
                                        .build()
                                        .getAuthorities())
                                .orElseThrow()); // or set a default authority
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3) Issue JWT for that user
        String jwt = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        "", // no password needed here
                        java.util.List.of(() -> "ROLE_USER")
                )
        );

        // 4) Return the JWT in JSON
        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));
    }
}
