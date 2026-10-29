package gui;

import admin_gui.AdminCourseAccess;
import admin_gui.AdminEnrollmentAccess;
import admin_gui.AdminStudentAccess;
import admin_gui.AdminSubjectAccess;
import admin_gui.AdminTeacherAccess;
import javax.swing.JFrame;
import landingPageGui.LogInPage;
import javax.swing.JOptionPane;

public class AdminHomePage extends javax.swing.JFrame {

    private String username = "Admin";

    public AdminHomePage() {
        initComponents();
        setLocationRelativeTo(null);
         setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public AdminHomePage(String username) {
        this();
        this.username = username;
        jLabel6.setText("Welcome, " + username + "!");
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        exitBtn1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        enrollmentBtn = new javax.swing.JButton();
        teacherBtn = new javax.swing.JButton();
        subjectBtn = new javax.swing.JButton();
        studentBtn = new javax.swing.JButton();
        coursetBtn = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        homeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 102, 102));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/warning_24dp_EA3323_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jLabel7.setText("Admin Access");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        exitBtn1.setBackground(new java.awt.Color(51, 51, 51));
        exitBtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        exitBtn1.setForeground(new java.awt.Color(255, 204, 204));
        exitBtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/logout_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        exitBtn1.setText("LOG-OUT");
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
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitBtn1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));

        jPanel7.setBackground(new java.awt.Color(102, 102, 102));

        enrollmentBtn.setBackground(new java.awt.Color(51, 51, 51));
        enrollmentBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        enrollmentBtn.setForeground(new java.awt.Color(204, 255, 255));
        enrollmentBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/history_edu_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        enrollmentBtn.setText("Enrollment");
        enrollmentBtn.setBorderPainted(false);
        enrollmentBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        enrollmentBtn.addActionListener(this::enrollmentBtnActionPerformed);

        teacherBtn.setBackground(new java.awt.Color(51, 51, 51));
        teacherBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        teacherBtn.setForeground(new java.awt.Color(204, 255, 255));
        teacherBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/face_6_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        teacherBtn.setText("Teachers");
        teacherBtn.setBorderPainted(false);
        teacherBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        teacherBtn.addActionListener(this::teacherBtnActionPerformed);

        subjectBtn.setBackground(new java.awt.Color(51, 51, 51));
        subjectBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        subjectBtn.setForeground(new java.awt.Color(204, 255, 255));
        subjectBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/topic_24dp_FFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        subjectBtn.setText("Subjects");
        subjectBtn.setBorderPainted(false);
        subjectBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        subjectBtn.addActionListener(this::subjectBtnActionPerformed);

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(102, 102, 102));

        jEditorPane1.setEditable(false);
        jEditorPane1.setBackground(new java.awt.Color(51, 51, 51));
        jEditorPane1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jEditorPane1.setForeground(new java.awt.Color(255, 255, 255));
        jEditorPane1.setText("Student Enrollment Management System are designed to make managing student Enrollment easier, faster, and more organized.  \nIt allows teachers and students to access and manage important academic information through a simple and user-friendly system.   \n\nStudent Enrollment Management System aims to reduce manual work, keep records organized, and provide a convenient way to manage student enrollment information in one platform.");
        jEditorPane1.setToolTipText("");
        jScrollPane1.setViewportView(jEditorPane1);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Welcome, Admin!!!");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Student Enrollment Management System");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 96)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("SEMS");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 36, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 37, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
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

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        // TODO add your handling code here:
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
        new AdminSubjectAccess(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_subjectBtnActionPerformed

    private void studentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentBtnActionPerformed
        // TODO add your handling code here:
        new AdminStudentAccess(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_studentBtnActionPerformed

    private void exitBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtn1ActionPerformed
        int choice = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to log out?", 
            "Logout Confirmation", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            // Return to Login Frame (Update class name if needed)
           new LogInPage().setVisible(true);
           this.dispose();
        }
    }//GEN-LAST:event_exitBtn1ActionPerformed

    private void coursetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coursetBtnActionPerformed
        // TODO add your handling code here:
        new AdminCourseAccess(username).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_coursetBtnActionPerformed

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
            java.util.logging.Logger.getLogger(AdminHomePage.class.getName())
                .log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new AdminHomePage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton coursetBtn;
    private javax.swing.JButton enrollmentBtn;
    private javax.swing.JButton exitBtn1;
    private javax.swing.JButton homeBtn;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton studentBtn;
    private javax.swing.JButton subjectBtn;
    private javax.swing.JButton teacherBtn;
    // End of variables declaration//GEN-END:variables
}
