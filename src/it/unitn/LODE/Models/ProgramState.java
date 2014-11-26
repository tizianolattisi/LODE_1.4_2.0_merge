package it.unitn.LODE.Models;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.utils.Messanger;

/**
 *
 * @author ronchet
 */
public class ProgramState {
  
    
    // ======= COURSE ==========================================================
    private Course currentCourse=null;
    public boolean isCourseDefined=false;
    
    public Course getCurrentCourse() {
        return currentCourse;
    }
    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
        isCourseDefined=true;
    }
    public void resetCourse(){
        currentCourse=null;
        isCourseDefined=false;
        resetLecture();
    }
    // ======= LECTURE =========================================================
    private Lecture currentLecture=null;
    public boolean isLectureDefined=false;
    public boolean currentLectureHasRecording=false;
    public boolean currentLectureHasBeenPostProcessed=false;
    public boolean currentLectureHasSlides=false;

   // ========= VIDEO ==========================================================
    public boolean is_video_pref_open=false;
    public boolean is_video_inited=false;
    public boolean hasRecordingStarted=false;
    public boolean isRecordingPaused=false;
    public boolean canRecord(){
        return (isLectureDefined &&
                (! currentLectureHasRecording) &&
                (! hasRecordingStarted));
    }
    void resetVideo(){
        hasRecordingStarted=false;
        isRecordingPaused=false;
    }
    public void setVideoInited(boolean value) {
        is_video_inited=value;
    }
    
    public Lecture getCurrentLecture() {
        return currentLecture;
    }
    public void setCurrentLecture(Lecture currentLecture) {
        this.currentLecture = currentLecture;
        isLectureDefined=true;
    }
    public void resetLecture(){
        currentLecture=null;
        isLectureDefined=false;
        currentLectureHasRecording=false;
        currentLectureHasBeenPostProcessed=false;
        currentLectureHasSlides=false;
        resetVideo();
    }
    public boolean canPostProcess(){
        return (isLectureDefined && 
                currentLectureHasRecording && 
                (! currentLectureHasBeenPostProcessed));
    }
    
    
    public void printState(){
        Messanger msg=Messanger.getInstance();
        msg.w("=== COURSE ==========",LODEConstants.MSG_DEBUG);
        msg.w("isCourseDefined     : "+isCourseDefined,LODEConstants.MSG_DEBUG);
        if (currentCourse!=null) {
           msg.w("name                : "+currentCourse.getCourseName(),LODEConstants.MSG_DEBUG);
           msg.w("year                : "+currentCourse.getYear(),LODEConstants.MSG_DEBUG);
           msg.w("dir                 : "+currentCourse.getFullPath(),LODEConstants.MSG_DEBUG);

        }
        msg.w("=== LECTURE =========",LODEConstants.MSG_DEBUG);
        msg.w("isLectureDefined    : "+isLectureDefined,LODEConstants.MSG_DEBUG);
        msg.w("has recording       : "+currentLectureHasRecording,LODEConstants.MSG_DEBUG);
        msg.w("has post-processing : "+currentLectureHasBeenPostProcessed,LODEConstants.MSG_DEBUG);
        msg.w("has slides:         : "+currentLectureHasSlides,LODEConstants.MSG_DEBUG);
        if (currentLecture!=null) {
           msg.w("name                : "+currentLecture.getLectureName(),LODEConstants.MSG_DEBUG);
           msg.w("date                : "+currentLecture.getDate(),LODEConstants.MSG_DEBUG);
           msg.w("dir                 : "+currentLecture.getDirName(),LODEConstants.MSG_DEBUG);
           msg.w("acqusistion path    : "+currentLecture.getAcquisitionPath(),LODEConstants.MSG_DEBUG);
           msg.w("course              : "+currentLecture.getCourse().getCourseName(),LODEConstants.MSG_DEBUG);
        }
        msg.w("=== VIDEO ===========",LODEConstants.MSG_DEBUG);
        msg.w("started             : "+hasRecordingStarted,LODEConstants.MSG_DEBUG);
        msg.w("paused              : "+isRecordingPaused,LODEConstants.MSG_DEBUG);
        // is_video_inited
        // is_video_pref_open
    }
    
    // ========== SOLITON PATTERN ==============================================
    private static ProgramState instance=null;
    public static synchronized ProgramState getInstance(){
        if (instance==null) instance=new ProgramState();
        return instance;
    }
    private ProgramState() {
    }
    //==========================================================================


}
