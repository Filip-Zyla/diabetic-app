package com.filipzyla.diabeticapp.backend.service;

import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean validateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean validateUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void registerUser(String email, String username, String pass) {
        User user = new User(username, pass, email);
        userRepository.save(user);
    }
}