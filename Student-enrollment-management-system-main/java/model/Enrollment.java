package model;

import java.io.Serializable;

public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String gradeLevel;
    private String courseName;
    private String sponsorship;
    private String classSection;
    private String status;

    public Enrollment() {}

    public Enrollment(String studentId, String firstName, String lastName, String gender, String email, String gradeLevel, 
                      String courseName, String sponsorship, String classSection, String status) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.gradeLevel = gradeLevel;
        this.courseName = courseName;
        this.sponsorship = sponsorship;
        this.classSection = classSection;
        this.status = status != null ? status : "Pending";
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getStudentName() { 
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return "";
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getSponsorship() { return sponsorship; }
    public void setSponsorship(String sponsorship) { this.sponsorship = sponsorship; }

    public String getClassSection() { return classSection; }
    public void setClassSection(String classSection) { this.classSection = classSection; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}