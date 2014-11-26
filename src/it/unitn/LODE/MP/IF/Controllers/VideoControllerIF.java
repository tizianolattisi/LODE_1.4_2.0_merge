package it.unitn.LODE.MP.IF.Controllers;
/*
 * VideoControllerIF.java
 * 
 * Created on 30-nov-2007, 21.21.39
 * @author ronchet
 */

import it.unitn.LODE.MP.IF.gui.SoundPanelIF;
import java.awt.Component;
import javax.swing.JPanel;

public interface VideoControllerIF {//implements PropertyChangeListener{

    //==========================================================================

    abstract public boolean isRecording();

    abstract public boolean isPreviewing();

    abstract public boolean isPausing();

    public abstract String getVideoDuration();

    public abstract int getTime();
    // =========== MAIN VIDEO METHODS ========================================== 

    /** Suspend/resume video recording
     */
    public abstract void togglePause();

    /** Stops video recording
     */
    public abstract void stop();

    //lancia il metodo per caricare il flusso proveniente dalla videocamera
    public abstract void startPreview(JPanel jPVideo, SoundPanelIF audiopanel);
    public abstract void stopPreview();

    /** Starts video recording
     * @param jPVideo unused!     
     * @param graph_path unused!
     */
    public abstract void record(JPanel jPVideo, String graph_path);

    /** Closes and frees the videoChannel
     * @param jPVideo unused!     
     */
    public abstract void disposeVideoChannel(JPanel jPVideo);

    /** Initializes the videoChannel and returns an AWT Component where the video can be previewd
     * @return the AWT component where the video can be viewed     
     */
    public abstract Component getVideoAsGraphicComponent();

    /** resets the state before initing the videoGrabberComponent
     */
    //public abstract void reset();

    /** opens the option panel
     */
    public abstract void showOptionPanels();

    //public abstract void resetVideo();
    
    public abstract void startAudioLevelMonitor(SoundPanelIF audioLevelPanel);
    
    public abstract void stopAudioLevelMonitor();

    public abstract void cleanup();
    //metodo implementato per poter utilizzare il metodo createDSFiltergraph che richiede come ultimo parametro
    //una variabile di tipo PropertyChangeListener
    /*public void propertyChange(java.beans.PropertyChangeEvent pe) {
    // this method must be implemented by a platform-specific subclass
    throw new RuntimeException("Unimplemented method propertyChange in VideoControllerIF");
    }*/
    //==========================================================================
    // Keep a visible CameraState
    public static final int CAMERA_IS_UNKNOWN=0;
    public static final int CAMERA_IS_INITED=1;
    public static final int CAMERA_IS_PREVIEWING=2;
    public static final int CAMERA_IS_RECORDING=3;
    public static final int CAMERA_IS_PAUSED=4;
    public static final int CAMERA_IS_IDLE=5;
    public static final int CAMERA_HAS_ENDED=6;

    public int getCameraState();
    public  void setCameraState(int programState);

}
