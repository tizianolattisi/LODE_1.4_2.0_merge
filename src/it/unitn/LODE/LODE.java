/**
 *
 * @author ronchet
 */
package it.unitn.LODE;


import it.unitn.LODE.Controllers.ActionController;
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MAC.HandlersInitializer;
import it.unitn.LODE.MP.IF.Controllers.VideoControllerIF;
import it.unitn.LODE.MP.IF.HandlersIF;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.MP.utils.Clock;
import it.unitn.LODE.MP.utils.CorrectPathFinder;
import it.unitn.LODE.MP.utils.SystemProps;
import it.unitn.LODE.Models.Course;
import it.unitn.LODE.Models.ProgramState;
import it.unitn.LODE.gui.AcquisitionWindow;
import it.unitn.LODE.gui.InspectorWindow; 
import it.unitn.LODE.gui.MenuManager;
import it.unitn.LODE.gui.SplashScreen;
import it.unitn.LODE.services.Scripter;
import it.unitn.LODE.utils.ExeExpander;
import it.unitn.LODE.utils.FileSystemManager;
import it.unitn.LODE.utils.FreeSpaceFinder;
import it.unitn.LODE.utils.Messanger;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/** Main class for the LODE app.
 * It initializes...
 */
public class LODE {


    private static SplashScreen splash;
    VideoControllerIF videoController = null;
    FileSystemManager fileSystemManager = null;
    Messanger messanger = null;
    public static boolean TESTING = false;
    Thread shutdownHook = null;
    File markerFile = null;
     /**
   * List directory contents for a resource folder. Not recursive.
   * This is basically a brute-force implementation.
   * Works for regular files and also JARs.
   * 
   * @author Greg Briggs
   * @param clazz Any java class that lives in the same place as the resources you want.
   * @param path Should end with "/", but not start with one.
   * @return Just the name of each member item, not the full paths.
   * @throws URISyntaxException 
   * @throws IOException 
   */
   private String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
      URL dirURL = clazz.getClassLoader().getResource(path);
      System.out.println("looking for "+path);
      System.out.println("class is "+clazz);

      System.out.println(dirURL);
      if (dirURL != null && dirURL.getProtocol().equals("file")) {
        System.out.println("it is a file");
        /* A file path: easy enough */
        return new File(dirURL.toURI()).list();
      } 

      if (dirURL == null) {
        /* 
         * In case of a jar file, we can't actually find a directory.
         * Have to assume the same jar as clazz.
         */
        String me = clazz.getName().replace(".", "/")+".class";
        dirURL = clazz.getClassLoader().getResource(me);
      }
      System.out.println(dirURL);

      if (dirURL.getProtocol().equals("jar")) {
        System.out.println("it is a jar");

        /* A JAR path */
        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
        JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
        Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
        while(entries.hasMoreElements()) {
          String name = entries.nextElement().getName();
          if (name.startsWith(path)) { //filter according to the path
            String entry = name.substring(path.length());
            int checkSubdir = entry.indexOf("/");
            if (checkSubdir >= 0) {
              // if it is a subdirectory, we just return the directory name
              entry = entry.substring(0, checkSubdir);
            }
            result.add(entry);
          }
        }
        return result.toArray(new String[result.size()]);
      } 
        
      throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
  }
   static LODE instance=null;
    private LODE() {
        //java -Duser-dir=. -Djava.library.path=./lib -jar LODE.jar
        
        /* LISTA LE RISORSE
        StringBuffer buffer=new StringBuffer();
        for (URL url : ((URLClassLoader)(Thread.currentThread().getContextClassLoader())).getURLs()) {
            buffer.append(new File(url.getPath()));
            buffer.append("\"");
        };
        String path=buffer.toString();
        path=path.replaceAll("\"", "\n");

        try {
        String[] list=getResourceListing(this.getClass(),"Resources/Images/");
        for (String s : list) {System.out.println(s);}
        } catch (Exception e) {e.printStackTrace();}

        //*=================================================
        URL dirURL = this.getClass().getClassLoader().getResource("Resources/Images/LODE.gif");
        System.out.println("dirURL is "+dirURL);
        //ImageIcon ICON_LODE=new ImageIcon(dirURL,"LODE");
        System.out.println(LODEConstants.ICON_LODE+" "+LODEConstants.ICON_LODE.getIconHeight()+"x"+LODEConstants.ICON_LODE.getIconWidth());
        System.out.println(LODEConstants.ICON_SPLASH+" "+LODEConstants.ICON_SPLASH.getIconHeight()+"x"+LODEConstants.ICON_SPLASH.getIconWidth()); 
        //System.exit(1);
        //====================================================   */ 
        //ImageIcon ICON_LODE=LODEConstants.ICON_LODE;
        //System.out.println(ICON_LODE+" "+ICON_LODE.getIconHeight()+"x"+ICON_LODE.getIconWidth());

        
        if (SystemProps.IS_OS_MAC_OSX) {
            LODEConstants.VLC_PATH=LODEConstants.VLC_PATH_ON_MAC;
        } else if (SystemProps.IS_OS_WINDOWS) {
            LODEConstants.VLC_PATH=LODEConstants.VLC_PATH_ON_WINDOWS;
        }
        System.out.println(SystemProps.USER_HOME);

        ControllersManager controllersManager=ControllersManager.getinstance();
        fileSystemManager = controllersManager.getFileSystemManager();
        _cleanTemporaryFiles();
        _checkDirectoryStructure();
        _checkIfAnotherCopyIsRunning();
        _showSplashScreen();

        // ========= SETUP SINGLETONS IN RIGHT ORDER ===========
        controllersManager.instatiatePrimaryManagers();
        messanger=controllersManager.getMessanger();

        _checkSoftwareDependencies();
        _startLogFile();
        _registerShutdownHook();

        controllersManager.instatiateSecondaryManagers();
        controllersManager.checkManagers();
        //System.exit(1);

        // ============= run mac specific initialization
        HandlersIF handler = new Handlers();
        if (SystemProps.IS_OS_MAC_OSX)
            new HandlersInitializer()._macInit(handler); // ONLY FOR MAC! mac specific initialization
        _startupSwing();
        instance=this;

    }
    public static LODE getInstance(){return instance;}
    
    public void cleanup() {
        // remove marker file that avoids double execution
        ProgramState programState=ProgramState.getInstance();
        AcquisitionWindow aw=ControllersManager.getinstance().getAcquisitionWindow();
        if (aw!=null) aw.clearInstance();
        // else VideoControllerIF.getInstance().cleanup();
        programState.resetLecture();
        markerFile.delete();

        _cleanTemporaryFiles();
        Messanger.getInstance().log(Clock.getInstance().getDateTime() + "LODE ended");
        Messanger.getInstance().closeLogFile();
    }    
    private void _checkDirectoryStructure() {
        // ============ make sure the LODE directory exists
        File lodeDir = new File(LODEConstants.LODE_HOME);
        if (!lodeDir.exists()) {
            fileSystemManager.createFolder(lodeDir);
        }
        // ============ make sure the temporary directories exists
        File tempDir2=new File(LODEConstants.TEMP_DIR2);
        if (!tempDir2.exists()) {
            fileSystemManager.createFolder(tempDir2);              
        }
        File tempDir=new File(LODEConstants.TEMP_DIR);
        if (!tempDir.exists()) {
            fileSystemManager.createFolder(tempDir);              
        }
        // ============ make sure the course directory exists
        File coursesDir = new File(LODEConstants.COURSES_HOME);
        if (!coursesDir.exists()) {
            fileSystemManager.createFolder(coursesDir);
        }
    }
    
    private void _cleanTemporaryFiles(){
        System.err.println("running cleanTempfiles");

        File tempdir = new File(LODEConstants.TEMP_DIR);
        fileSystemManager.recursiveDelete(tempdir);
        tempdir = new File(LODEConstants.TEMP_DIR2);
        fileSystemManager.recursiveDelete(tempdir);
    }
    private void _startLogFile(){
        String content[] = {Clock.getInstance().getDateTime() + "LODE started"};
        // =========== clean and create logfile
        File logFile = new File(LODEConstants.LOG_FILE);
        if (logFile.exists()) {
            logFile.delete();
        }
        fileSystemManager.createFile(logFile, content);
    }

    private void _checkSoftwareDependencies() {
        // =============== Check if the postprocessing tools are available
        //String actualFFMPEGX_PATH=CorrectPathFinder.getJarPath(LODEConstants.FFMPEG_COMMAND);
        File tempDir=new File(LODEConstants.TEMP_DIR2);
        //System.out.println(LODEConstants.FFMPEG_COMMAND);
        String path=LODEConstants.FFMPEGX_RES_PATH;
        InputStream is = LODE.class.getResourceAsStream(path);
        if (is==null) {
             path=CorrectPathFinder.getJarPath(LODEConstants.FFMPEGX_FS_PATH);
             is = LODE.class.getClassLoader().getResourceAsStream(path);
        }
        if (is==null) System.err.println("unable to open ffmpeg");
        else {
        System.err.println("successfully opened ffmpeg");
        try {
            is.close();
        } catch (IOException ex) {
            Logger.getLogger(LODE.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        System.out.println(path);
   
        String actualFFMPEGX_PATH=LODEConstants.FFMPEG_COMMAND;
        //(new ExeExpander()).expandResource(path,actualFFMPEGX_PATH);

        File ffmpegxFile = new File(actualFFMPEGX_PATH);
        if (!ffmpegxFile.exists()) {
            messanger.log("No ffmpeg installed");
            int choice = JOptionPane.showConfirmDialog(null,
                    "<HTML>It seems that ffmpeg is not installed.<BR>"
                    + "Can't find it in "+actualFFMPEGX_PATH+"<BR>"
                    + "Although you will be able to record videos, you cannot prostprocess them on this machine without ffmpegX.<BR><BR>"
                    + "Do you want to continue anyway?",
                    "No ffmpeg installed?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.NO_OPTION) {
                cleanup();
            }
        } 
        //=================================================
        //String actualFLV_PATH=CorrectPathFinder.getJarPath(LODEConstants.FLVTOOL2_PATH);        
        
        path=LODEConstants.FLVTOOL2_RES_PATH;
        is = LODE.class.getResourceAsStream(path);
        if (is==null) {
             path=CorrectPathFinder.getJarPath(LODEConstants.FLVTOOL2_FS_PATH);
             is = LODE.class.getClassLoader().getResourceAsStream(path);
        }
        if (is==null) System.err.println("unable to open flvtool2");
        else {
        System.err.println("successfully opened flvtool2");
        try {
            is.close();
        } catch (IOException ex) {
            Logger.getLogger(LODE.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        System.out.println(path);
   
        String actualFLV_PATH=LODEConstants.FLVTOOL2_PATH;
        (new ExeExpander()).expandResource(path,actualFLV_PATH);
        
        File flvtool2File = new File(actualFLV_PATH);
        File vlcFile = new File(LODEConstants.VLC_PATH);
        if (!flvtool2File.exists()) {
            messanger.log("No flvtool2 installed");
            int choice = JOptionPane.showConfirmDialog(null,
                    "<HTML>It seems that flvtool was not installed.<BR>"
                    + "Although you will be able to record and postprocess videos, teh resulting files will mnot have all the desired properties.<BR><BR>"
                    + "Do you want to continue anyway?",
                    "No flvtool installed?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.NO_OPTION) {
                cleanup();
                System.exit(1);
            }
        }
        /*
        if (!vlcFile.exists()) {
            messanger.log("No VLC installed");
            int choice = JOptionPane.showConfirmDialog(null,
                    "<HTML>It seems that VLC (<FONT COLOR=\"BLUE\">http://www.videolan.org/vlc/</FONT>) is not installed.<BR>"
                    + "You will not be able to create the web site for videos on this machine without VLC.<BR><BR>"
                    + "Do you want to continue anyway?",
                    "No VLC installed?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.NO_OPTION) {
                cleanup();
                System.exit(1);
            }
        }*/
    }
    private void _checkIfAnotherCopyIsRunning(){
        markerFile = new File(LODEConstants.MARKER_FILE);
        // =============== check if another copy is running
        if (markerFile.exists()) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Another copy of LODE seems to be running, or the previous run may have finished incorrectly. Do you want to continue anyway?",
                    "Another copy is running?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.NO_OPTION) {
                System.exit(1);
            }
        } else {
            String content[] = {"This is a marker file showing that an instance of LODE is running.",
                "This file should be(have been) cancelled when LODE quit(ted)."};
            fileSystemManager.createFile(markerFile, content);
        }
    }
    private void _showSplashScreen(){
        // ======== Show splash screen
    /*System.out.println("Image splash is "+LODEConstants.ICON_SPLASH);    
    URL imgURL = LODE.class.getResource(LODEConstants.IMAGE_SPLASH);
    if (imgURL != null) {
            splash = new SplashScreen(new ImageIcon(imgURL));
    } else { // we might be loading from a jar... 
        
    }       
    System.out.println(imgURL);
    ImageIcon imageIcon;
    if (imgURL.getProtocol().equals("jar")) 
        imageIcon=new ImageIcon(imgURL);
    else
        imageIcon=new ImageIcon(LODE.class.getResource(LODEConstants.IMAGE_SPLASH));
    if (imageIcon==null) System.err.println("Couldn't find file for splash screen ");
    splash = new SplashScreen(imageIcon);
    */
            splash = new SplashScreen(LODEConstants.ICON_SPLASH);

       /* java.net.URL imgURL = LODE.class.getResource(LODEConstants.IMAGE_SPLASH);
        ImageIcon imageIcon = null;
        if (imgURL != null) {
            splash = new SplashScreen(new ImageIcon(imgURL));
        } else {
            System.err.println("Couldn't find file for splash screen ");
        }*/
        // close the splash screen after a while
        Thread t = new Thread(new Runnable() {
            public void run() {
                it.unitn.LODE.MP.utils.Util.sleep(LODEConstants.SPLASHSCREEN_SECONDS);
                if (splash != null) {
                    splash.close();
                }
            }
        });
        t.start();
    }
    private void _registerShutdownHook(){
        // ======== Create shutdown handlers for cleanup  
        shutdownHook = new Thread() {
            @Override
            public void run() {
                cleanup();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
    private void _startupSwing(){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TESTING = LODEPreferences.getInstance().get(LODEPreferences.debug).equalsIgnoreCase("YES");
                TESTING = false;
                InspectorWindow inspector = InspectorWindow.getInstance();
                inspector.setVisible(true);
                Messanger.getInstance().w("Welcome to LODE!\nVersion: " + LODEVersion.VERSION, LODEConstants.MSG_ERROR);
                FreeSpaceFinder.findSpace();
                Messanger.getInstance().w("To start, you should open or create a course - or open an existing lecture in a course", LODEConstants.MSG_SUGGESTION);
                // "free space is " df -h ~ | awk '/dev/ { print $4 }'
                //AcquisitionWindow.getInstance();
                MenuManager.getInstance().updateMenuState();
                //videoController.reset(); // fix Quicktime on the mac
                //====================
                if (TESTING) {
                    automa();
                }
            }
        });
    }
    // =========================================================================
    private void automa() {
        // automatic operations to speed up debugging and testing
        // OPEN TEST COURSE =======================================
        String cDirName = LODEConstants.COURSES_HOME + File.separator + "__test_2011";
        String cName = cDirName + File.separator + LODEConstants.SERIALIZED_COURSE;
        Course course = null;
        try {
            course = (Course) (new Course()).resume(new File(cName), Course.class);
        } catch (Exception ex) {
            Logger.getLogger(LODE.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (course != null) {
            ProgramState.getInstance().setCurrentCourse(course);
            InspectorWindow.getInstance().update();
        }
        ActionController ac=ControllersManager.getinstance().getActionController(); 
        //ActionEvent ae=new ActionEvent(this, 0, ActionController.OPEN_COURSE);
        //ac.actionPerformed(ae);
        ActionEvent ae = new ActionEvent(this, 0, ActionController.NEW_LECTURE);
        ac.actionPerformed(ae);

        // OPEN TEST LECTURE =======================================
        /*
        Messanger m=Messanger.getInstance();
        m.w("TESTING MODE",LODEConstants.MSG_LOG);
        Lecture lecture=null;
        String  lDirName=LODEConstants.COURSES_HOME+File.separator+"__test_2011/Acquisition/02__2011-01-22";
        String lName=lDirName+File.separator+LODEConstants.SERIALIZED_LECTURE;
        lecture=(Lecture)(new Lecture()).resume(new File(lName),Lecture.class);
        if (lecture!=null) {
        ProgramState.setCurrentLecture(lecture);
        ProgramState.setCurrentCourse(lecture.getCourse());
        InspectorWindow.getInstance().update();
        }
        ActionController ac=ActionController.getInstance();
        ActionEvent ae=new ActionEvent(this, 0, ActionController.SEND_LECTURE);
        //ac.actionPerformed(ae);
         */
    }

   
    // =================== STATIC SECTION ======================================
    public static void main(String[] args) {

        // Note: this is a mac-only section.
        // =====================================================================
        //The following line seems to work only if it is the first line in the code
        //see http://lists.apple.com/archives/java-dev/2003/Jun/msg00104.html
        //else you must pass -Xdock:name="Lode" as runtime option
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Lode");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // ==== FIND OUT WHICH PLATFORM ARE WE RUNNING ON ======================
        // EXIT IF NOT A MAC!
        if (!(SystemProps.IS_OS_MAC_OSX||SystemProps.IS_OS_WINDOWS)) {
            JOptionPane.showMessageDialog(null,
                    "Sorry, this version of LODE only runs on MAC AND WINDOWS...",
                    "Only for Mac",
                    JOptionPane.WARNING_MESSAGE);
            //System.exit(1);
        }
        // this is needed by JCQ
        if (SystemProps.IS_OS_MAC_OSX) {
            System.setProperty("com.apple.eawt.CocoaComponent.CompatibilityMode", "false");
        }
        // ========== END OF MAC SECTION =======================================
        _setLookAndFeel();
        // ========== START PROCESS ============================================
        if (args.length == 0) {
            new LODE();
        } else if (args[0].equalsIgnoreCase("HEADLESS")) {
            new LODEHeadless(args);
        } else if (args[0].equalsIgnoreCase("SCRIPTER")) {
            new Scripter(args);
        }
    }
    private static void _setLookAndFeel(){
        String lookAndFeel = null;
        if (SystemProps.IS_OS_LINUX) {
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
    }
}
