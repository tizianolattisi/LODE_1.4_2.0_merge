package it.unitn.lodeWeb.gui;

//--------------------------------------------------------------------------
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.gui.FileTree.FileTreePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * Panel to select course directory and output directory (output web application)
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class ChooserPanel extends JPanel implements ActionListener {

    private JButton buttonCourseDir;
    private JButton buttonOutDir;
    private JFileChooser fc;
    private String choosertitle;
    public JTextField taskOutputCourse = null;
    public JTextField taskOutputDir = null;
    public JLabel jLabel1,  jLabel2;

    //--------------------------------------------------------------------------
    /**
     * Get the course directory
     *
     * @return String - course directory selected
     */
    //--------------------------------------------------------------------------
    public String getCourseDir() {
        if (taskOutputCourse != null) {
            return this.taskOutputCourse.getText();
        } else {
            return null;
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Get the output directory (web application)
     *
     * @return String - output directory selected
     */
    //--------------------------------------------------------------------------
    public String getOutDir() {
        if (taskOutputDir != null) {
            return this.taskOutputDir.getText();
        } else {
            return null;
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     *  Class Constructor
     *
     *  init graphics
     */
    //--------------------------------------------------------------------------
    public ChooserPanel() {

        super();

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        jLabel1 = new JLabel("Course Dir :");
        jLabel2 = new JLabel("Output dir :");

        buttonCourseDir = new JButton("Select...");
        buttonCourseDir.setName("buttonCourse");
        buttonCourseDir.addActionListener(this);
        jLabel1.setLabelFor(buttonCourseDir);

        buttonOutDir = new JButton("Select...");
        buttonOutDir.setName("buttonOutDir");
        buttonOutDir.addActionListener(this);
        jLabel2.setLabelFor(buttonOutDir);

        taskOutputCourse = new JTextField(20);
        taskOutputCourse.setColumns(100);
        taskOutputCourse.setEditable(false);

        taskOutputDir = new JTextField(20);
        taskOutputDir.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(this);
        this.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(buttonCourseDir).addComponent(buttonOutDir)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(taskOutputCourse, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE).addComponent(taskOutputDir, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(buttonCourseDir).addComponent(taskOutputCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(buttonOutDir).addComponent(taskOutputDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

    }
    //--------------------------------------------------------------------------
    /**
     *  actionPerformed
     *  create directory chooser
     *
     * @param e - ActionEvent
     */
    //--------------------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {
        // delegate the outdir to the old version
        JButton b = (JButton) e.getSource();
        if (b.getName().equals("buttonOutDir")) {
            actionPerformedOLD(e);
            return;
        }
        // here we deal with the new version of the course chooser
        FileTreePanel fileTreePanel=new FileTreePanel(LODEConstants.COURSES_HOME,0);
        String cDirName=fileTreePanel.selectCourse();
        if (cDirName==null || cDirName.indexOf(LODEConstants.FS)==-1) return;
        taskOutputCourse.setText(cDirName);
    }

    //--------------------------------------------------------------------------
    /**
     *  actionPerformed
     *  create directory chooser
     *
     * @param e - ActionEvent
     */
    //--------------------------------------------------------------------------
    public void actionPerformedOLD(ActionEvent e) {

        // creo il file chooser
        if (fc == null) {
            fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setAcceptAllFileFilterUsed(false);

            fc.setCurrentDirectory(new java.io.File("."));
        }

        JButton b = (JButton) e.getSource();
        if (b.getName().equals("buttonOutDir")) {
            choosertitle = "Output directory";
        } else {
            choosertitle = "Course directory";
        }
        fc.setDialogTitle(choosertitle);

        int returnVal = fc.showDialog(ChooserPanel.this, "Attach");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            JButton source = ((JButton) e.getSource());
            if (source.getName().equals("buttonCourse")) {
                taskOutputCourse.setText(file.getPath());
            } else if (source.getName().equals("buttonOutDir")) {
                taskOutputDir.setText(file.getPath());
            }

        }
        fc.setSelectedFile(null);
    }

}
