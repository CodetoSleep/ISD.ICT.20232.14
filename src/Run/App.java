package Run;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import controller.LoginController;
import controller.UserController;
import entity.db.AIMSDB;
import entity.user.User;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LoginController loginController = new LoginController();
        UserController userController = new UserController();

        // Connect to the database
        AIMSDB.getConnection();

        System.out.println("Welcome to the Login and User Management System");

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. View All Users");
            System.out.println("6. Exit");
            System.out.print("Your choice: ");
            String option = scanner.nextLine();

            try {
                switch (option) {
                    case "1":
                        login(scanner, loginController);
                        break;
                    case "2":
                        registerNewAccount(scanner, loginController);
                        break;
                    case "3":
                        updateUser(scanner, userController);
                        break;
                    case "4":
                        deleteUser(scanner, userController);
                        break;
                    case "5":
                        viewAllUsers(userController);
                        break;
                    case "6":
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please choose again.");
                }
            } catch (SQLException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
    private static void viewAllUsers(UserController userController) throws SQLException {
        List<User> users = userController.getAllUsersWithRoles();
        System.out.println("List of all users:");
        for (User user : users) {
            System.out.println("User ID: " + user.getUserId() +
                               ", Username: " + user.getUsername() +
                               ", Role: " + user.getRoleName());
        }
    }

    private static void registerNewAccount(Scanner scanner, LoginController loginController) throws SQLException {
        System.out.println("Register a new account");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Check if username already exists
        if (!loginController.isUsernameAvailable(username)) {
            System.out.println("Username already exists. Please choose another username.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Create a new User object with the input information
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        // Perform registration
        loginController.registerUser(newUser);
        System.out.println("Registration successful!");
    }

    private static void login(Scanner scanner, LoginController loginController) throws SQLException {
        System.out.println("Login to your account");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = loginController.login(username, password);
        if (user != null) {
            System.out.println("Login successful! Welcome");
            // Call the menu for the user logged in
            afterLoginMenu(scanner, loginController, user);
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void afterLoginMenu(Scanner scanner, LoginController loginController, User user) throws SQLException {
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Change password");
            System.out.println("2. Logout");
            System.out.print("Your choice: ");
            String option = scanner.nextLine();

            if (option.equals("1")) {
                changePassword(scanner, loginController, user);
            } else if (option.equals("2")) {
                System.out.println("Logged out successfully.");
                break;
            } else {
                System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private static void changePassword(Scanner scanner, LoginController loginController, User user) throws SQLException {
        System.out.println("Change your password");
        System.out.print("Enter old password: ");
        String oldPassword = scanner.nextLine();

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        boolean passwordChanged = loginController.changePassword(user.getUserId(), oldPassword, newPassword);
        if (passwordChanged) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Old password is incorrect. Please try again.");
        }
    }

    private static void updateUser(Scanner scanner, UserController userController) throws SQLException {
        System.out.println("Update user information");
        System.out.print("Enter user ID to update: ");
        int userId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter new username: ");
        String username = scanner.nextLine();

        System.out.print("Enter new password: ");
        String password = scanner.nextLine();

        System.out.print("Enter new role (user/manager): ");
        String role = scanner.nextLine();

        User user = new User(userId, username, password);
        userController.updateUser(user, role);
        System.out.println("User updated successfully!");
    }


    private static void deleteUser(Scanner scanner, UserController userController) throws SQLException {
        System.out.println("Delete user");
        System.out.print("Enter user ID to delete: ");
        int userId = Integer.parseInt(scanner.nextLine());

        userController.deleteUser(userId);
        System.out.println("User deleted successfully!");
    }
}