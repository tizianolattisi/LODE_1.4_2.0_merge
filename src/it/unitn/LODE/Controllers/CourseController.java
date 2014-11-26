package it.unitn.LODE.Controllers;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.LODEPreferences;
import it.unitn.LODE.Models.Course;
import it.unitn.LODE.Models.CoursePublicationInfo;
import it.unitn.LODE.Models.ProgramState;
import it.unitn.LODE.gui.CourseInputDialog;
import it.unitn.LODE.gui.FileTree.FileTreePanel;
import it.unitn.LODE.gui.InspectorWindow;
import it.unitn.LODE.utils.Messanger;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author ronchet
 */
public class CourseController {
    //

    Course course = null;  // Model
    CourseInputDialog view = null; // View
    CourseInputDialog cid = null;
    //FileSystemManager fileSystemManager=null;
    ProgramState programState=null;
    FileTreePanel fileTreePanel = null;
    String cDirName = null;
    InspectorWindow inspector = null;
    // ========== SOLITON PATTERN ==============================================
    static CourseController instance = null;

    public synchronized static CourseController getInstance() {
        if (instance == null) {
            instance = new CourseController();
        }
        return instance;
    }

    private CourseController() {
        //fileSystemManager=FileSystemManager.getInstance();
        programState=ProgramState.getInstance();
    }
    //==========================================================================

    public final void createNewCourse() {
        inspector = InspectorWindow.getInstance();
        cid = new CourseInputDialog(inspector);
        //cop=new Synchronizer();   
        //cs=CourseController.getInstance();
        // COURSE SELECTION ====================================================
        // create course
        cid.inputCourseData();
        course = cid.getCourse();
        if (course == null) return; //no course has been created
        {
            course.save(null);
            programState.setCurrentCourse(course);
            Messanger.getInstance().w("Created course " + course.getCourseName(), LODEConstants.MSG_LOG);
            inspector.update();
        }
        cDirName=course.getFullPath();
        String webPath=cDirName+LODEConstants.FS+LODEConstants.STANDARD_WEBSITE_DIRECTORY;
        //System.out.println(webPath);
        File publicationDir = new File(webPath);
        if (!publicationDir.exists()) {
            publicationDir.mkdirs();
        }
        CoursePublicationInfo coursePublication = new CoursePublicationInfo();
        coursePublication.setPublicationRootDir(webPath);
        coursePublication.persist(new File(cDirName + File.separator + LODEConstants.SERIALIZED_COURSE_PUBLICATION));
        //LODEPreferences.getInstance().changeProperty(LODEPreferences.course, cDirName);
        LODEPreferences.getInstance().addCourse(cDirName);
    }
    public final boolean openCourse(String cDirName) {
        String filename=null;
        try {
            filename=cDirName + File.separator + LODEConstants.SERIALIZED_COURSE;
            course = (Course) (new Course()).resume(new File(filename), Course.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            Messanger.getInstance().w("Error while deserializing XML file "+filename, LODEConstants.MSG_ERROR);
        }
        if (course == null) {
            return false;
        }
        this.cDirName=cDirName;
        programState.setCurrentCourse(course);
        CoursePublicationInfo coursePublication = getCoursePublication();
        Messanger.getInstance().w("Opened course " + course.getCourseName(), LODEConstants.MSG_LOG);
        //LODEPreferences.getInstance().changeProperty(LODEPreferences.course, cDirName);
        LODEPreferences.getInstance().addCourse(cDirName);
        InspectorWindow.getInstance().update();
        return true;
    }
    public final boolean selectExistingCourse() {
        /*
        ImageIcon icon=null;
        java.net.URL imgURL = CourseController.class.getResource(
        LODEConstants.IMGS_PREFIX +"addbutton.gif");
        if (imgURL != null) icon=new ImageIcon(imgURL);

        cDirName=fileSystemManager.selectAFolderForReading(
        LODEConstants.COURSES_HOME,
        LODEConstants.SERIALIZED_COURSE,
        "Select a LODE course",
        "Select",
        "Only valid LODE courses",
        icon); */
        fileTreePanel = new FileTreePanel(LODEConstants.COURSES_HOME, 0);
        cDirName = fileTreePanel.selectCourse();
        if (cDirName == null || cDirName.indexOf(LODEConstants.FS) == -1) {
            return false;
        }
        //LODEPreferences.getInstance().changeProperty(LODEPreferences.course, cDirName);
        LODEPreferences.getInstance().addCourse(cDirName);
        return openCourse(cDirName);
        /*
        try {
            course = (Course) (new Course()).resume(new File(cDirName + File.separator + LODEConstants.SERIALIZED_COURSE), Course.class);
        } catch (Exception ex) {
            Messanger.getInstance().w("Error while deserializing XML file", LODEConstants.MSG_ERROR);
        }
        if (course == null) {
            return false;
        }
        programState.setCurrentCourse(course);
        Messanger.getInstance().w("Opened course " + course.getCourseName(), LODEConstants.MSG_LOG);
        InspectorWindow.getInstance().update();
        return true;
        */
    }

    //==========================================================================
    public final CoursePublicationInfo getCoursePublication() {
        CoursePublicationInfo coursePublication = null;
        String path=cDirName + File.separator + LODEConstants.SERIALIZED_COURSE_PUBLICATION;
        try {
            coursePublication = (CoursePublicationInfo) (new CoursePublicationInfo()).resume(new File(path), CoursePublicationInfo.class);
        } catch (Exception ex) {
            //File does not exist, or is not readable, or has errors
            System.err.println("CourseController.getCoursePublication() - File does not exist, or is not readable, or has errors\n "+path);
        }
        return coursePublication;
    }

    public final void defineCoursePublicationLocation() {
        inspector = InspectorWindow.getInstance();
        String publicationDirName = null;
        CoursePublicationInfo coursePublication = getCoursePublication();

        File publicationDir = null;
        if (coursePublication != null) {
            publicationDirName = coursePublication.getPublicationRootDir();
            publicationDir = new File(publicationDirName);
            if (!publicationDir.exists()) {
                publicationDir = null;
            }
        } else {
            coursePublication = new CoursePublicationInfo();
        }

        /* String s=null;
        FileSystemManager fsm=ControllersManager.getinstance().getFileSystemManager();
        try {
        s = fsm.selectAFolderForReading(LODEConstants.COURSES_HOME);
        } catch (Exception ex) {
        Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(s);*/
        JFileChooser fc = null;
        if (publicationDir != null) {
            fc = new JFileChooser(publicationDir.getParentFile());
        } else {
            fc = new JFileChooser(LODEConstants.COURSES_HOME);
        }
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setCurrentDirectory(publicationDir);
        //int rc = fc.showOpenDialog(null, "Select directory for course publication");
        int rc = fc.showOpenDialog(null);
        if (rc != JFileChooser.APPROVE_OPTION) {
            System.out.println("Failure to select the root directory for the course publication");
            return;
        }
        File f = fc.getSelectedFile();
        publicationDirName = f.getAbsolutePath();
        course=programState.getCurrentCourse();
        cDirName=course.getFullPath();
        coursePublication.setPublicationRootDir(publicationDirName);
        coursePublication.persist(new File(cDirName + File.separator + LODEConstants.SERIALIZED_COURSE_PUBLICATION));
    }
}
