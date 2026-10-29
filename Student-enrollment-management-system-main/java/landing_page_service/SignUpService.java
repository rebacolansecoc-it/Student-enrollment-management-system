package landing_page_service;

import service.DatabaseConnection;
import util.PasswordUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpService {

    public boolean registerUser(String username, String password, String role) {
        if (isUsernameTaken(username)) {
            return false;
        }

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Hash password before saving to the database
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

    public boolean isUsernameTaken(String username) {
        String sql = "SELECT user_id FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}