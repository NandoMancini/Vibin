package com.nando.vibin.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nando.vibin.model.User;
import com.nando.vibin.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthProvider(User.AuthProvider.LOCAL);
        return userRepository.save(user);
    }


    @Transactional
    public User processOAuthPostLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        User existing = userRepository.findByEmail(email).orElse(null);

        if (existing == null) {
            User user = new User();
            user.setEmail(email);
            user.setUsername(oAuth2User.getAttribute("name")); // use “name” from Google
            user.setAuthProvider(User.AuthProvider.GOOGLE);
            // Note: no password for GOOGLE accounts
            return userRepository.save(user);
        }

        return existing;
    }
}
