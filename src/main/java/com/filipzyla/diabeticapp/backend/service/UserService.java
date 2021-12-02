package com.filipzyla.diabeticapp.backend.service;

import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByCredentials(String username, String password) {
        final Optional<User> userOptional = userRepository.findByUsernameAndPassword(username, password);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return userOptional;
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