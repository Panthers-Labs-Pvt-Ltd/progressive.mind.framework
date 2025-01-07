/*
package com.progressive.minds.chimera.core.databaseOps.service;

import static org.junit.jupiter.api.Assertions.*;

import com.progressive.minds.chimera.core.databaseOps.exception.ValidationException;
import com.progressive.minds.chimera.core.databaseOps.model.example.User;
import com.progressive.minds.chimera.core.databaseOps.repository.example.UserRepository;
import com.progressive.minds.chimera.core.databaseOps.service.example.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        System.setProperty("CHIMERA_EXE_ENV", "UnitTest");

        userRepository = mock(UserRepository.class); // Mock the repository
        userService = new UserService(userRepository);
    }

    @Test
    void testFetchAllUsers_ReturnsUserList() {
        // Arrange
        User user1 = new User(1, "John Doe", "john.doe@example.com");
        User user2 = new User(2, "Jane Doe", "jane.doe@example.com");
        when(userRepository.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = userService.fetchAllUsers();

        // Assert
        assertEquals(2, users.size());
        verify(userRepository, times(1)).getAllUsers(); // Verify method call
    }
    @Test
    void testFetchAllUsersWithFilter() {
        UserRepository userRepository = new UserRepository();

        // Example 1: Get all users with a specific name
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "John Doe");
        List<User> usersByName = userRepository.getAllUsers(filters);
        usersByName.forEach(user -> System.out.println(user.getName()));

        // Example 2: Get all users with a specific name and email
        filters.clear();
        filters.put("name", "Jane Doe");
        filters.put("email", "jane.doe@example.com");
        List<User> usersByNameAndEmail = userRepository.getAllUsers(filters);
        usersByNameAndEmail.forEach(user -> System.out.println(user.getEmail()));

        // Example 3: Get all users without any filters
        List<User> allUsers = userRepository.getAllUsers(new HashMap<>());
        allUsers.forEach(user -> System.out.println(user.getName()));
        // Act
        List<User> users = userService.fetchAllUsers();

        // Assert
        assertEquals(2, users.size());
        verify(userRepository, times(1)).getAllUsers(); // Verify method call
    }

    @Test
    void testUpdateUser() {
        String name = "John Doe";
        String email = "john.doe@example.com";
        UserRepository userRepository = new UserRepository();

        Map<String, Object> updateFields = new HashMap<>();
        updateFields.put("email", "new.email@example.com");
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "John Doe");
        int rowsUpdated = userRepository.updateUsers(updateFields, filters);
        System.out.println("Rows updated: " + rowsUpdated);

        // Update Using Service
        UserService userService = new UserService(new UserRepository());
        Map<String, Object> updateFieldsService = Map.of("email", "updated.email@example.com");
        Map<String, Object> filtersService = Map.of("name", "John Doe");

        int rowsUpdatedService = userService.updateUsers(updateFieldsService, filtersService);
        System.out.println("Rows updated: " + rowsUpdatedService);
    }

    @Test
    void testDeleteUser() {
        UserRepository userRepository = new UserRepository();

        // Delete users with a specific email
        Map<String, Object> filters = new HashMap<>();
        filters.put("email", "spam@example.com");

        int rowsDeleted = userRepository.deleteUsers(filters);
        System.out.println("Rows deleted: " + rowsDeleted);

        // Delete Using Service
        UserService userService = new UserService(new UserRepository());
        Map<String, Object> filtersService = Map.of("email", "spam@example.com");
        int rowsDeletedService = userService.deleteUsers(filtersService);
        System.out.println("Rows deleted: " + rowsDeletedService);

    }

    @Test
    void testCreateUser_ValidInput_CallsRepositorySave() {
        // Arrange
        String name = "John Doe";
        String email = "john.doe@example.com";

        // Act
        userService.createUser(name, email);

        // Assert
        verify(userRepository, times(1)).saveUser(any(User.class));
    }

    @Test
    void testCreateUser_InvalidName_ThrowsValidationException() {
        // Arrange
        String invalidName = "";
        String email = "john.doe@example.com";

        // Act & Assert
        assertThrows(ValidationException.class, () -> userService.createUser(invalidName, email));
        verify(userRepository, never()).saveUser(any(User.class)); // Ensure repository is not called
    }

    @Test
    void testCreateUser_InvalidEmail_ThrowsValidationException() {
        // Arrange
        String name = "John Doe";
        String invalidEmail = "invalid-email";

        // Act & Assert
        assertThrows(ValidationException.class, () -> userService.createUser(name, invalidEmail));
        verify(userRepository, never()).saveUser(any(User.class)); // Ensure repository is not called
    }
}
*/
