/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.utils;

import it.unitn.LODE.gui.InspectorWindow;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Controllers.ActionController;
import it.unitn.LODE.MP.factories.VideoControllerFactory;
import it.unitn.LODE.MP.IF.Controllers.VideoControllerIF;
import it.unitn.LODE.Models.ProgramState;
import it.unitn.LODE.Models.Lecture;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author ronchet
 */
public class Messanger {

    PrintWriter logger=null;
    //FileWriter logger=null;
    InspectorWindow iw=null;
    ProgramState programState=null;
    VideoControllerIF videoController=null;
    // ========== SOLITON PATTERN ==============================================
    static Messanger instance=null;
    public static synchronized Messanger getInstance(){
        if (instance==null) instance=new Messanger();
        return instance;
    }
    private Messanger() {
        iw=InspectorWindow.getInstance();
        programState=ProgramState.getInstance();
        videoController=VideoControllerFactory.getInstance();
        try {
            //logger = new FileWriter(LODEConstants.LOG_FILE, true); // open for append
            //logger = new PrintWriter(new FileOutputStream(LODEConstants.LOG_FILE));
            logger = new PrintWriter(new FileWriter(LODEConstants.LOG_FILE, true),true);
        } catch (IOException ex) {
            w("cannot open log file\n"+ex.getMessage(),LODEConstants.MSG_ERROR);
        }
    }
    
    //==========================================================================
    public final void w(String msg,int type){
       iw.writeMessage(msg,type);
    }
    public final void w(String msg){
        this.w(msg,LODEConstants.MSG_GENERIC);
    }

    public final void log(String s) {
        //try {
            logger.println(s+"\n"); //appends the string to the file
        /*} catch (IOException ex) {
            w("Cannot write on log file\n"+ex.getMessage()+"\n"+s+"\n",LODEConstants.MSG_ERROR);
        }*/
    }
    public final PrintWriter getLogger(){
        return logger;
    }
    public final void closeLogFile() {
        //try {
            logger.flush();
            logger.close();
        /*} catch (IOException ex) {
            ex.printStackTrace(); //we do not write this on the log!
        }*/
    }
    public final void giveSuggestion(String command){
        if (! programState.isLectureDefined) {
           if (! programState.isCourseDefined)
                w("To start, you should open or create a course - or open an existing lecture in a course",LODEConstants.MSG_SUGGESTION);
           else                
                w("You should open or create a lecture!",LODEConstants.MSG_SUGGESTION);
        } else {
           if ((command.equals(ActionController.START_VIDEO_RECORDING) || 
               (command.equals(ActionController.RESUME_VIDEO_RECORDING)) )) {
               w("Please synchronize slides with the speaker\n"+
                       "You can also stop or suspend recording",LODEConstants.MSG_SUGGESTION);
           } else if (command.equals(ActionController.SUSPEND_VIDEO_RECORDING)) {
               w("You can now resume or suspend recording",LODEConstants.MSG_SUGGESTION);
           } else {
               // a lecture exists!
               Lecture l=programState.getCurrentLecture();
               if (!l.hasSlides() && !l.hasVideo())
                    w("You should now probably import some slides?",LODEConstants.MSG_SUGGESTION);
               else if (!l.hasVideo()) {
                   if (videoController.getCameraState()==VideoControllerIF.CAMERA_IS_PREVIEWING)
                     w("I guess you are ready to start recording!",LODEConstants.MSG_SUGGESTION);
                   else if (videoController.getCameraState()==VideoControllerIF.CAMERA_IS_RECORDING)
                     w("Please synchronize slides with the speaker\n"+
                       "You can also stop or suspend recording",LODEConstants.MSG_SUGGESTION);
                   else if (videoController.getCameraState()==VideoControllerIF.CAMERA_IS_PAUSED)
                     w("You can resume recording, or you can stop.",LODEConstants.MSG_SUGGESTION);
                   }
               else if (!l.hasBeenPostProcessed())
                    w("You can now postprocess the lecture (it may take a while!)" +
                      " or you can close the lecture, or quit.",LODEConstants.MSG_SUGGESTION);
               else
                    w("I guess you're done. You can now close the lecture, or quit ",LODEConstants.MSG_SUGGESTION);
            }
        }
    }
}
