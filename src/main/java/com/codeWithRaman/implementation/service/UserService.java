package com.codeWithRaman.implementation.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeWithRaman.implementation.model.User;
import com.codeWithRaman.implementation.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthProvider(User.AuthProvider.LOCAL);
        userRepository.save(user);
    }

    @Transactional
    public void processOAuthPostLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(oAuth2User.getAttribute("name"));
            user.setAuthProvider(User.AuthProvider.GOOGLE);
            userRepository.save(user);
        }
    }
}
