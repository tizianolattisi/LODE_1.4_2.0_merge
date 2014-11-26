package it.unitn.LODE.utils;
/*
 * FileSystemUtils.java
 *
 * Created on 29-nov-2007, 22.17.28
 *
 *
 * @author ronchet
 */

import it.unitn.LODE.MP.utils.SystemProps;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.gui.InspectorWindow;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import javax.swing.*;
import javax.swing.filechooser.FileView;

public class FileSystemManager {
    // ========== SOLITON PATTERN ==============================================

    static FileSystemManager instance = null;

    public static FileSystemManager getInstance() {
        if (instance == null) {
            instance = new FileSystemManager();
        }
        return instance;
    }

    private FileSystemManager() {
    }
    //==========================================================================
    JPanel panel;

    void setJPanel(JPanel p) {
        panel = p;
    }

    /**
     * Attempts to create the specified folder - including all the needed
     * directories in the path. Issues a warning if the folder already exists
     *
     * @param folder
     * @return true if folder exists at the end of the process, false otherwise
     */
    public boolean createFolder(File folder) {

        if (!folder.exists()) {
            folder.mkdirs();
        } /*
         * else { System.err.println("WARNING - DIRECTORY ALREDY EXISTS:
         * "+folder.getAbsolutePath());
        }
         */
        return folder.isDirectory();
    }

    public boolean createFile(File outFile, String[] content) {
        boolean success = false;
        try {
            // Create a file as course marker
            FileOutputStream fos = new FileOutputStream(outFile);
            PrintWriter out = new PrintWriter(fos);
            for (String line : content) {
                out.println(line);
            }
            out.flush();
            out.close();
            fos.close();
            success = true;
        } catch (FileNotFoundException ex) {
            Messanger m = Messanger.getInstance();
            ex.printStackTrace(m.getLogger());
            m.w("Unable to create file " + outFile.getAbsolutePath(), LODEConstants.MSG_ERROR);
        } catch (IOException ex) {
            Messanger m = Messanger.getInstance();
            ex.printStackTrace(m.getLogger());
            m.w("Unable to create file " + outFile.getAbsolutePath(), LODEConstants.MSG_ERROR);
        }
        return success;
    }

    /**
     * Recursively deletes a folderOrFile and its content.
     *
     * @param folderOrFile
     * @return
     */
    public boolean recursiveDelete(File folderOrFile) {
        // it it is a directory, recursively delete the content
        if (folderOrFile.isDirectory()) {
            for (String name : folderOrFile.list()) {
                File child = new File(folderOrFile.getAbsolutePath() + File.separator + name);
                if (child.isDirectory()) {
                    recursiveDelete(child);
                } else {
                    child.delete();
                }
            }
        }
        // finally delete the empty folder or file
        return folderOrFile.delete();
    }

    public String selectAFolder(String initialPath,
            final String containedFileName,
            final String title,
            final String acceptButtonText,
            final String filterDescription,
            final Icon icon) {
        String path = null;
        // if (LODE.MAC) { // use swing component
        JFileChooser fc = new JFileChooser(initialPath);
        fc.setApproveButtonText(acceptButtonText);
        fc.setDialogTitle(title);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {

            public boolean accept(File d) {
                if (!d.isDirectory()) {
                    return false;
                }
                File f = new File(d.getAbsolutePath()
                        + File.separator
                        + containedFileName);
                return f.exists();
            }

            @Override
            public String getDescription() {
                return filterDescription;
            }
        };
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(ff);
        fc.setFileView(new FileView() {

            @Override
            public String getName(File f) {
                return null; //let the L&F FileView figure this out
            }

            @Override
            public String getDescription(File f) {
                return null; //let the L&F FileView figure this out
            }

            @Override
            public Boolean isTraversable(File f) {
                return null; //let the L&F FileView figure this out
            }

            private boolean isValid(File d) {
                if (!d.isDirectory()) {
                    return false;
                }
                File f = new File(d.getAbsolutePath()
                        + File.separator
                        + containedFileName);
                if (f.exists()) {
                    return true;
                }
                return false;
            }

            @Override
            public String getTypeDescription(File f) {
                if (isValid(f)) {
                    return "Valid LODE Course";
                }
                return "Not a valid LODE Course";
            }

            @Override
            public Icon getIcon(File f) {
                if (isValid(f)) {
                    return icon;
                }
                return null;
            }
        });
        //fc.setFileFilter(ff);
        //fc.addChoosableFileFilter(ff);
        //fc.setControlButtonsAreShown(false);
        //fc.setAcceptAllFileFilterUsed(false);
        int status = fc.showOpenDialog(panel);
        if (status == JFileChooser.APPROVE_OPTION) {
            path = fc.getSelectedFile().getAbsolutePath();
        } else {
            path = null;
        }
        /*
         * } else { // on a mac use the native FileDialog Frame f=new Frame();
         * FileDialog dialog =new FileDialog (f, "Select A
         * Folder",FileDialog.LOAD); dialog.setFilenameFilter(new
         * FilenameFilter(){ public boolean accept(File arg0, String arg1) { if
         * (! arg0.isDirectory()) return false; File f=new
         * File(arg0.getAbsolutePath()+File.separator+containedFileName); return
         * f.exists(); }} ); dialog.setVisible(true);
         * path=dialog.getDirectory(); System.out.println(path);
        }
         */
        return path;
    }

    public String selectAFolderForReading(String initialPath) throws Exception {
        String path = null;
        /*
        if (!SystemProps.IS_OS_MAC_OSX) { // use swing component
            JFileChooser fc = new JFileChooser(initialPath);
            fc.setDialogTitle("Select A Folder");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setApproveButtonText("Select");
            fc.showOpenDialog(panel);
            path = fc.getSelectedFile().getAbsolutePath();
        } else
        */ 
        { // on a mac use the native FileDialog
            FileDialog dialog = new FileDialog(InspectorWindow.getInstance(), "Select A Folder", FileDialog.LOAD);
            dialog.setVisible(true);
            path = dialog.getDirectory();
        }
        return path;
    }

    public String selectAFolderForWriting(String initialPath) throws Exception {
        String path = null;
        /*
        if (!SystemProps.IS_OS_MAC_OSX) { // use swing component
            JFileChooser fc = new JFileChooser(initialPath);
            fc.setDialogTitle("Select A Folder");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setApproveButtonText("Select");
            fc.showOpenDialog(panel);
            path = fc.getSelectedFile().getAbsolutePath();
        } else 
        */ 
        { // on a mac use the native FileDialog
            FileDialog dialog = new FileDialog(InspectorWindow.getInstance(), "Select A Folder", FileDialog.SAVE);
            dialog.setVisible(true);
            path = dialog.getDirectory();
        }
        return path;
    }

    public String selectAFile(String initialPath, final String filter) throws Exception {
        String fileName = null;
        String path = null;
        /*
        if (!SystemProps.IS_OS_MAC_OSX) { // use swing component
            JFileChooser fc = new JFileChooser(initialPath);
            fc.setDialogTitle("Select a file");
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setApproveButtonText("Select");
            fc.showOpenDialog(panel);
            fileName = fc.getSelectedFile().toString();
            path = fc.getSelectedFile().getAbsolutePath();
            int dotPos = fileName.lastIndexOf(".");
            String extension = fileName.substring(dotPos + 1);
            if (!extension.equalsIgnoreCase(filter)) {
                JOptionPane.showMessageDialog(null, "Wrong file type selected! Should be " + filter);
                path = "";
            }
        } else 
        */ 
        { // on a mac use the native FileDialog
            FileDialog dialog = new FileDialog(new Frame(), "Select a file", FileDialog.LOAD);
            dialog.setDirectory(initialPath);
            dialog.setFilenameFilter(new FilenameFilter() {

                public boolean accept(File arg0, String arg1) {
                    return (arg1.endsWith(filter));
                }
            });
            dialog.setVisible(true);
            fileName = dialog.getFile();
            if (fileName == null) {
                return null;
            }
            String dir = dialog.getDirectory();
            path = dir + File.separator + fileName;//new File(fileName).getAbsolutePath();
        }
        return path;
    }

    final boolean DEBUG=false;
    PrintStream ostream=System.err;
    public void nioCopyFile(File sourceFile, File destFile) throws IOException {
        if (DEBUG) ostream.println("COPYING "+sourceFile.getPath()+" INTO "+destFile.getPath());
        if (sourceFile.isDirectory()) {
            if (!destFile.exists()) {
                destFile.mkdirs();
            }

            if (DEBUG) ostream.println("DIR COPY "+sourceFile.getPath()+" INTO "+destFile.getPath());
            String[] children = sourceFile.list();
            for (int i = 0; i < children.length; i++) {
                nioCopyFile(new File(sourceFile, children[i]), new File(destFile, children[i]));
            }
        } else {
            if (!destFile.exists()) {
                if (DEBUG) ostream.println(">>>>>> FILE DOES NOT ESIST:"+destFile);
                if (! destFile.getParentFile().exists()) {
                    if (DEBUG) ostream.println(">>>>>> DIR DOES NOT ESIST:"+destFile.getParentFile());
                    destFile.getParentFile().mkdirs();
                    if (! destFile.getParentFile().exists()) {
                        ostream.println(">>>>>> DIR still DOES NOT ESIST:"+destFile.getParentFile());
                        ostream.println("IS THE LOCATION SPECIFIED IN COURSE_PUBLICATION.XML CORRECT?");
                    }
                }
                destFile.createNewFile();
            }

            FileChannel source = null;
            FileChannel destination = null;
            try {
                source = new FileInputStream(sourceFile).getChannel();
                destination = new FileOutputStream(destFile).getChannel();
                if (DEBUG) ostream.println("FILE COPY "+sourceFile.getPath()+" INTO "+destFile.getPath());

                destination.transferFrom(source, 0, source.size());
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            }
        }
    }

    public void cannotCopyMessage(IOException ex, String destinationPath, String sourcePath) {
        Messanger m = Messanger.getInstance();
        m.w("CANNOT COPY! " + sourcePath
                + " -> " + destinationPath,
                LODEConstants.MSG_ERROR);
        ex.printStackTrace(m.getLogger());
    }

    public void copyFiles(File targetLocation, File sourceLocation) throws IOException {
        //abilteremo la copia con nio (più efficiente) dopo averne verificato la funzionaltà
        boolean nio = true;
        if (nio) {
            nioCopyFile(sourceLocation, targetLocation);
        } else {
            if (sourceLocation.isDirectory()) {
                if (!targetLocation.exists()) {
                    targetLocation.mkdir();
                }
                String[] children = sourceLocation.list();
                for (int i = 0; i < children.length; i++) {
                    copyFiles(new File(targetLocation, children[i]), new File(sourceLocation, children[i]));
                }
            } else {
                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }
    }

    void deleteFile(String path) {
        File f = new File(path);
        f.delete();
    }

    public String getRealPath(String filename) {
        String path = "";
        try {
            URL url = FileSystemManager.class.getResource(filename);
            path = url.getFile().replace("%20", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public String getRealPath() {
        String path = "";
        File f = new File("");
        path = f.getAbsolutePath();
        return path;
    }

    public boolean fileExists(String filename) {
        File f = new File(filename);
        return f.exists();
    }

    public String getUserDirectory() {
        String curDir = System.getProperty("user.dir");
        return curDir;
    }
    //==========================================================================

    /**
     * launches an external browser, opens in it the passed URL and minimized
     * the passed frame
     *
     * @param url the URL to be shown in the defalt browser
     * @param frame to be minimized (can be null)
     * @return true if succesful, false otherwise.
     */
    public final boolean showInBrowser(String url, Frame frame) {
        //minimizes the app
        if (frame != null) {
            frame.setExtendedState(JFrame.ICONIFIED);
        }

        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
        try {
            if (os.indexOf("win") >= 0) {
                String[] cmd = new String[4];
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                cmd[2] = "start";
                cmd[3] = url;
                rt.exec(cmd);
            } else if (os.indexOf("mac") >= 0) {
                rt.exec("open " + url);
            } else {
                //prioritized 'guess' of users' preference
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                    "netscape", "opera", "links", "lynx"};

                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++) {
                    cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");
                }

                rt.exec(new String[]{"sh", "-c", cmd.toString()});
                //rt.exec("firefox http://www.google.com");
                //System.out.println(cmd.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    "\n\n The system failed to invoke your default web browser while attempting to access: \n\n " + url + "\n\n",
                    "Browser Error",
                    JOptionPane.WARNING_MESSAGE);

            return false;
        }
        return true;
    }
}
