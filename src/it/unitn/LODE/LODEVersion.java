/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.LODE;

/**
 *
 * @author ronchet
 */
public class LODEVersion {
     public final static String VERSION="1.40.031 xuggle - windows: "+BuildTime.build_time; // current version name
// uses:
    public static final String[] uses={
        "jpedal lgpl    : ?? http://www.jpedal.org/gpl.php",
        "apache poi : 3.7-20101029 http://poi.apache.org/",
        "jogl            : 1.1.1 22/5/2008 https://jogl.dev.java.net/ (UNUSED?)",
        "simple-xml : 2.6.2 20111016 http://sourceforge.net/projects/simple",
        "                  -- stax-1.2.0 (UNUSED?)",
        "                  -- stax-api-1.0.1 (UNUSED?)",
        "xuggler    : 5.4  http://xuggle.googlecode.com/svn/trunk/repo/share/java/xuggle/xuggle-xuggler/"            
    };   
}
