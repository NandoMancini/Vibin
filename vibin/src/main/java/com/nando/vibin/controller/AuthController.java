package com.nando.vibin.controller;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nando.vibin.model.User;
import com.nando.vibin.repository.UserRepository;
import com.nando.vibin.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService,
                          UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/login?registerSuccess";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        if (auth.getPrincipal() instanceof OAuth2User oauthUser) {
            userService.processOAuthPostLogin(oauthUser);
            model.addAttribute("username",  oauthUser.getAttribute("username"));
            model.addAttribute("email", oauthUser.getAttribute("email"));
        }
        else if (auth.getPrincipal() instanceof UserDetails ud) {
            String email = ud.getUsername();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("No user with email: " + email)
                    );
            model.addAttribute("username",  user.getUsername());
            model.addAttribute("email", email);

        }
        return "dashboard";
    }
}
