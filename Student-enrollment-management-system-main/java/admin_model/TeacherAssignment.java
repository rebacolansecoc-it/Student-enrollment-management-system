package admin_model;

// Purpose: Data model representing teacher assignment and profile details
public class TeacherAssignment {
    private String id;
    private String username;
    private String teacherName;
    private String email;
    private String course;
    private String subject;
    private String classSection;
    private int startYear;
    private int endYear;
    private int semester;
    private String status;

    public TeacherAssignment(String id, 
            String username, 
            String teacherName, 
            String email, 
            String course, 
            String subject, 
            String classSection, 
            int startYear, 
            int endYear, 
            int semester, 
            String status) {
        this.id = id;
        this.username = username;
        this.teacherName = teacherName;
        this.email = email;
        this.course = course;
        this.subject = subject;
        this.classSection = classSection;
        this.startYear = startYear;
        this.endYear = endYear;
        this.semester = semester;
        this.status = status;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getTeacherName() { return teacherName; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public String getSubject() { return subject; }
    public String getClassSection() { return classSection; }
    public int getStartYear() { return startYear; }
    public int getEndYear() { return endYear; }
    public int getSemester() { return semester; }
    public String getStatus() { return status; }
}