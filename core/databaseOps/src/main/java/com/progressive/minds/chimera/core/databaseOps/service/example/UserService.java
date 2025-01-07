package com.progressive.minds.chimera.core.databaseOps.service.example;

import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.exception.ValidationException;
import com.progressive.minds.chimera.core.databaseOps.model.example.User;
import com.progressive.minds.chimera.core.databaseOps.repository.example.UserRepository;
import com.progressive.minds.chimera.core.databaseOps.validation.*;

import java.util.List;
import java.util.Map;

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

    public int updateUsers(Map<String, Object> updateFields, Map<String, Object> filters) {
        if (updateFields == null || updateFields.isEmpty()) {
            throw new ValidationException("Update fields cannot be null or empty.");
        }
        if (filters == null || filters.isEmpty()) {
            throw new ValidationException("Filters cannot be null or empty for update operation.");
        }

        try {
            return userRepository.updateUsers(updateFields, filters);
        } catch (DatabaseException e) {
            throw new DatabaseException("Error updating users.", e);
        }
    }
    public int deleteUsers(Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            throw new ValidationException("Filters cannot be null or empty for delete operation.");
        }

        try {
            return userRepository.deleteUsers(filters);
        } catch (DatabaseException e) {
            throw new DatabaseException("Error deleting users.", e);
        }
    }

}
