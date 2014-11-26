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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import javax.media.Buffer;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.PushBufferStream;

/**
 * Implement an input stream where store Jmf audio stream.
 *
 * @author pietro.pilolli
 */
public class JmfAudioInputStream extends InputStream
        implements BufferTransferHandler {

  private List<Buffer> buffers = new CopyOnWriteArrayList();
  private int available = 0;
  private Semaphore semaphore = new Semaphore(0);
  private boolean blocked = false;
  private boolean eom = false;

  /**
   * Constructor.
   *
   * @param inputStream
   */
  @SuppressWarnings("LeakingThisInConstructor")
  public JmfAudioInputStream(PushBufferStream inputStream) {
    inputStream.setTransferHandler(this);
  }

  /**
   * Returns the number of bytes that can be read (or skipped over) from this
   * input stream without blocking by the next caller of a method for this input
   * stream.
   *
   * @return int
   */
  @Override
  public int available() {
    return available;
  }

  /**
   * Reads the next byte of data from the input stream.
   *
   * @return
   * @throws IOException
   */
  @Override
  public int read() throws IOException {
    if (eom) {
      return -1;
    }

    if (buffers.isEmpty()) {
      blocked = true;
      try {
        semaphore.acquire();
      } catch (InterruptedException e) {
        return -1;
      }
    }

    byte[] buff = new byte[1];
    int count = readBytes(buff);

    return count == -1 ? -1 : buff[0] & 0xff;
  }

  /**
   * Reads some number of bytes from the input stream and stores them into the
   * buffer array b.
   *
   * @param buff
   * @return
   */
  @Override
  public int read(byte[] buff) {
    if (buffers.isEmpty()) {
      blocked = true;
      try {
        semaphore.acquire();
      } catch (InterruptedException e) {
        return -1;
      }
    }
    return readBytes(buff);
  }

  /**
   * Notification from the PushBufferStream to the handler that data is
   * available to be read from stream.
   *
   * @param stream
   */
  @Override
  public void transferData(PushBufferStream stream) {
    Buffer buffer = new Buffer();
    try {
      stream.read(buffer);
      byte[] data = (byte[]) buffer.getData();
      buffer.setData(data);
      buffer.setOffset(0);
      buffer.setLength(data.length);
    } catch (IOException e) {
      /*
       * Ignored exception.
       */
    }

    available += (buffer.getLength() - buffer.getOffset());
    buffers.add(buffer);

    if (blocked) {
      blocked = false;
      semaphore.release();
    }
  }

  private int readBytes(byte[] buff) {
    if (buffers.isEmpty()) {
      return -1;
    }

    int count = 0;
    while (count < buff.length && !buffers.isEmpty()) {
      Buffer buffer = buffers.get(0);
      byte[] data = (byte[]) buffer.getData();

      int remainder = buff.length - count;
      int len = Math.min(remainder, buffer.getLength() - buffer.getOffset());

      System.arraycopy(data, buffer.getOffset(), buff, count, len);
      count += len;

      buffer.setOffset(buffer.getOffset() + len);
      if (buffer.getOffset() == buffer.getLength()) {
        buffers.remove(0);
      }

      if (buffer.isEOM()) {
        eom = true;
        //break;
      }

    }

    available -= count;
    return count;
  }
}
