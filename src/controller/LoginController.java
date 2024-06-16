package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import entity.db.AIMSDB;
import entity.user.User;
import entity.user.UserRole;

public class LoginController extends BaseController {
    private static Logger LOGGER = Logger.getLogger(LoginController.class.getName());

// login
 // login
    public User login(String username, String password) throws SQLException {
        String query = "SELECT * FROM User u JOIN UserRoles ur ON u.userId = ur.userId JOIN Roles r ON ur.roleId = r.roleId WHERE username = ? AND password = ?";
        try (PreparedStatement statement = AIMSDB.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRoleName(resultSet.getString("roleName"));
                return user;
            }
        }
        return null;
    }



//Check if the user name exists or not
    public boolean isUsernameAvailable(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM User WHERE username = '" + username + "'";
        try (Statement statement = AIMSDB.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                boolean isAvailable = resultSet.getInt(1) == 0;
                System.out.println("Username availability: " + isAvailable);
                return isAvailable;
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while checking username availability: " + e.getMessage());
            throw e;
        }
        System.out.println("Username check reached the end without result.");
        return false;
    }

// register
    public void registerUser(User user) throws SQLException {
        String query = "INSERT INTO User (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = AIMSDB.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();

            // Get the generated user ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                // Assign default role to the new user
                assignRole(userId, "user");
            }
        }
    }
// auto role user
    private void assignRole(int userId, String roleName) throws SQLException {
        // Get the roleId for the given roleName
        String roleQuery = "SELECT roleId FROM Roles WHERE roleName = ?";
        try (PreparedStatement roleStatement = AIMSDB.getConnection().prepareStatement(roleQuery)) {
            roleStatement.setString(1, roleName);
            ResultSet roleResultSet = roleStatement.executeQuery();
            if (roleResultSet.next()) {
                int roleId = roleResultSet.getInt("roleId");

                // Assign role to user
                String userRoleQuery = "INSERT INTO UserRoles (userId, roleId) VALUES (?, ?)";
                try (PreparedStatement userRoleStatement = AIMSDB.getConnection().prepareStatement(userRoleQuery)) {
                    userRoleStatement.setInt(1, userId);
                    userRoleStatement.setInt(2, roleId);
                    userRoleStatement.executeUpdate();
                }
            } else {
                throw new SQLException("Role not found: " + roleName);
            }
        }
    }
    // change password
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws SQLException {
        String checkPasswordQuery = "SELECT password FROM User WHERE userId = ? AND password = ?";
        try (PreparedStatement checkPasswordStatement = AIMSDB.getConnection().prepareStatement(checkPasswordQuery)) {
            checkPasswordStatement.setInt(1, userId);
            checkPasswordStatement.setString(2, oldPassword);
            ResultSet resultSet = checkPasswordStatement.executeQuery();
            if (resultSet.next()) {
                String updatePasswordQuery = "UPDATE User SET password = ? WHERE userId = ?";
                try (PreparedStatement updatePasswordStatement = AIMSDB.getConnection().prepareStatement(updatePasswordQuery)) {
                    updatePasswordStatement.setString(1, newPassword);
                    updatePasswordStatement.setInt(2, userId);
                    int rowsUpdated = updatePasswordStatement.executeUpdate();
                    return rowsUpdated > 0;
                }
            } else {
                System.out.println("Old password does not match.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while changing password: " + e.getMessage());
            throw e;
        }
    }
}