
package it.unitn.LODE.gui;
/**
 * AWT version, to be used on Macintosh
 * Due to some bugs, working with Swing does not enable Menus in the mac style
 * even when using         
 * System.setProperty("apple.laf.useScreenMenuBar", "true");
 *
 * @author ronchet
 */
import it.unitn.LODE.Controllers.ActionController;
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.LODE;
import it.unitn.LODE.LODEPreferences;
import it.unitn.LODE.MP.IF.Controllers.VideoControllerIF;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.MP.utils.SystemProps;
import it.unitn.LODE.Models.ProgramState;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
public class MenuManager_AWT extends MenuManager{
    
    ActionController actionController=null;
    
    MenuBar menu;
  //---------------------------  
    MenuItem newCourse;
    MenuItem selectCourse;
    MenuItem selectLastUsedCourse;
    MenuItem selectLastUsedCourse2;
    MenuItem selectLastUsedCourse3;
    MenuItem closeCourse;
  //---------------------------
    MenuItem newLecture;
    MenuItem selectLecture;
    MenuItem closeLecture;
    MenuItem checkLecture;
    MenuItem sendLecture;
//---------------------------
    MenuItem editCourse;
    MenuItem editLecture;
//---------------------------
    MenuItem importJPG;
    MenuItem importPPT;
    MenuItem importPPTX;
    MenuItem importODP;
    MenuItem importPDF;
    MenuItem importFake;
    MenuItem editSlides;
    MenuItem deleteJPG;
    //---------------------------
    MenuItem postProcessOne;
    MenuItem postProcessCourse;
    MenuItem postProcessAll;
    MenuItem rebuidDistribution;
    MenuItem postProcessAllRemotely;
    MenuItem convertVideo4accessibility;
    //---------------------------
    MenuItem convertVideo4iTunesU;//Aggiunto da Antonio
    MenuItem generateItunesuLectureMetadata;
    MenuItem generateItunesuCourseMetadata;
    //---------------------------
    MenuItem openPreferences;
    MenuItem openVideoPanel;
    MenuItem start;
    MenuItem pause;
    MenuItem resume;
    MenuItem stop;
    MenuItem resetVideo;
    //---------------------------
    MenuItem publishWebSite;
    MenuItem selectWebSiteLocation;
    //---------------------------
    MenuItem inspector;
    MenuItem acquisition; 
//---------------------------
    MenuItem showState=null;

    Menu recent = new Menu("Open recent...");


    VideoControllerIF videoController=null;
    ProgramState programState=null;
    // ========== SOLITON PATTERN ==============================================
    static MenuManager_AWT instance=null;
    public static synchronized MenuManager_AWT getInstance(){
        if (instance==null) instance=new MenuManager_AWT();
        return instance;
    }
    private MenuManager_AWT() {
          actionController=ControllersManager.getinstance().getActionController(); 
    }
    public MenuBar getMenuBar() {return menu; }
    //==========================================================================
    public MenuBar createMenuBar(){
        menu = new MenuBar();
        
        if (! SystemProps.IS_OS_MAC_OSX) {
            Menu empty_hidden = new Menu("                                                                                                        ");
            empty_hidden.setEnabled(false);
            menu.add(empty_hidden);
        }
        //======= FILE MENU ==================================================
        Menu courseMenu = new Menu("Course");
            //course.setMnemonic( 'C' );
            newCourse = new MenuItem("New course...");
            newCourse.addActionListener(actionController);
            newCourse.setActionCommand(ActionController.NEW_COURSE);
            courseMenu.add(newCourse);
            //Menu recent = new Menu("Open recent...");
            courseMenu.add(recent);
            updateRecentCourses();
            selectCourse = new MenuItem("Open course...");
            selectCourse.addActionListener(actionController);
            selectCourse.setActionCommand(ActionController.OPEN_COURSE);
            courseMenu.add(selectCourse);

            closeCourse = new MenuItem("Close course");
            closeCourse.addActionListener(actionController);
            closeCourse.setActionCommand(ActionController.CLOSE_COURSE);
            courseMenu.add(closeCourse);

            menu.add(courseMenu);

            /*
            MenuItem separator = new MenuItem("-");
            separator.setEnabled(false);
            courseMenu.add(separator);
             *
             */

            Menu lectureMenu = new Menu("Lecture");

            newLecture = new MenuItem("New lecture...");
            newLecture.addActionListener(actionController);
            newLecture.setActionCommand(ActionController.NEW_LECTURE);
            lectureMenu.add(newLecture);
            
            selectLecture= new MenuItem("Open lecture...");
            selectLecture.addActionListener(actionController);
            selectLecture.setActionCommand(ActionController.OPEN_LECTURE);
            lectureMenu.add(selectLecture);

            closeLecture = new MenuItem("Close lecture");
            closeLecture.addActionListener(actionController);
            closeLecture.setActionCommand(ActionController.CLOSE_LECTURE);
            lectureMenu.add(closeLecture);

            checkLecture = new MenuItem("Check lecture");
            checkLecture.addActionListener(actionController);
            checkLecture.setActionCommand(ActionController.CLOSE_LECTURE);
            checkLecture.setEnabled(false);
            lectureMenu.add(checkLecture);

            sendLecture = new MenuItem("Send lecture to server");
            sendLecture.addActionListener(actionController);
            sendLecture.setActionCommand(ActionController.CLOSE_LECTURE);
            sendLecture.setEnabled(false);
            lectureMenu.add(sendLecture);
        menu.add(lectureMenu);
        //======= EDIT MENU =================================================

        Menu edit = new Menu("Edit");
            
            //lecture.setMnemonic( 'L' );
            editCourse = new MenuItem("Edit Course");
            editCourse.addActionListener(actionController);
            editCourse.setActionCommand(ActionController.EDIT_COURSE);
            edit.add(editCourse);

            editLecture = new MenuItem("Edit Lecture");
            editLecture.addActionListener(actionController);
            editLecture.setActionCommand(ActionController.EDIT_LECTURE);
            edit.add(editLecture);

        //menu.add(edit);
        //======= RECORD MENU ==================================================
        Menu record = new Menu("Video");
            //course.setMnemonic( 'C' );
        
            openPreferences = new MenuItem("Preview and set video preferences");
            openPreferences.addActionListener(actionController);
            openPreferences.setActionCommand(ActionController.OPEN_VIDEO_PREFERENCES);
            openPreferences.setEnabled(true);
            //record.add(openPreferences);
            
            openVideoPanel = new MenuItem("Open video panel");
            openVideoPanel.addActionListener(actionController);
            openVideoPanel.setActionCommand(ActionController.OPEN_VIDEO_PANEL);
            //record.add(openVideoPanel);

            MenuItem separator2 = new MenuItem("-");
            separator2.setEnabled(false);
            //record.add(separator2);
            
            start = new MenuItem("Start recording");
            start.addActionListener(actionController);
            start.setActionCommand(ActionController.START_VIDEO_RECORDING);
            start.setEnabled(false);
            record.add(start);
            
            pause = new MenuItem("Suspend recording");
            pause.addActionListener(actionController);
            pause.setActionCommand(ActionController.SUSPEND_VIDEO_RECORDING);
            pause.setEnabled(false);
            record.add(pause);

            resume = new MenuItem("Resume recording");
            resume.addActionListener(actionController);
            resume.setActionCommand(ActionController.RESUME_VIDEO_RECORDING);
            resume.setEnabled(false);            
            record.add(resume);
            
            stop = new MenuItem("Stop recording");
            stop.addActionListener(actionController);
            stop.setActionCommand(ActionController.STOP_VIDEO_RECORDING);
            stop.setEnabled(false);
            record.add(stop);

            resetVideo = new MenuItem("Reset camera");
            resetVideo.addActionListener(actionController);
            resetVideo.setActionCommand(ActionController.RESET_VIDEO);
            //record.add(resetVideo);
        menu.add(record);
        
        
        //======= SLIDES MENU ==================================================
        Menu slideMenu = new Menu("Slides");

                importPDF = new MenuItem("Import PDF");
                importPDF.addActionListener(actionController);
                importPDF.setActionCommand(ActionController.IMPORT_PDF);
                importPDF.setEnabled(false);
                slideMenu.add(importPDF);
                
                importPPT = new MenuItem("Import PPT");
                importPPT.addActionListener(actionController);
                importPPT.setActionCommand(ActionController.IMPORT_PPT);
                slideMenu.add(importPPT);
                
                importPPTX = new MenuItem("Import PPTX");
                importPPTX.addActionListener(actionController);
                importPPTX.setActionCommand(ActionController.IMPORT_PPTX);
                slideMenu.add(importPPTX);
                
                importODP = new MenuItem("Import ODP");
                importODP.addActionListener(actionController);
                importODP.setActionCommand(ActionController.IMPORT_ODP);
                importODP.setEnabled(false);
                slideMenu.add(importODP);

                importJPG = new MenuItem("Import a JPG set");
                importJPG.addActionListener(actionController);
                importJPG.setActionCommand(ActionController.IMPORT_JPG);
                importJPG.setEnabled(false);
                slideMenu.add(importJPG);

                importFake = new MenuItem("Import placeholder slides");
                importFake.addActionListener(actionController);
                importFake.setActionCommand(ActionController.IMPORT_FAKE_SLIDES);
                importFake.setEnabled(false);
                slideMenu.add(importFake);

                editSlides = new MenuItem("Edit slides titles");
                editSlides.addActionListener(actionController);
                editSlides.setActionCommand(ActionController.EDIT_SLIDES_TITLES);
                editSlides.setEnabled(false);
                //slideMenu.add(editSlides);
                
                deleteJPG = new MenuItem("Delete current JPGs");
                deleteJPG.addActionListener(actionController);
                deleteJPG.setActionCommand(ActionController.DELETE_CURRRENT_PDF);
                deleteJPG.setEnabled(false);
                //slideMenu.add(deleteJPG);
            menu.add(slideMenu);
        //======= CONVERT MENU =================================================    
        Menu convert = new Menu("Postprocess");
            //convert.setMnemonic('V');
            postProcessOne = new MenuItem("Convert Current Lecture's Video");
            postProcessOne.addActionListener(actionController);
            postProcessOne.setActionCommand(ActionController.POST_PROCESS_ONE);
            postProcessOne.setEnabled(false);
            convert.add(postProcessOne);
            
            rebuidDistribution = new MenuItem("Rebuild Current Lecture's Distribution");
            rebuidDistribution.addActionListener(actionController);
            rebuidDistribution.setActionCommand(ActionController.REBUILD_DISTRIBUTION);
            rebuidDistribution.setEnabled(false);
            convert.add(rebuidDistribution);
            
            postProcessCourse = new MenuItem("Convert ALL Lectures of CURRENT Course");
            postProcessCourse.addActionListener(actionController);
            postProcessCourse.setActionCommand(ActionController.POST_PROCESS_COURSE);
            postProcessCourse.setEnabled(false);
            convert.add(postProcessCourse);

            MenuItem separator3 = new MenuItem("-");
            separator3.setEnabled(false);
            convert.add(separator3);

            postProcessAll = new MenuItem("Convert ALL Lectures of ALL Courses");
            postProcessAll.addActionListener(actionController);
            postProcessAll.setActionCommand(ActionController.POST_PROCESS_ALL);
            postProcessAll.setEnabled(false);
            convert.add(postProcessAll);

            sendLecture = new MenuItem("Send current lecture to the server");
            sendLecture.addActionListener(actionController);
            sendLecture.setActionCommand(ActionController.SEND_LECTURE);
            sendLecture.setEnabled(false);
            //convert.add(sendLecture);


            postProcessAllRemotely = new MenuItem("Convert all lectures on the server");
            postProcessAllRemotely.addActionListener(actionController);
            postProcessAllRemotely.setActionCommand(ActionController.POST_PROCESS_ALL_REMOTELY);
            postProcessAllRemotely.setEnabled(true);
            //convert.add(postProcessAllRemotely);
            
            convertVideo4accessibility = new MenuItem("Convert video of one lecture for Accessibility");
            convertVideo4accessibility.addActionListener(actionController);
            convertVideo4accessibility.setActionCommand(ActionController.CONVERT_ONE_4_ACCESSIBILITY);
            convertVideo4accessibility.setEnabled(true);
            //convert.add(convertVideo4accessibility);

        menu.add(convert);        
        //======= WINDOW MENU ==================================================
        Menu window = new Menu("Window");
            //course.setMnemonic( 'C' );
            inspector = new MenuItem("Show Inspector window");
            inspector.addActionListener(actionController);
            inspector.setActionCommand(ActionController.SHOW_INSPECTOR_WINDOW);
            window.add(inspector);
            
            acquisition = new MenuItem("Show Acquisition window");
            acquisition.addActionListener(actionController);
            acquisition.setActionCommand(ActionController.SHOW_ACQUISITION_WINDOW);
            window.add(acquisition);
        //menu.add(window);
        //======= PUBLISH MENU =================================================
        Menu publish = new Menu("Publish");
        /*
		selectWebSiteLocation = new MenuItem("Select web site location...");
		selectWebSiteLocation.addActionListener(actionController);
		selectWebSiteLocation.setActionCommand(ActionController.SELECT_WEB_SITE_LOCATION);
		publish.add(selectWebSiteLocation);
	*/
		publishWebSite = new MenuItem("Publish Web Site");
		publishWebSite.addActionListener(actionController);
		publishWebSite.setActionCommand(ActionController.PUBLISH_WEB_SITE);
		publish.add(publishWebSite);
        menu.add(publish);
        //======= PUBLISH MENU =================================================
        Menu iTunesU = new Menu("iTunesU");
            //Aggiunto da Antonio
            convertVideo4iTunesU = new MenuItem("Convert video of one lecture for iTunes U");
            convertVideo4iTunesU.addActionListener(actionController);
            convertVideo4iTunesU.setActionCommand(ActionController.CONVERT_ONE_4_ITUNESU);
            convertVideo4iTunesU.setEnabled(true);
            iTunesU.add(convertVideo4iTunesU);
            generateItunesuLectureMetadata = new MenuItem("Generate metadata for a lecture of current course");
            generateItunesuLectureMetadata.addActionListener(actionController);
            generateItunesuLectureMetadata.setActionCommand(ActionController.GENERATE_LECTURE_METADATA_4_ITUNESU);
            generateItunesuLectureMetadata.setEnabled(true);
            iTunesU.add(generateItunesuLectureMetadata);
            generateItunesuCourseMetadata = new MenuItem("Generate metadata for current course");
            generateItunesuCourseMetadata.addActionListener(actionController);
            generateItunesuCourseMetadata.setActionCommand(ActionController.GENERATE_COURSE_METADATA_4_ITUNESU);
            generateItunesuCourseMetadata.setEnabled(true);
            iTunesU.add(generateItunesuCourseMetadata);


        menu.add(iTunesU);

        //======= DEBUG MENU ==================================================
        Menu debug = new Menu("Debug");
            //course.setMnemonic( 'C' );
            showState = new MenuItem("Show state");
            showState.addActionListener(actionController);
            showState.setActionCommand(ActionController.SHOW_STATE);
            debug.add(showState);
  
            //course.setMnemonic( 'C' );
            MenuItem swapState = new MenuItem("Swap state");
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
                    selectLastUsedCourse = new MenuItem(courseName);
                    selectLastUsedCourse.addActionListener(actionController);
                    selectLastUsedCourse.setActionCommand(ActionController.REOPEN_LAST_COURSE);
                    recent.add(selectLastUsedCourse);
                }
            }
            String lastCoursePath2=LODEPreferences.getInstance().get(LODEPreferences.course2);
            if (lastCoursePath2!=null|| !lastCoursePath2.equals("")) {
                String courseName=lastCoursePath2.substring(1+lastCoursePath2.lastIndexOf(LODEConstants.FS));
                if (courseName!=null&&!courseName.equals("")) {
                    selectLastUsedCourse2 = new MenuItem(courseName);
                    selectLastUsedCourse2.addActionListener(actionController);
                    selectLastUsedCourse2.setActionCommand(ActionController.REOPEN_LAST_COURSE2);
                    recent.add(selectLastUsedCourse2);
                }
            }
            String lastCoursePath3=LODEPreferences.getInstance().get(LODEPreferences.course3);
            if (lastCoursePath3!=null|| !lastCoursePath3.equals("")) {
                String courseName=lastCoursePath3.substring(1+lastCoursePath3.lastIndexOf(LODEConstants.FS));
                if (courseName!=null&&!courseName.equals("")) {
                    selectLastUsedCourse3 = new MenuItem(courseName);
                    selectLastUsedCourse3.addActionListener(actionController);
                    selectLastUsedCourse3.setActionCommand(ActionController.REOPEN_LAST_COURSE3);
                    recent.add(selectLastUsedCourse3);
                }
            }
    }
}
