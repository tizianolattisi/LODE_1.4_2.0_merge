/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.MP.utils;

import java.text.*;
import java.util.Date;

/**
 *
 * @author ronchet
 */
public class Clock {
    private DateFormat df =null;
    // ========== SOLITON PATTERN ==============================================
    static Clock instance=null;
    public synchronized static Clock getInstance(){
        if (instance==null) instance=new Clock();
        return instance;
    }
    private Clock() {
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
    }
    //==========================================================================
     public final String getDateTime() {
        Date d=new Date();
        return df.format(d);
    }

}
