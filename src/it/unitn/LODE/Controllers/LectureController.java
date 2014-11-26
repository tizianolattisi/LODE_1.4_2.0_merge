package it.unitn.LODE.Controllers;

import it.unitn.LODE.gui.FileTree.FileTreePanel;
import it.unitn.LODE.gui.*;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.Course;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.Models.ProgramState;
import it.unitn.LODE.gui.InspectorWindow;
import it.unitn.LODE.utils.Messanger;
import java.io.File;

/**
 *
 * @author ronchet
 */
public class LectureController {
    //

    Course course = null;  // Model
    Lecture lecture = null;
    LectureInputDialog view = null; // View
    //
    LectureInputDialog cid = null;
    //FileSystemManager fileSystemManager=null;
    FileTreePanel fileTreePanel = null;
    String cDirName = null;
    InspectorWindow inspector = null;
    ProgramState programState=null;

    // ========== SOLITON PATTERN ==============================================
    static LectureController instance = null;

    public synchronized static LectureController getInstance() {
        if (instance == null) {
            instance = new LectureController();
        }
        return instance;

    }

    private LectureController() {
        //    fileSystemManager=FileSystemManager.getInstance();
        programState=ProgramState.getInstance();
    }
    //==========================================================================

    public final boolean createNewLecture() {
        // returns true if a lecture has been created
        inspector = InspectorWindow.getInstance();
        cid = new LectureInputDialog(inspector);
        //cop=new Synchronizer();   
        //cs=CourseController.getInstance();
        // COURSE SELECTION ====================================================
        // create course
        cid.inputLectureData();
        lecture = cid.getLecture();
        if (lecture != null) { // ============================= XXXXXXXXXXXXXXXXXX
            lecture.save(null);
            programState.setCurrentLecture(lecture);
            Messanger.getInstance().w("Created lecture " + lecture.getLectureName(), LODEConstants.MSG_LOG);
            inspector.update();
            return true;
        }
        return false;

    }
    /*
     public final Lecture selectExistingLectureToConvert(){ // ANTONIO
        
        fileTreePanel=new FileTreePanel(LODEConstants.COURSES_HOME,0);
        cDirName=fileTreePanel.selectLecture();
        if (cDirName==null || cDirName.indexOf(LODEConstants.FS)==-1) return null;
        lecture=(Lecture)(new Lecture()).resume(new File(cDirName+File.separator+LODEConstants.SERIALIZED_LECTURE),Lecture.class);
        if (lecture==null) return null;
        return lecture;
    }
     *
     */
    public final boolean selectExistingLecture() {
        // returns true if a lecture has been opened
        Course aCourse = programState.getCurrentCourse();
        if (aCourse != null) {
            fileTreePanel = new FileTreePanel(aCourse.getFullPath(), 0);
        } else {
            fileTreePanel = new FileTreePanel(LODEConstants.COURSES_HOME, 0);
        }
        cDirName = fileTreePanel.selectLecture();
        return setLectureFromPath(cDirName);
        //String dir= LODEConstants.COURSES_HOME;
        //if (aCourse!=null) dir= ProgramState.getCurrentCourse().getFullPath()+LODEConstants.ACQUISITION_SUBDIR;
        /*
        ImageIcon icon=null;
        java.net.URL imgURL = CourseController.class.getResource(
        LODEConstants.IMGS_PREFIX +"addbutton.gif");
        if (imgURL != null) icon=new ImageIcon(imgURL);
        cDirName=fileSystemManager.selectAFolder(
        dir,
        LODEConstants.SERIALIZED_LECTURE,
        "Select a LODE lecture",
        "Select",
        "Only valid LODE lectures",
        icon);
         */
    }

    public final boolean setLectureFromPath(String path) {
        if (path == null || path.indexOf(LODEConstants.FS) == -1) {
            return false;
        }
        lecture = (Lecture) (new Lecture()).resume(new File(path + File.separator + LODEConstants.SERIALIZED_LECTURE), Lecture.class);
        if (lecture == null) {
            return false;
        }
        programState.setCurrentLecture(lecture);
        programState.setCurrentCourse(lecture.getCourse());
        Messanger.getInstance().w("Opened lecture " + lecture.getLectureName(), LODEConstants.MSG_LOG);
        InspectorWindow.getInstance().update();
        return true;
    }
}
