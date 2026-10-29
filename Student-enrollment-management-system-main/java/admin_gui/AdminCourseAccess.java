package admin_gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import landingPageGui.LogInPage;
import model.Course;
import service.CourseService;
import gui.AdminHomePage;

public class AdminCourseAccess extends javax.swing.JFrame {

    private String username = "Admin";
    private final CourseService courseService = new CourseService();

    public AdminCourseAccess() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        loadCourseTableData();
        setupTableSelectionListener();
        
        // Auto-display the upcoming generated course ID
        addCourseID.setText(courseService.generateNextCourseId());
        addCourseID.setEditable(false);
    }

    public AdminCourseAccess(String username) {
        this();
        this.username = username;
    }

    // Loads and displays all courses from the database into coursesTbl[cite: 33]
private void loadCourseTableData() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Course ID", "Course", "Unit"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        List<Course> courses = courseService.getAllCourses();
        for (Course c : courses) {
            model.addRow(new Object[] {
                c.getCourseId(),
                c.getCourseName(),
                c.getUnits()
            });
        }

        coursesTbl.setModel(model);
        clearDetails();
        
        // Refresh the next ID preview
        if (addCourseID != null) {
            addCourseID.setText(courseService.generateNextCourseId());
            addCourseID.setEditable(false);
        }
    }

    // Clears the detail section view labels[cite: 33]
    private void clearDetails() {
        courseIdView.setText("Course ID");
        courseNameView.setText("Course");
        courseUnit.setText("Unit");
    }

    // Populates detail section when a row is clicked on coursesTbl[cite: 33]
    private void setupTableSelectionListener() {
        coursesTbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = coursesTbl.getSelectedRow();
                if (selectedRow != -1) {
                    courseIdView.setText((String) coursesTbl.getValueAt(selectedRow, 0));
                    courseNameView.setText((String) coursesTbl.getValueAt(selectedRow, 1));
                    courseUnit.setText((String) coursesTbl.getValueAt(selectedRow, 2));
                }
            }
        });
    }

    // Filters table rows based on search input[cite: 33]
    private void applySearchFilter() {

        DefaultTableModel model = (DefaultTableModel) coursesTbl.getModel();
        model.setRowCount(0);

        List<Course> courses = courseService.getAllCourses();
    }

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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        coursesTbl = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        addCourseName = new javax.swing.JTextField();
        addCourseID = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        addCourse = new javax.swing.JButton();
        editCourseDetails = new javax.swing.JButton();
        deletedCourseOnCoursetList = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        courseIdView = new javax.swing.JLabel();
        courseNameView = new javax.swing.JLabel();
        courseUnit = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitBtn1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exitBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)))
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
        courseBtn.setForeground(new java.awt.Color(51, 255, 51));
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(102, 102, 102));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("STUDENTS");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Course Name:");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Course ID:");

        coursesTbl.setAutoCreateRowSorter(true);
        coursesTbl.setBackground(new java.awt.Color(153, 153, 153));
        coursesTbl.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        coursesTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"none", "none", "none"},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Course ID", "Course", "Unit"
            }
        ));
        jScrollPane5.setViewportView(coursesTbl);

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Unit:");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Course Name:");

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
            .addGap(0, 180, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Course ID:");

        addCourseName.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        addCourseName.setText("name");

        addCourseID.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        addCourseID.setText("id");
        addCourseID.addActionListener(this::addCourseIDActionPerformed);

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Course Unit:");

        addCourse.setBackground(new java.awt.Color(51, 51, 51));
        addCourse.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addCourse.setForeground(new java.awt.Color(51, 255, 51));
        addCourse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/add_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        addCourse.setText("Add");
        addCourse.setBorderPainted(false);
        addCourse.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        addCourse.addActionListener(this::addCourseActionPerformed);

        editCourseDetails.setBackground(new java.awt.Color(51, 51, 51));
        editCourseDetails.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editCourseDetails.setForeground(new java.awt.Color(255, 153, 0));
        editCourseDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/edit_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        editCourseDetails.setText("Edit");
        editCourseDetails.setBorderPainted(false);
        editCourseDetails.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editCourseDetails.addActionListener(this::editCourseDetailsActionPerformed);

        deletedCourseOnCoursetList.setBackground(new java.awt.Color(51, 51, 51));
        deletedCourseOnCoursetList.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deletedCourseOnCoursetList.setForeground(new java.awt.Color(255, 51, 51));
        deletedCourseOnCoursetList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        deletedCourseOnCoursetList.setText("Delete");
        deletedCourseOnCoursetList.setBorderPainted(false);
        deletedCourseOnCoursetList.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        deletedCourseOnCoursetList.addActionListener(this::deletedCourseOnCoursetListActionPerformed);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 255, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("COURSE DETAILS");

        courseIdView.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        courseIdView.setForeground(new java.awt.Color(255, 255, 255));
        courseIdView.setText("Course ID");

        courseNameView.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        courseNameView.setForeground(new java.awt.Color(255, 255, 255));
        courseNameView.setText("Course");

        courseUnit.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        courseUnit.setForeground(new java.awt.Color(255, 255, 255));
        courseUnit.setText("Unit");

        jComboBox2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "18 unit", "24 unit", "30 unit" }));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 255, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("MANAGE COURSE");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(courseIdView, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(courseUnit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(courseNameView, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel9))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(addCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(addCourseID, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addComponent(editCourseDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(addCourse)))))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deletedCourseOnCoursetList)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(deletedCourseOnCoursetList, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(courseIdView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addComponent(courseNameView)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(courseUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel13))))
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(addCourseID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(editCourseDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)))
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)))
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
        new AdminHomePage(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_homeBtnActionPerformed

    private void exitBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtn1ActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to log out?", 
            "Logout Confirmation", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (choice == JOptionPane.YES_OPTION) {
           new LogInPage().setVisible(true);
           this.dispose();
        }
    }//GEN-LAST:event_exitBtn1ActionPerformed

    private void addCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseActionPerformed
       String courseName = addCourseName.getText().trim();
        String units = (String) jComboBox2.getSelectedItem();

        // Validate course name input
        if (courseName.isEmpty() || courseName.equals("name")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Course Name!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate unit selection
        if (units == null || units.equalsIgnoreCase("none")) {
            JOptionPane.showMessageDialog(this, "Please select a valid Course Unit!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Attempt to add course via service (which auto-generates CRS-001, CRS-002, etc.)
        boolean success = courseService.addCourse(courseName, units);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Course added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadCourseTableData(); // Refresh the table view immediately
            
            // Reset fields
            addCourseName.setText("name");
            jComboBox2.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add course. Please check database connection or constraints.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addCourseActionPerformed

    private void deletedCourseOnCoursetListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletedCourseOnCoursetListActionPerformed
      String courseId = courseIdView.getText().trim();
        if (courseId.isEmpty() || courseId.equals("Course ID")) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete course " + courseId + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = courseService.deleteCourse(courseId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Course deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCourseTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete course.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deletedCourseOnCoursetListActionPerformed
    
    private void addCourseIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addCourseIDActionPerformed

    private void studentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentBtnActionPerformed
        // TODO add your handling code here:
        new AdminStudentAccess(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_studentBtnActionPerformed

    private void courseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseBtnActionPerformed
        // TODO add your handling code here:
        loadCourseTableData();
    }//GEN-LAST:event_courseBtnActionPerformed

    private void subjectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectBtnActionPerformed
        // TODO add your handling code here:
        new AdminSubjectAccess(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_subjectBtnActionPerformed

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

    private void enrollmentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enrollmentBtnActionPerformed
        new AdminEnrollmentAccess(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_enrollmentBtnActionPerformed

    private void editCourseDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCourseDetailsActionPerformed
        // TODO add your handling code here:
        String courseId = courseIdView.getText().trim();
        if (courseId.isEmpty() || courseId.equals("Course ID")) {
            JOptionPane.showMessageDialog(this, "Please select a course from the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField nameField = new JTextField(courseNameView.getText());
        JComboBox<String> unitBox = new JComboBox<>(new String[]{"18 unit", "24 unit", "30 unit"});
        unitBox.setSelectedItem(courseUnit.getText());

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Edit Course Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Edit Course Unit:"));
        panel.add(unitBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Course Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newUnits = (String) unitBox.getSelectedItem();

            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Course Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = courseService.updateCourse(courseId, newName, newUnits);
            if (success) {
                JOptionPane.showMessageDialog(this, "Course updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCourseTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update course.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_editCourseDetailsActionPerformed

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
            java.util.logging.Logger.getLogger(AdminCourseAccess.class.getName())
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
        java.awt.EventQueue.invokeLater(() -> new AdminCourseAccess().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCourse;
    private javax.swing.JTextField addCourseID;
    private javax.swing.JTextField addCourseName;
    private javax.swing.JButton courseBtn;
    private javax.swing.JLabel courseIdView;
    private javax.swing.JLabel courseNameView;
    private javax.swing.JLabel courseUnit;
    private javax.swing.JTable coursesTbl;
    private javax.swing.JButton deletedCourseOnCoursetList;
    private javax.swing.JButton editCourseDetails;
    private javax.swing.JButton enrollmentBtn;
    private javax.swing.JButton exitBtn1;
    private javax.swing.JButton homeBtn;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JButton studentBtn;
    private javax.swing.JButton subjectBtn;
    private javax.swing.JButton teacherBtn;
    // End of variables declaration//GEN-END:variables

}
