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

import java.awt.image.BufferedImage;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import it.unitn.LODEvideo.recorder.video.CamImageContainer;

/**
 * Grab periodically from the FrameGrabbingControl and fill the
 * CamImageContainer.
 *
 * @author pietro.pilolli
 */
class QtkitCamImageObserver implements Runnable {

  /*
   * The frame rate to grab image from the camera.
   */
  private static final double FRAME_RATE = 30;
  
  /*
   * The pool executor that execute the encode task following the rate.
   */
  private ScheduledThreadPoolExecutor executor = null;
  
  /*
   * The frame grabbing control attached to the DataSource
   */
  private FrameGrabbingControl frameGrabber = null;
  
  /*
   * The CamRecorder thread.
   */
  private Thread engine = null;

  /*
   * Private constructor. I do not want that someone use it without passing the
   * right arguments.
   */
  private QtkitCamImageObserver() {
  }

  /**
   * Constructor.
   *
   * @param frameGrabber
   */
  QtkitCamImageObserver(FrameGrabbingControl frameGrabber) {
    this.frameGrabber = frameGrabber;
  }

  /**
   * Start the thread.
   */
  public void start() {
    engine = new Thread(this);
    engine.start();
  }

  /**
   * Stop the CamRecorder thread.
   */
  public void stop() {
    try {
      executor.awaitTermination((long) 1000, TimeUnit.MILLISECONDS);
    } catch (InterruptedException ex) {
      Logger.getLogger(QtkitCamImageObserver.class.getName()).log(
              Level.SEVERE,
              "The encoder has been interrupted;",
              ex);
    }

  }

  /**
   * Inner task runnable class called periodically.
   */
  private class ObserveTimerTask implements Runnable {

    public ObserveTimerTask() {
    }

    @Override
    /**
     * Run timer task.
     */
    public void run() {

      Buffer buffer = frameGrabber.grabFrame();
      if (buffer == null) {
        return;
      }

      fillImageContainer(buffer);
    }

    /**
     * Fill the CamImageContainer.
     */
    private synchronized void fillImageContainer(Buffer buffer) {
      BufferToImage btoi = new BufferToImage((VideoFormat) buffer.getFormat());
      BufferedImage image = (BufferedImage) btoi.createImage(buffer);

      CamImageContainer.getInstance().setImage(image);
    }
  }

  @Override
  /**
   * Run the thread.
   */
  public void run() {
    /*
     * Create a new executor.
     */
    executor = new ScheduledThreadPoolExecutor(1);

    /*
     * Schedule the grabber accordig to the selected frame rate.
     */
    executor.scheduleAtFixedRate(new ObserveTimerTask(),
            0, (long) (1000 / FRAME_RATE),
            TimeUnit.MILLISECONDS);
  }
}
