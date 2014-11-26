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
package it.unitn.LODEvideo.recorder.test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import it.unitn.LODEvideo.recorder.Recorder;
import it.unitn.LODEvideo.recorder.video.CamPreviewer;

/**
 * Main class for testing purposes.
 *
 * @author pietro.pilolli
 */
public class RecorderTest {

  /**
   * Main to demonstrate the API usage.
   *
   * @param args the command line arguments
   * @throws InterruptedException
   */
  public static void main(String[] args) throws InterruptedException {
    /*
     * Init the framework.
     */
    //customizeEnvironment();  
    Recorder.init();

    /*
     * Print the video device list.
     */
    Recorder.printVideoCaptureDevices();

    /*
     * Print the audio devices available.
     */
    Recorder.printAudioDevices();

    /*
     * Select the audio device.
     */
    //String mic = "Built-in Microphone";
    //Recorder.selectAudioCaptureDevice(mic);


    /*
     * Select the video device.
     */
    //Recorder.selectVideoCaptureDevice("MV700i");

    /*
     * Start the preview.
     */
    CamPreviewer camPreviewer = Recorder.getCamPreviewComponent(320, 240);

    /*
     * Embedd the component in a jframe
     */
    JFrame previewFrame = new JFrame();
    previewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    previewFrame.add(camPreviewer);
    previewFrame.pack();

    /*
     * Set the frame visible.
     */
    previewFrame.setVisible(true);

    /*
     * Sleep a bit.
     */
    Thread.sleep((long) 3000);

    /*
     * Start to record.
     */
    Recorder.startToRecord(640, 480, "test.mp4");

    /*
     * Sleep a bit.
     */
    Thread.sleep((long) 10000);

    /*
     * Pause to record.
     */
    Recorder.pauseToRecord();

    /*
     * Sleep a bit.
     */
    Thread.sleep((long) 10000);

    /*
     * Resume to record.
     */
    Recorder.resumeToRecord();

    /*
     * Sleep a bit.
     */
    Thread.sleep((long) 10000);

    /*
     * Stop to record.
     */
    Recorder.stopToRecord();

    /*
     * Get the audio volume.
     */
    // Recorder.getAudioVolume();

    /*
     * Dispose and exit.
     */
    Recorder.dispose();
    previewFrame.dispose();
    System.exit(1);
  }
  //================ SETTING PATH
   protected static void customizeEnvironment() {
    Map<String, String> env = System.getenv();
    String path=env.get("PATH");
    Map<String, String> newenv=new HashMap<String, String>();
    newenv.putAll(env);
    newenv.put("XUGGLE_HOME","/usr/local/xuggler"); 
    newenv.put("PATH",env.get("PATH")+":"+"/usr/local/xuggler:/usr/local/xuggler/bin:/usr/local/xuggler/lib");//+":"+"/Users/ronchet/Netbeans_Lode_project/lode-lectures-read-only/lode-recorder/lib/xuggler");
    String dyld=env.get("DYLD_LIBRARY_PATH");
    System.out.println("========> DYLD_LIBRARY_PATH WAS "+env.get("DYLD_LIBRARY_PATH"));
    newenv.put("DYLD_LIBRARY_PATH","/usr/local/xuggler/lib"); 
    //newenv.put("DYLD_LIBRARY_PATH","/Users/ronchet/Netbeans_Lode_project/lode-lectures-read-only/lode-recorder/lib/xuggler");
    setEnv(newenv);
    Map<String, String> env2 = System.getenv();
    System.out.println("========> PATH IS "+env2.get("PATH"));
    System.out.println("========> DYLD_LIBRARY_PATH IS "+env2.get("DYLD_LIBRARY_PATH"));

  }
  
  // see http://stackoverflow.com/questions/318239/how-do-i-set-environment-variables-from-java/7201825#7201825
  protected static void setEnv(Map<String, String> newenv)
{
  try
    {
        Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
        Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
        theEnvironmentField.setAccessible(true);
        Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
        env.putAll(newenv);
        Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
        theCaseInsensitiveEnvironmentField.setAccessible(true);
        Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
        cienv.putAll(newenv);
    }
    catch (NoSuchFieldException e)
    {
      try {
        Class[] classes = Collections.class.getDeclaredClasses();
        Map<String, String> env = System.getenv();
        for(Class cl : classes) {
            if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                Field field = cl.getDeclaredField("m");
                field.setAccessible(true);
                Object obj = field.get(env);
                Map<String, String> map = (Map<String, String>) obj;
                map.clear();
                map.putAll(newenv);
            }
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    } catch (Exception e1) {
        e1.printStackTrace();
    } 
}

}
