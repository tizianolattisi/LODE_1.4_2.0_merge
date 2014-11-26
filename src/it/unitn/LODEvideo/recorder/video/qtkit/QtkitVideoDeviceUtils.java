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
package it.unitn.LODEvideo.recorder.video.qtkit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.*;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import it.unitn.LODEvideo.recorder.audio.qtkit.QtkitAudioDeviceUtils;
import it.unitn.LODEvideo.recorder.video.CamPreviewer;
import net.mc_cubed.QTCubed;
import net.mc_cubed.qtcubed.media.protocol.quicktime.DataSource;

/**
 * Utilities to select and preview the video device cameras with Qtkit
 * framework.
 *
 * @author pietro.pilolli
 */
public class QtkitVideoDeviceUtils {

  /*
   * The object used to make the camera preview.
   */
  private static CamPreviewer camPreviewer = null;
  
  /*
   * The selected video deviceinfo.
   */
  private static CaptureDeviceInfo selectedVideoDeviceInfo = null;
  
  /*
   * The jmf player used to grab images from cam.
   */
  private static Player player = null;
  
  /*
   * The observer that take the image from the device and fill the
   * CamImageContainer.
   */
  private static QtkitCamImageObserver obs = null;
  
  /*
   * The DataSource of the selected videoDevice.
   */
  private static DataSource ds = null;

  /**
   * Get the data source.
   *
   * @return DataSource
   */
  public static DataSource getDataSource() {
    return ds;
  }

  /**
   * Set the data source.
   *
   * @param dataSource
   */
  public static void setDataSource(DataSource dataSource) {
    ds = dataSource;
  }

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
    List<String> videoDeviceList = new LinkedList();
    List deviceList = listVideoCaptureDeviceInfos();

    Iterator ite = deviceList.iterator();
    while (ite.hasNext()) {
      CaptureDeviceInfo device = (CaptureDeviceInfo) ite.next();
      videoDeviceList.add(device.getName());

    }
    return videoDeviceList;
  }

  /**
   * Select the video device passing the device name.
   *
   * @param name
   */
  public static void selectVideoCaptureDevice(String name) {
    CaptureDeviceInfo chosenCaptureDeviceInfo = getVideoCaptureDeviceInfo(name);
    setSelectedVideoCaptureDeviceInfo(chosenCaptureDeviceInfo);
    restartVideoCaptureStream();
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
    restartVideoCaptureStream();
    camPreviewer = new CamPreviewer(width, height);
    camPreviewer.start();
    /*
     * Log the start preview.
     */
    Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(Level.INFO,
            "Preview has been started");
    return camPreviewer;
  }

  /**
   * Stop the video camera preview.
   *
   */
  public static void stopPreview() {
    stopVideoCaptureStream();
    /*
     * Log the stop preview.
     */
    Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(Level.INFO,
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
   * Print the detected video devices.
   *
   */
  public static void printVideoCaptureDevices() {
    List videoDeviceList = listVideoCaptureDeviceNames();
    if (videoDeviceList != null) {
      Iterator videoDeviceIterator = videoDeviceList.iterator();

      /*
       * Print with logger each video device found.
       */
      while (videoDeviceIterator.hasNext()) {
        Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(
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
    /*
     * This is required in order to use the qtkit jmf frontend.
     */
    QTCubed.usesQTKit();
  }

  /**
   * Auto select the video device; the last one inserted will be taken.
   *
   */
  private static void autoSelectVideoCaptureDevice() {
    Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(
            Level.INFO,
            "Start auto video device detection");

    List deviceList = listVideoCaptureDeviceInfos();
    if ((deviceList != null) && (deviceList.size() > 0)) {
      int i = deviceList.size() - 1;
      CaptureDeviceInfo proposedDevInfo = (CaptureDeviceInfo) deviceList.get(i);

      Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(
              Level.INFO,
              "Auto selected the video device \"{0}\"",
              proposedDevInfo.getName());

      setSelectedVideoCaptureDeviceInfo(proposedDevInfo);
    } else {
      Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(
              Level.SEVERE,
              "No video devices found!");
    }
  }

  /**
   * Restart the video capture stream.
   *
   */
  private static void restartVideoCaptureStream() {
    stopVideoCaptureStream();
    startVideoCaptureStream();
  }

  /**
   * Dispose the video system.
   */
  private static void disposeVideoSystem() {
  }

  /**
   * Get the list of CaptureDeviceInfo representing the video devices detected
   * on the system.
   */
  private static List listVideoCaptureDeviceInfos() {
    List videoDevices = new LinkedList();
    List list = CaptureDeviceManager.getDeviceList(null);
    Iterator ite = list.iterator();
    while (ite.hasNext()) {
      CaptureDeviceInfo dev = (CaptureDeviceInfo) ite.next();
      Format[] formats = dev.getFormats();
      for (int j = 0; j < formats.length; j++) {
        String devname = dev.getName();
        if (formats[j] != null) {
          if (formats[j] instanceof VideoFormat) {
            /*
             * Add the device to the video device list.
             */
            videoDevices.add(dev);
            break;
          }
        } else {
          /*
           * This is an error the format must be not null.
           */
          Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(
                  Level.SEVERE,
                  "Device {0} has an unknown format type",
                  devname);
        }
      }
    }
    return videoDevices;
  }

  /**
   * Get the CaptureDeviceInfo of the video device passing its name.
   */
  private static CaptureDeviceInfo getVideoCaptureDeviceInfo(String name) {
    return CaptureDeviceManager.getDevice(name);
  }

  /**
   * Get the CapturedeviceInfo of selected video device.
   * @return CaptureDeviceInfo
   */
  public static CaptureDeviceInfo getSelectedVideoCaptureDeviceInfo() {
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
   * Is a video capture device selected?
   */
  private static boolean isVideoCaptureDeviceSelected() {
    if (getSelectedVideoCaptureDeviceInfo() == null) {
      return false;
    }

    return true;
  }

  /*
   * Stop video capture stream.
   */
  private static void stopVideoCaptureStream() {
    if (obs != null) {
      obs.stop();
    }
    if (player != null) {
      player.stop();
    }
  }

  /*
   * Restart and join the audio stream if it is needed.
   */
  private static void joinAudio(CaptureDeviceInfo device) {
    /*
     * This will be done if is chosen to grab audio/video from a muxed device.
     */
    CaptureDeviceInfo audioDev = QtkitAudioDeviceUtils.getSelectedAudioCaptureDeviceInfo();
    if ((audioDev != null)
            && (device.getName().equals(audioDev.getName()))) {
      Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(
              Level.INFO,
              "Start audio/video capture stream {0}",
              device.getName());
      /*
       * The audio device inherit the same videodevice datasource.
       */
      QtkitAudioDeviceUtils.setDataSource(ds);
      /*
       * I restart the audio device in order to grab the audio from the device.
       */
      QtkitAudioDeviceUtils.restartAudioCaptureStream();
    }
  }

  /*
   * Start the video capture stream.
   */
  private static void startVideoCaptureStream() {
    CaptureDeviceInfo device = getSelectedVideoCaptureDeviceInfo();

    Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(
            Level.INFO, "Start video capture stream {0}", device.getName());

    if (device != null) {
      MediaLocator ml = device.getLocator();
      try {

        /*
         * The datasource represents the device and can be created only one. If
         * the device is muxed we must use the same datasource to take the audio
         * and video stream.
         */
        ds = (DataSource) Manager.createDataSource(ml);

        player = Manager.createRealizedPlayer(ds);
        player.start();

        FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl(
                "javax.media.control.FrameGrabbingControl");

        obs = new QtkitCamImageObserver(fgc);
        obs.start();

        joinAudio(device);

      } catch (Exception ex) {
        Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(
                Level.SEVERE,
                null,
                ex);
      }
    }
  }
}
