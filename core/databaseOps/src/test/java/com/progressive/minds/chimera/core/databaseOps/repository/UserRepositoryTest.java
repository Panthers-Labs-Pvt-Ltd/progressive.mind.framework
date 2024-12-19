/*
package com.progressive.minds.chimera.core.databaseOps.repository;

import static org.junit.jupiter.api.Assertions.*;
import com.progressive.minds.chimera.core.databaseOps.config.DataSourceConfig;
import com.progressive.minds.chimera.core.databaseOps.model.example.User;
import com.progressive.minds.chimera.core.databaseOps.repository.example.UserRepository;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.FileReader;
import java.util.List;

public class UserRepositoryTest {

    private UserRepository userRepository;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("CHIMERA_EXE_ENV", "UnitTest");
        dataSource = DataSourceConfig.getDataSource();
        userRepository = new UserRepository();

        // Initialize in-memory database schema
        RunScript.execute(dataSource.getConnection(),
                new FileReader("src/test/resources/schema.sql"));
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clear the database after each test
        RunScript.execute(dataSource.getConnection(), new FileReader("src/test/resources/cleanup.sql"));
    }

    @Test
    void testSaveUser_AddsUserToDatabase() {
        // Arrange
        User user = new User(0, "John Doe", "john.doe@example.com");

        // Act
        userRepository.saveUser(user);

        // Assert
        List<User> users = userRepository.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    void testGetAllUsers_ReturnsEmptyList_WhenNoUsers() {
        // Act
        List<User> users = userRepository.getAllUsers();

        // Assert
        assertTrue(users.isEmpty());
    }

    @Test
    void testGetAllUsers_ReturnsAllUsers() {
        // Arrange
        userRepository.saveUser(new User(0, "John Doe", "john.doe@example.com"));
        userRepository.saveUser(new User(0, "Jane Doe", "jane.doe@example.com"));

        // Act
        List<User> users = userRepository.getAllUsers();
        users.forEach(user -> System.out.println("Users Name " + user.getName()));

        // Assert
        assertEquals(2, users.size());
    }
}
*/
