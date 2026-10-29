package admin_gui;

import admin_model.TeacherAssignment;
import gui.AdminHomePage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import landingPageGui.LogInPage;
import service.CourseService;
import service.TeacherService;

public class AdminTeacherAccess extends javax.swing.JFrame {

    private final TeacherService teacherService = TeacherService.getInstance();
    private final CourseService courseService = new CourseService();
    
    private String currentAdminUsername = "Admin";
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private boolean isEditModeEnabled = false;
    private String selectedTeacherId = null;

    public AdminTeacherAccess() {
        this("Admin1");
    }

    // Safe constructor with exception handling to prevent navigation crashes
    public AdminTeacherAccess(String adminUsername) {
        this.currentAdminUsername = adminUsername;
        initComponents();
        setupCustomComponents();
        
        try {
            loadCoursesIntoDropdowns(); // Safe database query
            loadTeacherTableData();   // Safe database query
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error loading teacher data from database: " + e.getMessage(),
                "Database Loading Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        setFieldsEnabled(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    // Purpose: Populate course dropdown filters from the database[cite: 54]
    private void loadCoursesIntoDropdowns() {
        String[] coursesFromAdmin = courseService.getCourseNamesArray();

        DefaultComboBoxModel<String> filterCourseModel = new DefaultComboBoxModel<>();
        filterCourseModel.addElement("All Courses");
        for (String c : coursesFromAdmin) {
            filterCourseModel.addElement(c);
        }
        sortByteacherAssignedCourse.setModel(filterCourseModel);
    }

    // Purpose: Initialize custom table column models, table row selection listeners, and search filters[cite: 54]
    private void setupCustomComponents() {
        String[] columnNames = {"Teacher's ID", "Name", "Course", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        TeacherAssignmentTbl.setModel(tableModel);

        rowSorter = new TableRowSorter<>(tableModel);
        TeacherAssignmentTbl.setRowSorter(rowSorter);

        teacherId.setEditable(false);
        sortByStatus.setModel(new DefaultComboBoxModel<>(new String[]{"All Status", "Assigned", "Un-assigned"}));

        TeacherAssignmentTbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = TeacherAssignmentTbl.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = TeacherAssignmentTbl.convertRowIndexToModel(selectedRow);
                    selectedTeacherId = (String) tableModel.getValueAt(modelRow, 0);
                    
                    teacherId.setText(selectedTeacherId);
                    teacherName.setText((String) tableModel.getValueAt(modelRow, 1));
                    
                    // Retrieve full email record from database service[cite: 54]
                    TeacherAssignment ta = teacherService.getAssignmentById(selectedTeacherId);
                    if (ta != null) {
                        teacherEmailAdd.setText(ta.getEmail());
                    }
                    
                    isEditModeEnabled = false;
                    setFieldsEnabled(false);
                }
            }
        });
    }

    // Purpose: Enable or disable detail form fields based on editing state[cite: 54]
    private void setFieldsEnabled(boolean enable) {
        teacherName.setEditable(enable);
        teacherEmailAdd.setEditable(enable);
    }

    // Purpose: Reset the form and clear current selection[cite: 54]
    private void clearForm() {
        selectedTeacherId = null;
        teacherId.setText("id");
        teacherName.setText("name");
        teacherEmailAdd.setText("email");
        TeacherAssignmentTbl.clearSelection();
    }

    // Purpose: Load teacher data from MySQL database and display assigned vs un-assigned status[cite: 54]
   // Purpose: Load teacher data from MySQL database and display assigned vs un-assigned status based on actual subject assignments[cite: 33]
    private void loadTeacherTableData() {
        tableModel.setRowCount(0);
        List<TeacherAssignment> list = teacherService.getAllAssignments();
        
        for (TeacherAssignment ta : list) {
            boolean isAssigned = false;
            
            // Check database directly to see if this teacher ID is assigned to any subject
            String checkSql = "SELECT COUNT(*) FROM subjects WHERE assigned_teacher_id = ?";
            try (Connection conn = service.DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setString(1, ta.getId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    isAssigned = rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Fallback to object property check if needed
            if (!isAssigned) {
                isAssigned = (ta.getSubject() != null 
                        && !ta.getSubject().trim().isEmpty() 
                        && !"None".equalsIgnoreCase(ta.getSubject().trim())
                        && !"N/A".equalsIgnoreCase(ta.getSubject().trim()));
            }

            String statusDisplay = isAssigned ? "Assigned" : "Un-assigned";

            tableModel.addRow(new Object[]{
                    ta.getId(),
                    ta.getTeacherName(),
                    ta.getCourse(),
                    statusDisplay
            });
        }
    }

    // Purpose: Apply search query and dropdown filters to the teacher table[cite: 54]
    private void applyFilters() {
        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();

        String course = (String) sortByteacherAssignedCourse.getSelectedItem();
        if (course != null && !course.equalsIgnoreCase("All Courses")) {
            filters.add(RowFilter.regexFilter("(?i)" + course, 2));
        }

        String status = (String) sortByStatus.getSelectedItem();
        if (status != null && !status.equalsIgnoreCase("All Status")) {
            filters.add(RowFilter.regexFilter("(?i)" + status, 3));
        }

        if (filters.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        exitBtn1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        studentBtn = new javax.swing.JButton();
        courseBtn = new javax.swing.JButton();
        subjectBtn = new javax.swing.JButton();
        teacherBtn = new javax.swing.JButton();
        enrollmentBtn = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TeacherAssignmentTbl = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        sortByteacherAssignedCourse = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        teacherName = new javax.swing.JTextField();
        teacherId = new javax.swing.JTextField();
        teacherEmailAdd = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        editTeacherDetails = new javax.swing.JButton();
        deletedTeacher = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        sortByStatus = new javax.swing.JComboBox<>();
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

        exitBtn1.setBackground(new java.awt.Color(51, 51, 51));
        exitBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        exitBtn1.setForeground(new java.awt.Color(255, 204, 204));
        exitBtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/logout_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        exitBtn1.setText("Exit");
        exitBtn1.setBorderPainted(false);
        exitBtn1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        exitBtn1.addActionListener(this::exitBtn1ActionPerformed);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 587, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitBtn1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        courseBtn.setBackground(new java.awt.Color(51, 51, 51));
        courseBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        courseBtn.setForeground(new java.awt.Color(204, 255, 255));
        courseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/school_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        courseBtn.setText("Courses");
        courseBtn.setBorderPainted(false);
        courseBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        courseBtn.addActionListener(this::courseBtnActionPerformed);

        subjectBtn.setBackground(new java.awt.Color(51, 51, 51));
        subjectBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        subjectBtn.setForeground(new java.awt.Color(204, 255, 255));
        subjectBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/topic_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        subjectBtn.setText("Subjects");
        subjectBtn.setBorderPainted(false);
        subjectBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        subjectBtn.addActionListener(this::subjectBtnActionPerformed);

        teacherBtn.setBackground(new java.awt.Color(51, 51, 51));
        teacherBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        teacherBtn.setForeground(new java.awt.Color(51, 255, 51));
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
                    .addComponent(courseBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(courseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(studentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(203, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(102, 102, 102));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("TEACHER");

        TeacherAssignmentTbl.setAutoCreateRowSorter(true);
        TeacherAssignmentTbl.setBackground(new java.awt.Color(153, 153, 153));
        TeacherAssignmentTbl.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        TeacherAssignmentTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Teacher's ID", "Name", "Course", "Status"
            }
        ));
        jScrollPane5.setViewportView(TeacherAssignmentTbl);

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Course:");

        sortByteacherAssignedCourse.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sortByteacherAssignedCourse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Courses" }));
        sortByteacherAssignedCourse.addActionListener(this::sortByteacherAssignedCourseActionPerformed);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Teacher's Name:");

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
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("ID:");

        teacherName.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        teacherName.setText("name");
        teacherName.addActionListener(this::teacherNameActionPerformed);

        teacherId.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        teacherId.setText("id");
        teacherId.addActionListener(this::teacherIdActionPerformed);

        teacherEmailAdd.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        teacherEmailAdd.setText("email");
        teacherEmailAdd.addActionListener(this::teacherEmailAddActionPerformed);

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Email Address:");

        editTeacherDetails.setBackground(new java.awt.Color(51, 51, 51));
        editTeacherDetails.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editTeacherDetails.setForeground(new java.awt.Color(255, 153, 0));
        editTeacherDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        editTeacherDetails.setText("Edit");
        editTeacherDetails.setBorderPainted(false);
        editTeacherDetails.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editTeacherDetails.addActionListener(this::editTeacherDetailsActionPerformed);

        deletedTeacher.setBackground(new java.awt.Color(51, 51, 51));
        deletedTeacher.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deletedTeacher.setForeground(new java.awt.Color(255, 51, 51));
        deletedTeacher.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        deletedTeacher.setText("Delete");
        deletedTeacher.setBorderPainted(false);
        deletedTeacher.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        deletedTeacher.addActionListener(this::deletedTeacherActionPerformed);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 255, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("SORT CONTENT");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 255, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("DETAILS");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Status:");

        sortByStatus.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sortByStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Status", "Assigned", "Un-assigned" }));
        sortByStatus.addActionListener(this::sortByStatusActionPerformed);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(sortByStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(sortByteacherAssignedCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(42, 42, 42)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                            .addGap(117, 117, 117)
                                            .addComponent(editTeacherDetails)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                            .addComponent(deletedTeacher))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(teacherName, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                                .addComponent(teacherId)
                                                .addComponent(teacherEmailAdd))))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(641, 641, 641)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(sortByteacherAssignedCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(sortByStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(teacherName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(teacherId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(teacherEmailAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(editTeacherDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(deletedTeacher, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
                .addComponent(homeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        // TODO add your handling code here:
        new AdminHomePage(currentAdminUsername).setVisible(true);
    this.dispose();
    }//GEN-LAST:event_homeBtnActionPerformed

    private void exitBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtn1ActionPerformed
        // TODO add your handling code here:
      int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new LogInPage().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_exitBtn1ActionPerformed

    private void studentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentBtnActionPerformed
        // TODO add your handling code here:
        new AdminStudentAccess(currentAdminUsername).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_studentBtnActionPerformed

    private void courseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseBtnActionPerformed
        // TODO add your handling code here:
        new AdminCourseAccess(currentAdminUsername).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_courseBtnActionPerformed

    private void subjectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectBtnActionPerformed
        // TODO add your handling code here:
        new AdminSubjectAccess(currentAdminUsername).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_subjectBtnActionPerformed

    private void teacherBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherBtnActionPerformed
        // TODO add your handling code here
        loadTeacherTableData();
    }//GEN-LAST:event_teacherBtnActionPerformed

    private void enrollmentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enrollmentBtnActionPerformed
        new AdminEnrollmentAccess(currentAdminUsername).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_enrollmentBtnActionPerformed

    private void sortByStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByStatusActionPerformed
        // TODO add your handling code here:
        applyFilters();
    }//GEN-LAST:event_sortByStatusActionPerformed

    private void deletedTeacherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletedTeacherActionPerformed
        // TODO add your handling code here:
        if (selectedTeacherId == null) {
            JOptionPane.showMessageDialog(this, "Please select a teacher from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete teacher ID " + selectedTeacherId + "? Associated subjects will become unassigned.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Delete teacher and unassign associated subjects in database
            boolean success = teacherService.deleteAssignment(selectedTeacherId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Teacher deleted successfully and subjects unassigned!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTeacherTableData();
                clearForm();
                isEditModeEnabled = false;
                setFieldsEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete teacher.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deletedTeacherActionPerformed

    private void editTeacherDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editTeacherDetailsActionPerformed
        // TODO add your handling code here:
       if (selectedTeacherId == null) {
            JOptionPane.showMessageDialog(this, "Please select a teacher from the table to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isEditModeEnabled) {
            isEditModeEnabled = true;
            setFieldsEnabled(true);
            JOptionPane.showMessageDialog(this, "Editing mode enabled. Update name and email, then click 'Edit' again to save changes.", "Edit Mode", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String name = teacherName.getText().trim();
            String email = teacherEmailAdd.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Teacher Name and Email cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TeacherAssignment existing = teacherService.getAssignmentById(selectedTeacherId);
            String course = existing != null ? existing.getCourse() : "Unassigned";

            boolean updated = teacherService.updateAssignment(selectedTeacherId, name, email, course);
            if (updated) {
                JOptionPane.showMessageDialog(this, "Teacher details updated successfully in database!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTeacherTableData();
                clearForm();
                isEditModeEnabled = false;
                setFieldsEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update teacher details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_editTeacherDetailsActionPerformed

    private void teacherEmailAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherEmailAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherEmailAddActionPerformed

    private void teacherIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherIdActionPerformed

    private void sortByteacherAssignedCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByteacherAssignedCourseActionPerformed
        // TODO add your handling code here:
        applyFilters();
    }//GEN-LAST:event_sortByteacherAssignedCourseActionPerformed

    private void teacherNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherNameActionPerformed

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
            java.util.logging.Logger.getLogger(AdminTeacherAccess.class.getName())
                .log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AdminTeacherAccess().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TeacherAssignmentTbl;
    private javax.swing.JButton courseBtn;
    private javax.swing.JButton deletedTeacher;
    private javax.swing.JButton editTeacherDetails;
    private javax.swing.JButton enrollmentBtn;
    private javax.swing.JButton exitBtn1;
    private javax.swing.JButton homeBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JComboBox<String> sortByStatus;
    private javax.swing.JComboBox<String> sortByteacherAssignedCourse;
    private javax.swing.JButton studentBtn;
    private javax.swing.JButton subjectBtn;
    private javax.swing.JButton teacherBtn;
    private javax.swing.JTextField teacherEmailAdd;
    private javax.swing.JTextField teacherId;
    private javax.swing.JTextField teacherName;
    // End of variables declaration//GEN-END:variables

}
