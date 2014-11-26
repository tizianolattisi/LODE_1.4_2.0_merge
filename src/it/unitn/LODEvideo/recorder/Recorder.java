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
package it.unitn.LODEvideo.recorder;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import it.unitn.LODEvideo.recorder.audio.java_sound.JavaSoundAudioDeviceUtils;
import it.unitn.LODEvideo.recorder.audio.qtkit.QtkitAudioDeviceUtils;
import it.unitn.LODEvideo.recorder.encoder.Encoder;
import it.unitn.LODEvideo.recorder.video.CamPreviewer;
import it.unitn.LODEvideo.recorder.video.lticivil.CivilVideoDeviceUtils;
import it.unitn.LODEvideo.recorder.video.qtkit.QtkitVideoDeviceUtils;

/**
 * The recorder class; it uses some audio/video grabbing front-end and it choose
 * the right one depending on th system and the available libraries.
 *
 * @author pietro.pilolli
 */
public class Recorder {

  /*
   * The object used to record.
   */
  private static Encoder encoder = new Encoder();
  

  /* Costants strings. */
  private static final String LTICIVIL = "civil";
  private static final String QTKIT = "qtkit";
  
  /* The audio/video front-end selected. */
  private static String videoCaptureframework = null;


  /**
   * Initialize the class.
   *
   */
  public static void init() {
    detectVideoCaptureFramework();

    /*
     * Init the video capture framework.
     */
    if (isAVideoCaptureFrameworkAvailable()) {
      if (isQtkitCaptureFrameworkAvailable()) {
        QtkitVideoDeviceUtils.init();
        JavaSoundAudioDeviceUtils.init();
        QtkitAudioDeviceUtils.init();
      } else if (isCivilVideoCaptureFrameworkAvailable()) {
        CivilVideoDeviceUtils.init();
        JavaSoundAudioDeviceUtils.init();
      }
    }

  }

  /**
   * Dispose and free the internal resources used by this class.
   *
   */
  public static void dispose() {
    if (isAVideoCaptureFrameworkAvailable()) {
      if (isQtkitCaptureFrameworkAvailable()) {
        QtkitVideoDeviceUtils.dispose();
        QtkitAudioDeviceUtils.dispose();
        JavaSoundAudioDeviceUtils.dispose();
      } else if (isCivilVideoCaptureFrameworkAvailable()) {
        CivilVideoDeviceUtils.dispose();
        JavaSoundAudioDeviceUtils.dispose();
      }
    }
  }

  /**
   * Get a list of the name of the available video device names.
   *
   * @return List of the video devices available to be grabbed
   */
  public static List<String> listVideoCaptureDeviceNames() {
    if (isAVideoCaptureFrameworkAvailable()) {
      if (isQtkitCaptureFrameworkAvailable()) {
        return QtkitVideoDeviceUtils.listVideoCaptureDeviceNames();
      } else if (isCivilVideoCaptureFrameworkAvailable()) {
        return CivilVideoDeviceUtils.listVideoCaptureDeviceNames();
      }
      return null;
    }
    return null;
  }

  /**
   * Print the video capture device on standard output.
   */
  public static void printVideoCaptureDevices() {
    if (isAVideoCaptureFrameworkAvailable()) {
      if (isQtkitCaptureFrameworkAvailable()) {
        QtkitVideoDeviceUtils.printVideoCaptureDevices();
      } else if (isCivilVideoCaptureFrameworkAvailable()) {
        CivilVideoDeviceUtils.printVideoCaptureDevices();
      }
    }
  }

  /**
   * Select the desired video device passing the device name.
   *
   * @param name
   */
  public static void selectVideoCaptureDevice(String name) {
    if (isAVideoCaptureFrameworkAvailable()) {
      if (isQtkitCaptureFrameworkAvailable()) {
        QtkitVideoDeviceUtils.selectVideoCaptureDevice(name);
      } else if (isCivilVideoCaptureFrameworkAvailable()) {
        CivilVideoDeviceUtils.selectVideoCaptureDevice(name);
      }
    }
  }

  /**
   * Get the preview frame.
   *
   * @param width
   * @param height
   * @return The cam previewer
   */
  public static CamPreviewer getCamPreviewComponent(int width, int height) {
    if (isAVideoCaptureFrameworkAvailable()) {
      if (isQtkitCaptureFrameworkAvailable()) {
        return QtkitVideoDeviceUtils.startPreview(width, height);
      } else if (isCivilVideoCaptureFrameworkAvailable()) {
        return CivilVideoDeviceUtils.startPreview(width, height);
      }
    }
    return null;
  }

  /**
   * Get the list of the device names.
   *
   * @return The list of the available audio device to be captured.
   */
  public static List<String> listAudioCaptureDeviceNames() {
    if (isAVideoCaptureFrameworkAvailable()) {
      if (isQtkitCaptureFrameworkAvailable()) {
        List<String> qtkitList = QtkitAudioDeviceUtils.listAudioCaptureDeviceNames();
        List<String> javasoundList = JavaSoundAudioDeviceUtils.listAudioCaptureDeviceNames();
        List<String> completeList = new LinkedList();
        completeList.addAll(qtkitList);
        completeList.addAll(javasoundList);
        return completeList;
      } else if (isCivilVideoCaptureFrameworkAvailable()) {
        return JavaSoundAudioDeviceUtils.listAudioCaptureDeviceNames();
      }
    }
    return null;
  }

  /**
   * Print the available audio device.
   */
  public static void printAudioDevices() {
    if (isAVideoCaptureFrameworkAvailable()) {
      if (isQtkitCaptureFrameworkAvailable()) {
        QtkitAudioDeviceUtils.printAudioCaptureDevices();
        JavaSoundAudioDeviceUtils.printAudioCaptureDevices();
      } else if (isCivilVideoCaptureFrameworkAvailable()) {
        JavaSoundAudioDeviceUtils.printAudioCaptureDevices();
      }
    }
  }

  /**
   * Select the desired video device passing the device name.
   *
   * @param name
   */
  public static void selectAudioCaptureDevice(String name) {
    if (isAVideoCaptureFrameworkAvailable()) {
      if (isQtkitCaptureFrameworkAvailable()) {
        List<String> jsDevL = JavaSoundAudioDeviceUtils.listAudioCaptureDeviceNames();
        if (jsDevL.contains(name)) {
          JavaSoundAudioDeviceUtils.selectAudioCaptureDevice(name);
        } else {
          QtkitAudioDeviceUtils.selectAudioCaptureDevice(name);
        }
      } else if (isCivilVideoCaptureFrameworkAvailable()) {
        JavaSoundAudioDeviceUtils.selectAudioCaptureDevice(name);
      }
    }
    encoder.start();
  }

  /**
   * Start to record from the selected video device. The filename must have a
   * meaningful extension, the encoder will select the encoder option basing on
   * the extension. The mpg, mpeg and mov format are been tested successfully.
   *
   * @param width
   * @param height
   * @param filename
   */
  public static void startToRecord(int width, int height, String filename) {
    encoder.start(width, height, filename);
    Logger.getLogger(Recorder.class.getName()).log(Level.INFO,
            "Recorder has been started");
  }

  /**
   * Stop to record.
   *
   */
  public static void stopToRecord() {
    if (encoder != null) {
      encoder.stop();
    }
  }

  /**
   * Pause to record.
   */
  public static void pauseToRecord() {
    if (encoder != null) {
      encoder.pause();
    }
  }

  /**
   * Resume to record.
   */
  public static void resumeToRecord() {
    if (encoder != null) {
      encoder.resume();
    }
  }

  /**
   * Restart to record.
   *
   * @param width
   * @param filename
   * @param height
   */
  public static void restartToRecord(int width, int height, String filename) {
    stopToRecord();
    startToRecord(width, height, filename);
  }

  /**
   * Return the audio volume in a semaphoric way with a float from 0 to 4.
   *
   * @return The audio volume
   */
  public static float getAudioVolume() {
    return encoder.getVolume();
  }

  /*
   * Is a video capture framework available.
   */
  private static boolean isAVideoCaptureFrameworkAvailable() {
    return (videoCaptureframework != null);
  }

  /*
   * Is the qtkit audio/video capture framework available.
   */
  private static boolean isQtkitCaptureFrameworkAvailable() {
    return videoCaptureframework.equals(QTKIT);
  }

  /*
   * Is the lticivil video capture framework available.
   */
  private static boolean isCivilVideoCaptureFrameworkAvailable() {
    return videoCaptureframework.equals(LTICIVIL);
  }

  /*
   * Detect the video capture framework available.
   */
  private static void detectVideoCaptureFramework() {
    try {
      /*
       * Test to detect if the operating system is OSX
       */
      Class<?> c = Class.forName("com.apple.eawt.CocoaComponent");
      videoCaptureframework = QTKIT;
    } catch (Exception e) {
      Logger.getLogger(Recorder.class.getName()).log(Level.INFO,
              "No QTKit available for cam grabbing;"
              + " could be that you are not running on MacOsX.");
      try {
        /*
         * Test to detect if the lti-civil library is available.
         */
        Class<?> c = Class.forName("com.lti.civil.Image");
        videoCaptureframework = LTICIVIL;
      } catch (Exception e1) {
        Logger.getLogger(Recorder.class.getName()).log(Level.INFO,
                "No lti-civil available for cam grabbing");
      }
    }
  }
}
