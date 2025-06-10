package com.nando.vibin.config;

import com.nando.vibin.model.User;
import com.nando.vibin.service.JwtService;
import com.nando.vibin.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public GoogleOAuth2SuccessHandler(UserService userService, JwtService jwtService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        // 1) Cast and extract the OAuth2User
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();

        // 2) Upsert the user in your DB
        User user = userService.processOAuthPostLogin(oauthUser);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // 3) Generate your JWT (for example, using your JwtService)
        String jwt = jwtService.generateToken(userDetails);

        // 4) Write the JWT back to the client (e.g. as JSON or a redirect with a query param)
        response.setContentType("application/json");
        response.getWriter().write("{ \"token\": \"" + jwt + "\" }");
        response.getWriter().flush();
    }
}
