package com.filipzyla.diabeticapp.backend.service;

import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @CachePut("user")
    public void saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Cacheable("user")
    public User findByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        else {
            throw new UsernameNotFoundException("Not found: " + username);
        }
    }

    public boolean validateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean validateUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean registerUser(String email, String username, String pass) {
        User user = new User(username, pass, email);
        mailService.registerEmail(user);
        saveUser(user);
        return true;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(RandomStringUtils.randomAlphabetic(15));
            mailService.forgotPassword(user);
            saveUser(user);
        }
    }
}