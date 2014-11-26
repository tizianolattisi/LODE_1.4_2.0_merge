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

import com.lti.civil.CaptureException;
import com.lti.civil.CaptureObserver;
import com.lti.civil.CaptureStream;
import com.lti.civil.awt.AWTImageConverter;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import it.unitn.LODEvideo.recorder.video.CamImageContainer;

/**
 * This is the observer that store the image captured by the camera.
 *
 * @author pietro.pilolli
 */
public class CivilCamImageObserver implements CaptureObserver {

  /**
   * This callback shots when an error occurs.
   *
   * @param sender
   * @param e
   */
  @Override
  public void onError(CaptureStream sender, CaptureException e) {
    Logger.getLogger(CivilCamImageObserver.class.getName()).log(
            Level.SEVERE,
            "Generic error grabbing cam;",
            e);
  }

  /**
   * This callback shots when a new image has been captured by the camera.
   *
   * @param sender
   * @param image
   */
  @Override
  public void onNewImage(CaptureStream sender, com.lti.civil.Image image) {
    try {
      storeInBufferedImage(image);
    } catch (Throwable t) {
      Logger.getLogger(CivilCamImageObserver.class.getName()).log(
              Level.SEVERE,
              "Error acquiring image;",
              t);
    }
  }

  /**
   * Convert Lti-civil image in buffered image and store it.
   */
  private synchronized void storeInBufferedImage(com.lti.civil.Image image) {
    BufferedImage bimage = AWTImageConverter.toBufferedImage(image);
    CamImageContainer.getInstance().setImage(bimage);
  }
}
