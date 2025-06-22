package com.example.kiro2.service;

import com.example.kiro2.entity.User;
import com.example.kiro2.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // You can return a custom UserDetails object or use Spring's default `User` object
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .password(user.getPassword()) // hashed password from the database
                .roles("USER") // you can also retrieve roles from the user entity if you have role management
                .build();
    }
}
