package teacher_gui;

import gui.TeacherHomePage;
import model.Subject;
import service.SubjectService;
import landingPageGui.LogInPage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;

public class TeacherSubjectAccess extends javax.swing.JFrame {

    private String currentTeacherUsername = "Teacher";
    private DefaultTableModel assignedSubjectsModel;
    private DefaultTableModel enrolledStudentsModel;
    private TableRowSorter<DefaultTableModel> subjectRowSorter;

    public TeacherSubjectAccess() {
        this("Teacher1");
    }

    public TeacherSubjectAccess(String username) {
        this.currentTeacherUsername = username;
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        initTeacherCustomLogic();
    }

    // Purpose: Initialize custom logic, table models, dynamic filters, and selection listeners for teachers
    private void initTeacherCustomLogic() {
        assignedSubjectsModel = (DefaultTableModel) subjectAssignedToTeacherTbl.getModel();
        enrolledStudentsModel = (DefaultTableModel) studentEnrolledInThisSubjectTbl.getModel();
        
        subjectRowSorter = new TableRowSorter<>(assignedSubjectsModel);
        subjectAssignedToTeacherTbl.setRowSorter(subjectRowSorter);

        // Row Selection Listener: Display students matching the selected subject's criteria (Course, Year Level, Semester)
        subjectAssignedToTeacherTbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && subjectAssignedToTeacherTbl.getSelectedRow() != -1) {
                int selectedRow = subjectAssignedToTeacherTbl.getSelectedRow();
                int modelRow = subjectAssignedToTeacherTbl.convertRowIndexToModel(selectedRow);
                String subjectCode = (String) assignedSubjectsModel.getValueAt(modelRow, 0);
                
                // Fetch the full Subject object details to get Course, Year Level, and Semester criteria
                Subject selectedSubject = SubjectService.getSubjectByCode(subjectCode);
                if (selectedSubject != null) {
                    loadStudentsEnrolledInSubjectCriteria(selectedSubject);
                } else {
                    loadStudentsEnrolledInSubject(subjectCode); // Fallback
                }
            }
        });

        applyFiltersAndSearch();
    }

    // Purpose: Load subjects assigned to the logged-in teacher filtered by year level, semester, and search query
    private void applyFiltersAndSearch() {
        assignedSubjectsModel.setRowCount(0);
        enrolledStudentsModel.setRowCount(0); // Clear student table when filters/search update subjects

        String selectedYear = (String) subjectSortBySchoolYear.getSelectedItem();
        String selectedSemester = (String) subjectSortBySemester.getSelectedItem();
        String searchText = searchtxt.getText() != null ? searchtxt.getText().trim() : "";

        List<Subject> assignedSubjects = SubjectService.getSubjectsForTeacher(currentTeacherUsername, selectedYear, selectedSemester);

        if (assignedSubjects != null) {
            for (Subject s : assignedSubjects) {
                boolean matchesSemester = (selectedSemester == null || selectedSemester.equalsIgnoreCase(s.getSemester()));
                
                if (matchesSemester && (searchText.isEmpty() || 
                    s.getSubjectCode().toLowerCase().contains(searchText.toLowerCase()) || 
                    s.getSubjectTitle().toLowerCase().contains(searchText.toLowerCase()))) {
                    
                    assignedSubjectsModel.addRow(new Object[]{
                        s.getSubjectCode(),
                        s.getSubjectTitle(),
                        s.getYearLevel(),
                        s.getSemester() != null ? s.getSemester() : "1st Semester"
                    });
                }
            }
        }
    }

    // Purpose: Fetch and display exclusively students enrolled matching the subject's course, year level, and semester criteria
    private void loadStudentsEnrolledInSubjectCriteria(Subject subject) {
        enrolledStudentsModel.setRowCount(0);
        // Fetch approved students filtered by the subject's specific course, year level, etc.
        List<String[]> students = SubjectService.getStudentsEnrolledForSubjectCriteria(
            subject.getCourseName(), 
            subject.getYearLevel(), 
            subject.getSemester()
        );
        if (students != null) {
            for (String[] student : students) {
                enrolledStudentsModel.addRow(student);
            }
        }
    }

    // Purpose: Fallback fetch for students enrolled in the selected subject code[cite: 37]
    private void loadStudentsEnrolledInSubject(String subjectCode) {
        enrolledStudentsModel.setRowCount(0);
        List<String[]> students = SubjectService.getStudentsEnrolledInSubject(subjectCode);
        if (students != null) {
            for (String[] student : students) {
                enrolledStudentsModel.addRow(student);
            }
        }
    }

    // Purpose: Trigger filter refresh when table button is clicked
    private void loadAssignedSubjectsToTable() {
        applyFiltersAndSearch();
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
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        searchtxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        exitBtn1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        subjectBtn = new javax.swing.JButton();
        teacherBtn = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        subjectSortBySemester = new javax.swing.JComboBox<>();
        subjectSortBySchoolYear = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        studentEnrolledInThisSubjectTbl = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        subjectAssignedToTeacherTbl = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        homeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Search:");

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/search_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton1.setBorder(null);
        jButton1.addActionListener(this::jButton1ActionPerformed);

        searchtxt.setToolTipText("searching area");
        searchtxt.addActionListener(this::searchtxtActionPerformed);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 255, 204));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/warning_24dp_F19E39_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jLabel7.setText("Teacher's Access");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        exitBtn1.setBackground(new java.awt.Color(51, 51, 51));
        exitBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        exitBtn1.setForeground(new java.awt.Color(255, 204, 204));
        exitBtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/logout_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        exitBtn1.setText("LOG-OUT");
        exitBtn1.setBorderPainted(false);
        exitBtn1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        exitBtn1.addActionListener(this::exitBtn1ActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitBtn1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(exitBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(searchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));

        jPanel7.setBackground(new java.awt.Color(102, 102, 102));

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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subjectBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(teacherBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(teacherBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(subjectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(102, 102, 102));

        subjectSortBySemester.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectSortBySemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Semester", "2nd Semester" }));
        subjectSortBySemester.addActionListener(this::subjectSortBySemesterActionPerformed);

        subjectSortBySchoolYear.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectSortBySchoolYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st year", "2nd year", "3rd year", "4th year" }));
        subjectSortBySchoolYear.addActionListener(this::subjectSortBySchoolYearActionPerformed);

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

        jLabel9.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 255, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("SORT CONTENT");

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

        studentEnrolledInThisSubjectTbl.setAutoCreateRowSorter(true);
        studentEnrolledInThisSubjectTbl.setBackground(new java.awt.Color(153, 153, 153));
        studentEnrolledInThisSubjectTbl.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        studentEnrolledInThisSubjectTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Student ID", "Student Name", "Courses", "Year Level"
            }
        ));
        jScrollPane5.setViewportView(studentEnrolledInThisSubjectTbl);

        subjectAssignedToTeacherTbl.setAutoCreateRowSorter(true);
        subjectAssignedToTeacherTbl.setBackground(new java.awt.Color(153, 153, 153));
        subjectAssignedToTeacherTbl.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        subjectAssignedToTeacherTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Subject Code", "Subject Name", "Year Level", "Semester"
            }
        ));
        jScrollPane6.setViewportView(subjectAssignedToTeacherTbl);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(subjectSortBySchoolYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(subjectSortBySemester, 0, 141, Short.MAX_VALUE)))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(subjectSortBySchoolYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(subjectSortBySemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE))
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
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setMaximumSize(new java.awt.Dimension(100, 100));

        homeBtn.setBackground(new java.awt.Color(51, 51, 51));
        homeBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        homeBtn.setForeground(new java.awt.Color(204, 0, 0));
        homeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/home_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        homeBtn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 4, true));
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
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        applyFiltersAndSearch();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void searchtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchtxtActionPerformed
        // TODO add your handling code here:
        applyFiltersAndSearch();
    }//GEN-LAST:event_searchtxtActionPerformed

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        // TODO add your handling code here:
        new TeacherHomePage(currentTeacherUsername).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_homeBtnActionPerformed

    private void exitBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtn1ActionPerformed
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
           new LogInPage().setVisible(true);
           this.dispose();
        }
    }//GEN-LAST:event_exitBtn1ActionPerformed

    private void subjectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectBtnActionPerformed
        // TODO add your handling code here:
       loadAssignedSubjectsToTable();
    }//GEN-LAST:event_subjectBtnActionPerformed

    private void teacherBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherBtnActionPerformed
        // TODO add your handling code here:
        new TeacherTeacherAccess(currentTeacherUsername).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_teacherBtnActionPerformed

    private void subjectSortBySemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectSortBySemesterActionPerformed
        // TODO add your handling code here:
        applyFiltersAndSearch();
    }//GEN-LAST:event_subjectSortBySemesterActionPerformed

    private void subjectSortBySchoolYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectSortBySchoolYearActionPerformed
        // TODO add your handling code here:
        applyFiltersAndSearch();
    }//GEN-LAST:event_subjectSortBySchoolYearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TeacherSubjectAccess.class.getName())
                                   .log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new TeacherSubjectAccess().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitBtn1;
    private javax.swing.JButton homeBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextField searchtxt;
    private javax.swing.JTable studentEnrolledInThisSubjectTbl;
    private javax.swing.JTable subjectAssignedToTeacherTbl;
    private javax.swing.JButton subjectBtn;
    private javax.swing.JComboBox<String> subjectSortBySchoolYear;
    private javax.swing.JComboBox<String> subjectSortBySemester;
    private javax.swing.JButton teacherBtn;
    // End of variables declaration//GEN-END:variables

    private void loadStudentIsolatedSubjects() {
    applyFiltersAndSearch();
    }
}
