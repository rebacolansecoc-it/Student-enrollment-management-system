package model;

import java.util.ArrayList;
import java.util.List;

// Purpose: Model representation of a Teacher entity
public class Teacher {
    private String teacherId;
    private String username;
    private String name;
    private String email;
    private String preferredCourse;
    private String preferredYearLevel;
    private List<Subject> assignedSubjects = new ArrayList<>();

    public Teacher(String teacherId, String username, String name, String email) {
        this.teacherId = teacherId;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public String getTeacherId() { return teacherId; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPreferredCourse() { return preferredCourse; }
    public void setPreferredCourse(String preferredCourse) { this.preferredCourse = preferredCourse; }
    public String getPreferredYearLevel() { return preferredYearLevel; }
    public void setPreferredYearLevel(String preferredYearLevel) { this.preferredYearLevel = preferredYearLevel; }
    public List<Subject> getAssignedSubjects() { return assignedSubjects; }
}