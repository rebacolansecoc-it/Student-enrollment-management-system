package model;

import java.io.Serializable;

// Represents a course entity mapped to the database courses table
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseId;
    private String courseName;
    private String units;

    // Default Constructor
    public Course() {}

    // Parameterized Constructor
    public Course(String courseId, String courseName, String units) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.units = units;
    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getUnits() { return units; }
    public void setUnits(String units) { this.units = units; }
}