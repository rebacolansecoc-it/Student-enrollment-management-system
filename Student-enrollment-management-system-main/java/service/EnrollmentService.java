package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Enrollment;
import student_model.StudentEnrollmentAccessModel;

public class EnrollmentService {
    

    public String generateNextClassSection(String courseName) {
        // Extract a clean short prefix or abbreviation for the course if needed, or use the course name directly
        // e.g., "BSIT"
        String coursePrefix = courseName != null && !courseName.trim().isEmpty() ? courseName.trim().toUpperCase().replaceAll("\\s+", "") : "GEN";
        if (coursePrefix.length() > 5) {
            coursePrefix = coursePrefix.substring(0, 4); // Keep it concise if it's a long name
        }

        // Query the latest class section for this course to calculate batch and queue numbers
        String sql = "SELECT class_section FROM enrollments WHERE course_name = ? AND class_section LIKE ? ORDER BY class_section DESC LIMIT 1";
        
        String latestSection = null;
        String searchPattern = coursePrefix + "-%";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, courseName);
            stmt.setString(2, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    latestSection = rs.getString("class_section");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int batchNumber = 1;
        int queueNumber = 1;

        if (latestSection != null) {
            // Example format: BSIT-01-01 or BSIT-01-040
            try {
                String[] parts = latestSection.split("-");
                if (parts.length >= 3) {
                    batchNumber = Integer.parseInt(parts[1]);
                    queueNumber = Integer.parseInt(parts[2]);

                    queueNumber++; // Increment queue for the next student
                    if (queueNumber > 40) {
                        queueNumber = 1; // Reset queue when batch hits 40 students max
                        batchNumber++;   // Move to the next batch section
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Format batch and queue numbers (Batch padded to 2 digits, Queue padded to 2 or 3 digits as specified: 01, 002, etc.)
        // Following your examples: 1st is BSIT-01-01, 2nd is BSIT-01-002, 41st is BSIT-02-001
        String queueFormatted = (queueNumber > 9) ? String.format("%03d", queueNumber) : String.format("%02d", queueNumber);
        if (batchNumber > 9) {
             return String.format("%s-%03d-%s", coursePrefix, batchNumber, queueFormatted);
        } else {
             return String.format("%s-%02d-%s", coursePrefix, batchNumber, queueFormatted);
        }
    }

    public String generateNextStudentId() {
        // 1. Generate School Year format (e.g., 2026 -> 2627)
        int currentYear = java.time.LocalDate.now().getYear();
        String schoolYear = (currentYear % 100) + String.format("%02d", (currentYear + 1) % 100);

        // 2. Query the latest student ID for the current school year prefix
        String prefix = schoolYear + "-";
        String sql = "SELECT student_id FROM enrollments WHERE student_id LIKE ? ORDER BY student_id DESC LIMIT 1";
        
        String latestId = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prefix + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    latestId = rs.getString("student_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int batchNumber = 1;
        int queueNumber = 1;

        if (latestId != null) {
            // Example format: 2627-001-001
            try {
                String[] parts = latestId.split("-");
                if (parts.length == 3) {
                    batchNumber = Integer.parseInt(parts[1]);
                    queueNumber = Integer.parseInt(parts[2]);

                    queueNumber++; // Increment queue for the next student
                    if (queueNumber > 999) {
                        queueNumber = 1; // Reset queue when batch hits 999
                        batchNumber++;   // Move to the next batch
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 3. Format into final ID string (e.g., 2627-001-001)
        return String.format("%s-%03d-%03d", schoolYear, batchNumber, queueNumber);
    }
    
    // Purpose: Automatically clean up declined drafts older than 15 days and fetch valid declined requests
    public List<Enrollment> getDeclinedEnrollmentRequests() {
        cleanupExpiredDeclinedDrafts(); // Clean up drafts older than 15 days automatically
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE status = 'Declined'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Enrollment(
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("gender"),
                    rs.getString("email"),
                    rs.getString("grade_level"),
                    rs.getString("course_name"),
                    rs.getString("sponsorship"),
                    rs.getString("class_section"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Purpose: Delete declined enrollment requests older than 15 days automatically
    private void cleanupExpiredDeclinedDrafts() {
        String sql = "DELETE FROM enrollments WHERE status = 'Declined' AND date_of_enrollment < NOW() - INTERVAL 15 DAY";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    public List<Enrollment> getApprovedStudents() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE status = 'Approved'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Enrollment(
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("gender"),
                    rs.getString("email"),
                    rs.getString("grade_level"),
                    rs.getString("course_name"),
                    rs.getString("sponsorship"),
                    rs.getString("class_section"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getActiveYearLevels() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT grade_level FROM enrollments WHERE status = 'Approved'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String val = rs.getString("grade_level");
                if (val != null && !val.trim().isEmpty()) list.add(val.trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getActiveCourses() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT course_name FROM enrollments WHERE status = 'Approved'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String val = rs.getString("course_name");
                if (val != null && !val.trim().isEmpty()) list.add(val.trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getActiveClassSections() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT class_section FROM enrollments WHERE status = 'Approved'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String val = rs.getString("class_section");
                if (val != null && !val.trim().isEmpty()) list.add(val.trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Enrollment> getPendingEnrollmentRequests() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE status = 'Pending'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Enrollment(
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("gender"),
                    rs.getString("email"),
                    rs.getString("grade_level"),
                    rs.getString("course_name"),
                    rs.getString("sponsorship"),
                    rs.getString("class_section"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

public boolean updateEnrollmentStatus(String studentId, String status) {
        // Update status and refresh date_of_enrollment so the 15-day draft timer resets if newly declined
        String sql = "UPDATE enrollments SET status = ?, date_of_enrollment = NOW() WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEnrollment(String studentId) {
        String sql = "DELETE FROM enrollments WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Enrollment getEnrollmentByStudent(String studentId) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? OR email LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setString(2, "%" + studentId + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Enrollment(
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("gender"),
                    rs.getString("email"),
                    rs.getString("grade_level"),
                    rs.getString("course_name"),
                    rs.getString("sponsorship"),
                    rs.getString("class_section"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StudentEnrollmentAccessModel getEnrollmentByStudentUsername(String usernameOrEmail) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? OR email = ? OR first_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);
            stmt.setString(3, usernameOrEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                StudentEnrollmentAccessModel model = new StudentEnrollmentAccessModel();
                model.setStudentId(rs.getString("student_id"));
                model.setStudentName(rs.getString("first_name") + " " + rs.getString("last_name"));
                model.setEmailAddress(rs.getString("email"));
                model.setGradeLevel(rs.getString("grade_level"));
                model.setCourse(rs.getString("course_name"));
                model.setSponsorship(rs.getString("sponsorship"));
                model.setClassSection(rs.getString("class_section"));
                model.setStatus(rs.getString("status"));
                model.setEnrolled("Approved".equalsIgnoreCase(rs.getString("status")));
                return model;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAvailableCourses() {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT course_name FROM courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String course = rs.getString("course_name");
                if (course != null && !course.trim().isEmpty()) {
                    courses.add(course.trim());
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

    // Purpose: Save new enrollment or update existing enrollment by student_id to prevent duplicates
    public boolean saveOrUpdateEnrollment(Enrollment enrollment) {
        String checkSql = "SELECT COUNT(*) FROM enrollments WHERE student_id = ?";
        String updateSql = "UPDATE enrollments SET first_name = ?, last_name = ?, gender = ?, email = ?, grade_level = ?, course_name = ?, sponsorship = ?, class_section = ?, status = ?, date_of_enrollment = NOW() WHERE student_id = ?";
        String insertSql = "INSERT INTO enrollments (student_id, first_name, last_name, gender, email, grade_level, course_name, sponsorship, class_section, status, date_of_enrollment) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DatabaseConnection.getConnection()) {
            boolean exists = false;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, enrollment.getStudentId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        exists = true;
                    }
                }
            }

            if (exists) {
                // Update the existing record so no duplicates are created
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, enrollment.getFirstName());
                    updateStmt.setString(2, enrollment.getLastName());
                    updateStmt.setString(3, enrollment.getGender());
                    updateStmt.setString(4, enrollment.getEmail());
                    updateStmt.setString(5, enrollment.getGradeLevel());
                    updateStmt.setString(6, enrollment.getCourseName());
                    updateStmt.setString(7, enrollment.getSponsorship());
                    updateStmt.setString(8, enrollment.getClassSection());
                    updateStmt.setString(9, enrollment.getStatus());
                    updateStmt.setString(10, enrollment.getStudentId());
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                // Insert new record if none exists
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, enrollment.getStudentId());
                    insertStmt.setString(2, enrollment.getFirstName());
                    insertStmt.setString(3, enrollment.getLastName());
                    insertStmt.setString(4, enrollment.getGender());
                    insertStmt.setString(5, enrollment.getEmail());
                    insertStmt.setString(6, enrollment.getGradeLevel());
                    insertStmt.setString(7, enrollment.getCourseName());
                    insertStmt.setString(8, enrollment.getSponsorship());
                    insertStmt.setString(9, enrollment.getClassSection());
                    insertStmt.setString(10, enrollment.getStatus());
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}