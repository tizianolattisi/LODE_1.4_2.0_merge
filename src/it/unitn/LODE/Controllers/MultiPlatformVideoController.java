/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.LODE.Controllers;

import it.unitn.LODE.LODEParameters;
import it.unitn.LODE.MAC.Video.MacVideoController;
import it.unitn.LODE.MP.IF.Controllers.VideoControllerIF;
import it.unitn.LODE.MP.IF.ServiceProviderIF;
import it.unitn.LODE.MP.IF.gui.SoundPanelIF;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.MP.factories.ServiceProviderFactory;
import it.unitn.LODE.MP.utils.Util;
import it.unitn.LODEvideo.recorder.Recorder;
import it.unitn.LODEvideo.recorder.video.CamPreviewer;
import java.awt.Color;
import java.awt.Component;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ronchet
 */
public class MultiPlatformVideoController implements VideoControllerIF{
    //private ProgramState programState;
    private JFrame videoChoiceFrame;
    CamPreviewer camPreviewer;
    private Thread audioMonitor;
    boolean isCheckingVolume = false;
    private boolean stopAudioMonitor = false;
    private long startingTime = 0;
    private long startPausingTime = 0;
    private long elapsedTime = 0;
    private boolean isMUXed=false;
    private String outputFilePath=null;
    // ========== SOLITON PATTERN ==============================================
    /*
    static MultiPlatformVideoController subclass_instance = null;
    public static synchronized MultiPlatformVideoController getInstance() {
        if (subclass_instance == null) {
            subclass_instance = new MultiPlatformVideoController();
        }
        return subclass_instance;
    }
    * 
    */
    private static int instanceCount=0;
    public MultiPlatformVideoController() {
        if (++instanceCount>1) throw new RuntimeException("Attempt to create multiple instances of MacLODEVideoController");
        Recorder.init();
        //programState = ProgramState.getInstance();
        setCameraState(VideoControllerIF.CAMERA_IS_UNKNOWN);
    }
    //========== STATE CHECKERS ================================================
    public boolean isIdle() {
        return getCameraState() == CAMERA_IS_IDLE;
    }

    public boolean isUnknown() {
        return getCameraState() == CAMERA_IS_UNKNOWN;
    }

    public boolean isInited() {
        return getCameraState() == CAMERA_IS_INITED;
    }

    public boolean hasEnded() {
        return getCameraState() == CAMERA_HAS_ENDED;
    }

    @Override
    public boolean isRecording() {
        return getCameraState() == CAMERA_IS_RECORDING;
    }

    @Override
    public boolean isPreviewing() {
        return getCameraState() == CAMERA_IS_PREVIEWING;
    }

    @Override
    public boolean isPausing() {
        return getCameraState() == CAMERA_IS_PAUSED;
    }
    // =========================================================================
    @Override
    public Component getVideoAsGraphicComponent() {
        if (isIdle() || (isUnknown())) {
            camPreviewer = Recorder.getCamPreviewComponent(640, 480);
            /*
            previewView = new CaptureView(); // create the AWT component
            jcqfactory.setCaptureView(previewView);
            */
            String fileName = LODEConstants.MOVIE_FILE;
            ServiceProviderIF spif=new ServiceProviderFactory().getServiceProvider();
            String workingDirectory = spif.getCurrentLectureAcquisitionPath();
            //String workingDirectory = ProgramState.getInstance().getCurrentLecture().getAcquisitionPath() + LODEConstants.FS;
            int version = 0;
            outputFilePath=workingDirectory + fileName + version + LODEConstants.MOVIE_EXTENSION;
            /*
            jcqfactory.setOutputFilePath(workingDirectory + fileName + version + LODEConstants.MOVIE_EXTENSION);
            * 
            */
            setCameraState(CAMERA_IS_INITED);
            return camPreviewer;
        }
        return null;
    }
    
@Override
    public void startPreview(JPanel jPVideo,SoundPanelIF audioPanel) {
        if (isInited()) {
            camPreviewer.setVisible(true);
            /*if (jcqfactory.getCaptureSession() == null) {
                jcqfactory.openSession();
            }

            VideoChooser vc=new VideoChooser(true); //TODO THIS IS AQUICK FIX
            isMUXed=vc.isMUXed();
            * 
            */
            isMUXed=false;
            startAudioLevelMonitor(audioPanel);
            setCameraState(VideoControllerIF.CAMERA_IS_PREVIEWING);
        }
    }

    @Override
    public void record(JPanel jPVideo, String graph_path) {
        if (isPreviewing()) {
            Recorder.startToRecord(640, 480, outputFilePath);
            //jcqfactory.startRecording();
            Date d = new Date();
            startingTime = d.getTime();
            //System.out.println("RECORDING: ==> video: " + jcqfactory.getCameraDevice() + " audio: " + jcqfactory.getMicrophoneDevice());
            Recorder.printVideoCaptureDevices();
            Recorder.printAudioDevices();
            setCameraState(VideoControllerIF.CAMERA_IS_RECORDING);
        }
    }

    @Override
    public void togglePause() {
        if (isPausing()) {
            //jcqfactory.resumeRecording();
            Recorder.resumeToRecord();
            Date d = new Date();
            startingTime = startingTime + (d.getTime() - startPausingTime);
            setCameraState(VideoControllerIF.CAMERA_IS_RECORDING);
            return;
        } else if (isRecording()) {
            //qtw.ccSuspendRecording();
            Recorder.pauseToRecord();
            //jcqfactory.pauseRecording();
            Date d = new Date();
            startPausingTime = d.getTime();
            setCameraState(CAMERA_IS_PAUSED);
        }
    }

    @Override
    public void stop() {
        if (isRecording() || isPausing()) {
            //jcqfactory.stopRecording();
            Recorder.stopToRecord();
            Date d = new Date();
            elapsedTime = startingTime - d.getTime();
            stopAudioLevelMonitor();
            setCameraState(CAMERA_HAS_ENDED);
        }
    }

    @Override
    public void stopPreview() {
        if (hasEnded() || isPreviewing() ) {
            //if (jcqfactory.getCaptureSession() == null) return;
            stopAudioLevelMonitor();
            Thread.yield();
            Util.sleep(1);
            Recorder.dispose();
            /*
            jcqfactory.stopPreviewing();
            Util.sleep(1);
            jcqfactory.closeSession();
            jcqfactory.destroySession();
            * 
            */
            /*Component p=camPreviewer;
            if (p!=null) while ( (p = p.getParent()) != null && !(p instanceof JFrame) );
            if (p!=null) ((JFrame) p).dispose();
             *
             */
            setCameraState(CAMERA_IS_IDLE);
        }
    }

    @Override
    public void disposeVideoChannel(JPanel jPVideo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showOptionPanels() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cleanup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
     // ========= AUDIO METHODS =================================================
    @Override
    public void stopAudioLevelMonitor() {
        stopAudioMonitor = true;
        Util.sleep_msec(10);
    }

    @Override
    public void startAudioLevelMonitor(final SoundPanelIF audioLevelPanel) {
        audioMonitor = new Thread() {

            private int taskingDelay = 20;

            @Override
            public void run() {

                //AcquisitionWindow aw=AcquisitionWindow.getInstance();
                if (isMUXed) {
                    audioLevelPanel.noMeter();
                    return;
                }
                Color c = Color.WHITE;
                //int volume = Math.round(5 * jcqfactory.getAudioLevels());//TODO 5 is a magic numbere here!
                int volume = Math.round(5 * Recorder.getAudioVolume());
                stopAudioMonitor = false;

                /*if (volume == 0) {
                Messanger.getInstance().w("NO AUDIO ?", LODEConstants.MSG_ERROR);
                }*/
                int oldCameraState = VideoControllerIF.CAMERA_IS_UNKNOWN;
                Object grabber;
                int numberOfZeroSounds = 0;
                final int TRESHOLD = 20;

                //while ((grabber = jcqfactory.getCaptureSession()) != null && stopAudioMonitor == false) {
                while (stopAudioMonitor == false) {

                    // Setting up what's needed to monitor the audio level
                    int cameraState_local = getCameraState();
                    if (cameraState_local != oldCameraState) {
                        oldCameraState = cameraState_local;
                    }
                    try {
                        Thread.sleep(taskingDelay);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MacVideoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Thread.yield();
                    //volume = Math.round(5 * jcqfactory.getAudioLevels());//TODO 100 is a magic numbere here!
                    volume = Math.round(5 * Recorder.getAudioVolume());

                    //System.err.println(volume + " " + LODEParameters.SOUND_YELLOW_THRESHOLD);
                    //SoundPanel audioLevelPanel=AcquisitionWindow.getInstance().audioPanel; // refresh as it might have changed!
                    if (volume < LODEParameters.SOUND_YELLOW_THRESHOLD) {
                        c = Color.BLACK;
                        numberOfZeroSounds++;
                        if (numberOfZeroSounds > TRESHOLD) {
                            audioLevelPanel.alarm();
                        }
                    } else {
                        if (numberOfZeroSounds > 0) {
                            numberOfZeroSounds = 0;
                            audioLevelPanel.endAlarm();
                        }
                        if (volume > LODEParameters.SOUND_RED_THRESHOLD) {
                            c = Color.RED;
                        } else if (volume > LODEParameters.SOUND_GREEN_THRESHOLD) {
                            c = Color.GREEN;
                        } else if (volume > LODEParameters.SOUND_ORANGE_THRESHOLD) {
                            c = Color.ORANGE;
                        } else if (volume > LODEParameters.SOUND_YELLOW_THRESHOLD) {
                            c = Color.YELLOW;
                        }
                    }
                    audioLevelPanel.setColor(c);
                }
                stopAudioMonitor = false;
                isCheckingVolume = false;
            }
        };
        audioMonitor.start();
    }

    // ========= TIME METHODS ==================================================
    @Override
    public final String getVideoDuration() {
        return "" + getTime();
    }

    @Override
    public final int getTime() {
        Date d = new Date();
        long totalTime = 0;
        if (isRecording()) {
            totalTime = (d.getTime() - startingTime);
        } else if (isPausing()) {
            totalTime = (startPausingTime - startingTime);
        } else {
            totalTime = elapsedTime;
        }
        return (int) totalTime / 1000;
    }    
    
    private int cameraState=CAMERA_IS_UNKNOWN;

    @Override
    public final int getCameraState() {
        return cameraState;
    }

    @Override
    public final void setCameraState(int cameraState) {
        this.cameraState = cameraState;
    }
}
