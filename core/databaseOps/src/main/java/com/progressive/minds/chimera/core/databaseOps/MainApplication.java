package com.progressive.minds.chimera.core.databaseOps;

import com.progressive.minds.chimera.core.databaseOps.model.example.User;
import com.progressive.minds.chimera.core.databaseOps.repository.example.UserRepository;
import com.progressive.minds.chimera.core.databaseOps.service.example.UserService;

import java.util.List;
import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Display Users");
            System.out.println("2. Add User");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1 -> {
                        List<User> users = userService.fetchAllUsers();
                        if (users.isEmpty()) {
                            System.out.println("No users found.");
                        } else {
                            users.forEach(user ->
                                    System.out.println("ID: " + user.getId() +
                                            ", Name: " + user.getName() +
                                            ", Email: " + user.getEmail()));
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        userService.createUser(name, email);
                        System.out.println("User created successfully.");
                    }
                    case 3 -> {
                        System.out.println("Exiting...");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice, try again.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
