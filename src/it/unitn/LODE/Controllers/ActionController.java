
package it.unitn.LODE.Controllers;

import it.unitn.LODE.Handlers;
import it.unitn.LODE.MP.factories.VideoControllerFactory;
import it.unitn.LODE.MP.IF.Controllers.VideoControllerIF;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.LODEPreferences;
import it.unitn.LODE.Models.Course;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.Models.ProgramState;
import it.unitn.LODE.gui.AcquisitionWindow;
import it.unitn.LODE.gui.InspectorWindow;
import it.unitn.LODE.gui.MenuManager;
import it.unitn.LODE.gui.lode2bind.LODE2Runner;
import it.unitn.LODE.itunesuMetadata.gui.CourseTablePanel;
import it.unitn.LODE.itunesuMetadata.gui.LectureTablePanel;
import it.unitn.LODE.services.PostProducerIF;
import it.unitn.LODE.services.SlideImporter;
import it.unitn.LODE.utils.Messanger;
import it.unitn.lodeWeb.noGui.CoursePublisher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

/**
 *
 * @author ronchet
 */
public class ActionController implements ActionListener {

    // ========== SOLITON PATTERN ==============================================
    static ActionController instance = null;
    private CourseController courseController = null;
    private LectureController lectureController = null;
    private ProgramState programState=null;
    private VideoControllerIF videoController=null;
    private ControllersManager controllersManager=null;
        
    public static ActionController getInstance() {
        if (instance == null) {
            instance = new ActionController();
        }
        return instance;
    }

    private ActionController() {
        controllersManager =ControllersManager.getinstance();
        courseController = CourseController.getInstance();
        lectureController = LectureController.getInstance();
        programState = ProgramState.getInstance();
        
    }
    // ========== CONSTANTS ====================================================
    static final public String QUIT = "QUIT";
    static final public String ABOUT = "ABOUT";
    static final public String PREFERENCES = "PREFERENCES";
    static final public String NEW_COURSE = "NEW_COURSE";
    static final public String OPEN_COURSE = "OPEN_COURSE";
    static final public String REOPEN_LAST_COURSE = "REOPEN_LAST_COURSE";
    static final public String REOPEN_LAST_COURSE2 = "REOPEN_LAST_COURSE2";
    static final public String REOPEN_LAST_COURSE3 = "REOPEN_LAST_COURSE3";
    static final public String CLOSE_COURSE = "CLOSE_COURSE";
    static final public String NEW_LECTURE = "NEW_LECTURE";
    static final public String OPEN_LECTURE = "OPEN_LECTURE";
    static final public String CLOSE_LECTURE = "CLOSE_LECTURE";
    static final public String EDIT_LECTURE = "EDIT_LECTURE";
    static final public String EDIT_COURSE = "EDIT_COURSE";
    static final public String IMPORT_PDF = "IMPORT_PDF";
    static final public String IMPORT_ODP = "IMPORT_ODP";
    static final public String IMPORT_PPT = "IMPORT_PPT";
    static final public String IMPORT_PPTX = "IMPORT_PPTX";    
    static final public String IMPORT_JPG = "IMPORT_JPG";
    static final public String IMPORT_FAKE_SLIDES = "IMPORT_FAKE_SLIDES";
    static final public String EDIT_SLIDES_TITLES = "EDIT_SLIDES_TITLES";
    static final public String DELETE_CURRRENT_PDF = "DELETE_CURRRENT_PDF";
    static final public String POST_PROCESS_ONE = "POST_PROCESS_ONE";
    static final public String POST_PROCESS_COURSE = "POST_PROCESS_COURSE";
    static final public String POST_PROCESS_ALL = "POST_PROCESS_ALL";
    static final public String CONVERT_ONE_4_ITUNESU="CONVERT_ONE_4_ITUNESU"; //aggiunto da Antonio
    static final public String GENERATE_COURSE_METADATA_4_ITUNESU="GENERATE_COURSE_METADATA_4_ITUNESU";
    static final public String GENERATE_LECTURE_METADATA_4_ITUNESU="GENERATE_LECTURE_METADATA_4_ITUNESU";
    static final public String CONVERT_ONE_4_ACCESSIBILITY="CONVERT_ONE_4_ACCESSIBILITY";
    static final public String SEND_LECTURE = "SEND_LECTURE";
    static final public String REBUILD_DISTRIBUTION = "REBUILD_DISTRIBUTION";
    static final public String POST_PROCESS_ALL_REMOTELY = "POST_PROCESS_ALL_REMOTELY";
    static final public String OPEN_VIDEO_PREFERENCES = "OPEN_VIDEO_PREFERENCES";
    static final public String OPEN_VIDEO_PANEL = "OPEN_VIDEO_PANEL";
    static final public String START_VIDEO_RECORDING = "START_VIDEO_RECORDING";
    static final public String STOP_VIDEO_RECORDING = "STOP_VIDEO_RECORDING";
    static final public String SUSPEND_VIDEO_RECORDING = "SUSPEND_VIDEO_RECORDING";
    static final public String RESUME_VIDEO_RECORDING = "RESUME_VIDEO_RECORDING";
    static final public String RESET_VIDEO = "RESET_VIDEO";
    static final public String SHOW_INSPECTOR_WINDOW = "SHOW_INSPECTOR_WINDOW";
    static final public String SHOW_ACQUISITION_WINDOW = "SHOW_ACQUISITION_WINDOW";
    static final public String SHOW_STATE = "SHOW_STATE";
    static final public String SWAP_STATE = "SWAP_STATE";
    static final public String ADD_SLIDE = "ADD_SLIDE";
    static final public String GO_TO_SLIDE = "GO_TO_SLIDE";
    static final public String EDIT_SLIDE = "EDIT_SLIDE";
    static final public String DELETE_SLIDE_AT_TIME = "DELETE_SLIDE_AT_TIME";
    static final public String GO_TO_NEXT_SLIDE = "GO_TO_NEXT_SLIDE";
    static final public String GO_TO_PREVIOUS_SLIDE = "GO_TO_PREVIOUS_SLIDE";
    public final static String PUBLISH_WEB_SITE = "PUBLISH_WEB_SITE";
    public final static String SELECT_WEB_SITE_LOCATION = "SELECT_WEB_SITE_LOCATION";

    // =========================================================================
    public void actionPerformed(ActionEvent arg0) {
        AcquisitionWindow acquisitionWindow=null;
        if (programState.isLectureDefined) {// acqusitionWindow and videocontroller are present only if a lecture is defined!
            acquisitionWindow= ControllersManager.getinstance().getAcquisitionWindow();
            videoController = VideoControllerFactory.getInstance();
        }
        String command = arg0.getActionCommand();
        // ======================================================= VIDEO========
        if (command.equals(START_VIDEO_RECORDING)) {
            if (!programState.canRecord()) // add the why!
            {
                JOptionPane.showMessageDialog(null, "You cannot start a recording!", "Warning!", JOptionPane.WARNING_MESSAGE);
            } else {
                acquisitionWindow.startRecordingAction();
            }
        } else if (command.equals(STOP_VIDEO_RECORDING)) {
            int state = acquisitionWindow.stopRecordingAction();
            if (state==0)  {
                acquisitionWindow.clearInstance();
                //acquisitionWindow.disableRecording(); // at the end of a recording no other recording is possible
                //acquisitionWindow.resetVideo();
            }
            // if lecture has no slides, create placeholder slide and timed slides
            Lecture lecture=programState.getCurrentLecture();
            if (! lecture.hasSlides()) {
                Messanger.getInstance().w("Adding fake slides", LODEConstants.MSG_LOG);
                SlideImporter.getInstance().importSlides(lecture, "noslides");
                lecture.addTimedSlide(1, "0");
        }            
            // INSERT "no slides" SLIDE IF NEEDED
            //int state = videoController.getCameraState();
            //if ((state != VideoControllerIF.CAMERA_IS_RECORDING) && (state != VideoControllerIF.CAMERA_IS_PAUSED)) {
            //    acquisitionWindow.hideFrame();
            //}
            //acquisitionWindow.disableRecording(); // at the end of a recording no other recording is possible
        } else if (command.equals(SUSPEND_VIDEO_RECORDING)) {
            acquisitionWindow.pauseRecordingAction();
        } else if (command.equals(RESUME_VIDEO_RECORDING)) {
            acquisitionWindow.resumeRecordingAction();
        /*
         * } else if (command.equals(RESET_VIDEO)) {
            if (videoController.getCameraState() == VideoControllerIF.CAMERA_IS_RECORDING) {
                acquisitionWindow.stopRecordingAction();
            }
            acquisitionWindow.resetVideo();
            * 
            */
        } else if (command.equals(OPEN_VIDEO_PREFERENCES)) {
            if (programState.is_video_inited) {
                JOptionPane.showMessageDialog(null, "Sorry, you cannot open the video preferences after having opened the video window!", "Warning!", JOptionPane.WARNING_MESSAGE);
            } else {
                VideoControllerFactory.getInstance().showOptionPanels();
            }
        } else if (command.equals(OPEN_VIDEO_PANEL)) {
            if (programState.is_video_pref_open) {
                JOptionPane.showMessageDialog(null, "Sorry, you cannot open the video window while the video preferences are open!", "Warning!", JOptionPane.WARNING_MESSAGE);
            } else {
                //VideoController.getInstance().resetVideo();
                //acquisitionWindow.setUpMainPanel();
                //AcquisitionWindow.createAndShow();
                // Apro il cam controller di lode2
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        LODE2Runner.initAndShowGUI();
                    }
                });
            }  
         // ======================================================= LODEMENU ======            
        } else if (command.equals(QUIT)) {
            Handlers h=new Handlers();
            h.handleQuit();
        } else if (command.equals(PREFERENCES)) {
            Handlers h=new Handlers();
            h.handlePreferences();
            h=null;
        } else if (command.equals(ABOUT)) {
            Handlers h=new Handlers();
            h.handleAbout();
            h=null;
        // ======================================================= SLIDES ======            
        } else if (command.equals(ADD_SLIDE)) {
            acquisitionWindow.addSlide();
        } else if (command.equals(GO_TO_SLIDE)) {
            acquisitionWindow.jumpToSlide();
        } else if (command.equals(EDIT_SLIDE)) {
            acquisitionWindow.editSlide();
        } else if (command.equals(DELETE_SLIDE_AT_TIME)) {
            acquisitionWindow.deleteSlideAtTime();
        } else if (command.equals(GO_TO_NEXT_SLIDE)) {
            acquisitionWindow.goToNextSlide(1);
        } else if (command.equals(GO_TO_PREVIOUS_SLIDE)) {
            acquisitionWindow.goToNextSlide(-1);    
        } else if (command.equals(EDIT_SLIDES_TITLES)) {
            if (!programState.isLectureDefined || !programState.getCurrentLecture().hasSlides()) {
                JOptionPane.showMessageDialog(null, "You must first add slides to the lecture!", "Warning!", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Sorry, this feature has not yet been implemented", "Warning!", JOptionPane.WARNING_MESSAGE);
            }

        } else if (command.equals(IMPORT_PPT)) {
            SlideImporter.getInstance().importSlides(programState.getCurrentLecture(), "ppt");
        } else if (command.equals(IMPORT_PPTX)) {
            JOptionPane.showMessageDialog(null, "Sorry, importing pptx is not explicitely supported yet,\n"
                    + "but you can either print your pptx file to PDF and import PDF, \n"
                    + "or open the pptx file from MS PowerPoint and save a copy as ppt, \n"
                    + "or else you can go to http://media-convert.com and convert your pptx file to ppt.", "Warning!", JOptionPane.WARNING_MESSAGE);

        } else if (command.equals(IMPORT_PDF)) {
            SlideImporter.getInstance().importSlides(programState.getCurrentLecture(), "pdf");
        } else if (command.equals(IMPORT_ODP)) {
            JOptionPane.showMessageDialog(null, "Sorry, importing ODP is not explicitely supported yet,\n"
                    + "but you either print your ODP file to PDF and import PDF, \n"
                    + "or you can go to http://media-convert.com and convert your ODP file to PPT.", "Warning!", JOptionPane.WARNING_MESSAGE);
        } else if (command.equals(IMPORT_JPG)) {
            SlideImporter.getInstance().importSlides(programState.getCurrentLecture(), "jpg");
        } else if (command.equals(IMPORT_FAKE_SLIDES)) {
            //SlideImporter.getInstance().importFakeSlides(programState.getCurrentLecture());
            SlideImporter.getInstance().importSlides(programState.getCurrentLecture(), null);
        } else if (command.equals(DELETE_CURRRENT_PDF)) {
        // ======================================================= COURSE ======            
        } else if (command.equals(NEW_COURSE)) {
            if (programState.getCurrentCourse() != null) {
                int r = JOptionPane.showConfirmDialog(null, "This will close the current course and lecture. Are you sure you want to continue?", "Warning!", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    programState.resetCourse();
                    InspectorWindow.getInstance().update();
                }
            }
            courseController.createNewCourse();
        } else if (command.equals(OPEN_COURSE)) {
            if (programState.getCurrentCourse() != null) {
                int r = JOptionPane.showConfirmDialog(null, "This will close the current course and lecture. Are you sure you want to continue?", "Warning!", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    programState.resetCourse();
                    InspectorWindow.getInstance().update();
                }
            }
            courseController.selectExistingCourse();
        } else if (command.equals(REOPEN_LAST_COURSE)) { 
            programState.resetCourse();
            courseController.openCourse(LODEPreferences.getInstance().get(LODEPreferences.course));
        } else if (command.equals(REOPEN_LAST_COURSE2)) {
            programState.resetCourse();
            courseController.openCourse(LODEPreferences.getInstance().get(LODEPreferences.course2));
        } else if (command.equals(REOPEN_LAST_COURSE3)) {
            programState.resetCourse();
            courseController.openCourse(LODEPreferences.getInstance().get(LODEPreferences.course3));
        } else if (command.equals(CLOSE_COURSE)) {
            int r = JOptionPane.showConfirmDialog(null, "This will close the current course and lecture. Are you sure you want to continue?", "Warning!", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                programState.resetCourse();
                InspectorWindow.getInstance().update();
            }
        } else if (command.equals(EDIT_COURSE)) {
            if (!programState.isCourseDefined) {
                JOptionPane.showMessageDialog(null, "You must first create or select a course!", "Warning!", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Sorry, this feature has not yet been implemented", "Warning!", JOptionPane.WARNING_MESSAGE);
            }
        // ======================================================= LECTURE =====
        } else if (command.equals(NEW_LECTURE)) {
            if (!programState.isCourseDefined) {
                // cannot create a lecture without a course!
                JOptionPane.showMessageDialog(null, "You must first create or select a course!", "Warning!", JOptionPane.WARNING_MESSAGE);
            } else {
                // if a lecture is already open you will close it!
                // make sure this is what the user wants.
                int r = JOptionPane.OK_OPTION;
                if (programState.isLectureDefined) {
                    r = JOptionPane.showConfirmDialog(null, "This will close the current lecture. Are you sure you want to continue?", "Warning!", JOptionPane.YES_NO_OPTION);
                }
                if (r == JOptionPane.OK_OPTION) {
                    if (lectureController.createNewLecture()) {
                        ActionEvent ae = new ActionEvent(this, 0, ActionController.OPEN_VIDEO_PANEL);
                        this.actionPerformed(ae);
                    }
                }
            }
        } else if (command.equals(OPEN_LECTURE)) {
            // if a lecture is already open you will close it!
            // make sure this is what the user wants.
            int r = JOptionPane.OK_OPTION;
            if (programState.isLectureDefined) {
                r = JOptionPane.showConfirmDialog(null, "This will close the current lecture. Are you sure you want to continue?", "Warning!", JOptionPane.YES_NO_OPTION);
            }
            if (r == JOptionPane.OK_OPTION) {
                if (lectureController.selectExistingLecture()) {
                    if (!programState.getCurrentLecture().hasVideo()) {
                        ActionEvent ae = new ActionEvent(this, 0, ActionController.OPEN_VIDEO_PANEL);
                        this.actionPerformed(ae);
                    }
                }
            }
        } else if (command.equals(CLOSE_LECTURE)) {
            int r = JOptionPane.showConfirmDialog(null, "This will close the current lecture. Are you sure you want to continue?", "Warning!", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                programState.resetLecture();
                //acquisitionWindow.clearInstance();
                InspectorWindow.getInstance().update();
            }
        } else if (command.equals(EDIT_LECTURE)) {
            if (!programState.isLectureDefined) {
                JOptionPane.showMessageDialog(null, "You must first create or select a lecture!", "Warning!", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Sorry, this feature has not yet been implemented", "Warning!", JOptionPane.WARNING_MESSAGE);
            }
        // ========================================= POST-PROCESS AND PUBLISH ==
        } else if (command.equals(POST_PROCESS_ONE)) {
            PostProducerIF pp=controllersManager.getPostProducer();
            pp.convertVideo(programState.getCurrentLecture().getAcquisitionPath()
                    + LODEConstants.FS + LODEConstants.MOVIE_FILE + "0" + LODEConstants.MOVIE_EXTENSION); // TODO - THIS SHOULD BE FIXED!
        } else if (command.equals(POST_PROCESS_COURSE)) {
            controllersManager.getPostProducer().convertAllLecturesInCourse(new File(programState.getCurrentCourse().getFullPath()));
        } else if (command.equals(POST_PROCESS_ALL)) {
            controllersManager.getPostProducer().convertAllLectures();
        } else if (command.equals(POST_PROCESS_ALL_REMOTELY)) {
            controllersManager.getPostProducer().postProcessAllRemotely();
        } else if (command.equals(SEND_LECTURE)) {
            programState.getCurrentLecture().send();
        } else if (command.equals(REBUILD_DISTRIBUTION)) {
            controllersManager.getPostProducer().createDistribution(programState.getCurrentLecture());
        } else if (command.equals(SELECT_WEB_SITE_LOCATION)) {
            courseController.defineCoursePublicationLocation();
        } else if (command.equals(PUBLISH_WEB_SITE)) {
            CoursePublisher ps = controllersManager.getCoursePublisher();
            ps.publish();
        // ========================================= ITUNESU ==
        } else if (command.equals(GENERATE_COURSE_METADATA_4_ITUNESU)) {
            Course course=programState.getCurrentCourse();
            CourseTablePanel ctp=new CourseTablePanel();
            ctp.getCourseData(course);
            ctp.initComponentsAndShow();
        } else if (command.equals(GENERATE_LECTURE_METADATA_4_ITUNESU)) {
            if (lectureController.selectExistingLecture()) {
                Lecture lecture=programState.getCurrentLecture();
                // generate lecture metadata
                LectureTablePanel ltp=new LectureTablePanel();
                ltp.getLectureData(programState.getCurrentLecture());
                ltp.initComponentsAndShow();
            }
       } else if (command.equals(CONVERT_ONE_4_ITUNESU)) {   //Antonio
            if (lectureController.selectExistingLecture()) {
                Lecture lecture=programState.getCurrentLecture();
                // generate video
                controllersManager.getPostProducer().convertVideo4Itunesu(lecture);
                // generate lecture metadata
                LectureTablePanel ltp=new LectureTablePanel();
                ltp.getLectureData(lecture);
                ltp.initComponentsAndShow();
            }
       } else if (command.equals(CONVERT_ONE_4_ACCESSIBILITY)) {   //Alberto
            if (lectureController.selectExistingLecture())
                controllersManager.getPostProducer().makeItAccessible(programState.getCurrentLecture());            /*
                    Lecture l = lectureController.selectExistingLectureToConvert();
            if (l != null) {
                controllersManager.getPostProducer().convertVideo4Itunesu(l);
            }
             *
             */
        // ================================================= INSPECTOR==========
        } else if (command.equals(SHOW_INSPECTOR_WINDOW)) {
        } else if (command.equals(SHOW_ACQUISITION_WINDOW)) {
        } else if (command.equals(SHOW_STATE)) {
            programState.printState();
        } else if (command.equals(SWAP_STATE)) {
            MenuManager.getInstance().swapState();
        }

        MenuManager.getInstance().updateMenuState();
        Messanger.getInstance().giveSuggestion(command);
    }
}
