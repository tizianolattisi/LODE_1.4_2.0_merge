package it.unitn.LODE.utils;

import it.unitn.LODE.MP.constants.LODEConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
/**
 * Launches and manages extarnal processes
 * Only the static methods of this class should be called!
 * The only public instance method (run) is public only for formal reasons!
 */
public class RunnableProcess implements Runnable {
    private Process p=null;
    private String cmd[]=null;
    // the thread that will shut down all started processes after closing the VM
    private static Thread shutdownHook;
    // the list of started processes
    private static HashSet<RunnableProcess> processList=null;
    //private constructor
    // keep a list of all processes that have to be killed when
    // the VM stops
    // add a thread that will be executed when the VM stops.
    private static Messanger m=Messanger.getInstance();
    /**
     *
     * @param surviveable true if the process has to survive after
     * the end of the java application
     */
    private RunnableProcess(boolean surviveable) {
        // if the process list does not exist yet,
        // create it and create a shoutdown hook to be launched
        // for final cleanup
        if (processList==null) {
            processList=new HashSet<RunnableProcess>();
            shutdownHook = new Thread() {
                public void run( ) {
                Iterator<RunnableProcess> iter=processList.iterator();
                    while (iter.hasNext()) {
                        Process p=iter.next().getProcess();
                        // if the process did not finish yet, kill it
                        try {
                            int exitValue = p.exitValue();
                        } catch (IllegalThreadStateException ex) {
                            ex.printStackTrace(m.getLogger());
                            p.destroy();
                        }
                    }
                }
            };
            Runtime.getRuntime( ).addShutdownHook(shutdownHook);
        }
        // add this process to the list of killable processes
        if (! surviveable) processList.add(this);
    }

    private Process getProcess() {return p;}

    /**
     * the actual launcher of the process
     * This method must be public for formal reasons but users should NEVER call it!
     */
    public void run() {
        try {
            p=Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            ex.printStackTrace(m.getLogger());
            m.w("Error while launching command "+cmd,LODEConstants.MSG_ERROR);
        }
    }
    /**
     * This is the method that should be called to launch an external command.
     * The launched process deos not survive the application.
     * If launching the program fails, the app quits.
     * @param s a string array defining an executable command and its parameters
     * @return the launched process
     */
    public static Process launch(String s[]){
        return launch(s,false,true);
    }

    /**
     * This is the method that should be called to launch an external command.
     * It has full options.
     * @param s a string array defining an executable command and its parameters
     * @param survivable if false the process is killed when the VM quits
     * @param exitOnFail if true the program stops when launching the process fails
     * @return
     */
    public static Process launch(String s[], boolean survivable, boolean exitOnFail){
        RunnableProcess r=new RunnableProcess(survivable);
        r.cmd=s;
        Thread t=new Thread(r);
        t.start();
        Process p=r.getProcess();
        int count=0;
        m.log("launching process "+s[0]);
        while (p==null&&count<10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                m.w("Interrupded exception on lauching process "+s[0],LODEConstants.MSG_WARNING);
                m.log("Interrupded exception on lauching process "+s[0]);
            }
            count++;
            m.log(""+count+": WARNING - PROCESS NOT YET ACTIVE");
            p=r.getProcess();
        }
        if (p==null){
            m.w("Unable to launch process"+s[0],LODEConstants.MSG_ERROR);
            m.log("Unable to launch process"+s[0]);
            if (exitOnFail) System.exit(1);
        }
        m.log("Process successfully launched!");
        return p;
    }

    /**
     * check if a process is still active
     * @param p the process
     * @return true if p is alive
     */
    public static boolean isActive(Process p){
        try {
                int exitValue = p.exitValue();
                return false;
        } catch (IllegalThreadStateException e) {
                return true;
        }
    }
    /**
     * this is here only for testing purposes
     * @param a
     */
    public static void main(String a[]){
        try {
            //String s[]={"osascript"};
            //String s[]={"/Application/Utility/Terminal.app/Contents/MacOS/Terminal"};
            String[] s = {"/bin/ls"};
            Process child = launch(s, true, true);
            // Get the input stream and read from it


            //printf "Encoding started on " && date && /Applications/ffmpegX.app//Contents/Resources/ffmpeg -i /Users/ronchet/_LODE_COURSES/W_2008/Acquisition/01_W_2008-09-01 -y -map 0.0:0.0 -f flv -vcodec flv -b 200 -aspect 4:3 -s 320x240 -r 12 -g 240 -me epzs -qmin 2 -qmax 15 -acodec mp3 -ab 56 -ar 22050 -ac 2  -map 0.1:0.1 -benchmark /Users/ronchet/_LODE_COURSES/W_2008/Acquisition/01_W_2008-09-01.ff.flv && /Applications/ffmpegX.app//Contents/Resources/flvtool2 -UP /Users/ronchet/_LODE_COURSES/W_2008/Acquisition/01_W_2008-09-01.ff.flv && printf "Encoding completed on " && date && printf "\a"
            InputStream in = child.getInputStream();
            int c;
            while ((c = in.read()) != -1) {
                System.out.print((char) c);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

