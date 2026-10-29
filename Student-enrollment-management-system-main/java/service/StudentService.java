package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentService {

    public List<String[]> getAllStudents() {
        List<String[]> studentList = new ArrayList<>();
        String sql = "SELECT student_id, first_name, last_name, email, gender, course_id, year_level, status FROM students";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String[] studentData = {
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getString("course_id"),
                    rs.getString("year_level"),
                    rs.getString("status")
                };
                studentList.add(studentData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentList;
    }

    public boolean addStudent(String studentId, String firstName, String lastName, String email, String gender, String courseId, String yearLevel, String status) {
        String sql = "INSERT INTO students (student_id, first_name, last_name, email, gender, course_id, year_level, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, gender);
            stmt.setString(6, courseId);
            stmt.setString(7, yearLevel);
            stmt.setString(8, status != null ? status : "Enrolled");

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(String studentId, String firstName, String lastName, String email, String gender, String courseId, String yearLevel, String status) {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, gender = ?, course_id = ?, year_level = ?, status = ? WHERE LOWER(student_id) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, gender);
            stmt.setString(5, courseId);
            stmt.setString(6, yearLevel);
            stmt.setString(7, status);
            stmt.setString(8, studentId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(String studentId) {
        String sql = "DELETE FROM students WHERE LOWER(student_id) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to automatically generate the next student ID if needed by UI
    public String generateNextStudentId() {
        String sql = "SELECT student_id FROM students ORDER BY student_id DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("student_id");
                if (lastId != null && lastId.matches(".*\\d+.*")) {
                    String prefix = lastId.replaceAll("\\d", "");
                    String numPart = lastId.replaceAll("\\D", "");
                    if (!numPart.isEmpty()) {
                        int num = Integer.parseInt(numPart) + 1;
                        return prefix + String.format("%03d", num);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "STU-001";
    }
}