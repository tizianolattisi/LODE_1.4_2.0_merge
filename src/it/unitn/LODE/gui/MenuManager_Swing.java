/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.LODE.gui;

import it.unitn.LODE.Controllers.ActionController;
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.LODE;
import it.unitn.LODE.LODEPreferences;
import it.unitn.LODE.MP.IF.Controllers.VideoControllerIF;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.MP.utils.SystemProps;
import it.unitn.LODE.Models.ProgramState;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author ronchet
 */
public class MenuManager_Swing extends MenuManager {
    ActionController actionController=null;
    
    JMenuBar menu;
  //--------------------------- 
    JMenuItem about;
    JMenuItem preferences;
    JMenuItem quit;
    //---------------------------
    JMenuItem newCourse;
    JMenuItem selectCourse;
    JMenuItem selectLastUsedCourse;
    JMenuItem selectLastUsedCourse2;
    JMenuItem selectLastUsedCourse3;
    JMenuItem closeCourse;
  //---------------------------
    JMenuItem newLecture;
    JMenuItem selectLecture;
    JMenuItem closeLecture;
    JMenuItem checkLecture;
    JMenuItem sendLecture;
//---------------------------
    JMenuItem editCourse;
    JMenuItem editLecture;
//---------------------------
    JMenuItem importJPG;
    JMenuItem importPPT;
    JMenuItem importPPTX;
    JMenuItem importODP;
    JMenuItem importPDF;
    JMenuItem importFake;
    JMenuItem editSlides;
    JMenuItem deleteJPG;
    //---------------------------
    JMenuItem postProcessOne;
    JMenuItem postProcessCourse;
    JMenuItem postProcessAll;
    JMenuItem rebuidDistribution;
    JMenuItem postProcessAllRemotely;
    JMenuItem convertVideo4accessibility;
    //---------------------------
    JMenuItem convertVideo4iTunesU;//Aggiunto da Antonio
    JMenuItem generateItunesuLectureMetadata;
    JMenuItem generateItunesuCourseMetadata;
    //---------------------------
    JMenuItem openPreferences;
    JMenuItem openVideoPanel;
    JMenuItem start;
    JMenuItem pause;
    JMenuItem resume;
    JMenuItem stop;
    JMenuItem resetVideo;
    //---------------------------
    JMenuItem publishWebSite;
    JMenuItem selectWebSiteLocation;
    //---------------------------
    JMenuItem inspector;
    JMenuItem acquisition; 
//---------------------------
    JMenuItem showState=null;

    JMenu recent = new JMenu("Open recent...");


    VideoControllerIF videoController=null;
    ProgramState programState=null;
    // ========== SOLITON PATTERN ==============================================
    static MenuManager_Swing instance=null;
    public static synchronized MenuManager_Swing getInstance(){
        if (instance==null) instance=new MenuManager_Swing();
        return instance;
    }
    private MenuManager_Swing() {
          actionController=ControllersManager.getinstance().getActionController(); 
    }
    public JMenuBar getJMenuBar() {return menu; }

    //==========================================================================
    public JMenuBar createJMenuBar(){
        menu = new JMenuBar();
        /*
        if (! SystemProps.IS_OS_MAC_OSX) {
            JMenu empty_hidden = new JMenu("                                                                                                        ");
            empty_hidden.setEnabled(false);
            menu.add(empty_hidden);
        }
        * 
        */
        // WIN-EXT B
        //======= LODE MENU  - not on MAC ======================================
        if (! SystemProps.IS_OS_MAC_OSX) {
        JMenu lodeMenu = new JMenu("LODE");
            about=new JMenuItem("About LODE...");
            about.addActionListener(actionController);
            about.setActionCommand(ActionController.ABOUT);
            lodeMenu.add(about);

            preferences=new JMenuItem("Preferences");
            preferences.addActionListener(actionController);
            preferences.setActionCommand(ActionController.PREFERENCES);
            //lodeMenu.add(preferences);
            
            quit=new JMenuItem("Quit");   
            quit.addActionListener(actionController);
            quit.setActionCommand(ActionController.QUIT);
            lodeMenu.add(quit);
            
            menu.add(lodeMenu);

        }
        // WIN-EXT E

        //======= FILE MENU ==================================================
        JMenu courseMenu = new JMenu("Course");
            //course.setMnemonic( 'C' );
            newCourse = new JMenuItem("New course...");
            newCourse.addActionListener(actionController);
            newCourse.setActionCommand(ActionController.NEW_COURSE);
            courseMenu.add(newCourse);
            //Menu recent = new JMenu("Open recent...");
            courseMenu.add(recent);
            updateRecentCourses();
            selectCourse = new JMenuItem("Open course...");
            selectCourse.addActionListener(actionController);
            selectCourse.setActionCommand(ActionController.OPEN_COURSE);
            courseMenu.add(selectCourse);

            closeCourse = new JMenuItem("Close course");
            closeCourse.addActionListener(actionController);
            closeCourse.setActionCommand(ActionController.CLOSE_COURSE);
            courseMenu.add(closeCourse);

            menu.add(courseMenu);

            /*
            JMenuItem separator = new JMenuItem("-");
            separator.setEnabled(false);
            courseMenu.add(separator);
             *
             */

            JMenu lectureMenu = new JMenu("Lecture");

            newLecture = new JMenuItem("New lecture...");
            newLecture.addActionListener(actionController);
            newLecture.setActionCommand(ActionController.NEW_LECTURE);
            lectureMenu.add(newLecture);
            
            selectLecture= new JMenuItem("Open lecture...");
            selectLecture.addActionListener(actionController);
            selectLecture.setActionCommand(ActionController.OPEN_LECTURE);
            lectureMenu.add(selectLecture);

            closeLecture = new JMenuItem("Close lecture");
            closeLecture.addActionListener(actionController);
            closeLecture.setActionCommand(ActionController.CLOSE_LECTURE);
            lectureMenu.add(closeLecture);

            checkLecture = new JMenuItem("Check lecture");
            checkLecture.addActionListener(actionController);
            checkLecture.setActionCommand(ActionController.CLOSE_LECTURE);
            checkLecture.setEnabled(false);
            lectureMenu.add(checkLecture);

            sendLecture = new JMenuItem("Send lecture to server");
            sendLecture.addActionListener(actionController);
            sendLecture.setActionCommand(ActionController.CLOSE_LECTURE);
            sendLecture.setEnabled(false);
            lectureMenu.add(sendLecture);
        menu.add(lectureMenu);
        //======= EDIT MENU =================================================

        JMenu edit = new JMenu("Edit");
            
            //lecture.setMnemonic( 'L' );
            editCourse = new JMenuItem("Edit Course");
            editCourse.addActionListener(actionController);
            editCourse.setActionCommand(ActionController.EDIT_COURSE);
            edit.add(editCourse);

            editLecture = new JMenuItem("Edit Lecture");
            editLecture.addActionListener(actionController);
            editLecture.setActionCommand(ActionController.EDIT_LECTURE);
            edit.add(editLecture);

        //menu.add(edit);
        //======= RECORD MENU ==================================================
        JMenu record = new JMenu("Video");
            //course.setMnemonic( 'C' );
        
            openPreferences = new JMenuItem("Preview and set video preferences");
            openPreferences.addActionListener(actionController);
            openPreferences.setActionCommand(ActionController.OPEN_VIDEO_PREFERENCES);
            openPreferences.setEnabled(true);
            //record.add(openPreferences);
            
            openVideoPanel = new JMenuItem("Open video panel");
            openVideoPanel.addActionListener(actionController);
            openVideoPanel.setActionCommand(ActionController.OPEN_VIDEO_PANEL);
            //record.add(openVideoPanel);

            JMenuItem separator2 = new JMenuItem("-");
            separator2.setEnabled(false);
            //record.add(separator2);
            
            start = new JMenuItem("Start recording");
            start.addActionListener(actionController);
            start.setActionCommand(ActionController.START_VIDEO_RECORDING);
            start.setEnabled(false);
            record.add(start);
            
            pause = new JMenuItem("Suspend recording");
            pause.addActionListener(actionController);
            pause.setActionCommand(ActionController.SUSPEND_VIDEO_RECORDING);
            pause.setEnabled(false);
            record.add(pause);

            resume = new JMenuItem("Resume recording");
            resume.addActionListener(actionController);
            resume.setActionCommand(ActionController.RESUME_VIDEO_RECORDING);
            resume.setEnabled(false);            
            record.add(resume);
            
            stop = new JMenuItem("Stop recording");
            stop.addActionListener(actionController);
            stop.setActionCommand(ActionController.STOP_VIDEO_RECORDING);
            stop.setEnabled(false);
            record.add(stop);

            resetVideo = new JMenuItem("Reset camera");
            resetVideo.addActionListener(actionController);
            resetVideo.setActionCommand(ActionController.RESET_VIDEO);
            //record.add(resetVideo);
        menu.add(record);
        
        
        //======= SLIDES MENU ==================================================
        JMenu slideMenu = new JMenu("Slides");

                importPDF = new JMenuItem("Import PDF");
                importPDF.addActionListener(actionController);
                importPDF.setActionCommand(ActionController.IMPORT_PDF);
                importPDF.setEnabled(false);
                slideMenu.add(importPDF);
                
                importPPT = new JMenuItem("Import PPT");
                importPPT.addActionListener(actionController);
                importPPT.setActionCommand(ActionController.IMPORT_PPT);
                slideMenu.add(importPPT);
                
                importPPTX = new JMenuItem("Import PPTX");
                importPPTX.addActionListener(actionController);
                importPPTX.setActionCommand(ActionController.IMPORT_PPTX);
                slideMenu.add(importPPTX);
                
                importODP = new JMenuItem("Import ODP");
                importODP.addActionListener(actionController);
                importODP.setActionCommand(ActionController.IMPORT_ODP);
                importODP.setEnabled(false);
                slideMenu.add(importODP);

                importJPG = new JMenuItem("Import a JPG set");
                importJPG.addActionListener(actionController);
                importJPG.setActionCommand(ActionController.IMPORT_JPG);
                importJPG.setEnabled(false);
                slideMenu.add(importJPG);

                importFake = new JMenuItem("Import placeholder slides");
                importFake.addActionListener(actionController);
                importFake.setActionCommand(ActionController.IMPORT_FAKE_SLIDES);
                importFake.setEnabled(false);
                slideMenu.add(importFake);

                editSlides = new JMenuItem("Edit slides titles");
                editSlides.addActionListener(actionController);
                editSlides.setActionCommand(ActionController.EDIT_SLIDES_TITLES);
                editSlides.setEnabled(false);
                //slideMenu.add(editSlides);
                
                deleteJPG = new JMenuItem("Delete current JPGs");
                deleteJPG.addActionListener(actionController);
                deleteJPG.setActionCommand(ActionController.DELETE_CURRRENT_PDF);
                deleteJPG.setEnabled(false);
                //slideMenu.add(deleteJPG);
            menu.add(slideMenu);
        //======= CONVERT MENU =================================================    
        JMenu convert = new JMenu("Postprocess");
            //convert.setMnemonic('V');
            postProcessOne = new JMenuItem("Convert Current Lecture's Video");
            postProcessOne.addActionListener(actionController);
            postProcessOne.setActionCommand(ActionController.POST_PROCESS_ONE);
            postProcessOne.setEnabled(false);
            convert.add(postProcessOne);
            
            rebuidDistribution = new JMenuItem("Rebuild Current Lecture's Distribution");
            rebuidDistribution.addActionListener(actionController);
            rebuidDistribution.setActionCommand(ActionController.REBUILD_DISTRIBUTION);
            rebuidDistribution.setEnabled(false);
            convert.add(rebuidDistribution);
            
            postProcessCourse = new JMenuItem("Convert ALL Lectures of CURRENT Course");
            postProcessCourse.addActionListener(actionController);
            postProcessCourse.setActionCommand(ActionController.POST_PROCESS_COURSE);
            postProcessCourse.setEnabled(false);
            convert.add(postProcessCourse);

            JMenuItem separator3 = new JMenuItem("-");
            separator3.setEnabled(false);
            convert.add(separator3);

            postProcessAll = new JMenuItem("Convert ALL Lectures of ALL Courses");
            postProcessAll.addActionListener(actionController);
            postProcessAll.setActionCommand(ActionController.POST_PROCESS_ALL);
            postProcessAll.setEnabled(false);
            convert.add(postProcessAll);

            sendLecture = new JMenuItem("Send current lecture to the server");
            sendLecture.addActionListener(actionController);
            sendLecture.setActionCommand(ActionController.SEND_LECTURE);
            sendLecture.setEnabled(false);
            //convert.add(sendLecture);


            postProcessAllRemotely = new JMenuItem("Convert all lectures on the server");
            postProcessAllRemotely.addActionListener(actionController);
            postProcessAllRemotely.setActionCommand(ActionController.POST_PROCESS_ALL_REMOTELY);
            postProcessAllRemotely.setEnabled(true);
            //convert.add(postProcessAllRemotely);
            
            convertVideo4accessibility = new JMenuItem("Convert video of one lecture for Accessibility");
            convertVideo4accessibility.addActionListener(actionController);
            convertVideo4accessibility.setActionCommand(ActionController.CONVERT_ONE_4_ACCESSIBILITY);
            convertVideo4accessibility.setEnabled(true);
            //convert.add(convertVideo4accessibility);

        menu.add(convert);        
        //======= WINDOW MENU ==================================================
        JMenu window = new JMenu("Window");
            //course.setMnemonic( 'C' );
            inspector = new JMenuItem("Show Inspector window");
            inspector.addActionListener(actionController);
            inspector.setActionCommand(ActionController.SHOW_INSPECTOR_WINDOW);
            window.add(inspector);
            
            acquisition = new JMenuItem("Show Acquisition window");
            acquisition.addActionListener(actionController);
            acquisition.setActionCommand(ActionController.SHOW_ACQUISITION_WINDOW);
            window.add(acquisition);
        //menu.add(window);
        //======= PUBLISH MENU =================================================
        JMenu publish = new JMenu("Publish");
        /*
		selectWebSiteLocation = new JMenuItem("Select web site location...");
		selectWebSiteLocation.addActionListener(actionController);
		selectWebSiteLocation.setActionCommand(ActionController.SELECT_WEB_SITE_LOCATION);
		publish.add(selectWebSiteLocation);
	*/
		publishWebSite = new JMenuItem("Publish Web Site");
		publishWebSite.addActionListener(actionController);
		publishWebSite.setActionCommand(ActionController.PUBLISH_WEB_SITE);
		publish.add(publishWebSite);
        menu.add(publish);
        //======= PUBLISH MENU =================================================
        JMenu iTunesU = new JMenu("iTunesU");
            //Aggiunto da Antonio
            convertVideo4iTunesU = new JMenuItem("Convert video of one lecture for iTunes U");
            convertVideo4iTunesU.addActionListener(actionController);
            convertVideo4iTunesU.setActionCommand(ActionController.CONVERT_ONE_4_ITUNESU);
            convertVideo4iTunesU.setEnabled(true);
            iTunesU.add(convertVideo4iTunesU);
            generateItunesuLectureMetadata = new JMenuItem("Generate metadata for a lecture of current course");
            generateItunesuLectureMetadata.addActionListener(actionController);
            generateItunesuLectureMetadata.setActionCommand(ActionController.GENERATE_LECTURE_METADATA_4_ITUNESU);
            generateItunesuLectureMetadata.setEnabled(true);
            iTunesU.add(generateItunesuLectureMetadata);
            generateItunesuCourseMetadata = new JMenuItem("Generate metadata for current course");
            generateItunesuCourseMetadata.addActionListener(actionController);
            generateItunesuCourseMetadata.setActionCommand(ActionController.GENERATE_COURSE_METADATA_4_ITUNESU);
            generateItunesuCourseMetadata.setEnabled(true);
            iTunesU.add(generateItunesuCourseMetadata);

        //ON WINDOWS DISABLE iTunesU    
        //menu.add(iTunesU);

        //======= DEBUG MENU ==================================================
        JMenu debug = new JMenu("Debug");
            //course.setMnemonic( 'C' );
            showState = new JMenuItem("Show state");
            showState.addActionListener(actionController);
            showState.setActionCommand(ActionController.SHOW_STATE);
            debug.add(showState);
  
            //course.setMnemonic( 'C' );
            JMenuItem swapState = new JMenuItem("Swap state");
            swapState.addActionListener(actionController);
            swapState.setActionCommand(ActionController.SWAP_STATE);
            debug.add(swapState);
        if (LODE.TESTING) menu.add(debug);

        return menu;
    }
    public void swapState(){
        showState.setEnabled(! showState.isEnabled());
    }
    public void updateMenuState(){
        if (videoController==null)  videoController=ControllersManager.getinstance().getVideoController();
        boolean isNotRecording=videoController.getCameraState()!=VideoControllerIF.CAMERA_IS_RECORDING;
        if (programState==null) programState=ProgramState.getInstance();
        newCourse.setEnabled(isNotRecording);
        selectCourse.setEnabled(isNotRecording);
        closeCourse.setEnabled(programState.isCourseDefined&&isNotRecording);
        newLecture.setEnabled(programState.isCourseDefined&&isNotRecording);
        selectLecture.setEnabled(isNotRecording);
        closeLecture.setEnabled(programState.isLectureDefined&&isNotRecording);
    //---------------------------
        editLecture.setEnabled(programState.isLectureDefined);
        editCourse.setEnabled(programState.isCourseDefined);
    //---------------------------
        importJPG.setEnabled(programState.isLectureDefined && programState.is_video_inited);
        importPPTX.setEnabled(programState.isLectureDefined && programState.is_video_inited);
        importPPT.setEnabled(programState.isLectureDefined && programState.is_video_inited);
        importPDF.setEnabled(programState.isLectureDefined && programState.is_video_inited);
        importODP.setEnabled(programState.isLectureDefined && programState.is_video_inited);
        importFake.setEnabled(programState.isLectureDefined && programState.is_video_inited);
        deleteJPG.setEnabled(false);
        //---------------------------
        postProcessOne.setEnabled(programState.isLectureDefined &&
                               programState.getCurrentLecture().hasVideo() &&
                               ! programState.getCurrentLecture().hasBeenPostProcessed()); // programState.canPostProcess());
        postProcessAll.setEnabled(isNotRecording); // always true
        postProcessAllRemotely.setEnabled(isNotRecording); // always true
        postProcessCourse.setEnabled(programState.isCourseDefined&&isNotRecording);
        rebuidDistribution.setEnabled(programState.isLectureDefined &&
                               programState.getCurrentLecture().hasBeenPostProcessed());
        /*
         sendLecture.setEnabled(programState.isLectureDefined &&
                               programState.getCurrentLecture().hasVideo() );
         * 
         */
        //---------------------------
        openPreferences.setEnabled(
                (videoController.getCameraState()==VideoControllerIF.CAMERA_IS_UNKNOWN) ||
                (videoController.getCameraState()==VideoControllerIF.CAMERA_HAS_ENDED)
                );
        openVideoPanel.setEnabled(programState.isLectureDefined &&
                                 (!programState.is_video_pref_open) &&
                                 (videoController.getCameraState()==VideoControllerIF.CAMERA_IS_UNKNOWN
                                 || videoController.getCameraState()==VideoControllerIF.CAMERA_HAS_ENDED));// && (!ProgramState.is_video_inited));
        start.setEnabled(programState.isLectureDefined &&
                         ! programState.getCurrentLecture().hasVideo() &&
                         videoController.getCameraState()==VideoControllerIF.CAMERA_IS_INITED);
        pause.setEnabled(!isNotRecording);
        resume.setEnabled(videoController.getCameraState()==VideoControllerIF.CAMERA_IS_PAUSED);
        stop.setEnabled((!isNotRecording) ||
                        (videoController.getCameraState()==VideoControllerIF.CAMERA_IS_PAUSED));
    //---------------------------
        inspector.setEnabled(true);
        acquisition.setEnabled(true); 
    //---------------------------
        showState.setEnabled(true);
        //---------------------------
        convertVideo4iTunesU.setEnabled(programState.isCourseDefined&&isNotRecording);
	generateItunesuLectureMetadata.setEnabled(programState.isCourseDefined&&isNotRecording);
        generateItunesuCourseMetadata.setEnabled(programState.isCourseDefined&&isNotRecording);
	//---------------------------
        publishWebSite.setEnabled(programState.isCourseDefined);
        //selectWebSiteLocation.setEnabled(programState.isCourseDefined);
        //---------------------------
        /*
        if (videoController.getCameraState()==VideoControllerIF.CAMERA_HAS_ENDED) {
            start.setEnabled(false);
            newLecture.setEnabled(false);
            newCourse.setEnabled(false);
            acquisition.setEnabled(false);
        }
         *
         */
    }
    public void updateRecentCourses() { //Menu recent) {
        recent.removeAll();
        String lastCoursePath=LODEPreferences.getInstance().get(LODEPreferences.course);
            if (lastCoursePath!=null|| !lastCoursePath.equals("")) {
                String courseName=lastCoursePath.substring(1+lastCoursePath.lastIndexOf(LODEConstants.FS));
                if (courseName!=null&&!courseName.equals("")) {
                    selectLastUsedCourse = new JMenuItem(courseName);
                    selectLastUsedCourse.addActionListener(actionController);
                    selectLastUsedCourse.setActionCommand(ActionController.REOPEN_LAST_COURSE);
                    recent.add(selectLastUsedCourse);
                }
            }
            String lastCoursePath2=LODEPreferences.getInstance().get(LODEPreferences.course2);
            if (lastCoursePath2!=null|| !lastCoursePath2.equals("")) {
                String courseName=lastCoursePath2.substring(1+lastCoursePath2.lastIndexOf(LODEConstants.FS));
                if (courseName!=null&&!courseName.equals("")) {
                    selectLastUsedCourse2 = new JMenuItem(courseName);
                    selectLastUsedCourse2.addActionListener(actionController);
                    selectLastUsedCourse2.setActionCommand(ActionController.REOPEN_LAST_COURSE2);
                    recent.add(selectLastUsedCourse2);
                }
            }
            String lastCoursePath3=LODEPreferences.getInstance().get(LODEPreferences.course3);
            if (lastCoursePath3!=null|| !lastCoursePath3.equals("")) {
                String courseName=lastCoursePath3.substring(1+lastCoursePath3.lastIndexOf(LODEConstants.FS));
                if (courseName!=null&&!courseName.equals("")) {
                    selectLastUsedCourse3 = new JMenuItem(courseName);
                    selectLastUsedCourse3.addActionListener(actionController);
                    selectLastUsedCourse3.setActionCommand(ActionController.REOPEN_LAST_COURSE3);
                    recent.add(selectLastUsedCourse3);
                }
            }
    }    
}
