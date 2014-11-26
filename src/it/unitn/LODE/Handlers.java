/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.LODE;

import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.IF.Controllers.VideoControllerIF;
import it.unitn.LODE.MP.IF.HandlersIF;
import it.unitn.LODE.gui.AboutBox;
import it.unitn.LODE.gui.PreferencesPanel;

/**
 *
 * @author ronchet
 */
public class Handlers implements HandlersIF{

            public void handleAbout() {
                AboutBox aboutBox=new AboutBox(null);
                aboutBox.setVisible(true);
             }

            public void handlePreferences() {
                PreferencesPanel.getInstance().showPrefs();
            }

            public void handleQuit() {
                VideoControllerIF videoController;
                videoController=ControllersManager.getinstance().getVideoController();
                int state = videoController.getCameraState();
                if (state == VideoControllerIF.CAMERA_IS_RECORDING
                        || state == VideoControllerIF.CAMERA_IS_PAUSED) {
                    ControllersManager.getinstance().getAcquisitionWindow().closeWindow(); // stop and save recording!
                }
                System.exit(0);            }
}
