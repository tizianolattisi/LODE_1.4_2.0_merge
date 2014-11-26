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
package it.unitn.LODEvideo.recorder.audio.java_sound;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;
import it.unitn.LODEvideo.recorder.audio.AudioInputStreamContainer;

/**
 * Utilities to handle the audio devices.
 *
 * @author pietro.pilolli
 */
public class JavaSoundAudioDeviceUtils {

  /*
   * This method creates and returns the AudioFormat object for a given set of
   * format parameters.
   */
  private static AudioFormat audioFormat = new AudioFormat(Encoding.PCM_SIGNED,
          44100,
          16,
          1,
          2,
          44100,
          false);
  /*
   * Setup a Line.Info instance specifically of the TargetDataLine class.
   */
  private static final Line.Info targetDLInfo = new Line.Info(
          TargetDataLine.class);
  /*
   * The info object of the selected audio device.
   */
  private static Info selectedAudioDeviceInfo = null;

  /*
   * The target DataLine.
   */
  private static TargetDataLine targetDataLine = null;

  /**
   * Initialize the audio device class.
   *
   */
  public static void init() {
    autoSelectAudioCaptureDevice();
  }

  /**
   * Dispose and free the internal resources used by this class.
   */
  public static void dispose() {
    /*
     * Do nothing.
     */
  }

  /**
   * Get current target data line.
   *
   * @return TargetDataLine
   */
  public static TargetDataLine getTargetDataLine() {
    return targetDataLine;
  }

  /**
   * Get the list of the device names.
   *
   * @return List of the audio device able to be captured.
   */
  public static List<String> listAudioCaptureDeviceNames() {
    List<String> devices = new LinkedList<String>();
    Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

    if (mixerInfo != null) {
      for (int i = 0; i < mixerInfo.length; i++) {
        Info mixerInfoItem = mixerInfo[i];

        /*
         * Get a temporary instance of the current mixer.
         */
        Mixer currentMixer = AudioSystem.getMixer(mixerInfoItem);

        /*
         * This select only the device that are recordable.
         */
        if (currentMixer.isLineSupported(targetDLInfo)) {

          /*
           * Here we test the mixer; if it does not work we pass to an other
           * one.
           */
          if (!testMixer(mixerInfoItem)) {
            continue;
          }

          devices.add(mixerInfoItem.getName());
        }
      }    // end for loop
    }
    return devices;
  }

  /**
   * Print the available audio devices.
   *
   */
  public static void printAudioCaptureDevices() {
    List audioDeviceList = listAudioCaptureDeviceNames();
    if (audioDeviceList != null) {
      Iterator audioDeviceIterator = audioDeviceList.iterator();

      /*
       * Iterate the audio device list and print with logger each audio device.
       */
      while (audioDeviceIterator.hasNext()) {
        Logger.getLogger(JavaSoundAudioDeviceUtils.class.getName()).log(
                Level.INFO,
                "Detected audio device \"{0}\"",
                audioDeviceIterator.next());
      }
    }
  }

  /**
   * Select the desired video device passing the device name.
   *
   * @param name
   */
  public static void selectAudioCaptureDevice(String name) {
    Info desiredCaptureDeviceInfo = getAudioCaptureDeviceInfo(name);

    if (desiredCaptureDeviceInfo == null) {
      /*
       * Log a warning the desired device can not be selected.
       */
      Logger.getLogger(JavaSoundAudioDeviceUtils.class.getName()).log(
              Level.WARNING,
              "Selection Failed; can not found the audio device \"{0}\"",
              name);
    } else {
      /*
       * Log the audio device used.
       */
      Logger.getLogger(JavaSoundAudioDeviceUtils.class.getName()).log(
              Level.INFO,
              "Selected the audio device \"{0}\"",
              desiredCaptureDeviceInfo.getName());

      setSelectedAudioCaptureDeviceInfo(desiredCaptureDeviceInfo);
    }
  }

  /**
   * Test if the mixer support the audio format for the grabbing.
   */
  private static boolean testMixer(Info mixerInfoItem) {
    return true;
  }

  /**
   * Get the audio capture device info whose name matches the passed name.
   */
  private static Info getAudioCaptureDeviceInfo(String name) {
    List<String> devices = new LinkedList<String>();
    Info[] mixerInfo = AudioSystem.getMixerInfo();

    if (mixerInfo != null) {
      for (int i = 0; i < mixerInfo.length; i++) {
        Info mixerInfoItem = mixerInfo[i];

        /*
         * Get a temporanery instance of the current mixer.
         */
        Mixer currentMixer = AudioSystem.getMixer(mixerInfoItem);

        if (currentMixer != null) {
          /*
           * Select only the devices that are recordable.
           */
          if (currentMixer.isLineSupported(targetDLInfo)) {
            if (mixerInfoItem.getName().equals(name)) {

              if (!testMixer(mixerInfoItem)) {
                continue;
              }

              return mixerInfoItem;
            }
          }
        }
      }
    }

    return null;
  }

  /**
   * Set the selected audio capture device info.
   */
  private static void setSelectedAudioCaptureDeviceInfo(
          Info desiredCaptureDeviceInfo) {
    selectedAudioDeviceInfo = desiredCaptureDeviceInfo;
    restartAudioCaptureStream();
  }

  /**
   * Get the selected audio capture device info.
   */
  private static Info getSelectedAudioCaptureDeviceInfo() {
    return selectedAudioDeviceInfo;
  }

  /**
   * Get the Mixer info of the device that is taken for default.
   */
  private static Info getDefaultDeviceMixerInfo() {
    List<String> devices = new LinkedList<String>();
    Info[] mixerInfo = AudioSystem.getMixerInfo();

    /*
     * List the mixers and found the grabbable ones.
     */
    for (int i = mixerInfo.length - 1; i >= 0; i--) {
      Info mixerInfoItem = mixerInfo[i];

      /*
       * Get a temporary instance of the current mixer.
       */
      Mixer currentMixer = AudioSystem.getMixer(mixerInfoItem);

      /*
       * This select only the device that are recordable.
       */
      if (currentMixer.isLineSupported(targetDLInfo)) {
        if (!testMixer(mixerInfoItem)) {
          continue;
        }

        /*
         * This is the right one!
         */
        return mixerInfoItem;
      }
    }

    return null;
  }

  /**
   * Auto select the audio capture device.
   */
  private static void autoSelectAudioCaptureDevice() {
    Info proposedCaptureDeviceInfo = getDefaultDeviceMixerInfo();

    setSelectedAudioCaptureDeviceInfo(proposedCaptureDeviceInfo);
    Logger.getLogger(JavaSoundAudioDeviceUtils.class.getName()).log(
            Level.INFO,
            "Auto selected the audio device \"{0}\"",
            proposedCaptureDeviceInfo.getName());
  }

  /**
   * Start to capture the audio stream.
   */
  private static void startAudioCaptureStream() {

    /*
     * First of all if an audio device is not selected; take one automagically.
     */
    if (getSelectedAudioCaptureDeviceInfo() == null) {
      autoSelectAudioCaptureDevice();
    }

    Info captureDeviceInfo = getSelectedAudioCaptureDeviceInfo();

    /*
     * Log the start audio capture.
     */
    Logger.getLogger(JavaSoundAudioDeviceUtils.class.getName()).log(
            Level.INFO,
            "Start to capture audio stream from \"{0}\"",
            captureDeviceInfo.getName());


    targetDataLine = getTargetDataLine(captureDeviceInfo);

    try {
      /*
       * Start to capture the selected targetDataLine.
       */
      targetDataLine.open(audioFormat);
    } catch (LineUnavailableException ex) {
      Logger.getLogger(JavaSoundAudioDeviceUtils.class.getName()).log(
              Level.SEVERE,
              null,
              ex);
    }
    targetDataLine.start();
    AudioInputStream audioIs = new AudioInputStream(targetDataLine);
    AudioInputStreamContainer.getInstance().setAudioInputStream(audioIs);
  }

  /**
   * Stop to capture the audio stream.
   */
  private static void stopAudioCaptureStream() {
    if (targetDataLine != null) {
      Info captureDeviceInfo = getSelectedAudioCaptureDeviceInfo();
      /*
       * Log the stop.
       */
      Logger.getLogger(JavaSoundAudioDeviceUtils.class.getName()).log(
              Level.INFO,
              "Stop to capture audio stream from \"{0}\"",
              captureDeviceInfo.getName());

      /*
       * Stop and close the audio line.
       */
      if (targetDataLine.isRunning()) {
        targetDataLine.stop();
      }

      targetDataLine.flush();

      if (targetDataLine.isOpen()) {
        targetDataLine.close();
      }
    }
  }

  /**
   * Restart to capture the audio stream.
   */
  private static void restartAudioCaptureStream() {
    stopAudioCaptureStream();
    startAudioCaptureStream();
  }

  /**
   * Get the target data line passing the info structure.
   */
  private static TargetDataLine getTargetDataLine(Info mixerInfo) {
    /*
     * Get a temporary instance of the current mixer.
     */
    Mixer currentMixer = AudioSystem.getMixer(mixerInfo);

    TargetDataLine targetRecordLine = null;
    try {

      DataLine.Info newInfo = new DataLine.Info(
              TargetDataLine.class, audioFormat);
      targetRecordLine = (TargetDataLine) AudioSystem.getLine(newInfo);

    } catch (LineUnavailableException ex) {
      Logger.getLogger(JavaSoundAudioDeviceUtils.class.getName()).log(
              Level.SEVERE,
              null,
              ex);
    }

    return targetRecordLine;
  }

  /**
   * Get the target data line passing the name.
   */
  private static TargetDataLine getTargetDataLine(String name) {
    Mixer.Info mixerInfo = getAudioCaptureDeviceInfo(name);
    return getTargetDataLine(mixerInfo);
  }
}
