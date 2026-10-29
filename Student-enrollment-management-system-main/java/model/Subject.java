package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Represents a subject entity mapped with courses, units, year levels, and assigned teachers
public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String subjectCode;
    private String subjectTitle;
    private int units;
    private String courseName;
    private String teacherName;
    
    // Helper fields used by UI filters and views
    private String yearLevel = "1st year";
    private String semester = "1st Semester";
    private List<String> assignedTeachers = new ArrayList<>();
    private List<String> assignedCourses = new ArrayList<>();

    // Default Constructor
    public Subject() {}

    // Parameterized Constructor
    public Subject(String subjectCode, String subjectTitle, int units, String courseName, String teacherName) {
        this.subjectCode = subjectCode;
        this.subjectTitle = subjectTitle;
        this.units = units;
        this.courseName = courseName != null ? courseName : "Unassigned";
        this.teacherName = teacherName != null ? teacherName : "Unassigned";
        
        if (this.teacherName != null && !this.teacherName.equals("Unassigned")) {
            assignedTeachers.add(this.teacherName);
        }
        if (this.courseName != null && !this.courseName.equals("Unassigned")) {
            assignedCourses.add(this.courseName);
        }
    }

    // Getters and Setters
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectTitle() { return subjectTitle; }
    public void setSubjectTitle(String subjectTitle) { this.subjectTitle = subjectTitle; }

    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { 
        this.teacherName = (teacherName == null || teacherName.trim().isEmpty()) ? "Unassigned" : teacherName; 
    }

    public String getYearLevel() { return yearLevel; }
    public void setYearLevel(String yearLevel) { this.yearLevel = yearLevel; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    // Purpose: Provide formatted string of assigned teachers for table display
    public Object getTeachersDisplay() {
        if (assignedTeachers != null && !assignedTeachers.isEmpty()) {
            return String.join(", ", assignedTeachers);
        }
        return (teacherName == null || teacherName.trim().isEmpty()) ? "Unassigned" : teacherName;
    }

    // Display helpers for JTables
    public String getTeacherDisplay() {
        Object display = getTeachersDisplay();
        return display != null ? display.toString() : "Unassigned";
    }

    public String getCoursesDisplay() {
        return (courseName == null || courseName.trim().isEmpty()) ? "Unassigned" : courseName;
    }
}