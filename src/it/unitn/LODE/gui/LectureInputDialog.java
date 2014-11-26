/*
 * CourseInputDialog.java
 *
 * Created on 7 giugno 2008, 22.42
 */
package it.unitn.LODE.gui;

import it.unitn.LODE.Models.Course;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.Models.ProgramState;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 * @author  ronchet
 */
public class LectureInputDialog extends javax.swing.JDialog {

    final String insertNewLecturer = "insert new Lecturer";
    Course course = null;
    Lecture lecture = null;
    ProgramState programState = null;
    Set<String> lecturers = null;
    //CourseController controller=null;

    /** Creates new form CourseInputDialog */
    public LectureInputDialog(java.awt.Frame parent) {
        super(parent, true); // modal dialog
        //this.controller=controller;
        programState = ProgramState.getInstance();
        course = programState.getCurrentCourse();
        System.out.println(course);
        initComponents();
        if (course != null) {
            sequenceNumberSpinner.setValue(new Integer(course.getNumberOfLectures() + 1));
            lecturers = course.getLecturers();
            Iterator iter = lecturers.iterator();
            if (iter.hasNext()) {
                lecturerComboBox.removeAllItems();
                while (iter.hasNext()) {
                    lecturerComboBox.addItem(iter.next());
                }
                lecturerComboBox.setEnabled(true);
            }
        }
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void inputLectureData() {
        this.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        courseNameLabel = new javax.swing.JLabel();
        yearLabel = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        lectureNameTextField = new javax.swing.JTextField();
        dateButton = new it.unitn.LODE.utils.CalendarDialog.DateButton();
        sequenceLabel = new javax.swing.JLabel();
        lecturerTextField = new javax.swing.JTextField();
        lecturerLabel = new javax.swing.JLabel();
        lecturerComboBox = new javax.swing.JComboBox();
        sequenceNumberSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        jPopupMenu2.setName("jPopupMenu2"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        courseNameLabel.setText("Lecture Name");
        courseNameLabel.setName("courseNameLabel"); // NOI18N

        yearLabel.setText("Lecture Date");
        yearLabel.setName("yearLabel"); // NOI18N

        okButton.setText("OK");
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        lectureNameTextField.setName("lectureNameTextField"); // NOI18N

        dateButton.setName("dateButton"); // NOI18N

        sequenceLabel.setText("Sequence Number in the course");
        sequenceLabel.setName("sequenceLabel"); // NOI18N

        lecturerTextField.setName("lecturerTextField"); // NOI18N

        lecturerLabel.setText("Lecturer");
        lecturerLabel.setName("lecturerLabel"); // NOI18N

        lecturerComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "insert new lecturer" }));
        lecturerComboBox.setEnabled(false);
        lecturerComboBox.setName("lecturerComboBox"); // NOI18N
        lecturerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lecturerComboBoxActionPerformed(evt);
            }
        });

        sequenceNumberSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), null, null, Integer.valueOf(1)));
        sequenceNumberSpinner.setName("sequenceNumberSpinner"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        jLabel1.setText("select an existing lecturer or insert a new one");
        jLabel1.setName("jLabel1"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(23, 23, 23)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(courseNameLabel)
                            .add(yearLabel))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lectureNameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE))
                            .add(layout.createSequentialGroup()
                                .add(9, 9, 9)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel1)
                                    .add(layout.createSequentialGroup()
                                        .add(dateButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 77, Short.MAX_VALUE)
                                        .add(sequenceLabel)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(sequenceNumberSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(58, 58, 58)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(lecturerLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lecturerComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 173, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lecturerTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 351, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(okButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cancelButton)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(44, 44, 44)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(courseNameLabel)
                    .add(lectureNameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(16, 16, 16)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(yearLabel)
                    .add(dateButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sequenceNumberSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sequenceLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lecturerLabel)
                    .add(lecturerComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lecturerTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(okButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    // read fields
    String lectureName = lectureNameTextField.getText();

    if (lectureName == null || lectureName.equals("")) {
        lectureName = "_";
    }
    /* {
    JOptionPane jop=new JOptionPane(
    "Lecture name cannot be empty!",
    JOptionPane.WARNING_MESSAGE,
    JOptionPane.DEFAULT_OPTION);
    JDialog d=jop.createDialog(this, "Error");
    d.setVisible(true);
    return;
    } */
    Date date = dateButton.getDate();
    String lecturer = null;
    if (lecturerComboBox.getSelectedIndex() != 0) {
        // the selected lecturer already exists in the course
        lecturer = (String) lecturerComboBox.getSelectedItem();
    } else {
        lecturer = lecturerTextField.getText();
        if (lecturer.equals("")
                || lecturer == null) {
            lecturer = "-";
        }
        lecturers.add(lecturer);
        //course.setLecturers(lecturers);
    }
    int seqNum = Integer.parseInt(sequenceNumberSpinner.getValue().toString());
    lecture = new Lecture(programState.getCurrentCourse(), lectureName, lecturer, date, seqNum);
    //controller.setLecture(lecture);
    resetValues();
    //lodeController.cop.resume();

    //Object[] params={};
    //lodeController.executeNextAction(params);
}//GEN-LAST:event_okButtonActionPerformed

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    lecture = null;
    resetValues();
}//GEN-LAST:event_cancelButtonActionPerformed

private void lecturerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lecturerComboBoxActionPerformed
    //String s=(String)lecturerComboBox.getSelectedItem();
    //int i=lecturerComboBox.getSelectedIndex();
    //if (i!=0) {
    lecturerTextField.setText((String) lecturerComboBox.getSelectedItem());
    //lecturerComboBox.setEditable(false);
    //} else {
    //    lecturerTextField.setText("");
    //    lecturerComboBox.setEditable(true);
    //}
}//GEN-LAST:event_lecturerComboBoxActionPerformed
    private void resetValues() {
        //reset values for next use;
        this.setVisible(false);
        this.lectureNameTextField.setText("...");
        this.sequenceLabel.setText("...");
        ////this.yearSpinner.setValue(LODE.year);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel courseNameLabel;
    private it.unitn.LODE.utils.CalendarDialog.DateButton dateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JTextField lectureNameTextField;
    private javax.swing.JComboBox lecturerComboBox;
    private javax.swing.JLabel lecturerLabel;
    private javax.swing.JTextField lecturerTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel sequenceLabel;
    private javax.swing.JSpinner sequenceNumberSpinner;
    private javax.swing.JLabel yearLabel;
    // End of variables declaration//GEN-END:variables
}