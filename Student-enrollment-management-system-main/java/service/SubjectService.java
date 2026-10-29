package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Subject;

public class SubjectService {

    // Purpose: Fetch students enrolled matching exact course, year level, and semester criteria
    public static List<String[]> getStudentsEnrolledForSubjectCriteria(String courseName, String yearLevel, String semester) {
        List<String[]> students = new ArrayList<>();
        String enrollSql = "SELECT student_id, CONCAT(first_name, ' ', last_name) AS student_name, course_name, grade_level " +
                           "FROM enrollments " +
                           "WHERE status = 'Approved' " +
                           "AND LOWER(course_name) = LOWER(?) " +
                           "AND LOWER(grade_level) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement enrollStmt = conn.prepareStatement(enrollSql)) {
            enrollStmt.setString(1, courseName != null ? courseName : "");
            enrollStmt.setString(2, yearLevel != null ? yearLevel : "");
            ResultSet enrollRs = enrollStmt.executeQuery();

            while (enrollRs.next()) {
                String[] studentData = {
                    enrollRs.getString("student_id"),
                    enrollRs.getString("student_name"),
                    enrollRs.getString("course_name"),
                    enrollRs.getString("grade_level")
                };
                students.add(studentData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // 1. Fetch all subjects joined with courses and teachers for display
    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT s.subject_code, s.subject_title, s.units, c.course_name, t.full_name AS teacher_name, s.year_level, s.semester " +
                     "FROM subjects s " +
                     "LEFT JOIN courses c ON s.course_id = c.course_id " +
                     "LEFT JOIN teachers t ON s.assigned_teacher_id = t.teacher_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Subject sub = new Subject(
                    rs.getString("subject_code"),
                    rs.getString("subject_title"),
                    rs.getInt("units"),
                    rs.getString("course_name"),
                    rs.getString("teacher_name")
                );
                sub.setYearLevel(rs.getString("year_level") != null ? rs.getString("year_level") : "1st year");
                sub.setSemester(rs.getString("semester") != null ? rs.getString("semester") : "1st Semester");
                list.add(sub);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Purpose: Fetch subjects filtered specifically by student's enrolled course, year level, and semester from MySQL
    public static List<Subject> getSubjectsForStudent(String enrolledCourse, String enrolledYearLevel, String enrolledSemester) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT s.subject_code, s.subject_title, s.units, c.course_name, t.full_name AS teacher_name, s.year_level, s.semester " +
                     "FROM subjects s " +
                     "LEFT JOIN courses c ON s.course_id = c.course_id " +
                     "LEFT JOIN teachers t ON s.assigned_teacher_id = t.teacher_id " +
                     "WHERE (LOWER(c.course_name) = LOWER(?) OR LOWER(s.course_id) = LOWER(?)) " +
                     "AND LOWER(s.year_level) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, enrolledCourse);
            stmt.setString(2, enrolledCourse);
            stmt.setString(3, enrolledYearLevel);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Subject sub = new Subject(
                    rs.getString("subject_code"),
                    rs.getString("subject_title"),
                    rs.getInt("units"),
                    rs.getString("course_name"),
                    rs.getString("teacher_name")
                );
                sub.setYearLevel(rs.getString("year_level") != null ? rs.getString("year_level") : enrolledYearLevel);
                sub.setSemester(rs.getString("semester") != null ? rs.getString("semester") : enrolledSemester);
                list.add(sub);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Add a new subject to the database with auto-generated code
    public boolean addSubject(String title, int units, String courseName, String teacherName) {
        String teacherIdSql = "SELECT teacher_id FROM teachers WHERE full_name = ?";
        String insertSql = "INSERT INTO subjects (subject_code, subject_title, units, course_id, assigned_teacher_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            String courseId = null;
            String courseQuery = "SELECT course_id FROM courses WHERE course_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(courseQuery)) {
                stmt.setString(1, courseName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) courseId = rs.getString("course_id");
            }

            String teacherId = null;
            if (teacherName != null && !teacherName.equals("Unassigned")) {
                try (PreparedStatement stmt = conn.prepareStatement(teacherIdSql)) {
                    stmt.setString(1, teacherName);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) teacherId = rs.getString("teacher_id");
                }
            }

            String subjectCode = "SUB-" + (1000 + (int)(Math.random() * 9000));

            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, subjectCode);
                stmt.setString(2, title);
                stmt.setInt(3, units);
                stmt.setString(4, courseId);
                stmt.setString(5, teacherId);

                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Update an existing subject
    public boolean updateSubject(String subjectCode, String title, int units) {
        String sql = "UPDATE subjects SET subject_title = ?, units = ? WHERE subject_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setInt(2, units);
            stmt.setString(3, subjectCode);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Delete a subject by code
    public boolean deleteSubject(String subjectCode) {
        String sql = "DELETE FROM subjects WHERE subject_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subjectCode);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Fetch all registered course names for dropdowns
    public List<String> getCourseNames() {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT course_name FROM courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(rs.getString("course_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // 6. Fetch all teacher names for dropdowns
    public List<String> getTeacherNames() {
        List<String> teachers = new ArrayList<>();
        teachers.add("Unassigned");
        String sql = "SELECT full_name FROM teachers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                teachers.add(rs.getString("full_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }
    
    // Purpose: Fetch subjects assigned to a specific teacher filtered by year level and semester from MySQL
    public static List<Subject> getSubjectsForTeacher(String teacherUsernameOrName, String yearLevel, String semester) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT s.subject_code, s.subject_title, s.units, c.course_name, t.full_name AS teacher_name, s.year_level, s.semester " +
                     "FROM subjects s " +
                     "LEFT JOIN courses c ON s.course_id = c.course_id " +
                     "LEFT JOIN teachers t ON s.assigned_teacher_id = t.teacher_id " +
                     "WHERE (LOWER(t.teacher_id) = LOWER(?) OR LOWER(t.full_name) = LOWER(?) OR LOWER(t.email) = LOWER(?)) " +
                     "AND LOWER(s.year_level) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teacherUsernameOrName);
            stmt.setString(2, teacherUsernameOrName);
            stmt.setString(3, teacherUsernameOrName);
            stmt.setString(4, yearLevel != null ? yearLevel : "1st year");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Subject sub = new Subject(
                    rs.getString("subject_code"),
                    rs.getString("subject_title"),
                    rs.getInt("units"),
                    rs.getString("course_name"),
                    rs.getString("teacher_name")
                );
                sub.setYearLevel(rs.getString("year_level") != null ? rs.getString("year_level") : yearLevel);
                sub.setSemester(rs.getString("semester") != null ? rs.getString("semester") : semester);
                list.add(sub);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Purpose: Fetch specific single Subject object by code
    public static Subject getSubjectByCode(String subjectCode) {
        String sql = "SELECT s.subject_code, s.subject_title, s.units, c.course_name, t.full_name AS teacher_name, s.year_level, s.semester " +
                     "FROM subjects s " +
                     "LEFT JOIN courses c ON s.course_id = c.course_id " +
                     "LEFT JOIN teachers t ON s.assigned_teacher_id = t.teacher_id " +
                     "WHERE s.subject_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subjectCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Subject sub = new Subject(
                    rs.getString("subject_code"),
                    rs.getString("subject_title"),
                    rs.getInt("units"),
                    rs.getString("course_name"),
                    rs.getString("teacher_name")
                );
                sub.setYearLevel(rs.getString("year_level"));
                sub.setSemester(rs.getString("semester"));
                return sub;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Purpose: Fetch students enrolled in a specific subject matching exact course, year level, and semester criteria
    public static List<String[]> getStudentsEnrolledInSubject(String subjectCode) {
        List<String[]> students = new ArrayList<>();
        Subject sub = getSubjectByCode(subjectCode);
        if (sub != null) {
            return getStudentsEnrolledForSubjectCriteria(sub.getCourseName(), sub.getYearLevel(), sub.getSemester());
        }
        return students;
    }
}