package admin_gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import landingPageGui.LogInPage;
import model.Enrollment;
import service.EnrollmentService;
import gui.AdminHomePage;

public class AdminStudentAccess extends javax.swing.JFrame {

    private String username = "Admin";
    private final EnrollmentService enrollmentService = new EnrollmentService();

    public AdminStudentAccess() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        loadDynamicFilters();
        refreshTableData();
        setupTableSelectionListener();
        setFieldsEditable(false);
    }

    public AdminStudentAccess(String username) {
        this();
        this.username = username;
    }

    // Purpose: Populate dynamic filter dropdowns based on active database records
    private void loadDynamicFilters() {
        sortBySchoolYear.removeAllItems();
        sortBySchoolYear.addItem("All Year Level");
        List<String> years = enrollmentService.getActiveYearLevels();
        for (String y : years) {
            sortBySchoolYear.addItem(y);
        }

        sortByCourse.removeAllItems();
        sortByCourse.addItem("Courses");
        List<String> courses = enrollmentService.getActiveCourses();
        for (String c : courses) {
            sortByCourse.addItem(c);
        }

        updateClassSectionComboBox();
    }

    // Purpose: Dynamically update class section choices based on current course selection
    private void updateClassSectionComboBox() {
        sortByClassSection.removeAllItems();
        sortByClassSection.addItem("All Class Section");
        List<String> sections = enrollmentService.getActiveClassSections();
        for (String s : sections) {
            sortByClassSection.addItem(s);
        }
    }

    // Purpose: Load and filter students table data based on combo box selections and search input
    private void refreshTableData() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Student-ID", "Name", "Gender", "Course", "Year Level", "Class Section"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        String selectedYear = (String) sortBySchoolYear.getSelectedItem();
        String selectedCourse = (String) sortByCourse.getSelectedItem();
        String selectedSection = (String) sortByClassSection.getSelectedItem();
        String selectedGender = (String) sortByGender.getSelectedItem();

        List<Enrollment> students = enrollmentService.getApprovedStudents();
        for (Enrollment enr : students) {
            boolean matchesYear = (selectedYear == null || selectedYear.equals("All Year Level") || selectedYear.equalsIgnoreCase(enr.getGradeLevel()));
            boolean matchesCourse = (selectedCourse == null || selectedCourse.equals("Courses") || selectedCourse.equalsIgnoreCase(enr.getCourseName()));
            boolean matchesSection = (selectedSection == null || selectedSection.equals("All Class Section") || selectedSection.equalsIgnoreCase(enr.getClassSection()));
            boolean matchesGender = (selectedGender == null || selectedGender.equals("All Gender") || selectedGender.equalsIgnoreCase(enr.getGender()));

            if (matchesYear && matchesCourse && matchesSection && matchesGender) {
                model.addRow(new Object[] {
                    enr.getStudentId(),
                    enr.getStudentName(),
                    enr.getGender() != null ? enr.getGender() : "N/A",
                    enr.getCourseName(),
                    enr.getGradeLevel(),
                    enr.getClassSection()
                });
            }
        }
        studentsTbl.setModel(model);
    }

    // Purpose: Set text field editability status for details section
    private void setFieldsEditable(boolean editable) {
        studentNameEdit.setEditable(editable);
        studentIdViewOnly.setEditable(false); // ID remains non-editable for integrity
        studentGenderViewOnly.setEditable(false);
        eMailAddViewOnly.setEditable(editable);
    }

    // Purpose: Handle table row selection to populate student details
    private void setupTableSelectionListener() {
        studentsTbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentsTbl.getSelectedRow();
                if (selectedRow != -1) {
                    String studentId = (String) studentsTbl.getValueAt(selectedRow, 0);
                    Enrollment enr = enrollmentService.getEnrollmentByStudent(studentId);
                    if (enr != null) {
                        studentNameEdit.setText(enr.getStudentName());
                        studentIdViewOnly.setText(enr.getStudentId());
                        studentGenderViewOnly.setText(enr.getGender() != null ? enr.getGender() : "N/A");
                        eMailAddViewOnly.setText(enr.getEmail());
                        setFieldsEditable(false);
                    }
                }
            }
        });
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
        coursetBtn = new javax.swing.JButton();
        subjectBtn = new javax.swing.JButton();
        teacherBtn = new javax.swing.JButton();
        enrollmentBtn = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        studentsTbl = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        sortBySchoolYear = new javax.swing.JComboBox<>();
        sortByCourse = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        sortByClassSection = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        studentNameEdit = new javax.swing.JTextField();
        studentIdViewOnly = new javax.swing.JTextField();
        eMailAddViewOnly = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        studentGenderViewOnly = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        sortByGender = new javax.swing.JComboBox<>();
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
        studentBtn.setForeground(new java.awt.Color(51, 255, 51));
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
                .addContainerGap(180, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(102, 102, 102));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("STUDENTS");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Year level:");

        studentsTbl.setAutoCreateRowSorter(true);
        studentsTbl.setBackground(new java.awt.Color(153, 153, 153));
        studentsTbl.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        studentsTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Student-ID", "Name", "Gender", "Course", "Year Level", "Class Section"
            }
        ));
        jScrollPane5.setViewportView(studentsTbl);

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Course:");

        sortBySchoolYear.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sortBySchoolYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Year Level", "1st year", "2nd year", "3rd year", "4th year" }));
        sortBySchoolYear.addActionListener(this::sortBySchoolYearActionPerformed);

        sortByCourse.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sortByCourse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Courses" }));
        sortByCourse.addActionListener(this::sortByCourseActionPerformed);

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Class Section:");

        sortByClassSection.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sortByClassSection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Class Section", "" }));
        sortByClassSection.addActionListener(this::sortByClassSectionActionPerformed);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Student Name:");

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
        jLabel11.setText("ID:");

        studentNameEdit.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        studentNameEdit.setText("name");

        studentIdViewOnly.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        studentIdViewOnly.setText("id");
        studentIdViewOnly.addActionListener(this::studentIdViewOnlyActionPerformed);

        eMailAddViewOnly.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        eMailAddViewOnly.setText("email");
        eMailAddViewOnly.addActionListener(this::eMailAddViewOnlyActionPerformed);

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Email Address:");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 255, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("SORT CONTENT");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 255, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("DETAILS");

        studentGenderViewOnly.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        studentGenderViewOnly.setText("gender");
        studentGenderViewOnly.addActionListener(this::studentGenderViewOnlyActionPerformed);

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Gender:");

        sortByGender.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sortByGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Gender", "Male", "Female", " " }));
        sortByGender.addActionListener(this::sortByGenderActionPerformed);

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Class Section:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jScrollPane5)
                        .addContainerGap())
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sortByGender, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(sortByClassSection, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(sortByCourse, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(sortBySchoolYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(55, 55, 55)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(studentNameEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                                            .addComponent(studentIdViewOnly)
                                            .addComponent(eMailAddViewOnly)
                                            .addComponent(studentGenderViewOnly))))))
                        .addGap(136, 136, 136))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(studentNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(studentIdViewOnly, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(studentGenderViewOnly, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(eMailAddViewOnly, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(sortBySchoolYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(sortByCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(sortByClassSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(sortByGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17)))))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
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
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new LogInPage().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_exitBtn1ActionPerformed

    private void sortByClassSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByClassSectionActionPerformed
        // TODO add your handling code here:
        refreshTableData();
    }//GEN-LAST:event_sortByClassSectionActionPerformed

    private void sortByCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByCourseActionPerformed
        // TODO add your handling code here:
        refreshTableData();
    }//GEN-LAST:event_sortByCourseActionPerformed

    private void sortBySchoolYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortBySchoolYearActionPerformed
        // TODO add your handling code here:
        refreshTableData();
    }//GEN-LAST:event_sortBySchoolYearActionPerformed

    private void eMailAddViewOnlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eMailAddViewOnlyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eMailAddViewOnlyActionPerformed

    private void studentIdViewOnlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentIdViewOnlyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studentIdViewOnlyActionPerformed

    private void studentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentBtnActionPerformed
        // TODO add your handling code here:
        new AdminStudentAccess(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_studentBtnActionPerformed

    private void coursetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coursetBtnActionPerformed
        // TODO add your handling code here:
        new AdminCourseAccess(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_coursetBtnActionPerformed

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

    private void studentGenderViewOnlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentGenderViewOnlyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studentGenderViewOnlyActionPerformed

    private void sortByGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByGenderActionPerformed
        // TODO add your handling code here:
        refreshTableData();
    }//GEN-LAST:event_sortByGenderActionPerformed

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
            java.util.logging.Logger.getLogger(AdminStudentAccess.class.getName())
                .log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AdminStudentAccess().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton coursetBtn;
    private javax.swing.JTextField eMailAddViewOnly;
    private javax.swing.JButton enrollmentBtn;
    private javax.swing.JButton exitBtn1;
    private javax.swing.JButton homeBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JComboBox<String> sortByClassSection;
    private javax.swing.JComboBox<String> sortByCourse;
    private javax.swing.JComboBox<String> sortByGender;
    private javax.swing.JComboBox<String> sortBySchoolYear;
    private javax.swing.JButton studentBtn;
    private javax.swing.JTextField studentGenderViewOnly;
    private javax.swing.JTextField studentIdViewOnly;
    private javax.swing.JTextField studentNameEdit;
    private javax.swing.JTable studentsTbl;
    private javax.swing.JButton subjectBtn;
    private javax.swing.JButton teacherBtn;
    // End of variables declaration//GEN-END:variables

}
