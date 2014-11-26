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
package it.unitn.LODEvideo.recorder.video;

import java.awt.image.BufferedImage;

/**
 * The cam image container will contain the last image grabbed from the cam.
 *
 * @author pietro.pilolli
 */
public class CamImageContainer {

  /*
   * The singleton instance.
   */
  private static CamImageContainer instance = null;
  /*
   * The last buffered image stored from the cam.
   */
  private static BufferedImage image = null;

  /*
   * Avoid to use the costructor; Please use the static method getInstance
   * instead.
   */
  private CamImageContainer() {
    /*
     * Empty constructor.
     */
  }

  /**
   * Return the instance of the singleton.
   *
   * @return CamImageContainer
   */
  public static CamImageContainer getInstance() {
    /*
     * Return the valid instance.
     */
    if (instance != null) {
      return instance;
    }
    /*
     * Return a new instance.
     */
    return new CamImageContainer();
  }

  /**
   * Get the last image grabbed from the cam.
   *
   * @return BufferedImage
   */
  public BufferedImage getImage() {
    return image;
  }

  /**
   * Set the last image grabbed from the cam.
   *
   * @param imageIn
   */
  public void setImage(BufferedImage imageIn) {
    image = imageIn;
  }
}
