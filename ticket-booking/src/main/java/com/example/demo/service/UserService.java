package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(String username){
        User user = new User(username);
        userRepository.save(user);
    }

}
