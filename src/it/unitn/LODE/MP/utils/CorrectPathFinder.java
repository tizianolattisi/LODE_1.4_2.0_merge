/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.MP.utils;

import it.unitn.LODE.MP.constants.LODEConstants;
import java.io.File;
import java.net.URL;

/**
 *
 * @author ronchet
 */
public class CorrectPathFinder {
   /**
    * in the distribution, filename is file:/path/.../Java/LODE.jar...
    * This method throws away head (file:) and tail (/Java/LODE.jar)
    * @param s the logical path of the resource, typically /dir/resource
    * @return the physical path of the resource
    */
    public static String prefix = null;
    public static final boolean DEBUG=true;
    public static String getJarPath(String s){
        String userdir=System.getProperty("user.dir");
        //System.out.println("userdir is : "+userdir);
        return userdir+s;
    }
    public static String getPath(String s) {
        // calculate correct prefix (only the first time)
        if (prefix == null) {
            // la sample directory deve essere dentro src e nell'eseguibile mac a fianco del jar
            String sample = "/Resources";
            if (DEBUG) System.err.println("Evaluating prefix for "+ sample);
            String jarName = LODEConstants.jarName;
            int jarNameLenght = jarName.length();
            URL url = CorrectPathFinder.class.getResource(sample);
            String urlFile = url.getFile();
            urlFile=urlFile.replaceAll("%20"," ");
            if (DEBUG) printMsg("URL ===> :" + urlFile);
            File f = new File(urlFile);
            if (f.exists()) { //inside the jar
                prefix = urlFile.substring(0,urlFile.indexOf(sample));
            } else {
                int jarNamePos = urlFile.indexOf(jarName + "!");
                if (jarNamePos==-1) { // NOT IN THE a jar
                    prefix=urlFile.substring(0,urlFile.indexOf(sample));
                } else {
                    if (DEBUG) printMsg("pos=" + jarNamePos);
                    String u = urlFile.substring(jarNamePos + jarNameLenght + 1);
                    if (DEBUG) printMsg("u=" + u);
                    String p = urlFile.substring(5, jarNamePos - 1);
                    prefix = p; // in classes
                    if (DEBUG) printMsg(p + "$" + u);
                }
            }
            if (DEBUG) System.out.println("prefix is :"+prefix);
        }
        if (DEBUG) System.err.println("CorrectPathFinder - IN :" + s);
        String path=null;
        if (new File(s).exists()) {
            path=s;
        } else {
            path = prefix + s;
            File f = new File(path);
            if (! f.exists()) {
                System.err.println("ERROR IN FINDING PATH [A] FOR " + s +
                        "\n wrong path is:"+path);
                return null;
            }
        }
        if (DEBUG) System.err.println("CorrectPathFinder - OUT:" + path);
        return path;
    }
    public static String getPath3(String s) { // THIS IS NEVER USED!!!
        // calculate correct prefix (only the first time)
        if (prefix == null) {
            // la sample directory deve essere dentro src e nell'eseguibile mac a fianco del jar
            String sample = LODEConstants.FS + "Resources";
            if (DEBUG) System.err.println("Evaluating prefix for "+ sample);
            String jarName = LODEConstants.jarName;
            int jarNameLenght = jarName.length();
            URL url = CorrectPathFinder.class.getResource(sample);
            String urlFile = url.getFile();
            if (DEBUG) printMsg("URL:" + urlFile);
            File f = new File(urlFile);
            if (f.exists()) { //inside the jar
                prefix = urlFile.substring(0,urlFile.indexOf(sample));
            } else {
                int jarNamePos = urlFile.indexOf(jarName + "!");
                if (DEBUG) printMsg("pos=" + jarNamePos);
                String u = urlFile.substring(jarNamePos + jarNameLenght + 1);
                if (DEBUG) printMsg("u=" + u);
                String p = urlFile.substring(5, jarNamePos - 1);
                prefix = p; // in classes
                if (DEBUG) printMsg(p + "$" + u);
            }
            if (DEBUG) System.out.println("prefix is :"+prefix);
        }
        if (DEBUG) System.err.println("CorrectPathFinder - IN :" + s);
        String path=null;
        if (new File(s).exists()) {
            path=s;
        } else {
            path = prefix + s;
            File f = new File(path);
            if (! f.exists()) {
                System.err.println("ERROR IN FINDING PATH [B] FOR " + s +
                        "\n wrong path is:"+path);
                return null;
            }
        }
        if (DEBUG) System.err.println("CorrectPathFinder - OUT:" + path);
        return path;
    }
    /* UNUSED PREVIOUS VERSION
    public static String getPath2(String s) {
        System.out.println("CorrectPathFinder - IN :"+s);
        String jarName=LODEConstants.jarName;
        int jarNameLenght=jarName.length();
        URL url=CorrectPathFinder.class.getResource(s);
        String urlFile=url.getFile();
        String path=urlFile;
        printMsg("URL:"+urlFile);
        File f=new File(urlFile);
        if (! f.exists()) {
            int jarNamePos=urlFile.indexOf(jarName+"!");
            printMsg("pos="+jarNamePos);
            String u=urlFile.substring(jarNamePos+jarNameLenght+1);
            printMsg("u="+u);
            String p=urlFile.substring(5,jarNamePos-1);
            printMsg(p+"$"+u);
            path=p+u;
            f=new File(path);
        }
        printMsg("file exists:"+f.exists());
        System.out.println("CorrectPathFinder - OUT:"+s);
        if (f.exists()) return path;
        else return null;
    }
    */
    private static void printMsg(String s){
                System.out.println(s);
    }

}
