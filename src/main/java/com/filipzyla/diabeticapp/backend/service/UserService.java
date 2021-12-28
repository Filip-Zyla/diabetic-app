package com.filipzyla.diabeticapp.backend.service;

import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @CachePut("user")
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Cacheable("user")
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

    public boolean registerUser(String email, String username, String pass) {
        User user = new User(username, pass, email);
        userRepository.save(user);
        return true;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}