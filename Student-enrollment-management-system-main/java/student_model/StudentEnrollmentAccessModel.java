package student_model;

public class StudentEnrollmentAccessModel {
    private String studentId;
    private String studentName;
    private String emailAddress;
    private String gradeLevel;
    private String course;
    private String sponsorship;
    private String classSection;
    private String status;
    private String semester = "1st Semester"; // Default fallback
    private boolean isEnrolled;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getSponsorship() { return sponsorship; }
    public void setSponsorship(String sponsorship) { this.sponsorship = sponsorship; }

    public String getClassSection() { return classSection; }
    public void setClassSection(String classSection) { this.classSection = classSection; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public boolean isEnrolled() { return isEnrolled; }
    public void setEnrolled(boolean enrolled) { isEnrolled = enrolled; }
}