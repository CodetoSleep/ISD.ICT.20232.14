package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import entity.db.AIMSDB;
import entity.user.User;

public class UserController {

    // Phương thức cập nhật người dùng hiện tại
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE User SET username = ?, password = ? WHERE userId = ?";
        try (Connection connection = AIMSDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getUserId());
            statement.executeUpdate();
        }
    }

    // Phương thức xóa vai trò của người dùng
    public void deleteUser(int userId) throws SQLException {
        try (Connection connection = AIMSDB.getConnection()) {
            if (connection != null) {
                // Xóa vai trò và người dùng
                String deleteRolesQuery = "DELETE FROM UserRoles WHERE userId = ?";
                String deleteUserQuery = "DELETE FROM User WHERE userId = ?";
                try (PreparedStatement deleteRolesStatement = connection.prepareStatement(deleteRolesQuery);
                     PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserQuery)) {
                    
                    // Thiết lập tham số cho phương thức xóa vai trò
                    deleteRolesStatement.setInt(1, userId);
                    // Thực thi câu lệnh xóa vai trò
                    deleteRolesStatement.executeUpdate();

                    // Thiết lập tham số cho phương thức xóa người dùng
                    deleteUserStatement.setInt(1, userId);
                    // Thực thi câu lệnh xóa người dùng
                    deleteUserStatement.executeUpdate();
                    
                    System.out.println("User and associated roles deleted successfully!");
                }
            } else {
                System.out.println("Failed to establish connection to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Ném lại ngoại lệ để thông báo lỗi lên tới lớp gọi
        }
    }

    // Phương thức thêm người dùng mới
    public void createUser(User user) throws SQLException {
        String query = "INSERT INTO User (username, password) VALUES (?, ?)";
        try (Connection connection = AIMSDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        }
    }

    // Phương thức lấy thông tin người dùng theo ID
    public User getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM User WHERE userId = ?";
        try (Connection connection = AIMSDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    return new User(userId, username, password);
                } else {
                    return null; // Không tìm thấy người dùng với ID tương ứng
                }
            }
        }
    }
}
