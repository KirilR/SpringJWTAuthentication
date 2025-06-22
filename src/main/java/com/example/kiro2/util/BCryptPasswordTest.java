package com.example.kiro2.util;
//THAT IS A JUST A TEST CLASS TO SEE WHETHER THE BYCRIPT ENCODER OF THE PASSWORDS IN THE DB AND THE GIVEN ONE MATCHES
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordTest implements CommandLineRunner {

    private final BCryptPasswordEncoder passwordEncoder;

    public BCryptPasswordTest(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String rawPassword = "12345678";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Raw password: " + rawPassword);
        System.out.println("Generated Hash: " + hashedPassword);

        // Check if it matches the hash from your database
        String dbHashedPassword = "$2a$10$eHA/w8690LN3I6k2n5JZjulRyEDz0KKCYzkIxvdX8SZkJ3CPSpbea"; // Replace with the hash from your DB
        boolean matches = passwordEncoder.matches(rawPassword, dbHashedPassword);
        System.out.println("Does the password match with the DB hash? " + matches);
    }
}
