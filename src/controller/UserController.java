package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.db.AIMSDB;
import entity.user.User;

// get list user 
public class UserController {
	public List<User> getAllUsersWithRoles() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.userId, u.username, r.roleName, password " +
                       "FROM User u " +
                       "JOIN UserRoles ur ON u.userId = ur.userId " +
                       "JOIN Roles r ON ur.roleId = r.roleId";

        try (PreparedStatement statement = AIMSDB.getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setRoleName(resultSet.getString("roleName"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }
        }
        return users;
    }

	public void updateUser(User user, String newRole) throws SQLException {
	    String updateUserQuery = "UPDATE User SET username = ?, password = ? WHERE userId = ?";
	    String updateUserRoleQuery = "UPDATE UserRoles SET roleId = ? WHERE userId = ?";
	    
	    int roleId;
	    switch (newRole.toLowerCase()) {
	        case "user":
	            roleId = 1; // roleId tương ứng với user trong cơ sở dữ liệu
	            break;
	        case "manager":
	            roleId = 3; // roleId tương ứng với manager trong cơ sở dữ liệu
	            break;
	        // roleId = 2 tương ứng với admin
	        default:
	            throw new IllegalArgumentException("Invalid role. Role must be either 'user' or 'manager'.");
	    }

	    // Get the database connection
	    Connection connection = AIMSDB.getConnection();
	    try {
	        connection.setAutoCommit(false);

	        // Cập nhật thông tin người dùng
	        try (PreparedStatement updateUserStatement = connection.prepareStatement(updateUserQuery);
	             PreparedStatement updateUserRoleStatement = connection.prepareStatement(updateUserRoleQuery)) {
	            
	            // Thiết lập tham số cho câu lệnh cập nhật thông tin người dùng
	            updateUserStatement.setString(1, user.getUsername());
	            updateUserStatement.setString(2, user.getPassword());
	            updateUserStatement.setInt(3, user.getUserId());
	            updateUserStatement.executeUpdate();

	            // Thiết lập tham số cho câu lệnh cập nhật vai trò của người dùng
	            updateUserRoleStatement.setInt(1, roleId);
	            updateUserRoleStatement.setInt(2, user.getUserId());
	            updateUserRoleStatement.executeUpdate();

	            connection.commit();
	        }
	    } catch (SQLException e) {
	        connection.rollback();
	        throw e;
	    } finally {

	    }
	}

	public int deleteUser(int userId) throws SQLException {
	    Connection connection = null;
	    try {
	        // Get the database connection
	        connection = AIMSDB.getConnection();
	        
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
	            return deleteUserStatement.executeUpdate();
	            
	            //System.out.println("User and associated roles deleted successfully!");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e; 
	    } finally {
	    }
	}


    // Phương thức thêm người dùng mới giống register


    // Phương thức lấy thông tin người dùng theo ID
    public User getUserById(int userId) throws SQLException {
        String query = "SELECT u.userId, u.username, u.password, r.roleName " +
                       "FROM User u " +
                       "JOIN UserRoles ur ON u.userId = ur.userId " +
                       "JOIN Roles r ON ur.roleId = r.roleId " +
                       "WHERE u.userId = ?";
        try ( 
             PreparedStatement statement = AIMSDB.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String roleName = resultSet.getString("roleName");
                    return new User(userId, username, password, roleName);
                } else {
                    return null;
                }
            }
        }
    }
    
}
