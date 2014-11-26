package it.unitn.LODE.MP.factories;

import it.unitn.LODE.MP.IF.Controllers.VideoControllerIF;
import it.unitn.LODE.MP.utils.SystemProps;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoControllerFactory {

    static VideoControllerIF instance = null;

    private VideoControllerFactory() {
        if (instance == null) {
            try {
                if (SystemProps.IS_OS_MAC_OSX) {
                    instance = (VideoControllerIF) Class.forName("it.unitn.LODE.Controllers.MultiPlatformVideoController").newInstance();
                    //instance = (VideoControllerIF) Class.forName("it.unitn.LODE.MAC.Video.MacJCQVideoController").newInstance();
                } else {
                    instance = (VideoControllerIF) Class.forName("it.unitn.LODE.Controllers.MultiPlatformVideoController").newInstance();
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(VideoControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(VideoControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VideoControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static VideoControllerIF getInstance() {
        if (instance==null) new VideoControllerFactory();
        return instance;
    }
}
