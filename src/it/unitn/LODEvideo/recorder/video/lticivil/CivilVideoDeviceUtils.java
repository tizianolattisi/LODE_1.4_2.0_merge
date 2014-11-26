/*
 * Lode -- a program to record your teaching session
 * This program has been written such as a freedom sonet
 * We believe in the freedom and in the freedom of education
 *
 * Copyright (C) 2009 Pilolli Pietro <pilolli.pietro@gmail.com>
 *
 * Lode is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Lode is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.unitn.LODEvideo.recorder.video.lticivil;

import com.lti.civil.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import it.unitn.LODEvideo.recorder.video.CamPreviewer;

/**
 * Utilities to select and preview the video device cameras with lti-civil
 * framework.
 *
 * @author pietro.pilolli
 */
public class CivilVideoDeviceUtils {

  /*
   * The object used to make the camera preview.
   */
  private static CamPreviewer camPreviewer = null;

  /*
   * The capture stream.
   */
  private static CaptureStream captureStream = null;

  /*
   * The selected video deviceinfo.
   */
  private static CaptureDeviceInfo selectedVideoDeviceInfo = null;

  /*
   * The capture system.
   */
  private static CaptureSystem system = null;

  /**
   * Initialize the framework and internal resources needed to use this class.
   *
   */
  public static void init() {
    initVideoSystem();
    autoSelectVideoCaptureDevice();
  }

  /**
   * Dispose the framework and free the internal resources used by this class.
   *
   */
  public static void dispose() {
    disposeVideoSystem();
  }

  /**
   * Get a list of the name of the available video device names.
   *
   * @return The list of the video capture device name
   */
  public static List<String> listVideoCaptureDeviceNames() {
    List videoDevicesList = null;
    try {
      if (system != null) {
        List list = system.getCaptureDeviceInfoList();

        if (list != null) {
          videoDevicesList = new LinkedList();
          for (int i = 0; i < list.size(); i++) {
            com.lti.civil.CaptureDeviceInfo info =
                    (com.lti.civil.CaptureDeviceInfo) list.get(i);

            videoDevicesList.add(info.getDescription());
          }
        }
      }
    } catch (CaptureException ex) {
      Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(Level.SEVERE,
              null,
              ex);
    }
    return videoDevicesList;
  }

  /**
   * Select the desired video device passing the device name.
   *
   * @param name
   */
  public static void selectVideoCaptureDevice(String name) {
    CaptureDeviceInfo desiredCaptureDeviceInfo = getVideoCaptureDeviceInfo(name);

    setSelectedVideoCaptureDeviceInfo(desiredCaptureDeviceInfo);
    restartCaptureStream();
  }

  /**
   * Start the video camera preview; It return the JFrame object containing the
   * preview.
   *
   * @param width
   * @param height
   * @return CamPreviewer.
   */
  public static CamPreviewer startPreview(int width, int height) {
    restartCaptureStream();
    camPreviewer = new CamPreviewer(width, height);
    camPreviewer.start();
    /*
     * Log the start preview.
     */
    Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(Level.INFO,
            "Preview has been started");
    return camPreviewer;
  }

  /**
   * Stop the video camera preview.
   *
   */
  public static void stopPreview() {
    stopCaptureStream();
    /*
     * Log the stop preview.
     */
    Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(Level.INFO,
            "Preview has been stopped");
  }

  /**
   * Restart the video camera preview.
   *
   * @param width
   * @param height
   * @return CamPreviewer.
   */
  public static CamPreviewer restartPreview(int width, int height) {
    stopPreview();

    return startPreview(width, height);
  }

  /**
   * Print to terminal the detected video devices.
   *
   */
  public static void printVideoCaptureDevices() {
    List videoDeviceList = listVideoCaptureDeviceNames();
    if (videoDeviceList != null) {
      Iterator videoDeviceIterator = videoDeviceList.iterator();

      /*
       * Print with logger each device found.
       */
      while (videoDeviceIterator.hasNext()) {
        Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(
                Level.INFO,
                "Detected video device \"{0}\"",
                videoDeviceIterator.next());
      }
    }
  }

  /**
   * Initialize the internal video system.
   */
  private static void initVideoSystem() {
    if (system == null) {
      try {
        CaptureSystemFactory factory =
                DefaultCaptureSystemFactorySingleton.instance();

        system = factory.createCaptureSystem();
        system.init();
      } catch (CaptureException ex) {
        Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(
                Level.SEVERE,
                "Unable to init the civil system;"
                + "Can be that civil native library is not found."
                + " Try to put civil.dll or libcivil.so in the same jar' folder",
                ex);
      }
    }
  }

  /**
   * Auto select the video device; the last one inserted will be taken.
   *
   */
  private static void autoSelectVideoCaptureDevice() {

    Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(
            Level.INFO,
            "Start auto video device detection");

    List<CaptureDeviceInfo> list = listVideoCaptureDeviceInfos();
    if ((list != null) && (list.size() > 0)) {
      CaptureDeviceInfo proposedCaptureDeviceInfo = list.get(0);

      Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(
              Level.INFO,
              "Auto selected the video device \"{0}\"",
              proposedCaptureDeviceInfo.getDescription());
      System.out.println(proposedCaptureDeviceInfo.getDescription());
      setSelectedVideoCaptureDeviceInfo(proposedCaptureDeviceInfo);
    } else {
      Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(
              Level.SEVERE,
              "No video devices found!");
    }
  }

  /**
   * Restart the capture stream.
   *
   */
  private static void restartCaptureStream() {
    stopCaptureStream();
    startCaptureStream();
  }

  /**
   * Dispose the video system.
   */
  private static void disposeVideoSystem() {
    if (system != null) {
      try {
        system.dispose();
      } catch (CaptureException ex) {
        Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(
                Level.SEVERE,
                null,
                ex);
      }
      system = null;
    }
  }

  /**
   * Get the list of CaptureDeviceInfo representing the video devices detected
   * on the system.
   */
  private static List<CaptureDeviceInfo> listVideoCaptureDeviceInfos() {
    List videoDevicesList = null;
    try {
      if (system != null) {
        List list = system.getCaptureDeviceInfoList();

        if (list != null) {
          videoDevicesList = new LinkedList();
          for (int i = 0; i < list.size(); i++) {
            CaptureDeviceInfo info = (CaptureDeviceInfo) list.get(i);

            videoDevicesList.add(info);
          }
        }
      }
    } catch (CaptureException ex) {
      Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(
              Level.SEVERE,
              null,
              ex);
    }
    return videoDevicesList;
  }

  /**
   * Get the CaptureDeviceInfo of the video device passing its name.
   */
  private static CaptureDeviceInfo getVideoCaptureDeviceInfo(String name) {
    try {
      List videoDevicesList = new LinkedList();
      List list = system.getCaptureDeviceInfoList();

      if (list != null) {
        for (int i = 0; i < list.size(); ++i) {
          CaptureDeviceInfo info = (CaptureDeviceInfo) list.get(i);

          if (info.getDescription().equals(name)) {
            return info;
          }

          videoDevicesList.add(info.getDescription());
        }
      }
    } catch (CaptureException ex) {
      Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(
              Level.SEVERE,
              null,
              ex);
    }
    return null;
  }

  /**
   * Get the CapturedeviceInfo of selected video device.
   */
  private static CaptureDeviceInfo getSelectedVideoCaptureDeviceInfo() {
    return selectedVideoDeviceInfo;
  }

  /**
   * Set the selected video device passing its CaptureDeviceInfo.
   */
  private static void setSelectedVideoCaptureDeviceInfo(
          CaptureDeviceInfo captureDeviceInfo) {
    selectedVideoDeviceInfo = captureDeviceInfo;
  }

  /**
   * Is a video capture device selected.
   */
  private static boolean isVideoCaptureDeviceSelected() {
    if (getSelectedVideoCaptureDeviceInfo() == null) {
      return false;
    }

    return true;
  }

  /**
   * Start to capture the stream.
   */
  private static void startCaptureStream() {
    try {
      /*
       * If is not selected a device; auto select it.
       */
      if (isVideoCaptureDeviceSelected() == false) {
        autoSelectVideoCaptureDevice();
      }

      /*
       * Open the stream on the selected video device.
       */
      CaptureDeviceInfo captureDeviceInfo = getSelectedVideoCaptureDeviceInfo();

      if (system != null) {
        String deviceID = captureDeviceInfo.getDeviceID();
        captureStream = system.openCaptureDeviceStream(deviceID);

        /*
         * The observer is the object that will contains the images grabbed from
         * the cam.
         */
        captureStream.setObserver(new CivilCamImageObserver());
        captureStream.start();
      }
    } catch (CaptureException ex) {
      Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(
              Level.SEVERE,
              null,
              ex);
    }
  }

  /**
   * Stop the capture stream.
   */
  private static void stopCaptureStream() {
    if (captureStream != null) {
      try {
        captureStream.stop();
      } catch (CaptureException ex) {
        Logger.getLogger(CivilVideoDeviceUtils.class.getName()).log(
                Level.SEVERE,
                null,
                ex);
      }
    }
  }
}
