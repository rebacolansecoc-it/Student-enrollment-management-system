package landing_page_service;

import service.DatabaseConnection;
import util.PasswordUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInService {

    public String authenticate(String username, String password) {
        String sql = "SELECT password, role FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    // Securely verify password hash against database record
                    if (PasswordUtils.verifyPassword(password, storedHash)) {
                        return rs.getString("role");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean usernameExists(String username) {
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

    public String getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean changePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Hash the new password before storing it securely
            String secureHash = PasswordUtils.hashPassword(newPassword);

            stmt.setString(1, secureHash);
            stmt.setString(2, username);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}