package service;

import admin_model.TeacherAssignment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherService {
    private static TeacherService instance;

    private TeacherService() {}

    public static synchronized TeacherService getInstance() {
        if (instance == null) {
            instance = new TeacherService();
        }
        return instance;
    }

    // Purpose: Fetch teacher assignment details and status by teacher ID or email[cite: 46]
    public TeacherAssignment getAssignmentById(String teacherId) {
        String sql = "SELECT t.teacher_id, t.full_name, t.email, c.course_name, s.subject_title, t.status, t.department " +
                     "FROM teachers t " +
                     "LEFT JOIN subjects s ON t.teacher_id = s.assigned_teacher_id " +
                     "LEFT JOIN courses c ON s.course_id = c.course_id " +
                     "WHERE LOWER(t.teacher_id) = LOWER(?) OR LOWER(t.email) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setString(2, teacherId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("teacher_id");
                String courseName = rs.getString("course_name");
                if (courseName == null || courseName.trim().isEmpty()) {
                    courseName = rs.getString("department");
                }
                String dbStatus = rs.getString("status");
                String subjectTitle = rs.getString("subject_title");
                
                boolean isAssigned = (subjectTitle != null && !subjectTitle.trim().isEmpty() && !"None".equalsIgnoreCase(subjectTitle.trim()))
                        || "Status".equalsIgnoreCase(dbStatus) || "ASSIGNED".equalsIgnoreCase(dbStatus);

                return new TeacherAssignment(
                    id,
                    id,
                    rs.getString("full_name"),
                    rs.getString("email"),
                    courseName != null ? courseName : "Unassigned",
                    subjectTitle != null ? subjectTitle : "None",
                    "N/A",
                    2026,
                    2026,
                    1,
                    isAssigned ? "Assigned" : "Waiting to be assign"
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Purpose: Save or update teacher application submission in MySQL database[cite: 46]
    public boolean saveOrUpdateTeacherApplication(String teacherId, String fullName, String email, String preferredCourse) {
        String sql = "INSERT INTO teachers (teacher_id, full_name, email, department, status) VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE full_name = VALUES(full_name), email = VALUES(email), department = VALUES(department)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setString(2, fullName);
            stmt.setString(3, email);
            stmt.setString(4, preferredCourse);
            stmt.setString(5, "Waiting to be assign");

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Purpose: Fetch available courses added by admin for teacher preference dropdown selection[cite: 46]
    public List<String> getAvailableCourses() {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT course_name FROM courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String c = rs.getString("course_name");
                if (c != null && !c.trim().isEmpty()) {
                    courses.add(c.trim());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (courses.isEmpty()) {
            courses.add("General Course");
        }
        return courses;
    }

    // Purpose: Retrieve all teacher records and their assigned subjects for the Admin table[cite: 46]
    public List<TeacherAssignment> getAllAssignments() {
        List<TeacherAssignment> list = new ArrayList<>();
        // Fallback to selecting directly from teachers first to guarantee rows appear
        String sql = "SELECT teacher_id, full_name, email, department, status FROM teachers";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("teacher_id");
                String name = rs.getString("full_name");
                String email = rs.getString("email");
                String department = rs.getString("department");
                String dbStatus = rs.getString("status");

                String courseDisplay = (department != null && !department.trim().isEmpty()) ? department : "Unassigned";
                String statusDisplay = ("ASSIGNED".equalsIgnoreCase(dbStatus) || "Status".equalsIgnoreCase(dbStatus)) ? "Assigned" : "Un-assigned";

                list.add(new TeacherAssignment(
                    id,
                    id,
                    name != null ? name : "Unknown",
                    email != null ? email : "N/A",
                    courseDisplay,
                    "None",
                    "N/A",
                    2026,
                    2026,
                    1,
                    statusDisplay
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Purpose: Update teacher details (name, email, course) in the MySQL database[cite: 46]
    public boolean updateAssignment(String teacherId, String name, String email, String course) {
        String sql = "UPDATE teachers SET full_name = ?, email = ?, department = ? WHERE teacher_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, course);
            stmt.setString(4, teacherId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Purpose: Delete a teacher from the database and set assigned subjects to unassigned[cite: 46]
    // Purpose: Delete a teacher from the database and set assigned subjects to unassigned[cite: 50]
    public boolean deleteAssignment(String teacherId) {
        String unassignSubjectsSql = "UPDATE subjects SET assigned_teacher_id = NULL, teacher_name = 'Unassigned' WHERE assigned_teacher_id = ?";
        String deleteTeacherSql = "DELETE FROM teachers WHERE teacher_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(unassignSubjectsSql);
                 PreparedStatement stmt2 = conn.prepareStatement(deleteTeacherSql)) {

                // Unassign subjects linked to this teacher first[cite: 50]
                stmt1.setString(1, teacherId);
                stmt1.executeUpdate();

                // Delete the teacher record[cite: 50]
                stmt2.setString(1, teacherId);
                int rows = stmt2.executeUpdate();

                conn.commit();
                return rows > 0;
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}