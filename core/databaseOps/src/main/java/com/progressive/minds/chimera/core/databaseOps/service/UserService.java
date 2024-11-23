package com.progressive.minds.chimera.core.databaseOps.service;

import com.progressive.minds.chimera.core.databaseOps.model.User;
import com.progressive.minds.chimera.core.databaseOps.repository.UserRepository;
import com.progressive.minds.chimera.core.databaseOps.validation.*;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> fetchAllUsers() {
        return userRepository.getAllUsers();
    }

    public void createUser(String name, String email) {
        // Validate input
        InputValidator.validateName(name);
        InputValidator.validateEmail(email);

        // Create user and save
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userRepository.saveUser(user);
    }
}
