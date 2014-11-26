/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.utils;
import it.unitn.LODE.MP.utils.CorrectPathFinder;
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.constants.LODEConstants;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
public class FreeSpaceFinder {
    public static void findSpace() {
        Messanger m=Messanger.getInstance();
        int space=(int) (new File(LODEConstants.LODE_HOME).getFreeSpace()/(1024*1024));
        //Double hours=space/(LODEConstants.MBperHour*3600);
        Double hours=((double)space)/LODEConstants.MBperHour;
        hours=Math.floor(hours*10)/10;
        int type=LODEConstants.MSG_LOG;
        if (hours<=10) type=LODEConstants.MSG_WARNING;
        m.w("Free Megabytes on disk: "+space);
        m.w("Available recording hours: "+hours, type);
    }
    public static void findSpace2() {
        // this is the old implementation based on running an os command
        Messanger m=Messanger.getInstance();
        //m.w("entering findspace",LODEConstants.MSG_ERROR);
    	try {
            m.w("User Directory is :"+ControllersManager.getinstance().getFileSystemManager().getUserDirectory(),LODEConstants.MSG_ERROR);
            String scriptsPath=CorrectPathFinder.getPath(LODEConstants.SCRIPT_PREFIX);

            //String scriptsPath=FreeSpaceFinder.class.getResource(LODEConstants.SCRIPT_PREFIX).getFile();
            //if (scriptsPath.indexOf("/LODE.jar!/")>-1) scriptsPath=scriptsPath.replaceFirst("/LODE.jar!/","/");
            /*m.w("PATH = "+
                    scriptsPath
                    //+ "\n" + scriptsPath.indexOf("/LODE.jar!/")
                    );*/
            Process p2x = Runtime.getRuntime().exec("chmod a+x "+scriptsPath);
            try {
                p2x.waitFor();
                //Process p1 = Runtime.getRuntime().exec("ls -l "+scriptsPath+"freespace");
                //Scanner sc1 = new Scanner(p1.getInputStream());
                //System.out.println(sc1.nextLine());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            Process p2 = Runtime.getRuntime().exec("chmod a+x "+scriptsPath+"freespace");
            try {
                p2.waitFor();
                //Process p1 = Runtime.getRuntime().exec("ls -l "+scriptsPath+"freespace");
                //Scanner sc1 = new Scanner(p1.getInputStream());
                //System.out.println(sc1.nextLine());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
    		//Process p1 = Runtime.getRuntime().exec("ls -l "+scriptsPath+"freespace");
            //Scanner sc1 = new Scanner(p1.getInputStream());
            //System.out.println(sc1.nextLine());

    		Process p = Runtime.getRuntime().exec(scriptsPath+"freespace "+LODEConstants.LODE_HOME);
    		Scanner sc = new Scanner(p.getInputStream());
            //System.out.println(sc.nextLine());
    		int space=Integer.parseInt(sc.nextLine())/1024;
                int space2=(int) (new File(LODEConstants.LODE_HOME).getFreeSpace()/(1024*1024));
                System.out.println("FREE SPACE DIFFERENCE="+(space2-space));
                System.out.println(space2);

            //System.out.println("Megabyte available on disk: "+space/1024);
            Double hours=space/(0.165*3600);
            hours=Math.floor(hours*10)/10;
            int type=LODEConstants.MSG_LOG;
            if (hours<=10) type=LODEConstants.MSG_WARNING;
            m.w("Free Megabytes on disk: "+space);
            m.w("Available recording hours: "+hours, type);
        }
    	catch (IOException e) {
            m.w(e.toString(),LODEConstants.MSG_ERROR);
    	}
    }
}


