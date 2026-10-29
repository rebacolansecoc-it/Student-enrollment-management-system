package student_model;

import java.io.Serializable;
import java.time.LocalDate;

public class EnrollmentRequestModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentUsername;
    private String studentName;
    private String gradeLevel;
    private String semester;
    private String course;
    private String sponsorship;
    private String emailAddress;
    private String studentId;        // e.g. 2026-0001-01[cite: 17]
    private String classSection;     // e.g. BSIT-01-01[cite: 17]
    private String status;           // PENDING, APPROVED, DECLINED[cite: 17]
    private String dateOfEnrollment;

    // Default Constructor
    public EnrollmentRequestModel() {
        this.status = "PENDING";
        this.semester = "1st Semester";
        this.dateOfEnrollment = LocalDate.now().toString();
    }

    public EnrollmentRequestModel(String studentUsername, String studentName, String gradeLevel,
                             String course, String sponsorship, String emailAddress,
                             String studentId, String classSection) {
        this(studentUsername, studentName, gradeLevel, course, sponsorship, emailAddress,
             studentId, classSection, "1st Semester");
    }

    public EnrollmentRequestModel(String studentUsername, String studentName, String gradeLevel,
                             String course, String sponsorship, String emailAddress,
                             String studentId, String classSection, String semester) {
        this.studentUsername = studentUsername;
        this.studentName = studentName;
        this.gradeLevel = gradeLevel;
        this.course = course;
        this.sponsorship = sponsorship;
        this.emailAddress = emailAddress;
        this.studentId = studentId;
        this.classSection = classSection;
        this.semester = semester == null || semester.trim().isEmpty() ? "1st Semester" : semester.trim();
        this.status = "PENDING";
        this.dateOfEnrollment = LocalDate.now().toString();
    }

    // Getters and Setters
    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String studentUsername) { this.studentUsername = studentUsername; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }

    public String getSemester() { return semester == null || semester.trim().isEmpty() ? "1st Semester" : semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getSponsorship() { return sponsorship; }
    public void setSponsorship(String sponsorship) { this.sponsorship = sponsorship; }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getClassSection() { return classSection; }
    public void setClassSection(String classSection) { this.classSection = classSection; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDateOfEnrollment() { return dateOfEnrollment; }
    public void setDateOfEnrollment(String dateOfEnrollment) { this.dateOfEnrollment = dateOfEnrollment; }
}