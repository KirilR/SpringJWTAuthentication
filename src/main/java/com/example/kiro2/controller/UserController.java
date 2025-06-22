package com.example.kiro2.controller;
//package com.example.kiro2.entity; // Assuming your other user-related classes are here

import com.example.kiro2.entity.User;
import com.example.kiro2.entity.UserRepository;
import com.example.kiro2.exception.ApiException;
import com.example.kiro2.service.UserService; // Import UserService
import jakarta.persistence.Entity; // Assuming you're using Jakarta Persistence
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {
    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        // Check if password is at least 8 characters
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }

        // Check if email is written
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ApiException("Invalid input, Email is required", HttpStatus.BAD_REQUEST);
        }

        if (user.getName().length() < 6) {
            throw new ApiException("Invalid input, Username must be at least 6 characters", HttpStatus.BAD_REQUEST);
        }

        // Check if user with the same name or email already exists
        if (userRepository.findByUserName(user.getName()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with the same name or email already exists");
        }

        // Encode password before saving the user
        user.setPassword(encodePassword(user.getPassword()));

        // Get last added user id and set id if it's null
        Integer lastId = userService.getLastAddedUserId();
        user.setId(lastId == null ? 100 : lastId + 1);
        String username = user.getName(); // Assuming username is sent as "userName"

        if (!user.getName().matches(".*[a-zA-Z]{3,}.*")) {
            throw new ApiException("Invalid input, Username must contain at least 3 alphabetic characters in a row", HttpStatus.BAD_REQUEST);
        }

        // Set the username explicitly
        user.setName(username);
        logger.info("User name in back end: " + user.getName());
        return userService.createUser(user);
    }

    // Implement a method to encode password
    private String encodePassword(String password) {
        // You can use a password encoder like BCryptPasswordEncoder to encode the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @GetMapping("/username")
    public ResponseEntity<?> getName(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(authentication.getName());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }
}


