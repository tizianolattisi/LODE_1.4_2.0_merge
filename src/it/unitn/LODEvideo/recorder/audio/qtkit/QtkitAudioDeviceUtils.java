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
package it.unitn.LODEvideo.recorder.audio.qtkit;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.*;
import javax.media.format.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import it.unitn.LODEvideo.recorder.audio.AudioInputStreamContainer;
import it.unitn.LODEvideo.recorder.video.qtkit.QtkitVideoDeviceUtils;
import net.mc_cubed.QTCubed;
import net.mc_cubed.qtcubed.media.protocol.quicktime.DataSource;

/**
 * Utilities to handle the qtkit audio devices.
 *
 * @author pietro.pilolli
 */
public class QtkitAudioDeviceUtils {

  /*
   * The selected audio deviceinfo.
   */
  private static CaptureDeviceInfo selectedAudioDeviceInfo = null;

  /*
   * The jmf player used to grab audio stream.
   */
  private static Player player = null;
  
  /*
   * The DataSource of the selected videoDevice.
   */
  private static DataSource ds = null;

  /**
   * Set data source.
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
    initAudioSystem();
    autoSelectAudioCaptureDevice();
  }

  /**
   * Dispose the framework and free the internal resources used by this class.
   *
   */
  public static void dispose() {
    disposeAudioSystem();
  }

  /**
   * Get a list of the name of the available audio device names.
   *
   * @return The list of the audio capture device name
   */
  public static List<String> listAudioCaptureDeviceNames() {
    List<String> audioDeviceList = new LinkedList();
    List deviceList = listAudioCaptureDeviceInfos();

    Iterator ite = deviceList.iterator();
    while (ite.hasNext()) {
      CaptureDeviceInfo device = (CaptureDeviceInfo) ite.next();
      audioDeviceList.add(device.getName());

    }
    return audioDeviceList;
  }

  /**
   * Print the detected audio devices.
   *
   */
  public static void printAudioCaptureDevices() {
    List audioDeviceList = listAudioCaptureDeviceNames();
    if (audioDeviceList != null) {
      Iterator audioDeviceIterator = audioDeviceList.iterator();

      /*
       * Print with logger each audio device found.
       */
      while (audioDeviceIterator.hasNext()) {
        Logger.getLogger(QtkitAudioDeviceUtils.class.getName()).log(
                Level.INFO,
                "Detected audio device \"{0}\"",
                audioDeviceIterator.next());
      }
    }
  }

  /**
   * Select the video device passing the device name.
   *
   * @param name
   */
  public static void selectAudioCaptureDevice(String name) {
    CaptureDeviceInfo chosenDeviceInfo = getAudioCaptureDeviceInfo(name);
    setSelectedAudioCaptureDeviceInfo(chosenDeviceInfo);
    restartAudioCaptureStream();
  }

  /**
   * Dispose the audio system.
   */
  private static void disposeAudioSystem() {
  }

  /**
   * Initialize the internal audio system.
   */
  private static void initAudioSystem() {
    /*
     * This is required in order to use the qtkit jmf frontend.
     */
    QTCubed.usesQTKit();
  }

  /**
   * Auto select the audio device; the last one inserted will be taken.
   *
   */
  private static void autoSelectAudioCaptureDevice() {
    Logger.getLogger(QtkitAudioDeviceUtils.class.getName()).log(
            Level.INFO,
            "Start auto audio device detection");

    List deviceList = listAudioCaptureDeviceInfos();
    if ((deviceList != null) && (deviceList.size() > 0)) {
      int i = deviceList.size() - 1;
      CaptureDeviceInfo proposedCaptureDeviceInfo =
              (CaptureDeviceInfo) deviceList.get(i);

      Logger.getLogger(QtkitAudioDeviceUtils.class.getName()).log(
              Level.INFO,
              "Auto selected the audio device \"{0}\"",
              proposedCaptureDeviceInfo.getName());

      setSelectedAudioCaptureDeviceInfo(proposedCaptureDeviceInfo);
    } else {
      Logger.getLogger(QtkitAudioDeviceUtils.class.getName()).log(
              Level.SEVERE,
              "No audio devices found!");
    }
  }

  /*
   * Has a the video device a sound stream?
   */
  private static boolean hasASoundStream(CaptureDeviceInfo dev) {
    DataSource tmpDs = null;
    try {
      MediaLocator ml = dev.getLocator();
      tmpDs = (DataSource) Manager.createDataSource(ml);
    } catch (NoDataSourceException ex) {
      Logger.getLogger(QtkitAudioDeviceUtils.class.getName()).log(
              Level.SEVERE,
              null,
              ex);
    } catch (IOException ex) {
      Logger.getLogger(QtkitAudioDeviceUtils.class.getName()).log(
              Level.SEVERE,
              null,
              ex);
    }

    /*
     * Try to add the audio (This will work with muxed device only. )
     */
    AudioInputStream audioInputStream = JmfUtils.toAudioInputStream(tmpDs);
    /*
     * If it has not and audio stream it is a simple not muxed video device.
     */
    if (audioInputStream != null) {
      return true;
    }

    return false;
  }

  /**
   * Get the list of CaptureDeviceInfo representing the audio devices detected
   * on the system.
   */
  private static List listAudioCaptureDeviceInfos() {
    List audioDevices = new LinkedList();
    List list = CaptureDeviceManager.getDeviceList(null);
    Iterator ite = list.iterator();
    while (ite.hasNext()) {
      CaptureDeviceInfo dev = (CaptureDeviceInfo) ite.next();
      Format[] formats = dev.getFormats();
      for (int j = 0; j < formats.length; j++) {
        Format format = formats[j];
        String devname = dev.getName();
        if (format != null) {
          if (format instanceof AudioFormat) {
            /*
             * Add the device to the audio device list. Now the audioDevice
             * supported by java audio are demanded on this framework.
             */
            //audioDevices.add(dev);
            break;
          } else {
            if (hasASoundStream(dev)) {
              /*
               * This is a muxed device with a built-in microphone.
               */
              Logger.getLogger(QtkitAudioDeviceUtils.class.getName()).log(
                      Level.INFO,
                      "Video device {0} has a Built-in Microphone;"
                      + " you can grab the audio from it",
                      devname);
              audioDevices.add(dev);
            }
          }

        } else {
          /*
           * This is a severe error; the format might be not null.
           */
          Logger.getLogger(QtkitAudioDeviceUtils.class.getName()).log(
                  Level.SEVERE,
                  "Device {0} has an unknown format type",
                  devname);
        }
      }
    }
    return audioDevices;
  }

  /**
   * Set the selected audio device passing its CaptureDeviceInfo.
   */
  private static void setSelectedAudioCaptureDeviceInfo(
          CaptureDeviceInfo captureDeviceInfo) {
    selectedAudioDeviceInfo = captureDeviceInfo;
    restartAudioCaptureStream();
  }

  /*
   * Get the audio capture device info.
   */
  private static CaptureDeviceInfo getAudioCaptureDeviceInfo(String name) {
    return CaptureDeviceManager.getDevice(name);
  }

  /**
   * Restart the capture stream.
   *
   */
  public static void restartAudioCaptureStream() {
    stopAudioCaptureStream();
    startAudioCaptureStream();
  }

  /**
   * Get the CapturedeviceInfo of selected audio device.
   * @return CaptureDeviceInfo
   */
  public static CaptureDeviceInfo getSelectedAudioCaptureDeviceInfo() {
    return selectedAudioDeviceInfo;
  }

  /*
   * Stop audio capture stream.
   */
  private static void stopAudioCaptureStream() {
    if (player != null) {
      player.stop();
    }
  }

  /*
   * Start audio capture stream.
   */
  private static void startAudioCaptureStream() {

    CaptureDeviceInfo device = getSelectedAudioCaptureDeviceInfo();
    Logger.getLogger(QtkitVideoDeviceUtils.class.getName()).log(
            Level.INFO,
            "Start audio capture stream {0}",
            device.getName());

    if (device != null) {

      try {
        MediaLocator ml = device.getLocator();
        /*
         * If the dataSource is uninitialized or the selected device is changed.
         */
        if ((ds == null) || (!ds.getLocator().equals(ml))) {
          /*
           * Reinstantiate a datasource and start a new player instance.
           */
          ds = (DataSource) Manager.createDataSource(ml);
          player = Manager.createRealizedPlayer(ds);
          player.start();
        }
      } catch (Exception ex) {
        Logger.getLogger(QtkitAudioDeviceUtils.class.getName()).log(
                Level.SEVERE,
                null,
                ex);
      }

      /*
       * Try to add the audio stream. This will work for muxed device only.
       */
      AudioInputStream audioInputStream = JmfUtils.toAudioInputStream(
              ds);

      if (audioInputStream != null) {
        AudioInputStreamContainer.getInstance().setAudioInputStream(
                audioInputStream);

        Logger.getLogger(QtkitAudioDeviceUtils.class.getName()).log(
                Level.INFO,
                "Autoselected audio device {0}", device.getName());


      }
    }
  }
}
