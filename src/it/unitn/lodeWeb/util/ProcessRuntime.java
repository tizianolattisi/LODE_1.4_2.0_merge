package it.unitn.lodeWeb.util;

//--------------------------------------------------------------------------
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class ProcessRuntime {

    public enum Sop {

        WINDOWS, MAC, LINUX, NULL
    }

    //--------------------------------------------------------------------------
    /**
     * 	Function to determine the operating system
     *
     * @return Sop -  operating system (SO)
     */
    //--------------------------------------------------------------------------
    public Sop getSo() {
        Sop sisOp = Sop.NULL;
        String os = System.getProperty("os.name");
        boolean WINDOWS = (os.toLowerCase().indexOf("windows") >= 0);
        boolean MAC = (os.toLowerCase().indexOf("mac") >= 0);
        boolean LINUX = (os.toLowerCase().indexOf("linux") >= 0);
        if (WINDOWS) {
            sisOp = Sop.WINDOWS;
        }
        if (MAC) {
            sisOp = Sop.MAC;
        }
        if (LINUX) {
            sisOp = Sop.LINUX;
        }

        return sisOp;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Read the output of one process
     *
     * @param child - process
     *
     * @return String - output lines
     *
     * @throws java.io.IOException
     */
    //--------------------------------------------------------------------------
    public String readInputStream(Process child) throws IOException {
        String result = null;
        // Read from an input stream
        InputStream in = child.getInputStream();
        int c;
        while ((c = in.read()) != -1) {
            result += ((char) c);
        }
        in.close();

        in = child.getErrorStream();
        while ((c = in.read()) != -1) {
            result += ((char) c);
        }
        in.close();
        child.notify();
        return result;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * 	Performs one process
     *
     * @param comand - A specified system command.
     * @param   envp -array of strings, each element of which has environment variable settings in the format name=value,
     *          or null if the subprocess should inherit the environment of the current process.
     * @param runDir - the working directory of the subprocess, or null if the subprocess should inherit the working directory of the current process.
     *
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     * 
     * @return Process
     */
    //--------------------------------------------------------------------------
    public Process execute(String comand, String[] envp, File runDir) throws InterruptedException, IOException {
        String result = null;
        Process p;
        // Execute a command
        if (runDir == null) {
            p = Runtime.getRuntime().exec(comand);
        } else {
            p = Runtime.getRuntime().exec(comand, envp, runDir);
        }

        //Scanner sc = new Scanner(child.getInputStream());
        return p;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * @param comand String[] - array of strings, array of commands
     * @param   envp -array of strings, each element of which has environment variable settings in the format name=value,
     *          or null if the subprocess should inherit the environment of the current process.
     * @param runDir - the working directory of the subprocess, or null if the subprocess should inherit the working directory of the current process.
     *
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     *
     * @return Process
     */
    //--------------------------------------------------------------------------
    public Process execute(String[] comand, String[] envp, File runDir) throws InterruptedException, IOException {
        String result = null;
        Process p;
        // Execute a command
        if (runDir == null) {
            p = Runtime.getRuntime().exec(comand);
        } else {
            p = Runtime.getRuntime().exec(comand, envp, runDir);
        }

        //int exitValue =  p.waitFor();

        //Scanner sc = new Scanner(child.getInputStream());
        return p;
    }
}
