/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.itunesuMetadata.model.components;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author ronchet
 */
public class TimeStamp {

         // Mon, 29 May 2010 12:24:21 +0000

  final static String DATE_FORMAT_NOW = "EEE, dd MMM yyyy HH:mm:ss ZZZZ";

  public static String now() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    return sdf.format(cal.getTime());

  }
}
