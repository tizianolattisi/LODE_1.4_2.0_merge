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
package it.unitn.LODEvideo.recorder.audio;

import javax.sound.sampled.AudioInputStream;

/**
 * The audio input stream container will contain the audio input stream of the
 * selected device.
 *
 * @author pietro.pilolli
 */
public class AudioInputStreamContainer {

  /*
   * The singleton instance.
   */
  private static AudioInputStreamContainer instance = null;
  
  /*
   * The audio input stream.
   */
  private static AudioInputStream audioInputStream = null;

  /*
   * Avoid to use the costructor; Please use the static method getInstance
   * instead.
   */
  private AudioInputStreamContainer() {
    /*
     * Empty constructor.
     */
  }

  /**
   * Return the instance of the singleton.
   *
   * @return CamImageContainer
   */
  public static AudioInputStreamContainer getInstance() {
    /*
     * Return the valid instance.
     */
    if (instance != null) {
      return instance;
    }
    /*
     * Return a new instance.
     */
    return new AudioInputStreamContainer();
  }

  /**
   * Get the input audio stream.
   *
   * @return BufferedImage
   */
  public AudioInputStream getAudioInputStream() {
    return audioInputStream;
  }

  /**
   * Set the input audio stream.
   *
   * @param is
   */
  public void setAudioInputStream(AudioInputStream is) {
    audioInputStream = is;
  }
}
