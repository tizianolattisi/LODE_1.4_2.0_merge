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
package it.unitn.LODEvideo.recorder.encoder;

import com.xuggle.ferry.IBuffer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IAudioSamples.Format;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import it.unitn.LODEvideo.recorder.audio.AudioInputStreamContainer;
import it.unitn.LODEvideo.recorder.video.CamImageContainer;

/**
 * Record images captured by the camera and audio from the selected stream.
 *
 * @author pietro.pilolli
 */
public class Encoder implements Runnable {

  /*
   * Used for audio stream.
   */
  private static final int AUDIO_STREAM_ID = 1;

  /*
   * The frame rate of the recorded video.
   */
  private static final double FRAME_RATE = 15;

  /*
   * Used for video stream.
   */
  private static final int VIDEO_STREAM_ID = 0;

  /*
   * The CamRecorder thread.
   */
  private Thread engine = null;

  /*
   * The pool executor that execute the encode task following the rate.
   */
  private ScheduledThreadPoolExecutor executor = null;

  /*
   * The height of the recorded video.
   */
  private int height = 0;

  /*
   * The name of the recorded video.
   */
  private String outputFilename = null;

  /*
   * The time od the pausing.
   */
  private long pauseTime = 0;

  /*
   * The start time of the recording.
   */
  private long startTime = 0;

  /*
   * The audio volumeter audio in a semaphoric way [0..4].
   */
  float volume = 0;

  /*
   * The width of the recorded video.
   */
  private int width = 0;

  /*
   * The xuggle mediawrites object used to encode the video.
   */
  private IMediaWriter writer = null;

  /*
   * Flag used to put the recorder in a pause state.
   */
  private boolean paused = false;

  /**
   * Constructor by fields.
   */
  public Encoder() {
  }

  /**
   * Return the volume expressed by a float in the range 0 to 4.
   *
   * @return The volume
   */
  public float getVolume() {
    return volume;
  }

  /**
   * Start the CamRecorder thread.
   */
  public void start() {
    engine = new Thread(this);
    engine.start();
  }

  /**
   * Start the CamRecorder thread.
   *
   * @param width
   * @param height
   * @param outputFilename
   */
  public void start(int width, int height, String outputFilename) {
    this.width = width;
    this.height = height;

    if (outputFilename != null) {

      AudioInputStreamContainer aisc = AudioInputStreamContainer.getInstance();
      AudioInputStream audioInputStream = aisc.getAudioInputStream();

      this.outputFilename = outputFilename;

      /*
       * Let's make a IMediaWriter to write the file.
       */
      writer = ToolFactory.makeWriter(outputFilename);

      if ((width > 0) && (height > 0)) {
        /*
         * Add the video stream to the xuggle writer.
         */
        writer.addVideoStream(VIDEO_STREAM_ID, 0, width, height);
      } else {
        Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE,
                "Invalid video size");
        System.exit(1);
      }

      /*
       * Add the audio stream, if it existes, to the xuggle writer.
       */
      if (audioInputStream != null) {

        writer.addAudioStream(AUDIO_STREAM_ID, 0,
                1,
                (int) audioInputStream.getFormat().getSampleRate());

      } else {
        Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE,
                "No audio passed to the encoder");
        System.exit(1);
      }

    }

    start();
  }

  /**
   * Stop the CamRecorder thread.
   */
  public void stop() {
    pause();

    System.out.println("");
    System.err.print("Quitting recorder");
    if( true ) return;

    /*
     * Shutdown the encoder executor
     */
    //executor.shutdownNow();

    /*
     * Take the time to terminate all the task handled by the executor.
     */
    while (!executor.isTerminated()) {
      try {
        System.err.print(".");
        /*
         * Wait a bit.
         */
        executor.awaitTermination((long) 1000, TimeUnit.MILLISECONDS);

      } catch (InterruptedException ex) {
        Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE,
                "The encoder has been interrupted;",
                ex);
      }
    }


    if (writer != null) {
      try {
        /*
         * Tell the writer to close and write the trailer if needed.
         */
        writer.close();
      } catch (Exception ex) {
        Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE,
                "The encoder is not able to write the trailer;",
                ex);
        System.exit(1);
      }

    }


    if (engine != null) {
      engine.interrupt();
    }

    Logger.getLogger(Encoder.class.getName()).log(Level.INFO,
            "Encoder has been stopped");
  }

  /**
   * Pause the CamRecorder recording.
   */
  public void pause() {
    if (!paused) {
      System.err.println("Pause");
      paused = true;
      pauseTime = System.nanoTime() - startTime;
      Logger.getLogger(Encoder.class.getName()).log(Level.INFO,
              "Encoder has been paused");
    }
  }

  /**
   * Restore the CamRecorder recording.
   */
  public void resume() {
    if (paused) {
      paused = false;
      startTime = pauseTime + startTime;
      Logger.getLogger(Encoder.class.getName()).log(Level.INFO,
              "Encoder has been resumed");
    }
  }

  /**
   * Run the thread.
   */
  @Override
  public void run() {

    /*
     * Store the starting time in the internal variable.
     */
    startTime = System.nanoTime();

    /*
     * Create a new executor.
     */
    executor = new ScheduledThreadPoolExecutor(1);

    /*
     * Schedule the encoding accordig to the selected frame rate.
     */
    executor.scheduleAtFixedRate(new EncodeTimerTask(),
            0, (long) (1000 / FRAME_RATE),
            TimeUnit.MILLISECONDS);
  }

  /**
   * This is the task called periodically to encode the audio/video.
   */
  private class EncodeTimerTask implements Runnable {

    /**
     * Is executed each time that this task has been called by the scheduler.
     */
    @Override
    public void run() {
      try {
        byte[] audioBuf = getAudioBytes();

        /*
         * Update the local variable containing the last volume counted.
         */
        volume = calculatePeakLevel(audioBuf) * 4;

        if (!paused) {
          encodeVideo();

          if (audioBuf != null) {
            encodeAudio(audioBuf);
          }
        }
      } catch (IOException ex) {
        Logger.getLogger(Encoder.class.getName()).log(
                Level.SEVERE,
                null,
                ex);
      }
    }

    /**
     * Convert the buffered image in the type given in input.
     */
    private BufferedImage convertToType(BufferedImage sourceImage,
            int width,
            int height,
            int targetType) {

      /*
       * The returned buffered image.
       */
      BufferedImage image;

      /*
       * If the source image is already the target type then return the source
       * image.
       */
      if (sourceImage.getType() == targetType) {
        image = sourceImage;
      } else {
        /*
         * Otherwise create a new image of the target type and draw the new
         * image.
         */
        image = new BufferedImage(width, height, targetType);
        image.getGraphics().drawImage(sourceImage,
                0,
                0,
                width,
                height,
                null);
      }

      return image;
    }

    /**
     * Encode the available video image grabbed from the cam.
     */
    private void encodeVideo() {
      if (writer != null) {
        /*
         * Take the last image stored in the CamImageContainer .
         */
        BufferedImage webcamImage = CamImageContainer.getInstance().getImage();

        if (webcamImage != null) {
          /*
           * Convert to the right image type supported by xuggle.
           */
          BufferedImage bgrWebcamImage = convertToType(webcamImage,
                  width,
                  height,
                  BufferedImage.TYPE_3BYTE_BGR);

          /*
           * Encode the image into video stream .
           */
          writer.encodeVideo(VIDEO_STREAM_ID,
                  bgrWebcamImage, System.nanoTime() - startTime,
                  TimeUnit.NANOSECONDS);
        }
      }
    }

    /**
     * Get all the bytes available from the selected target data line.
     */
    private byte[] getAudioBytes() throws IOException {
      AudioInputStreamContainer aisc = AudioInputStreamContainer.getInstance();
      AudioInputStream audioInputStream = aisc.getAudioInputStream();

      if (audioInputStream != null) {

        /*
         * Audio buffer.
         */
        int numBytesToRead = audioInputStream.available();
        byte[] audioBuf = new byte[numBytesToRead];

        /*
         * Encode the audio into audio stream.
         */
        int nBytesRead = audioInputStream.read(
                audioBuf,
                0,
                numBytesToRead);

        if (nBytesRead == 0) {
          return null;
        }

        return audioBuf;
      }

      return null;
    }

    /**
     * Encode the available audio bytes.
     */
    private void encodeAudio(byte[] audioBuf) {
      if (writer != null) {
        if (audioBuf == null) {

          /*
           * Drop the bytes.
           */
          return;
        }

        long pts = (System.nanoTime() - startTime) / 1000;
        AudioInputStreamContainer aisc = AudioInputStreamContainer.getInstance();
        AudioInputStream audioInputStream = aisc.getAudioInputStream();
        IBuffer iBuf = IBuffer.make(null,
                audioBuf,
                0,
                audioBuf.length);

        /*
         * FMT_S16 format is signed integer with 16 bit per channel. FMT_S24
         * format is signed integer with 24 bit per channel. FMT_S32 format is
         * signed integer with 32 bit per channel. FMT_FLT format is signed
         * float with 32 bit per channel. FMT_U8 format is unisgned 8 bit per
         * channel.
         */
        Format format = IAudioSamples.Format.FMT_S16;
        int channels = audioInputStream.getFormat().getChannels();
        int sampleRate = (int) audioInputStream.getFormat().getSampleRate();

        IAudioSamples smp = IAudioSamples.make(iBuf,
                channels,
                format);

        if (smp == null) {
          return;
        }

        long numSample = audioBuf.length / smp.getSampleSize();

        /*
         * Logger.getLogger(Encoder.class.getName()).log( Level.INFO, null, "NUM
         * SAMPLE " + numSample + " SMP size " + smp.getSampleSize());
         *
         */

        smp.setComplete(true,
                numSample,
                sampleRate,
                channels,
                format,
                pts);

        // encode audio to audio stream
        writer.encodeAudio(AUDIO_STREAM_ID, smp);
      }
    }

    /**
     * This return the peak value; the returned float is from 0 to 1.
     */
    private float calculatePeakLevel(byte[] buffer) {
      float MAX_8_BITS_SIGNED = Byte.MAX_VALUE;
      float MAX_8_BITS_UNSIGNED = 0xff;
      float MAX_16_BITS_SIGNED = Short.MAX_VALUE;
      float MAX_16_BITS_UNSIGNED = 0xffff;
      int max = 0;
      float level = 0;
      AudioInputStreamContainer aisc = AudioInputStreamContainer.getInstance();
      AudioInputStream audioStream = aisc.getAudioInputStream();
      AudioFormat format = audioStream.getFormat();
      boolean use16Bit = (format.getSampleSizeInBits() == 16);
      boolean signed = (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED);
      boolean bigEndian = (format.isBigEndian());

      if (buffer == null) {

        /*
         * No buffer 0 peak.
         */
        return level;
      }

      if (use16Bit) {
        for (int i = 0; i < buffer.length; i += 2) {
          int value;

          /*
           * Deal with endianness.
           */
          int hiByte = (bigEndian
                  ? buffer[i]
                  : buffer[i + 1]);
          int loByte = (bigEndian
                  ? buffer[i + 1]
                  : buffer[i]);

          if (signed) {
            short shortVal = (short) hiByte;

            shortVal = (short) ((shortVal << 8) | (byte) loByte);
            value = shortVal;
          } else {
            value = (hiByte << 8) | loByte;
          }

          max = Math.max(max, value);
        }    // for
      } else {

        /*
         * 8 bit - no endianness issues, just sign.
         */
        for (int i = 0; i < buffer.length; i++) {
          int value;

          if (signed) {
            value = buffer[i];
          } else {
            short shortVal = 0;

            shortVal = (short) (shortVal | buffer[i]);
            value = shortVal;
          }

          max = Math.max(max, value);
        }    // for
      }        // 8 bit

      /*
       * Express max as float of 0.0 to 1.0 of max value of 8 or 16 bits (signed
       * or unsigned).
       */
      if (signed) {
        if (use16Bit) {
          level = (int) max / MAX_16_BITS_SIGNED;
        } else {
          level = (int) max / MAX_8_BITS_SIGNED;
        }
      } else {
        if (use16Bit) {
          level = (int) max / MAX_16_BITS_UNSIGNED;
        } else {
          level = (int) max / MAX_8_BITS_UNSIGNED;
        }
      }

      return level;
    }
  }
}
