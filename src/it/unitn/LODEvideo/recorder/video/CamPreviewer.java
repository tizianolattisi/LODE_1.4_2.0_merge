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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Preview window that display images captured by the camera.
 *
 * @author pietro.pilolli
 */
public class CamPreviewer extends JComponent implements Runnable {

  /*
   * The preview frame rate.
   */
  private static final double FRAME_RATE = 30;

  /*
   * The image captured by
   */
  private Image image = null;

  /*
   * The CamPreview thread.
   */
  private Thread engine;

  /**
   * Constructor by fields.
   *
   * @param width
   * @param height
   */
  public CamPreviewer(int width, int height) {
    Dimension dimension = new Dimension(width, height);

    this.setSize(dimension);
    this.setPreferredSize(dimension);
  }

  /**
   * Start the capture preview thread.
   */
  public void start() {
    engine = new Thread(this);
    engine.start();
  }

  /**
   * Stop the capture preview thread.
   */
  public void stop() {
    if (engine != null) {
      engine.interrupt();
    }
  }

  /**
   * Called when the preview thread is started.
   */
  @Override
  public void run() {

    /*
     * Create a new executor.
     */
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    executor.scheduleAtFixedRate(new UpdatePreviewTimerTask(),
            0,
            (long) (1000 / FRAME_RATE),
            TimeUnit.MILLISECONDS);
  }

  /**
   * Paint the image.
   * @param g 
   */
  @Override
  public void paint(Graphics g) {
    if (image != null) {
      g.drawImage(image, 0, 0, getSize().width, getSize().height, this);
    }
  }

  /**
   * Repaint the preview window.
   */
  private void setImageInSwingThread(Image image) {
    repaint();
  }

  /**
   * The image runnable inner class.
   */
  private class ImageRunnable implements Runnable {

    private final Image newImage;

    public ImageRunnable(Image newImage) {
      super();
      this.newImage = newImage;
    }

    @Override
    /**
     * Set the camera image in the swing component.
     */
    public void run() {
      setImageInSwingThread(newImage);
    }
  }

  /**
   * The timer used to update the preview window.
   */
  private class UpdatePreviewTimerTask extends TimerTask {

    @Override
    public void run() {
      /*
       * Take a camera image from the observer.
       */
      image = CamImageContainer.getInstance().getImage();

      if (image != null) {
        SwingUtilities.invokeLater(new ImageRunnable(image));
      }
    }
  }
}
