package com.example.kiro2.service;


import com.example.kiro2.entity.User;
import com.example.kiro2.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Integer getLastAddedUserId() {
        return userRepository.findLastAddedUserId();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }


}

