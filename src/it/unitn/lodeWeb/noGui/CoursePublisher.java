package it.unitn.lodeWeb.noGui;

import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.Controllers.CourseController;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.Course;
import it.unitn.LODE.Models.CoursePublicationInfo;
import it.unitn.LODE.Models.ProgramState;
import it.unitn.lodeWeb.execution.Exec2;
import it.unitn.lodeWeb.gui.create.CreateSite_SmallPanel;
import it.unitn.lodeWeb.gui.create.ProgressJDialog;
import it.unitn.lodeWeb.util.Constants;
import it.unitn.lodeWeb.util.OptionClass;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author ronchet
 */
public class CoursePublisher {

    // ========== SOLITON PATTERN ==============================================
    static CoursePublisher instance = null;

    public static CoursePublisher getInstance() {
        if (instance == null) {
            instance = new CoursePublisher();
        }
        return instance;
    }

    private CoursePublisher() { // this constructor only inhibits calling new on this class
    }

    public static void main(String a[]) {
        getInstance().publish();
    }

    /**
     * check if VLC is installed
     * @return true if installed, false otherwise
     */
    private boolean _isVLCok() {
        File f = new File(LODEConstants.VLC_PATH);
        if (!f.exists()) {
            JOptionPane.showMessageDialog(null, "<HTML>VLC is not installed. Please get it from <a href=\"http://www.videolan.org/vlc/\">videolan</a>, installalo e riprova!", "VLC non installato", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void publish() { //throws Exception {

        String css_path = Constants.dirCss + Constants.cssList[4];
        String webSiteLoc = null;
        //check if VLC is installed
        if (!_isVLCok()) {
            return;
        }
        // ============== GET COURSE INFO ======================================
        // == il corso deve essere definito
        if (!ProgramState.getInstance().isCourseDefined) {
            JOptionPane.showMessageDialog(null, "Devi prima scegliere un corso!", "Corso non definito", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Course course = ProgramState.getInstance().getCurrentCourse();
        // READ information on course publication froma saved file
        // - if unavailable ask the user though the CourseController
        CoursePublicationInfo coursePublicationInfo = null;
        try {
            coursePublicationInfo = (CoursePublicationInfo) (new CoursePublicationInfo()).resume(new File(course.getFullPath() + File.separator + LODEConstants.SERIALIZED_COURSE_PUBLICATION), CoursePublicationInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //CoursePublicationInfo coursePublicationInfo=courseController.getCoursePublication();
        if (coursePublicationInfo == null) {
            CourseController courseController = CourseController.getInstance();
            courseController.defineCoursePublicationLocation();
            coursePublicationInfo = courseController.getCoursePublication();
        }
        webSiteLoc = coursePublicationInfo.getPublicationRootDir();
        // =====================================================================
        // Prepare the DTO
        OptionClass oc = new OptionClass(
                true, //Images from video - optionPanel.jCheckBox1.isSelected(),
                true, // Images from slides - optionPanel.jCheckBox6.isSelected(),
                LODEConstants.VLC_PATH,
                true, // Lectures distribution - optionPanel.jCheckBox2.isSelected(),
                true, // Zip file lectures - optionPanel.jCheckBox3.isSelected(),
                false, // File notes - optionPanel.jCheckBox4.isSelected(),
                true, // Slides Sources optionPanel.jCheckBox5.isSelected(),
                Constants.ContextWeb.STATIC.toString(), // admin username - optionPanel.jComboBoxContext.getSelectedItem().toString(),
                css_path, "pippo", "pippo"); //admin password optionPanel.css_path, username, password);

        ProgressJDialog jfp = ControllersManager.getinstance().getProgressJDialog();
        jfp.reset("Publishing course...");

        Course c = ProgramState.getInstance().getCurrentCourse();
        String coursePath = c.getFullPath();
        String[] args = {coursePath + Constants.FS + Constants.acquisNameDir,
            coursePath + Constants.FS + Constants.distNameDir,//acquisitionDir, distributionDir,
            coursePath,
            webSiteLoc,//chooserPanel.getOutDir(),
            "1",
            Constants.ContextWeb.STATIC.toString()};//optionPanel.jComboBoxContext.getSelectedItem().toString()};

        //log.setForeground(Color.BLUE);
        //log.setText("");
        CreateSite_SmallPanel publishingPanel = new CreateSite_SmallPanel();
        publishingPanel.display();
        publishingPanel.start();
        Exec2 prova = new Exec2(args, oc, publishingPanel);
        Thread t=new Thread(prova);
        t.start();
        //prova.run();
        //new Thread(prova).start();

        /*    }

        } else {
        setLogError("Bad course directory");
        }
        } else {
        setLogError("Bad output directory");
        }*/
    }
}
