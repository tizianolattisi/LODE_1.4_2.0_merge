package it.unitn.LODE.services.slides;
/**
 *
 * @author ronchet
 */
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.utils.FileSystemManager;
import it.unitn.LODE.gui.ProgressBar;
import it.unitn.LODE.services.SlideImporter;
import it.unitn.LODE.utils.Messanger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Hyperlink;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

public class PPTParser {
    SlideShow ppt=null;
    Slide[] slides=null;
    String outputDirectory=null;
    Messanger m=Messanger.getInstance();

    /**
     * Select and open a ppt file, and create the temporary place where to generate images (jpegs) of the slides
     * @param pptFileName  the ppt file to be imported
     * @param outDir  the dierctory where the jpeg images will be created
     * @return true if it was possible to open and read the ppt file
     */
    public boolean prepareParser(String pptFileName,String outDir ){
        boolean success=true;
        this.outputDirectory=outDir;
        try {
            ppt = new SlideShow(new HSLFSlideShow(pptFileName));
            slides=ppt.getSlides();
            ControllersManager.getinstance().getFileSystemManager().createFolder(new File(outputDirectory));
        } catch (IOException ex) {
            success=false;
            ex.printStackTrace(m.getLogger());
            m.w("Error opening file "+pptFileName,LODEConstants.MSG_ERROR);
        }
        return success;
    }
    /**
     * Extracts titles, text and images from the ppt presentation, and stores the extracted data into a LODE lecture
     * @param progressBarFrame  the progress bar that will be shown while parsing (can be null)
     * @param lecture  the LODE lecture into which the slides will be imported
     * @param mode idicates if the slides replace existing slides or are appended after existing slides
     */
    public void extractImagesAndText(String pptFileName, ProgressBar progressBarFrame, Lecture lecture, int mode){
        Dimension pgsize = ppt.getPageSize();
        int slidesAlreadyInLecture=lecture.getSlideCount();
        int lastSlide=-1;
        for (int i = 0; i < slides.length; i++) {
            // IMPORT IMAGE ====================================================
            // the extracted image will be saved in a temporary buffer with a name 
            // obtained from the sequence number
            // the final name though will depend on wheter there are already other slides
            // associated with the lecture
            String outFileName=(i+1) + ".jpg"; 
            String finalOutFileName=outFileName;
            if (mode==SlideImporter.APPEND) 
                finalOutFileName=(i+1+slidesAlreadyInLecture) + ".jpg";
            if (progressBarFrame!=null) progressBarFrame.showProgress("Importing images from ppt file - slide",i, slides.length);
            //
            BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
            final Graphics2D graphics = img.createGraphics();
            //clear the drawing area
            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
            //render
            //final Graphics g=graphics;
            // the draw operation can take forever on certain kind of images.
            // we therefore run it in a separate Thread, and we give it only a limited time 
            // to execute - after which we cut the execution.
            //slides[i].draw(graphics);
            final int ii=i;
            Thread t=new Thread(){
                public void run(){
                    slides[ii].draw(graphics);
                }
            };
            t.start();
            try {
                t.join(10000);
                if (t.isAlive()) {
                m.log("Image extraction from slide n. "+(ii+1)+" cannot be completed");
                t.interrupt();
                }
                t=null;
            } catch (InterruptedException ex) {
                System.out.println("interrupted slide n."+ii);
            } 
            //save the output to a jpg file
            FileOutputStream out;
            try {
                out = new FileOutputStream(outputDirectory+File.separator+ outFileName);
                javax.imageio.ImageIO.write(img, "jpg", out);
                out.close();            
            } catch (FileNotFoundException ex) {
                ex.printStackTrace(m.getLogger());
                m.w("File not found "+outputDirectory+File.separator+ outFileName,LODEConstants.MSG_ERROR);
            } catch (IOException ex) {
                ex.printStackTrace(m.getLogger());
                m.w("I/O exception of file "+outputDirectory+File.separator+ outFileName,LODEConstants.MSG_ERROR);
            }
            // IMPORT TEXT =====================================================
            
             int n=slides[i].getSlideNumber();
            // extract slide title ---------------------------------------------
            String title=slides[i].getTitle();
            if (title!=null) title.trim();
            if (title==null || title.equals("")) title="Slide "+(slidesAlreadyInLecture+i+1);
            // extract slide text ----------------------------------------------
            StringBuffer sb=new StringBuffer();
            TextRun[] tr=slides[i].getTextRuns();
            m.w(tr.length+" "+title,LODEConstants.MSG_LOG);
            for (int j=0;j<tr.length;j++) {
                //extract text and add it to a stringbuffer 
                String txt=tr[j].getText().trim();                            
                if (!( (j==0) && (title.equals(txt) ) ) ) // skip titles 
                    sb.append(txt).append("/n");
                if (false) { // NEVER EXECUTE THIS CODE - IT'S HERE ONLY FOR FUTURE USES!
                    //extract (textual) hyperlinks -- but they're not saved.
                    Hyperlink[] links = tr[j].getHyperlinks();
                    if(links != null) 
                        //for (int l = 0; l < links.length; l++) 
                        for (Hyperlink link:links) {
                            String linkTitle = link.getTitle();
                            String address = link.getAddress();
                            String substring = txt.substring(link.getStartIndex(), link.getEndIndex()-1); //in ppt end index is inclusive
                        }
                    //extract (non-textual) hyperlinks -- but they're not saved.
                    //in PowerPoint you can assign a hyperlink to a shape without text,
                    //for example to a Line object. The code below demonstrates how to
                    //read such hyperlinks
                    Shape[] sh = slides[i].getShapes();
                    for (Shape aShape:sh) {
                        Hyperlink link = aShape.getHyperlink();
                        if(link != null)  {
                            String linkTitle = link.getTitle();
                             String address = link.getAddress();
                        }
                    }
                }
            }
            
            // =================================================================
            // add info about the slide to the lecture 
            lecture.addSlide(i+1+slidesAlreadyInLecture, title, sb.toString(), finalOutFileName);
            lastSlide=i+1+slidesAlreadyInLecture;
            //System.out.println((i+1+slidesAlreadyInLecture)+finalOutFileName+" "+slidesAlreadyInLecture);
            
            //lecture.addSlide(i+1+slidesAlreadyInLecture, "XX", "XX", finalOutFileName);
        }
        // =====================================================================
        // save the lecture (and the slides) to file(s) 
        lecture.addSlidesGroup(pptFileName, slidesAlreadyInLecture+1, lastSlide);
        lecture.persistSlides();
        System.gc();
        if (progressBarFrame!=null) progressBarFrame.closeWindow();
    }

    public static void main(String a[]) throws Exception {
    /*
        Lecture lecture=new Lecture(null,"",new Date());
        PPTParser parser=new PPTParser();
        parser.setFile("/Users/ronchet/Desktop/MET9.ppt");
        parser.setOutputDirectory("/Users/ronchet/Desktop/images");
        parser.extractImagesAndText(ProgressBar.getProgressBar(), lecture);
    */  
         //for (PPTSlideTextContent c : parser.sts){
        //   System.out.println(c.getSlideNumber()+"|"+c.getTitle());
        //}
        //parser.extractImages(progressBarFrame);
    }
}

/*===================== CODICE PER PPTX DA TESTARE
 *
 public ImageIcon display() throws JPresentationException {

    Background background;
    background = slides[current].getBackground();
    Fill f = background.getFill();
    Color color = f.getForegroundColor();
    Dimension dimension = ppt.getPageSize();
    shapes = slides[current].getShapes();
    BufferedImage img = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = img.createGraphics();
    graphics.setPaint(color);
    graphics.fill(new Rectangle2D.Float(0, 0, dimension.width, dimension.height));
    slides[current].draw(graphics);
    ImageIcon icon = new ImageIcon(img);

    return icon;

 */
