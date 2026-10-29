package admin_gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import landingPageGui.LogInPage;
import service.DatabaseConnection;
import gui.AdminHomePage;

public class AdminSubjectAccess extends javax.swing.JFrame {

    private String username = "Admin";

    public AdminSubjectAccess() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        loadCoursesIntoComboBox();
        loadAdminSubjectTable();
        setupTableSelectionListener();
        clearDetails();
    }

    public AdminSubjectAccess(String username) {
        this();
        this.username = username;
    }

    // Loads courses added by admin into the course filter and dialog combo boxes
    private void loadCoursesIntoComboBox() {
        DefaultComboBoxModel<String> courseModel = new DefaultComboBoxModel<>();
        courseModel.addElement("All Courses");
        
        String sql = "SELECT course_name FROM courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courseModel.addElement(rs.getString("course_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        subjectSortByCourse.setModel(courseModel);
    }

    // Loads and displays all subjects from the database into subjectTable
    private void loadAdminSubjectTable() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Subject Code", "Subject Name", "Courses", "Year Level", "Unit", "Assigned Teacher", "Semester"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        String sql = "SELECT s.subject_code, s.subject_title, s.units, s.course_id, c.course_name, " +
                     "s.assigned_teacher_id, t.full_name, s.year_level, s.semester " +
                     "FROM subjects s " +
                     "LEFT JOIN courses c ON s.course_id = c.course_id " +
                     "LEFT JOIN teachers t ON s.assigned_teacher_id = t.teacher_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String teacherName = rs.getString("full_name");
                if (teacherName == null || teacherName.trim().isEmpty()) {
                    teacherName = "Unassigned";
                }
                String courseName = rs.getString("course_name");
                if (courseName == null || courseName.trim().isEmpty()) {
                    courseName = "Unassigned";
                }

                model.addRow(new Object[] {
                    rs.getString("subject_code"),
                    rs.getString("subject_title"),
                    courseName,
                    rs.getString("year_level") != null ? rs.getString("year_level") : "1st year",
                    rs.getInt("units"),
                    teacherName,
                    rs.getString("semester") != null ? rs.getString("semester") : "1st Semester"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        subjectTable.setModel(model);
        clearDetails();
    }

    // Clears the detail view fields
    private void clearDetails() {
        subjectCodeViewOnly.setText("");
        subjectNameEdit.setText("");
        unitViewOnly.setText("");
        subjectReassignTeacher.removeAllItems();
        subjectReassignTeacher.addItem("Unassigned");
        subjectCodeViewOnly.setEditable(false);
        unitViewOnly.setEditable(false);
        subjectNameEdit.setEditable(false);
        subjectReassignTeacher.setEnabled(false);
    }

    // Dynamically update the assigned teacher combo box based on the selected course
    private void updateAssignedTeacherComboBox(String courseName, String currentAssignedTeacher) {
        subjectReassignTeacher.removeAllItems();
        subjectReassignTeacher.addItem("Unassigned");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT teacher_id, full_name FROM teachers WHERE LOWER(department) = LOWER(?) OR department IS NULL OR department = ''")) {
            stmt.setString(1, courseName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subjectReassignTeacher.addItem(rs.getString("full_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (currentAssignedTeacher != null && !currentAssignedTeacher.isEmpty()) {
            subjectReassignTeacher.setSelectedItem(currentAssignedTeacher);
        }
    }

    // Handles table row selection to populate detail sections
    private void setupTableSelectionListener() {
        subjectTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = subjectTable.getSelectedRow();
                if (selectedRow != -1) {
                    subjectCodeViewOnly.setText((String) subjectTable.getValueAt(selectedRow, 0));
                    subjectNameEdit.setText((String) subjectTable.getValueAt(selectedRow, 1));
                    unitViewOnly.setText(String.valueOf(subjectTable.getValueAt(selectedRow, 4)));
                    String courseName = (String) subjectTable.getValueAt(selectedRow, 2);
                    String assignedTeacher = (String) subjectTable.getValueAt(selectedRow, 5);
                    updateAssignedTeacherComboBox(courseName, assignedTeacher);
                }
            }
        });
    }

    // Applies filters and search query across subjectTable
    private void applyFiltersAndSearch() {
        String schoolYear = (String) subjectSortBySchoolYear.getSelectedItem();
        String semester = (String) subjectSortBySemester.getSelectedItem();
        String course = (String) subjectSortByCourse.getSelectedItem();
        String unitStr = (String) subjectSortByUnit.getSelectedItem();

        DefaultTableModel model = (DefaultTableModel) subjectTable.getModel();
        model.setRowCount(0);

        StringBuilder sql = new StringBuilder("SELECT s.subject_code, s.subject_title, s.units, c.course_name, t.full_name, s.year_level, s.semester " +
                                              "FROM subjects s " +
                                              "LEFT JOIN courses c ON s.course_id = c.course_id " +
                                              "LEFT JOIN teachers t ON s.assigned_teacher_id = t.teacher_id WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (schoolYear != null && !schoolYear.equals("All")) {
            sql.append(" AND LOWER(s.year_level) = LOWER(?)");
            params.add(schoolYear);
        }
        if (semester != null && !semester.equals("All")) {
            sql.append(" AND LOWER(s.semester) = LOWER(?)");
            params.add(semester);
        }
        if (course != null && !course.equals("All Courses")) {
            sql.append(" AND LOWER(c.course_name) = LOWER(?)");
            params.add(course);
        }
        if (unitStr != null && !unitStr.trim().isEmpty()) {
            try {
                int unitVal = Integer.parseInt(unitStr.trim());
                sql.append(" AND s.units = ?");
                params.add(unitVal);
            } catch (NumberFormatException ignored) {}
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String subCode = rs.getString("subject_code");
                String subTitle = rs.getString("subject_title");
                String courseName = rs.getString("course_name") != null ? rs.getString("course_name") : "Unassigned";
                String yrLvl = rs.getString("year_level") != null ? rs.getString("year_level") : "1st year";
                int units = rs.getInt("units");
                String teacher = rs.getString("full_name") != null ? rs.getString("full_name") : "Unassigned";
                String sem = rs.getString("semester") != null ? rs.getString("semester") : "1st Semester";

                model.addRow(new Object[]{subCode, subTitle, courseName, yrLvl, units, teacher, sem});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        exitBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        studentBtn = new javax.swing.JButton();
        coursetBtn = new javax.swing.JButton();
        subjectBtn = new javax.swing.JButton();
        teacherBtn = new javax.swing.JButton();
        enrollmentBtn = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        subjectTable = new javax.swing.JTable();
        subjectSortBySemester = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        subjectSortBySchoolYear = new javax.swing.JComboBox<>();
        subjectSortByCourse = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        subjectNameEdit = new javax.swing.JTextField();
        subjectCodeViewOnly = new javax.swing.JTextField();
        unitViewOnly = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        saveEdit = new javax.swing.JButton();
        editSubject = new javax.swing.JButton();
        deletedBtn = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        subjectSortByUnit = new javax.swing.JComboBox<>();
        formSubmit = new javax.swing.JButton();
        subjectReassignTeacher = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        homeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 255, 204));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/warning_24dp_F19E39_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jLabel12.setText("Teacher's Access");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        exitBtn.setBackground(new java.awt.Color(51, 51, 51));
        exitBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        exitBtn.setForeground(new java.awt.Color(255, 204, 204));
        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/logout_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        exitBtn.setText("Exit");
        exitBtn.setBorderPainted(false);
        exitBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        exitBtn.addActionListener(this::exitBtnActionPerformed);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SEMS");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitBtn)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));

        jPanel7.setBackground(new java.awt.Color(102, 102, 102));

        studentBtn.setBackground(new java.awt.Color(51, 51, 51));
        studentBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        studentBtn.setForeground(new java.awt.Color(204, 255, 255));
        studentBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/account_circle_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        studentBtn.setText("Students");
        studentBtn.setBorderPainted(false);
        studentBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        studentBtn.addActionListener(this::studentBtnActionPerformed);

        coursetBtn.setBackground(new java.awt.Color(51, 51, 51));
        coursetBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        coursetBtn.setForeground(new java.awt.Color(204, 255, 255));
        coursetBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/school_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        coursetBtn.setText("Courses");
        coursetBtn.setBorderPainted(false);
        coursetBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        coursetBtn.addActionListener(this::coursetBtnActionPerformed);

        subjectBtn.setBackground(new java.awt.Color(51, 51, 51));
        subjectBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        subjectBtn.setForeground(new java.awt.Color(51, 255, 51));
        subjectBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/topic_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        subjectBtn.setText("Subjects");
        subjectBtn.setBorderPainted(false);
        subjectBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        subjectBtn.addActionListener(this::subjectBtnActionPerformed);

        teacherBtn.setBackground(new java.awt.Color(51, 51, 51));
        teacherBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        teacherBtn.setForeground(new java.awt.Color(204, 255, 255));
        teacherBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/face_6_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        teacherBtn.setText("Teachers");
        teacherBtn.setBorderPainted(false);
        teacherBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        teacherBtn.addActionListener(this::teacherBtnActionPerformed);

        enrollmentBtn.setBackground(new java.awt.Color(51, 51, 51));
        enrollmentBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        enrollmentBtn.setForeground(new java.awt.Color(204, 255, 255));
        enrollmentBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/history_edu_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        enrollmentBtn.setText("Enrollment");
        enrollmentBtn.setBorderPainted(false);
        enrollmentBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        enrollmentBtn.addActionListener(this::enrollmentBtnActionPerformed);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enrollmentBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subjectBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(studentBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(teacherBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(coursetBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(enrollmentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(teacherBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(subjectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(coursetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(studentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(314, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(102, 102, 102));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SUBJECTS");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Semester:");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Year level:");

        subjectTable.setAutoCreateRowSorter(true);
        subjectTable.setBackground(new java.awt.Color(153, 153, 153));
        subjectTable.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject Code", "Subject Name", "Courses", "Year Level", "Unit", "Assigned Teacher", "Semester"
            }
        ));
        jScrollPane5.setViewportView(subjectTable);

        subjectSortBySemester.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectSortBySemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Semester", "1st Semester", "2nd Semester" }));
        subjectSortBySemester.addActionListener(this::subjectSortBySemesterActionPerformed);

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Course:");

        subjectSortBySchoolYear.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectSortBySchoolYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All subjects", "1st year", "2nd year", "3rd year", "4th year" }));
        subjectSortBySchoolYear.addActionListener(this::subjectSortBySchoolYearActionPerformed);

        subjectSortByCourse.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectSortByCourse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Courses" }));
        subjectSortByCourse.addActionListener(this::subjectSortByCourseActionPerformed);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Subject Name:");

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 162, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Subject Code:");

        subjectNameEdit.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectNameEdit.setText("name");
        subjectNameEdit.addActionListener(this::subjectNameEditActionPerformed);

        subjectCodeViewOnly.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectCodeViewOnly.setText("Exm; GEN-001");
        subjectCodeViewOnly.addActionListener(this::subjectCodeViewOnlyActionPerformed);

        unitViewOnly.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        unitViewOnly.setText("Unit");
        unitViewOnly.addActionListener(this::unitViewOnlyActionPerformed);

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Unit:");

        saveEdit.setBackground(new java.awt.Color(51, 51, 51));
        saveEdit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        saveEdit.setForeground(new java.awt.Color(51, 255, 51));
        saveEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/add_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        saveEdit.setText("Save");
        saveEdit.setBorderPainted(false);
        saveEdit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveEdit.addActionListener(this::saveEditActionPerformed);

        editSubject.setBackground(new java.awt.Color(51, 51, 51));
        editSubject.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editSubject.setForeground(new java.awt.Color(255, 153, 0));
        editSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        editSubject.setText("Edit");
        editSubject.setBorderPainted(false);
        editSubject.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editSubject.addActionListener(this::editSubjectActionPerformed);

        deletedBtn.setBackground(new java.awt.Color(51, 51, 51));
        deletedBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deletedBtn.setForeground(new java.awt.Color(255, 51, 51));
        deletedBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        deletedBtn.setText("Delete");
        deletedBtn.setBorderPainted(false);
        deletedBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        deletedBtn.addActionListener(this::deletedBtnActionPerformed);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 255, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("SORT CONTENT");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 255, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("DETAILS");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Unit:");

        subjectSortByUnit.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectSortByUnit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "all Unit", "2", "3", "4", "5" }));
        subjectSortByUnit.addActionListener(this::subjectSortByUnitActionPerformed);

        formSubmit.setBackground(new java.awt.Color(51, 51, 51));
        formSubmit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        formSubmit.setForeground(new java.awt.Color(255, 255, 0));
        formSubmit.setText("ADD SUBJECT");
        formSubmit.setBorderPainted(false);
        formSubmit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        formSubmit.addActionListener(this::formSubmitActionPerformed);

        subjectReassignTeacher.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectReassignTeacher.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "all Unit", "2", "3", "4", "5" }));
        subjectReassignTeacher.addActionListener(this::subjectReassignTeacherActionPerformed);

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Assinged Teacher:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(subjectSortByCourse, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(subjectSortBySchoolYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(subjectSortBySemester, 0, 141, Short.MAX_VALUE)))
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(subjectSortByUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(32, 32, 32)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(saveEdit)
                                        .addGap(27, 27, 27)
                                        .addComponent(editSubject)
                                        .addGap(33, 33, 33)
                                        .addComponent(deletedBtn))))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(subjectNameEdit)
                                    .addComponent(subjectCodeViewOnly, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                    .addComponent(unitViewOnly)
                                    .addComponent(subjectReassignTeacher, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formSubmit)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(formSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(subjectSortBySchoolYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(subjectSortBySemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(subjectSortByCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel13))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel16)
                                            .addComponent(subjectSortByUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel7)
                                            .addComponent(subjectNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel11)
                                            .addComponent(subjectCodeViewOnly, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel15)
                                            .addComponent(unitViewOnly, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel17)
                                            .addComponent(subjectReassignTeacher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(9, 9, 9)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(saveEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(editSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(deletedBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setMaximumSize(new java.awt.Dimension(100, 100));

        homeBtn.setBackground(new java.awt.Color(51, 51, 51));
        homeBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        homeBtn.setForeground(new java.awt.Color(204, 0, 0));
        homeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/home_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        homeBtn.setBorder(null);
        homeBtn.addActionListener(this::homeBtnActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(homeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(homeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new LogInPage().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_exitBtnActionPerformed

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        // TODO add your handling code here:
        new AdminHomePage(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_homeBtnActionPerformed

    private void enrollmentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enrollmentBtnActionPerformed
        new AdminEnrollmentAccess(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_enrollmentBtnActionPerformed

    private void teacherBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherBtnActionPerformed
        // TODO add your handling code here:
        try {
            AdminTeacherAccess teacherAccess = new AdminTeacherAccess(username);
            teacherAccess.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Failed to open Teacher Access: " + e.getMessage(), 
                "Navigation Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_teacherBtnActionPerformed

    private void subjectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectBtnActionPerformed
        // TODO add your handling code here:
        loadAdminSubjectTable();
    }//GEN-LAST:event_subjectBtnActionPerformed

    private void coursetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coursetBtnActionPerformed
        // TODO add your handling code here:
        new AdminCourseAccess(username).setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_coursetBtnActionPerformed

    private void studentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentBtnActionPerformed
        // TODO add your handling code here:
        new AdminStudentAccess(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_studentBtnActionPerformed

    private void subjectSortByUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectSortByUnitActionPerformed
        // TODO add your handling code here:
        applyFiltersAndSearch();
    }//GEN-LAST:event_subjectSortByUnitActionPerformed

    private void deletedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletedBtnActionPerformed
        String subjectCode = subjectCodeViewOnly.getText().trim();
        if (subjectCode.isEmpty() || subjectCode.equals("Exm; GEN-001")) {
            JOptionPane.showMessageDialog(this, "Please select a subject to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete subject " + subjectCode + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM subjects WHERE subject_code = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, subjectCode);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Subject deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAdminSubjectTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Subject not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deletedBtnActionPerformed

    private void editSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSubjectActionPerformed
        if (subjectCodeViewOnly.getText().trim().isEmpty() || subjectCodeViewOnly.getText().equals("Exm; GEN-001")) {
            JOptionPane.showMessageDialog(this, "Please select a subject to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        subjectNameEdit.setEditable(true);
        unitViewOnly.setEditable(true);
        subjectReassignTeacher.setEnabled(true);
        JOptionPane.showMessageDialog(this, "Edit mode enabled for Subject Name, Units, and Assigned Teacher.", "Information", JOptionPane.INFORMATION_MESSAGE);
        
    }//GEN-LAST:event_editSubjectActionPerformed

    private void saveEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveEditActionPerformed
        // TODO add your handling code here:
        String code = subjectCodeViewOnly.getText().trim();
        String name = subjectNameEdit.getText().trim();
        String unitStr = unitViewOnly.getText().trim();
        String teacherName = (String) subjectReassignTeacher.getSelectedItem();

        if (code.isEmpty() || name.isEmpty() || unitStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int units = Integer.parseInt(unitStr);
            String teacherId = null;
            if (teacherName != null && !teacherName.equals("Unassigned")) {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("SELECT teacher_id FROM teachers WHERE full_name = ?")) {
                    stmt.setString(1, teacherName);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        teacherId = rs.getString("teacher_id");
                    }
                }
            }

            String sql = "UPDATE subjects SET subject_title = ?, units = ?, assigned_teacher_id = ? WHERE subject_code = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setInt(2, units);
                stmt.setString(3, teacherId);
                stmt.setString(4, code);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    if (teacherId != null) {
                        try (PreparedStatement tStmt = conn.prepareStatement("UPDATE teachers SET status = 'Assigned' WHERE teacher_id = ?")) {
                            tStmt.setString(1, teacherId);
                            tStmt.executeUpdate();
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Subject updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAdminSubjectTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Units must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveEditActionPerformed

    private void unitViewOnlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitViewOnlyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitViewOnlyActionPerformed

    private void subjectCodeViewOnlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectCodeViewOnlyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subjectCodeViewOnlyActionPerformed

    private void subjectNameEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectNameEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subjectNameEditActionPerformed

    private void subjectSortBySchoolYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectSortBySchoolYearActionPerformed
        // TODO add your handling code here:
        applyFiltersAndSearch();
    }//GEN-LAST:event_subjectSortBySchoolYearActionPerformed

    private void subjectSortBySemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectSortBySemesterActionPerformed
        // TODO add your handling code h
        applyFiltersAndSearch();
    }//GEN-LAST:event_subjectSortBySemesterActionPerformed

    private void subjectSortByCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectSortByCourseActionPerformed
        // TODO add your handling code here:
        applyFiltersAndSearch();
    }//GEN-LAST:event_subjectSortByCourseActionPerformed

    private void formSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formSubmitActionPerformed
       JTextField subNameField = new JTextField();
        JComboBox<String> yearBox = new JComboBox<>(new String[]{"1st year", "2nd year", "3rd year", "4th year"});
        JComboBox<String> semesterBox = new JComboBox<>(new String[]{"1st Semester", "2nd Semester"});
        JComboBox<String> unitBox = new JComboBox<>(new String[]{"2", "3", "4"});

        List<String> courseList = new ArrayList<>();
        List<String> courseIdList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT course_id, course_name FROM courses")) {
            while (rs.next()) {
                courseIdList.add(rs.getString("course_id"));
                courseList.add(rs.getString("course_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JComboBox<String> courseBox = new JComboBox<>(courseList.toArray(new String[0]));

        JComboBox<String> teacherBox = new JComboBox<>();
        
        java.util.function.Consumer<String> updateTeachersForCourse = (selectedCourse) -> {
            teacherBox.removeAllItems();
            teacherBox.addItem("Unassigned");
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT teacher_id, full_name FROM teachers WHERE LOWER(department) = LOWER(?) OR department IS NULL OR department = ''")) {
                stmt.setString(1, selectedCourse);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    teacherBox.addItem(rs.getString("full_name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };

        if (!courseList.isEmpty()) {
            updateTeachersForCourse.accept(courseList.get(0));
        }

        courseBox.addActionListener(e -> {
            String selectedCourse = (String) courseBox.getSelectedItem();
            if (selectedCourse != null) {
                updateTeachersForCourse.accept(selectedCourse);
            }
        });

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Subject Name:"));
        panel.add(subNameField);
        panel.add(new JLabel("Course:"));
        panel.add(courseBox);
        panel.add(new JLabel("Year Level:"));
        panel.add(yearBox);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterBox);
        panel.add(new JLabel("Units:"));
        panel.add(unitBox);
        panel.add(new JLabel("Assigned Teacher (Preferred Course Match):"));
        panel.add(teacherBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Subject", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String subName = subNameField.getText().trim();
            if (subName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Subject Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String subCode = "SUB-" + (1000 + (int)(Math.random() * 9000));
            int units = Integer.parseInt((String) unitBox.getSelectedItem());
            String yearLevel = (String) yearBox.getSelectedItem();
            String semester = (String) semesterBox.getSelectedItem();

            String selectedCourseId = !courseIdList.isEmpty() ? courseIdList.get(courseBox.getSelectedIndex()) : null;
            
            String selectedTeacherName = (String) teacherBox.getSelectedItem();
            String selectedTeacherId = null;
            if (selectedTeacherName != null && !selectedTeacherName.equals("Unassigned")) {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("SELECT teacher_id FROM teachers WHERE full_name = ?")) {
                    stmt.setString(1, selectedTeacherName);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        selectedTeacherId = rs.getString("teacher_id");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            String sql = "INSERT INTO subjects (subject_code, subject_title, units, course_id, assigned_teacher_id, year_level, semester) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                         "ON DUPLICATE KEY UPDATE subject_title = VALUES(subject_title), units = VALUES(units), course_id = VALUES(course_id), assigned_teacher_id = VALUES(assigned_teacher_id), year_level = VALUES(year_level), semester = VALUES(semester)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, subCode);
                stmt.setString(2, subName);
                stmt.setInt(3, units);
                stmt.setString(4, selectedCourseId);
                stmt.setString(5, selectedTeacherId);
                stmt.setString(6, yearLevel);
                stmt.setString(7, semester);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    if (selectedTeacherId != null) {
                        try (PreparedStatement tStmt = conn.prepareStatement("UPDATE teachers SET status = 'Assigned' WHERE teacher_id = ?")) {
                            tStmt.setString(1, selectedTeacherId);
                            tStmt.executeUpdate();
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Subject added successfully with Code: " + subCode, "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAdminSubjectTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add subject.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_formSubmitActionPerformed

    private void subjectReassignTeacherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectReassignTeacherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subjectReassignTeacherActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminSubjectAccess.class.getName())
                .log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new AdminSubjectAccess().setVisible(true));
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton coursetBtn;
    private javax.swing.JButton deletedBtn;
    private javax.swing.JButton editSubject;
    private javax.swing.JButton enrollmentBtn;
    private javax.swing.JButton exitBtn;
    private javax.swing.JButton formSubmit;
    private javax.swing.JButton homeBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton saveEdit;
    private javax.swing.JButton studentBtn;
    private javax.swing.JButton subjectBtn;
    private javax.swing.JTextField subjectCodeViewOnly;
    private javax.swing.JTextField subjectNameEdit;
    private javax.swing.JComboBox<String> subjectReassignTeacher;
    private javax.swing.JComboBox<String> subjectSortByCourse;
    private javax.swing.JComboBox<String> subjectSortBySchoolYear;
    private javax.swing.JComboBox<String> subjectSortBySemester;
    private javax.swing.JComboBox<String> subjectSortByUnit;
    private javax.swing.JTable subjectTable;
    private javax.swing.JButton teacherBtn;
    private javax.swing.JTextField unitViewOnly;
    // End of variables declaration//GEN-END:variables

}
