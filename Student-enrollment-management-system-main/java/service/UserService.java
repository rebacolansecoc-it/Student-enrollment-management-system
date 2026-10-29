package service;

import java.sql.*;
import util.PasswordUtils; // Import your password utility

public class UserService {

    // 1. Authenticate user credentials securely with password hashing
    public String authenticateUser(String username, String password) {
        // First, fetch the stored hash and role for this username
        String sql = "SELECT password, role FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                
                // Verify if the entered password matches the secure database hash
                if (PasswordUtils.verifyPassword(password, storedHash)) {
                    return rs.getString("role"); // Authentication successful, returns role
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Authentication failed
    }

    // 2. Register a new user account with hashed password
    public boolean registerUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Hash the password securely before saving it to MySQL
            String secureHash = PasswordUtils.hashPassword(password);

            stmt.setString(1, username);
            stmt.setString(2, secureHash);
            stmt.setString(3, role);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Check if a username already exists
    public boolean isUsernameTaken(String username) {
        String sql = "SELECT user_id FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}