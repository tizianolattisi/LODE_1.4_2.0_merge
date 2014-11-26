package it.unitn.LODE.services;
/**
 * SlideImporter.java
 * 
 */
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.Models.ProgramState;
import it.unitn.LODE.gui.AcquisitionWindow;
import it.unitn.LODE.gui.InspectorWindow;
import it.unitn.LODE.gui.ProgressBar;
import it.unitn.LODE.services.slides.PDFParser;
import it.unitn.LODE.services.slides.PPTParser;
import it.unitn.LODE.MP.utils.CorrectPathFinder;
import it.unitn.LODE.utils.FileSystemManager;
import it.unitn.LODE.utils.Messanger;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
public class SlideImporter {
    // ========== SOLITON PATTERN ==============================================
    static SlideImporter instance=null;
    public static synchronized SlideImporter getInstance(){
        if (instance==null) instance=new SlideImporter();
        return instance;
    }
    private SlideImporter() {
        fileSystemManager=ControllersManager.getinstance().getFileSystemManager();
        messanger=ControllersManager.getinstance().getMessanger();

    }
    //==========================================================================
    FileSystemManager fileSystemManager;
    Messanger messanger;
    public static final int REPLACE=2;
    public static final int APPEND=1;
    public static final int DO_NOTHING=0;
    /**
     * Import images, titles and text from ppt or pdf files.<br>
     * Known issues: for pdf files text and title are not yet imported,<br>
     * The quality of the jpgs produced by pdf is much higher than that produced by ppt!
     *  
     * @param lecture  the lecture into which slides are imported
     * @param extension  the type of file (only ppt and pdf are supported 
     * - null if you want to import fake slades as placeholders)
     */
    public final void importSlides(Lecture lecture,String extension){
        // place where the slides should be imported
        AcquisitionWindow acquisitionWindow=ControllersManager.getinstance().getAcquisitionWindow();
        String destinationPath=lecture.getAcquisitionPath()+LODEConstants.ACQUISITION_IMG_SUBDIR;
        File destinationDir=new File(destinationPath);
        // check if this lecture already has slides and find if you want to replace or append
        int mode=checkExistingSlides();
        if (mode==DO_NOTHING) return;
        if (mode==REPLACE) {
            lecture.zapSlides();
            fileSystemManager.recursiveDelete(destinationDir);
        }

        if (! destinationDir.exists()) fileSystemManager.createFolder(destinationDir);                
        int slidesAlreadyPresent=lecture.getSlideCount();
        // temporary place where we shall find the slides 
        String slidesDirectoryPath=LODEConstants.TEMP_DIR+LODEConstants.SLIDES_PREFIX;
        //Import slides depending on extension
        //================================= FAKE SLIDES ========================
        if (extension==null) {// null extension, import fake slides
            // in the distribution app, filename is file:/path/.../Java/LODE.jar...
            // throw away head and tail!
            slidesDirectoryPath=CorrectPathFinder.getPath(LODEConstants.SLIDES_RES_PREFIX);
            File sourceFolder = new File(slidesDirectoryPath);
            String[] sourceFiles = sourceFolder.list();
            int NUM_IMAGES = sourceFiles.length;
            int n=0;
            for (int i = 1; i <= NUM_IMAGES; i++) {
                n=i+slidesAlreadyPresent;
                String source=slidesDirectoryPath+i+".jpg";
                String destination=destinationPath+LODEConstants.FS+n+".jpg";
                System.out.println(source+"->"+destination);
                try {
                    fileSystemManager.copyFiles(new File(destination),new File(source));
                } catch (IOException ex) {
                    fileSystemManager.cannotCopyMessage (ex,  destinationPath,  slidesDirectoryPath);
                }                  
                lecture.addSlide(n," Slide "+n," Slide "+n,LODEConstants.SLIDES_RES_PREFIX + n + ".jpg");
                //System.out.println("ADD < Slide "+n+"><"+LODEConstants.SLIDES_RES_PREFIX + n + ".jpg>");
            }
            lecture.addSlidesGroup("#PLACEHOLDERS#", slidesAlreadyPresent+1, n);
            lecture.persistSlides();
            if (acquisitionWindow!=null) acquisitionWindow.redrawElements();
            return;
       //================================= JPG ================================
        } else if (extension.equalsIgnoreCase("jpg")) {// jpg extension, as generated from ppt
            try {
                slidesDirectoryPath=fileSystemManager.selectAFolderForReading(LODEConstants.WORKING_DIR);
            } catch (Exception ex) {ex.printStackTrace(messanger.getLogger()); return;}
            File sourceFolder = new File(slidesDirectoryPath);
            String[] sourceFiles = sourceFolder.list();
            int NUM_IMAGES = sourceFiles.length;
            int n=0;
            for (int i = 1; i <= NUM_IMAGES; i++) {
                n=i+slidesAlreadyPresent;
                String source=slidesDirectoryPath+"Slide"+i+".jpg";
                String destination=destinationPath+LODEConstants.FS+n+".jpg";
                System.out.println(source+"->"+destination);
                try {
                    fileSystemManager.copyFiles(new File(destination),new File(source));
                } catch (IOException ex) {
                    fileSystemManager.cannotCopyMessage (ex,  destinationPath,  slidesDirectoryPath);
                }                  
                lecture.addSlide(n," Slide "+n," Slide "+n,LODEConstants.SLIDES_PREFIX + n + ".jpg");
                //System.out.println("ADD < Slide "+n+"><"+LODEConstants.SLIDES_RES_PREFIX + n + ".jpg>");
            }
            lecture.addSlidesGroup("#JPEG#"+slidesDirectoryPath, slidesAlreadyPresent+1, n);
            lecture.persistSlides();
            if (acquisitionWindow!=null) acquisitionWindow.redrawElements();
            return;
        //================================= NOSLIDES============================
       } else if (extension.equalsIgnoreCase("noslides")) {
           slidesDirectoryPath=CorrectPathFinder.getPath(LODEConstants.NO_SLIDES_PREFIX);
            try {
                fileSystemManager.copyFiles(new File(destinationPath), new File(slidesDirectoryPath));
            } catch (IOException ex) {
                fileSystemManager.cannotCopyMessage (ex,  destinationPath,  slidesDirectoryPath);
            }
            lecture.addSlide(1," Slide "+1," Slide "+1,LODEConstants.NO_SLIDES_PREFIX + 1 + ".jpg");
            lecture.addSlidesGroup("#NOSLIDES#", 1, 1);
            //System.out.println("adding fake slide");
            lecture.persistSlides();
            if (acquisitionWindow!=null) acquisitionWindow.redrawElements();
            return;            
        //================================= OTHER TYPES ========================
        } else { // select a file and import slides from it
            // select the file to be imported
            String pathOfFileToBeImported=null;
            try {
 
                pathOfFileToBeImported = fileSystemManager.selectAFile(LODEConstants.WORKING_DIR, extension);
                if (pathOfFileToBeImported==null) return;
            } catch (Exception ex) {ex.printStackTrace(messanger.getLogger()); return;}

            if (((pathOfFileToBeImported != null) && 
                    !pathOfFileToBeImported.equalsIgnoreCase("null/null")) ) {
                //==============================================================
                // make a copy of the selected file in Acquisition
                String filename=pathOfFileToBeImported;
                if (filename.lastIndexOf(File.separator)!=-1) 
                    filename=pathOfFileToBeImported.substring(1+filename.lastIndexOf(File.separator));
                String sourcesDir=lecture.getAcquisitionPath()+LODEConstants.SLIDES_SOURCES_SUBDIR;
                try {
                    fileSystemManager.createFolder(new File(sourcesDir));
                    fileSystemManager.copyFiles(new File(sourcesDir+filename), 
                            new File(pathOfFileToBeImported));
                } catch (IOException ex) {
                    
                        messanger.w("CANNOT COPY SOURCE SLIDES! "+destinationPath+
                        " -> "+ pathOfFileToBeImported,
                        LODEConstants.MSG_ERROR
                        );
                ex.printStackTrace(messanger.getLogger());
                }
                //==============================================================
                //String command[]={"cp",pathOfFileToBeImported,lecture.getAcquisitionPath()+LODEConstants.SLIDES_SOURCES_SUBDIR+filename};
                //final Process p=RunnableProcess.launch(command, false, false);
                /*
                try {
                    destinationPath=lecture.getAcquisitionPath()+LODEConstants.SLIDES_SOURCES_SUBDIR+filename;
                    
                    System.err.println("COPYING "+destinationPath+" INTO "+destinationPath);
                    fileSystemManager.copyFiles(new File(destinationPath), new File(destinationPath));
                } catch (IOException ex) {
                    fileSystemManager.cannotCopyMessage (ex,  destinationPath,  pathOfFileToBeImported);
                } 
                /* 
                */
                // empty the buffer directory 
                fileSystemManager.recursiveDelete(new File(slidesDirectoryPath));
                //if (mode==REPLACE) lecture.zapSlides();
                // deal with ODP, PPT and PDF files
                // in all cases, the generated imagesa are stored in the temporary buffer directory
                if (extension.equalsIgnoreCase("odp")) {
                    JOptionPane.showMessageDialog(null, "Sorry, import from Open Office format is not supported yet!");
                    return;
                } else if (extension.equalsIgnoreCase("pdf")) {
                    new PDFParser(pathOfFileToBeImported,slidesDirectoryPath+File.separator,ProgressBar.getProgressBar(),lecture);
                } else if (extension.equalsIgnoreCase("ppt")) {
                    PPTParser parser=new PPTParser();
                    parser.prepareParser(pathOfFileToBeImported,slidesDirectoryPath);
                    parser.extractImagesAndText(pathOfFileToBeImported,ProgressBar.getProgressBar(), lecture, mode);
                }
            }
        }
        // load the images from the slidesDirectoryPath
        boolean drawSlides=(extension!=null)&&(!extension.equalsIgnoreCase("noslides"));
        System.out.println("slidesDirectoryPath is"+slidesDirectoryPath);
        copySlidesFromFolder(slidesDirectoryPath,mode,lecture,drawSlides);
        // now the inspector window should show that the current lecture has slides
        InspectorWindow.getInstance().update();
    }
    /**
     * Check if lecture already has slides.
     * If yes, ask user if they have to be kept, replaced, or if the operation should be aborted
     * @return REPLACE=2; APPEND=1; DO_NOTHING=0;
     */
    private int checkExistingSlides(){
        final InspectorWindow gui=InspectorWindow.getInstance();
        File f = new File(ProgramState.getInstance().getCurrentLecture().getAcquisitionPath()
                +LODEConstants.SLIDES_PREFIX);
        if (! f.exists()) return REPLACE;
        Object[] options = {"Leave slides as they are",
                "Append after existing slides",
                "Replace existing slides"};
        Thread.yield();
        int choice = JOptionPane.showOptionDialog(null,
                "Slides already exist for this lecture. What would you like to do?",
                "Slides exist",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                LODEConstants.ICON_LODE,
                options,
                options[2]);
        return choice;
    }
/**
 * 
 * @param sourceFolderPath
 * @param choice  REPLACE=2; APPEND=1; DO_NOTHING=0;
 * @param lecture
 * @param drawSlides 
 */
    private void copySlidesFromFolder (String sourceFolderPath,int choice, Lecture lecture, boolean drawSlides){
        System.gc();
        System.runFinalization();
        String destinationFolderPath=ProgramState.getInstance().getCurrentLecture().getAcquisitionPath()
                        +LODEConstants.FS+LODEConstants.SLIDES_PREFIX;
        File destinationFolder = new File(destinationFolderPath);
        if (! destinationFolder.exists()) fileSystemManager.createFolder(destinationFolder);

        int existing_files_number = 0; //number of file in the lecture folder
        switch (choice) {
            case REPLACE: //Replace existing slides, or add slides to empty set
                fileSystemManager.recursiveDelete(destinationFolder);
                if (! destinationFolder.exists()) fileSystemManager.createFolder(destinationFolder);
                break;
            case APPEND: //Append after existing slides
                existing_files_number = destinationFolder.list().length; //in the lecture folder
                break;
            case DO_NOTHING:
                return;
        }
        File sourceFolder = new File(sourceFolderPath);
        String[] sourceFiles = sourceFolder.list();
        int nonjpegs=0;
        // LinkedList jpegsLot=new LinkedList<String>();
        StringBuilder nonJpegList=new StringBuilder();
        for (int i=0; i<sourceFiles.length; i++) {
            //get the last 3 chars
            String extension4=sourceFiles[i].substring(sourceFiles[i].length()-4).toLowerCase();
            String extension5=sourceFiles[i].substring(sourceFiles[i].length()-5).toLowerCase();
            boolean isJpg=(extension4.equals(".jpg"))||(extension5.equals(".jpeg"));
            if (isJpg) {
          //      jpegsLot.add(sourceFiles[i]);
            } else {
                nonjpegs++;
                nonJpegList.append("<li>").append(sourceFiles[i]).append("</li>");
            }
        }
        if (nonjpegs!=0)
            JOptionPane.showMessageDialog(InspectorWindow.getInstance(),
                 "<html>Non-jpg files found - will be skipped:"+nonJpegList.toString(), "Non JPG file found",
                            JOptionPane.ERROR_MESSAGE);
        /*
          ListIterator<String> iter=jpegsLot.listIterator();
        
           while (iter.hasNext()) {
            System.out.println(iter.next());
        }
         */
        //int lastSlideNumber=0;
        for (int i=0; i<sourceFiles.length+1; i++) {
            // try with n.jpg and with SlideN.jpg
            File source=new File (sourceFolderPath+LODEConstants.FS+i+".jpg");
            // the prfix Slide is generated when converting ppt into jpegs
            // TODO this code should be more general and capture any prefix
            if (! source.exists()) source=new File (sourceFolderPath+LODEConstants.FS+"Slide"+i+".jpg");
            if (! source.exists()) continue;

            int newSequenceNumber=i+existing_files_number;
            try {
                fileSystemManager.copyFiles(new File(destinationFolderPath+LODEConstants.FS+newSequenceNumber+".jpg"),
                                            source);
            } catch (IOException ex) {
                fileSystemManager.cannotCopyMessage(ex, destinationFolderPath+LODEConstants.FS+newSequenceNumber+".jpg", source.getAbsolutePath());
            }
        }
        // now draw the slides
        AcquisitionWindow acquisitionWindow=ControllersManager.getinstance().getAcquisitionWindow();
        if (drawSlides && acquisitionWindow!=null) acquisitionWindow.redrawElements();
    }
}
