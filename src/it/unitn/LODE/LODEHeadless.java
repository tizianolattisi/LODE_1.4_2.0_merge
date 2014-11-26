package it.unitn.LODE;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.services.PostProducer;
import it.unitn.LODE.MP.utils.Clock;
import it.unitn.LODE.MP.utils.CorrectPathFinder;
import it.unitn.LODE.utils.FileSystemManager;
import it.unitn.LODE.utils.Messanger;
import java.io.File;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This class creates a GUI-less instance of LODE to be used for performing
 * operations like postprocessing
 * @author ronchet
 */
public class LODEHeadless {

    public static boolean WINDOWS = false;
    public static boolean MAC = false;
    public static boolean LINUX = false;
    FileSystemManager fileSystemManager = null;
    Messanger messanger = null;

    public static void main(String[] args) {
        // ==== FIND OUT WHICH PLATFORM ARE WE RUNNING ON ======================
        String os = System.getProperty("os.name");
        WINDOWS = (os.toLowerCase().indexOf("windows") >= 0);
        MAC = (os.toLowerCase().indexOf("mac") >= 0);
        LINUX = (os.toLowerCase().indexOf("linux") >= 0);
        // ========== SET Look&Feel ============================================
        String lookAndFeel = null;
        if (LINUX) {
            lookAndFeel = "Metal";
        } else //lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        {
            lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        }
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn't find class for specified look and feel:" + lookAndFeel);
            System.err.println("Did you include the L&F library in the class path?");
            System.err.println("Using the default look and feel.");
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Can't use the specified look and feel (" + lookAndFeel + ") on this platform.");
            System.err.println("Using the default look and feel.");
        } catch (Exception e) {
            System.err.println("Couldn't get specified look and feel (" + lookAndFeel + "), for some reason.");
            System.err.println("Using the default look and feel.");
        }
        // http://forums.sun.com/thread.jspa?threadID=5336611
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

        // ========== START PROCESS ============================================
        new LODEHeadless(args);
    }
    private final String POSTPROCESS_ALL = "POSTPROCESS_ALL";
    private final String POSTPROCESS_COURSE = "POSTPROCESS_COURSE";
    private final String POSTPROCESS_LECTURE = "POSTPROCESS_LECTURE";

    /**
     * Params syntax:
     * POSTPROCESS_ALL
     * POSTPROCESS_COURSE course-name
     * POSTPROCESS_LECTURE course-name lecture-name
     * @param args
     */
    public LODEHeadless(String[] args) {
        // make sure this is not run from within LODE
        if (fileSystemManager != null) {
            messanger.log("LODEHeadless started in wrong context");
            System.err.println("LODEHeadless started in wrong context");
            System.exit(1);
        }
        // ========= SETUP SINGLETONS =========================================
        fileSystemManager = FileSystemManager.getInstance();


        // ============ make sure the course directory exists
        File lodeDir = new File(LODEConstants.LODE_HOME);
        if (!lodeDir.exists()) {
            fileSystemManager.createFolder(lodeDir);
        }

        // ============ make sure the course directory exists
        File coursesDir = new File(LODEConstants.COURSES_HOME);
        if (!coursesDir.exists()) {
            fileSystemManager.createFolder(coursesDir);
        }

        LODEPreferences prefs = LODEPreferences.getInstance();

        // ========= SETUP SINGLETONS =========================================
        messanger = Messanger.getInstance();

        // ============ make sure the log file exists
        File log = new File(LODEConstants.LOG_FILE);
        if (!log.exists()) {
            String content[] = {Clock.getInstance().getDateTime() + "Log file created"};
            fileSystemManager.createFile(log, content);
        } else {
            messanger.log(Clock.getInstance().getDateTime() + "Headless LODE started -----------v");
        }

        // =============== Check if ffmpegX is available
        String actualFFMPEGX_PATH=CorrectPathFinder.getJarPath(LODEConstants.FFMPEG_COMMAND);
        //File ffmpegxFile = new File(LODEConstants.FFMPEGX_PATH);
        File ffmpegxFile = new File(actualFFMPEGX_PATH);
        if (!ffmpegxFile.exists()) {
            messanger.log("No ffmpegX for Java installed");
            System.exit(1);
        }
        //File flvtool2File = new File(LODEConstants.FLVTOOL2_PATH);
        String actualFLV_PATH=CorrectPathFinder.getJarPath(LODEConstants.FLVTOOL2_PATH);        
        File flvtool2File = new File(actualFLV_PATH);
        if (!flvtool2File.exists()) {
            messanger.log("No flvtool2 installed");
            System.exit(1);
        }
        try {
            exec_command(args);
        } catch (InterruptedException ex) {
            ex.printStackTrace(messanger.getLogger());
        }
        System.out.println("OK");
        messanger.log(Clock.getInstance().getDateTime() + "Headless LODE ended ___________^");
        System.exit(0);
    }

    private void exec_command(String[] args) throws InterruptedException {
        if (args.length < 2) {
            quitWithError("NO ARGUMENTS");
        }
        if (args[1].equalsIgnoreCase(POSTPROCESS_ALL)) {
            Thread t = PostProducer.getInstance().convertAllLectures(); // ONLY FOR MAC!
            t.join();
        } else if (args[1].equalsIgnoreCase(POSTPROCESS_COURSE)) {
            if (args.length != 2) {
                quitWithError("WRONG NUMBER OF ARGUMENTS " + args);
            }
            // postprocess course
        } else if (args[1].equalsIgnoreCase(POSTPROCESS_LECTURE)) {
            if (args.length != 3) {
                quitWithError("WRONG NUMBER OF ARGUMENTS " + args);
            }
            //postprocesss lecture
        }
    }

    private void quitWithError(String s) {
        messanger.log(Clock.getInstance().getDateTime() + s);
        messanger.log(Clock.getInstance().getDateTime() + "Headless LODE ended ___________^");
        System.exit(1);
    }
}
