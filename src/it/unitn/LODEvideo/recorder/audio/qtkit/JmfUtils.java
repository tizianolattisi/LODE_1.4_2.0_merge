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

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import net.mc_cubed.qtcubed.media.protocol.quicktime.DataSource;

/**
 * Utilities to make a bridge between Jmf and java sound audio formats.
 *
 * @author pietro.pilolli
 */
public class JmfUtils {

  /**
   * Convert a JMF format to a JavaSound format.
   * @param fmt
   * @return javax.sound.sampled.AudioFormat
   */
  public static javax.sound.sampled.AudioFormat convertFormat(
          javax.media.format.AudioFormat fmt) {

    return new javax.sound.sampled.AudioFormat(
            (fmt.getSampleRate()
            == javax.media.format.AudioFormat.NOT_SPECIFIED
            ? 8000f : (float) fmt.getSampleRate()),
            (fmt.getSampleSizeInBits()
            == javax.media.format.AudioFormat.NOT_SPECIFIED
            ? 16 : fmt.getSampleSizeInBits()),
            (fmt.getChannels()
            == javax.media.format.AudioFormat.NOT_SPECIFIED
            ? 1 : fmt.getChannels()),
            (fmt.getSigned()
            == javax.media.format.AudioFormat.SIGNED
            ? true : false),
            (fmt.getEndian()
            == javax.media.format.AudioFormat.BIG_ENDIAN
            ? true : false));
  }

  /*
   * Get jmf audio stream.
   */
  private static PushBufferStream getAudioStream(
          PushBufferDataSource pbds) {
    if (pbds.getStreams() == null) {
      /*
       * No audio stream.
       */
      return null;
    }

    int streamLenght = pbds.getStreams().length;

    if (streamLenght == 0) {
      /*
       * No audio stream.
       */
      return null;
    }

    for (int i = 0; i < streamLenght; i++) {
      PushBufferStream stream = pbds.getStreams()[i];

      javax.media.Format jmfFormat = stream.getFormat();
      if (jmfFormat instanceof javax.media.format.AudioFormat) {
        /*
         * I have found the jmf audio stream!
         */
        return stream;
      }

    }

    return null;
  }

  /*
   * Get jmf audio stream.
   */
  private static PushBufferStream getAudioStream(
          DataSource ds) {

    if (!(ds instanceof PushBufferDataSource)) {
      /*
       * No audio stream.
       */
      return null;
    }

    PushBufferDataSource pbds = (PushBufferDataSource) ds;

    if (pbds.getStreams() == null) {
      /*
       * No audio stream.
       */
      return null;
    }

    return getAudioStream(pbds);
  }


  /*
   * Get the Jmf Audio format.
   */
  private static javax.media.format.AudioFormat getJmfAudioFormat(
          PushBufferStream audioStream) {
    javax.media.Format format = audioStream.getFormat();
    if (format instanceof javax.media.format.AudioFormat) {
      return (javax.media.format.AudioFormat) format;
    }
    /*
     * It is not an audio device!
     */
    return null;
  }

  /**
   * Build an audio input stream associated to the data source.
   * @param ds
   * @return AudioInputStream
   */
  public static AudioInputStream toAudioInputStream(DataSource ds) {

    PushBufferStream pbas = getAudioStream(ds);

    if (pbas == null) {
      /*
       * No audio stream.
       */
      return null;
    }

    InputStream is = new JmfAudioInputStream(pbas);

    javax.media.format.AudioFormat jmfAudioFormat = getJmfAudioFormat(pbas);

    if (jmfAudioFormat == null) {
      /*
       * No audio stream.
       */
      return null;
    }

    AudioFormat javaSoundFormat = convertFormat(jmfAudioFormat);

    /*
     * Log the java audio sound format.
     */
    Logger.getLogger(JmfUtils.class.getName()).log(Level.INFO,
            "Java Sound Audio format to be passed to the lode encoder:"
            + " encoding {0}, "
            + "{1} Mhz, "
            + "{2} bit, "
            + "BigEndian {3}, "
            + "channells {4}",
            new Object[]{javaSoundFormat.getEncoding(),
              javaSoundFormat.getFrameRate(),
              javaSoundFormat.getSampleSizeInBits(),
              javaSoundFormat.isBigEndian(),
              javaSoundFormat.getChannels()});

    return new AudioInputStream(is,
            javaSoundFormat,
            AudioSystem.NOT_SPECIFIED);


  }
}
