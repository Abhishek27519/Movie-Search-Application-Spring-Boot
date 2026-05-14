package com.example.moviesearchapp.service;

import com.example.moviesearchapp.entity.User;
import com.example.moviesearchapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("password123"); // Requirement #2: Saving to H2
            userRepository.save(user);
            System.out.println("Default user 'admin' created in H2.");
        }
    }
}