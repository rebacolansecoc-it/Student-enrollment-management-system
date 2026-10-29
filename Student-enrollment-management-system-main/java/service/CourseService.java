package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Course;

public class CourseService {

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Course(
                    rs.getString("course_id"),
                    rs.getString("course_name"),
                    rs.getString("units")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String[] getCourseNamesArray() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT course_name FROM courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("course_name");
                if (name != null && !name.trim().isEmpty()) {
                    names.add(name.trim());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names.toArray(new String[0]);
    }

    // Purpose: Automatically generate a sequential formatted course ID (e.g., CRS-001, CRS-002)[cite: 31]
    public String generateNextCourseId() {
        String nextId = "CRS-001";
        String sql = "SELECT course_id FROM courses ORDER BY course_id DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("course_id");
                if (lastId != null && lastId.startsWith("CRS-")) {
                    try {
                        int num = Integer.parseInt(lastId.substring(4)) + 1;
                        nextId = String.format("CRS-%03d", num);
                    } catch (NumberFormatException e) {
                        // Fallback if ID format varies
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

    public boolean addCourse(String courseName, String units) {
        String courseId = generateNextCourseId();
        return addCourse(courseId, courseName, units);
    }

    public boolean addCourse(String courseId, String courseName, String units) {
        String sql = "INSERT INTO courses (course_id, course_name, units) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, courseId);
            stmt.setString(2, courseName);
            stmt.setString(3, units);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCourse(String courseId, String courseName, String units) {
        String sql = "UPDATE courses SET course_name = ?, units = ? WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, courseName);
            stmt.setString(2, units);
            stmt.setString(3, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(String courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
