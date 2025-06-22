package com.example.kiro2.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

import com.example.kiro2.entity.User;
import com.example.kiro2.entity.UserRepository;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

@RestController
public class LoginController {

    @Value("${jwt.secret}")
    private String secretKeyBase64;

    private Key secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyBase64);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/users/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUserName(request.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String hashedPassword = user.getPassword();

            System.out.println("Username found: " + request.getUsername());
            System.out.println("Hashed password from DB: " + hashedPassword);
            System.out.println("Password to check: " + request.getPassword());

            // Directly compare the plain-text password with the hashed password
            if (passwordEncoder.matches(request.getPassword(), hashedPassword)) {
                String token = generateJwtToken(request.getUsername());
                return ResponseEntity.ok(token);
            } else {
                System.out.println("Password mismatch.");
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } else {
            System.out.println("User not found.");
            return ResponseEntity.status(401).body("User not found");
        }
    }




    /*private String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

     */
    private String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 360000)) // 1 hour expiration
                .signWith(this.secretKey)
                .compact();
    }

}

class LoginRequest {
    private String username;
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
