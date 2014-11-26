package it.unitn.LODE.services;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.utils.LoggedProcessWaiter;
import it.unitn.LODE.utils.ProcessWaiter;
import it.unitn.LODE.utils.RunnableProcess;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ronchet
 */
public class Scripter {
    /* If you want shell features, run a shell:

String[] cmd = {
"/bin/sh",
"-c",
"dir | grep gpc | grep -v 25"
};

Process p = Runtime.getRuntime().exec(cmd);
*/
    public Scripter(){
    }
    public Scripter(String args[]){
        demo();
    }
    public void demo(){
        String from="log.txt";
        setMachine("localhost");
        setPassword("julia22");
        //remoteCopy(LODEConstants.LODE_HOME+LODEConstants.FS+from,"123_log.txt",true);

        String cmd="/bin/ls -la";
        //cmd="/usr/bin/java -jar /Users/ronchet/NetBeansProjects/LODE/release/LODE.app/Contents/Resources/Java/LODE.jar HEADLESS POSTPROCESS_ALL";
        //remoteLaunch(cmd, true);
        localLaunch(new String[]{
            "#/bin/sh",
            "/usr/bin/java -jar /Users/ronchet/NetBeansProjects/LODE/release/LODE.app/Contents/Resources/Java/LODE.jar HEADLESS POSTPROCESS_ALL"
        },true);
        /*
        // launch headless postprocessing
        localLaunch(new String[]{
            "#/bin/sh",
            "/usr/bin/java -jar /Users/ronchet/NetBeansProjects/LODE/release/LODE.app/Contents/Resources/Java/LODE.jar HEADLESS POSTPROCESS_ALL"
        });
        localLaunch(new String[]{
            "#/bin/sh",
            "cd /Users/ronchet/_LODE/COURSES/Pippo_2008/Distribution",
            "/usr/bin/zip -r archive.zip  01_Test_2008-11-15 ../COURSE.XML"
        }, true);*/
        //System.exit(0);
    }
    /**
     * Copies vis scp a file tp a remote machine
     * Machine, user and password are specified with the setters methods
     * @param from path of the local file to be copied
     * @param to path of the remote destination file
     * @param isSynchronous if true the execution is sequential, i.e. it will
     * wait for the end of the copy before returning - else it will return
     * immediately after launching the copy
     */
    public void remoteCopy(String from, String to, boolean isSynchronous){
        SimpleDateFormat df = new SimpleDateFormat("HHmmssSSS");
        File temp=createTempFile("scp"+df.format(new Date()));
        createScpCommandFile(temp,from,to);
        // now launch the script
        String command2[]=new String[3];
        command2[0]="/usr/bin/expect";
        command2[1]="-f";
        command2[2]=temp.getAbsolutePath();
        Process p=RunnableProcess.launch(command2, true, false);
        LoggedProcessWaiter lpw=new LoggedProcessWaiter(p,"remote copy");
        lpw.start();
        if (isSynchronous) {
            try {
                lpw.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        //temp.delete();
    }
    /**
     * Executes vis ssh a command on a remote machine
     * Machine, user and password are specified with the setters methods
     * @param cmd the (single) command to be executed remotely
     * @param isSynchronous if true the execution is sequential, i.e. it will
     * wait for the end of the script before returning - else it will return
     * immediately after launching the script     */
    public void remoteLaunch(String cmd, boolean isSynchronous){
        SimpleDateFormat df = new SimpleDateFormat("HHmmssSSS");
        File temp=createTempFile("ssh"+df.format(new Date()));
        createSshCommandFile(temp,cmd);
        // now launch the script using expect
        String command2[]=new String[3];
        command2[0]="/usr/bin/expect";
        command2[1]="-f";
        command2[2]=temp.getAbsolutePath();
        Process p=RunnableProcess.launch(command2, true, false);
        LoggedProcessWaiter lpw=new LoggedProcessWaiter(p,"remote script");
        lpw.start();
        if (isSynchronous) {
            try {
                lpw.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        temp.delete();
    }
    /**
     *
     * @param a array of strings containing the list of commands to be execcuted
     * (i.e. the script body). It usually starts with "#/bin/sh"
     * @param isSynchronous if true the execution is sequential, i.e. it will
     * wait for the end of the script before returning - else it will return
     * immediately after launching the script
     */
    public void localLaunch(String [] a, boolean isSynchronous){
        SimpleDateFormat df = new SimpleDateFormat("HHmmssSSS");
        File temp=createTempFile("script"+df.format(new Date()));
        //createScriptFile(temp);
        prepareFile(temp,a);
        chmod(temp,"777");
        // now launch the script
        String command2[]=new String[3];
        command2[0]="/bin/sh";
        command2[1]="-c";
        command2[2]=temp.getAbsolutePath();
        Process p=RunnableProcess.launch(command2, true, false);
        LoggedProcessWaiter lpw=new LoggedProcessWaiter(p,"script");
        lpw.start();
        if (isSynchronous) {
            try {
                if (lpw.isAlive()) lpw.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        temp.delete();
    }
 
// ========================================================================
/* To run  command (e.g. ls)via ssh from the shell:
 * create the following file (called e.g. "cmd")
spawn "/bin/sh"
expect "$"
send "ssh ronchet@localhost ls\r"
expect "Password:"
send "pw\r"
 * where pw is your password
 * then execute:
 /usr/bin/expect -f cmd
 */
   private void createSshCommandFile(File f, String cmd){
        String nl_quote="\\r\"";
        //
        String [] a={
            //"set  timeout  30",
            "set send_human {.1 .3 1 .05 2}", //fast and consistent typist
            "spawn \"/bin/sh\"",
            "expect \"$\"",
            "send -h \"ssh "+user+"@"+machine+"  "+cmd+nl_quote,
            "expect \"assword:\"",
            "send -h \""+password+nl_quote,
            "expect \"$\"",
            "exit"
        };
        prepareFile(f,a);
   }

   private void createScpCommandFile(File f, String from, String to){
        String nl_quote="\\r\"";
        //
        String [] a={
            "set send_human {.1 .3 1 .05 2}", //fast and consistent typist
            "spawn \"/bin/sh\"",
            "expect \"$\"",
            "send -h \"scp "+from+" "+user+"@"+machine+":"+to+nl_quote,
            "expect \"assword:\"",
            "send -h \""+password+nl_quote,
            "expect \"$\"",
            "exit"
        };
        prepareFile(f,a);
   }
   /**
    * create a file starting from its content expressed as an array of Strings
    * @param file name of the file to be generated
    * @param content content of the file's rows
    */
   private void prepareFile(File file,String[] content){
        // write the content of the temporary file =============================
        if (file.exists()) file.delete();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        FileOutputStream output=null;
        try {
            output = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        PrintWriter channel = new PrintWriter(output);
        for (String s:content) {
            channel.println(s);
        }
        channel.flush();
        channel.close();
   }
   /**
    * Execute externally the chmod shell command
    * @param f file on which permissions shuould be changed
    * @param permission expresssed with unix syntax
    */
   private void chmod(File f,String permission){
        String [] command={"chmod",permission,f.getAbsolutePath()};
        final Process chmod=RunnableProcess.launch(command, true, false);
        ProcessWaiter waitForChmod= new ProcessWaiter(chmod,"chmod");
        waitForChmod.start();
   }

   private File createTempFile(String filename) {
        // Create temporary file and make it executable ========================
        File tempDir=new File(LODEConstants.TEMP_DIR);
        if (!tempDir.exists()) tempDir.mkdir();
        File f=new File(LODEConstants.TEMP_DIR+LODEConstants.FS+filename);
        if (f.exists()) f.delete();
        try {
            f.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return f; 
   }
   // FIELDS AND SETTERS =======================================================
    private String password="egov2008";
    private String user="lode";
    private String machine="latemar.science.unitn.it";
    public String REMOTE_INBOX="_LODE/TEMP";

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
