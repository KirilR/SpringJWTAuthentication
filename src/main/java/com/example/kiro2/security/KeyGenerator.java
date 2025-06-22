package com.example.kiro2.security;



import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generate a key suitable for HS256
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Base64 Encoded Key: " + base64Key);
    }
}
