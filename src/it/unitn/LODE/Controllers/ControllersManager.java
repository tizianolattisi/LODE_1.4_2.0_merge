package it.unitn.LODE.Controllers;

import it.unitn.LODE.MP.factories.VideoControllerFactory;
import it.unitn.LODE.MP.IF.Controllers.VideoControllerIF;
import it.unitn.LODE.LODEParameters;
import it.unitn.LODE.LODEPreferences;
import it.unitn.LODE.Models.ProgramState;
import it.unitn.LODE.gui.AcquisitionWindow;
import it.unitn.LODE.gui.InspectorWindow;
import it.unitn.LODE.gui.MenuManager;
import it.unitn.LODE.gui.ProcessLoggerWindow;
import it.unitn.LODE.services.PostProducerIF;
import it.unitn.LODE.services.SlideImporter;
import it.unitn.LODE.MP.utils.Clock;
import it.unitn.LODE.utils.FileSystemManager;
import it.unitn.LODE.utils.Messanger;
import it.unitn.lodeWeb.gui.create.ProgressJDialog;
import it.unitn.lodeWeb.noGui.CoursePublisher;

/**
 * This is a central class that manages all soliton accesses.
 * It helps making sure that the dependencies are respected
 * @author ronchet
 */
public class ControllersManager {
    
    private static ControllersManager controllersManager=null;
    
    public static ControllersManager getinstance() {
        if (controllersManager==null) controllersManager=new ControllersManager();
        return controllersManager;
    }
    private Messanger messanger=null;
    private ActionController actionController=null;
    private PostProducerIF postProducer=null;
    private FileSystemManager fileSystemManager=null;
    private ProcessLoggerWindow processLoggerWindow=null;
    private CoursePublisher coursePublisher=null;
    private ProgressJDialog progressJDialog=null;
    private VideoControllerIF videoController=null;
    private AcquisitionWindow acquisitionWindow=null;

    private ControllersManager() {
        fileSystemManager=FileSystemManager.getInstance();
    }
    
    public void instatiatePrimaryManagers(){

        LODEPreferences.getInstance();
        LODEParameters.getInstance();
        ProgramState.getInstance();
        CourseController.getInstance();
        LectureController.getInstance();

        actionController=ActionController.getInstance();
        MenuManager.getInstance(); // ONLY FOR MAC!, on other platforms should be JMenuManager
        InspectorWindow.getInstance();
        processLoggerWindow=ProcessLoggerWindow.getInstance();
        videoController=VideoControllerFactory.getInstance();
        //MacVideoController.getInstance();
        messanger=Messanger.getInstance();
    }
    public void instatiateSecondaryManagers(){
        SlideImporter.getInstance();
        coursePublisher=CoursePublisher.getInstance();
        postProducer=PostProducerIF.getInstance(); 
        MenuManager.getInstance(); // ONLY FOR MAC!, on other platforms should be JMenuManager
        Clock.getInstance();
        progressJDialog=ProgressJDialog.getInstance();
    }
    public void checkManagers(){
       this.getActionController();
       this.getMessanger();
       this.getPostProducer();
       this.getFileSystemManager();
       this.getProcessLoggerWindow();
       this.getCoursePublisher();
       this.getProgressJDialog();
       this.getVideoController();
       System.out.println("===> MANAGER CHECK COMPLETED.");
    }
    public void setAcquistionWindow(AcquisitionWindow aw){
        acquisitionWindow=aw;
    }
    public AcquisitionWindow getAcquisitionWindow() {
        if (acquisitionWindow==null)
            System.err.println("Invalid invokation of getAcquisitionWindow");
        return acquisitionWindow;
    }
    public void unsetAcquisitionWindow() {
        acquisitionWindow=null;
    }
    //============ getters =====================================================
    public ActionController getActionController(){
        if (actionController==null)
            System.err.println("Invalid invokation of getActionController");
        return actionController;
    }
    public Messanger getMessanger(){
        if (messanger==null)
            System.err.println("Invalid invokation of getMessanger");
        return messanger;
    }
    public PostProducerIF getPostProducer(){
        if (postProducer==null)
            System.err.println("Invalid invokation of getPostProducer");
        return postProducer;
    }

    public FileSystemManager getFileSystemManager() {
        if (fileSystemManager==null)
            System.err.println("Invalid invokation of getFileSystemManager");
        return fileSystemManager;
    }

    public ProcessLoggerWindow getProcessLoggerWindow() {
        if (processLoggerWindow==null)
            System.err.println("Invalid invokation of getProcessLoggerWindow");
        return processLoggerWindow;
    }

    CoursePublisher getCoursePublisher() {
        if (coursePublisher==null)
            System.err.println("Invalid invokation of getProcessLoggerWindow");
        return coursePublisher;
    }

    public ProgressJDialog getProgressJDialog() {
        if (progressJDialog==null)
            System.err.println("Invalid invokation of getProcessLoggerWindow");
        return progressJDialog;
    }

    public VideoControllerIF getVideoController() {
        if (videoController==null)
            System.err.println("Invalid invokation of getVideoController");
        return videoController;
    }
}
