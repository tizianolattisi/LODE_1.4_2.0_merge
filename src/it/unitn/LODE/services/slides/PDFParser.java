package it.unitn.LODE.services.slides;

import it.unitn.LODE.Controllers.ControllersManager;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import it.unitn.LODE.gui.ProgressBar;

import it.unitn.LODE.Models.Lecture;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.utils.Messanger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFileChooser;
import org.jpedal.PdfDecoder;

public class PDFParser {

    private int dpi = 72;
    //dimensioni immagine - 100%
    private static int scaling = 100;
    //formato immagine
    private static String format = "jpg";
    //flag per controllare se il messaggio e' stato scritto*/
    public static boolean outputMessages = false;
    //String output_dir = "";
    //Separatore in base all'OS
    String separator = System.getProperty("file.separator");
    //decoder che decodifica il pdf e ritorna data object
    PdfDecoder decode_pdf = null;

    Messanger m = Messanger.getInstance();

    //controlla l'estensione del file, definisce la directory di output e richiama il metodo per estrarre le immagini
    public PDFParser(String file_name, String output_dir, ProgressBar progressBarFrame, Lecture lecture) {
        PDFTextParser ptp = new PDFTextParser();
        ArrayList<String> als = ptp.decodeFile(file_name, progressBarFrame);

        if (ControllersManager.getinstance().getAcquisitionWindow().hasExternalWindow()) {
            scaling = 200;
        }
        scaling = 100;
        _extractImages(file_name, output_dir, progressBarFrame, lecture, als);
        //extractImages(file_name, output_dir, progressBarFrame, lecture, als);
    }

    private  void _extractImages(String file_name, String output_dir, ProgressBar progressBarFrame, Lecture lecture, ArrayList<String> als) {
        int pageCount = __initializeDecoder(file_name);
        if (pageCount <= 0) return;
        __getImages(pageCount, file_name, output_dir, progressBarFrame);
        __prepareTitles(pageCount, file_name, lecture, als, progressBarFrame);
        //chiusura pdf
        decode_pdf.closePdfFile();
        //JOptionPane.showMessageDialog(null, "Conversione avvenuta");
        m.w("PDF file conversion is finished." + LODEConstants.MSG_LOG);
        m.log("PDF file conversion is finished.");
        if (progressBarFrame != null) {
            progressBarFrame.closeWindow();
        }
    }
    /**
     * 
     * @param file_name name of the PDF input file
     * @return true if file can be decoded, false if there are problems
     */
    private  int __initializeDecoder(String file_name) {
        boolean error = false;
        String name = "";
        //cattura solo in nome del file senza estensione e percorso
        int pointer = file_name.lastIndexOf(separator);
        if (pointer == -1) {
            pointer = file_name.lastIndexOf("/");
        }
        if (pointer != -1) {
            name = file_name.substring(pointer + 1, file_name.length() - 4);
        } else if ((file_name.toLowerCase().endsWith(".pdf"))) {
            name = file_name.substring(0, file_name.length() - 4);
        }
        try {
            m.w("Converting file :" + file_name + " at " + dpi + " dpi", LODEConstants.MSG_LOG);
            decode_pdf = new PdfDecoder(true);
            // Please do not use. Use setPageParameters(scalingValue, pageNumber) instead;
            //decode_pdf.setExtractionMode(0, 72, dpi / 72);
            decode_pdf.setExtractionMode(PdfDecoder.FINALIMAGES);
            decode_pdf.setPageParameters(4.0F, 1);
            decode_pdf.openPdfFile(file_name);

        } catch (Exception ex) {
            m.log("Error while converting pdf file [1]" + file_name);
            ex.printStackTrace(m.getLogger());
            m.w("Error while converting pdf file [1]" + file_name, LODEConstants.MSG_ERROR);
            error = true;
        }

        //estrae i dati dal pdf se si hanno i permessi
        if ((decode_pdf.isEncrypted()
                && (!decode_pdf.isPasswordSupplied()))
                && (!decode_pdf.isExtractionAllowed())) {
            m.log("PDF file si protected! " + file_name);
            m.w("PDF file si protected! " + file_name, LODEConstants.MSG_WARNING);
            error = true;
        }

        if (error) {
            //chiusura pdf
            decode_pdf.closePdfFile();
            return -1;
        } else {
            return decode_pdf.getPageCount();
        }
    }


    /**
     *
     * @param pageCount number of pages in file
     * @param file_name name of the file on which the extraction is operated. Used only on error messges
     * @param progressBarFrame
     */
    private  void __getImages(int pageCount, String file_name, String output_dir, ProgressBar progressBarFrame) {
        //creiamo la directory di output se non esiste
        File output_path = new File(output_dir);
        if (!output_path.exists()) {
            output_path.mkdirs();
        }
        //range delle pagine da convertire
        try {
            int n = -1;
            for (int page = 1; page <= pageCount ; page++) { //lettura pagine
                System.gc();
                System.runFinalization();
                if (progressBarFrame != null) {
                    progressBarFrame.showProgress("Extracting images from pdf file", page, pageCount);
                }

                m.w("importing page " + page, LODEConstants.MSG_LOG);
                String image_name = String.valueOf(page);
                BufferedImage image_to_save = null;
                try {
                    image_to_save = decode_pdf.getPageAsImage(page);
                } catch (Exception e) {
                    System.err.println("Potential problem in image extraction on page " + page + " on file " + file_name + " [ " + e.getMessage());
                }
                if (image_to_save == null) {
                    m.w("Unable to generate images ", LODEConstants.MSG_WARNING);
                    m.log("Unable to generate images ");
                } else {
                    //utilizzato se si vuole modificare la dimensione delle pagine
                    if (scaling != 100) {
                        int newWidth = image_to_save.getWidth() * scaling / 100;
                        Image scaledImage = image_to_save.getScaledInstance(newWidth, -1, BufferedImage.SCALE_SMOOTH);
                        image_to_save = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2 = image_to_save.createGraphics();
                        g2.drawImage(scaledImage, 0, 0, null);
                    }
                    //salva l'immagine
                    decode_pdf.getObjectStore().saveStoredImage(output_dir + image_name, image_to_save, true, false, format);
                }
            }
        } catch (Exception ex) {
            decode_pdf.closePdfFile();
            m.log("Error while converting pdf file [2] " + file_name);
            ex.printStackTrace(m.getLogger());
            ex.printStackTrace();
            m.w("Error while converting pdf file [2]" + file_name, LODEConstants.MSG_ERROR);
        }
    }

    /**
     *
     * @param pageCount number of pages in pdf file
     * @param file_name name of the pdf file (used for logging)
     * @param lecture lecture to which the slides have to be added
     * @param als arraylist of the slide texts
     */
    private void __prepareTitles(int pageCount, String file_name, Lecture lecture, ArrayList<String> als, ProgressBar progressBarFrame) {
        int slidesAlreadyInLecture = lecture.getSlideCount();
        String textArray[] = new String[pageCount];
        for (int page = 1; page <= pageCount; page++) { //lettura pagine
            int n = page + slidesAlreadyInLecture;
            String title = " ";
            String text = " ";
            try {
                text = als.get(page - 1);//(n-1);
                title = text.substring(0, Math.min(14, text.length())) + "...";
            } catch (Exception e) {
                m.log("PDFParser: Unable to extract text from slide " + n + " [" + e.getMessage() + "]");
                m.w("PDFParser: Unable to extract text from slide " + n + " [" + e.getMessage() + "]", LODEConstants.MSG_WARNING);
            }
            textArray[page - 1] = text;

            //lecture.addSlide(n, "Slide "+n, "Slide "+n, LODEConstants.SLIDES_PREFIX + n + ".jpg");
            //lecture.addSlide(n, title, text, LODEConstants.SLIDES_PREFIX + n + ".jpg");
        }
        __cleanup(textArray);
        int n=0;
        for (int page = 1; page <= pageCount; page++) { //lettura pagine
            n = slidesAlreadyInLecture + page;
            String text = textArray[page-1];
            //strip numbers at the beginning
            String title = text;
            int before = 0;
            int after = 0;
            do {
                before = title.length();
                title = title.replaceAll("^\\s+", ""); //strip leading blanks
                title = title.replaceAll("^\\d+", ""); //strip leading numbers
                after = title.length();
            } while (before != after);
            if (title.length() > 17) {
                title = title.substring(0, Math.min(14, text.length())) + "...";
            }
            lecture.addSlide(n, title, text, LODEConstants.SLIDES_PREFIX+LODEConstants.FS + n + ".jpg");
        }
        lecture.addSlidesGroup(file_name, slidesAlreadyInLecture + 1, n);
        lecture.persistSlides();
        decode_pdf.flushObjectValues(true);
    }

    private void __cleanup(String[] textArray) {
        /*HashMap hm=new HashMap();
        for (int i=0; i<textArray.length; i++) {
        hm.put(new Integer(i), textArray[i]);
        }*/
        //java.util.Arrays.sort(textArray);
        final int MIN_LENGTH = 5;
        final int MIN_REPETITIONS = 5;
        List<String> similarText = new LinkedList<String>();
        int nStrings = textArray.length;
        for (int i = 0; i < nStrings - 1; i++) {
            // elimina le slides per cui non è stato estratto del testo
            if (textArray[i].startsWith("Slide")) {
                continue;
            }
            int j = i + 1;
            int k = 0;
            int l1 = textArray[i].length();
            int l2 = textArray[j].length();
            while (k < l1 && k < l2 && textArray[i].charAt(k) == (textArray[j].charAt(k))) {
                k++;
            }
            if (k <= MIN_LENGTH) {
                continue;
            }
            similarText.add(textArray[i].substring(0, k));
        }
        Collections.sort(similarText);
        int i = 0, count = 0;
        int elements = similarText.size();
        List<String> radixes = new LinkedList<String>();
        /*
        class Occurrence{
        String s;
        int count;
        private Occurrence(String s, int count) {
        this.s=s;this.count=count;
        }
        }
        List<Occurrence> occurenceList=new LinkedList<Occurrence>();*/
        while (i < elements) {
            String s = similarText.get(i++);
            while (i < elements && similarText.get(i++).startsWith(s)) {
                count++;
            }
            //occurenceList.add(new Occurrence(s,count));
            if (count >= MIN_REPETITIONS) {
                System.out.println(count + " | " + s);
                radixes.add(s);
            }
        }
        for (int w = 0; w < textArray.length; w++) {
            for (String radix : radixes) {
                if (textArray[w].startsWith(radix)) {
                    textArray[w] = textArray[w].substring(radix.length());
                    break;
                }
            }
        }
    }

    /*
    // this is the original function
    private void extractImages(String file_name, String output_dir, ProgressBar progressBarFrame, Lecture lecture, ArrayList<String> als) {

        String name = "";
        //cattura solo in nome del file senza estensione e percorso
        int pointer = file_name.lastIndexOf(separator);
        if (pointer == -1) {
            pointer = file_name.lastIndexOf("/");
        }
        if (pointer != -1) {
            name = file_name.substring(pointer + 1, file_name.length() - 4);
        } else if ((file_name.toLowerCase().endsWith(".pdf"))) {
            name = file_name.substring(0, file_name.length() - 4);
        }
        try {
            m.w("Converting file :" + file_name + " at " + dpi + " dpi", LODEConstants.MSG_LOG);
            decode_pdf = new PdfDecoder(true);
            // Please do not use. Use setPageParameters(scalingValue, pageNumber) instead;
            //decode_pdf.setExtractionMode(0, 72, dpi / 72);
            decode_pdf.setExtractionMode(PdfDecoder.FINALIMAGES);
            decode_pdf.setPageParameters(4.0F, 1);
            decode_pdf.openPdfFile(file_name);

        } catch (Exception ex) {
            m.log("Error while converting pdf file [1]" + file_name);
            ex.printStackTrace(m.getLogger());
            m.w("Error while converting pdf file [1]" + file_name, LODEConstants.MSG_ERROR);
        }

        //estrae i dati dal pdf se si hanno i permessi
        if ((decode_pdf.isEncrypted()
                && (!decode_pdf.isPasswordSupplied()))
                && (!decode_pdf.isExtractionAllowed())) {
            m.log("PDF file si protected! " + file_name);
            m.w("PDF file si protected! " + file_name, LODEConstants.MSG_WARNING);
        } else {
            //creiamo la directory di output se non esiste
            File output_path = new File(output_dir);
            if (!output_path.exists()) {
                output_path.mkdirs();
            }
            //range delle pagine da convertire
            int start = 1, end = decode_pdf.getPageCount();
            try {
                int n = -1;
                int slidesAlreadyInLecture = lecture.getSlideCount();
                String textArray[] = new String[end];
                for (int page = start; page < end + 1; page++) { //lettura pagine
                    if (progressBarFrame != null) {
                        progressBarFrame.showProgress("Importing images from pdf file", page, end);
                    }

                    m.w("importing page " + page, LODEConstants.MSG_LOG);
                    String image_name = String.valueOf(page);
                    BufferedImage image_to_save = null;
                    try {
                        image_to_save = decode_pdf.getPageAsImage(page);
                    } catch (Exception e) {
                        System.err.println("Potential problem in image extraction on page " + page + " in " + file_name + " [ " + e.getMessage());
                    }
                    if (image_to_save == null) {
                        m.w("Unable to generate images ", LODEConstants.MSG_WARNING);
                        m.log("Unable to generate images ");
                    } else {
                        //utilizzato se si vuole modificare la dimensione delle pagine
                        if (scaling != 100) {
                            int newWidth = image_to_save.getWidth() * scaling / 100;
                            Image scaledImage = image_to_save.getScaledInstance(newWidth, -1, BufferedImage.SCALE_SMOOTH);
                            image_to_save = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
                            Graphics2D g2 = image_to_save.createGraphics();
                            g2.drawImage(scaledImage, 0, 0, null);
                        }
                        //salva l'immagine
                        decode_pdf.getObjectStore().saveStoredImage(output_dir + image_name, image_to_save, true, false, format);
                        n = page + slidesAlreadyInLecture;
                        //System.out.println(n);
                        String title = " ";
                        String text = " ";

                        try {
                            text = als.get(page - 1);//(n-1);
                            title = text.substring(0, Math.min(14, text.length())) + "...";
                        } catch (Exception e) {
                            m.log("PDFParser: Unable to extract text from slide " + n + " [" + e.getMessage() + "]");
                            m.w("PDFParser: Unable to extract text from slide " + n + " [" + e.getMessage() + "]", LODEConstants.MSG_WARNING);
                        }
                        textArray[page - 1] = text;

                        //lecture.addSlide(n, "Slide "+n, "Slide "+n, LODEConstants.SLIDES_PREFIX + n + ".jpg");
                        //lecture.addSlide(n, title, text, LODEConstants.SLIDES_PREFIX + n + ".jpg");
                    }
                }
                __cleanup(textArray);
                int slidesAlreadyInLecture2 = lecture.getSlideCount();
                for (int page = start - 1; page < end; page++) { //lettura pagine
                    n = slidesAlreadyInLecture2 + page + 1;
                    String text = textArray[page];
                    //strip numbers at the beginning
                    String title = text;
                    int before = 0;
                    int after = 0;
                    do {
                        before = title.length();
                        title = title.replaceAll("^\\s+", ""); //strip leading blanks
                        title = title.replaceAll("^\\d+", ""); //strip leading numbers
                        after = title.length();
                    } while (before != after);
                    if (title.length() > 17) {
                        title = title.substring(0, Math.min(14, text.length())) + "...";
                    }
                    lecture.addSlide(n, title, text, LODEConstants.SLIDES_RES_PREFIX + n + ".jpg");
                }
                lecture.addSlidesGroup(file_name, slidesAlreadyInLecture + 1, n);
                lecture.persistSlides();
                decode_pdf.flushObjectValues(true);
            } catch (Exception ex) {
                decode_pdf.closePdfFile();
                m.log("Error while converting pdf file [2] " + file_name);
                ex.printStackTrace(m.getLogger());
                ex.printStackTrace();
                m.w("Error while converting pdf file [2]" + file_name, LODEConstants.MSG_ERROR);
            }
        }
        //chiusura pdf
        decode_pdf.closePdfFile();
        //JOptionPane.showMessageDialog(null, "Conversione avvenuta");
        m.w("PDF file conversion is finished." + LODEConstants.MSG_LOG);
        m.log("PDF file conversion is finished.");
        if (progressBarFrame != null) {
            progressBarFrame.closeWindow();
        }
    }
     *
     */

    public static void main(String[] args) {
        String file_name = "";
        File pdf_file = null;
        JFileChooser c = new JFileChooser();
        do {
            c.showDialog(null, "Select the file to convert");
            file_name = c.getSelectedFile().getAbsolutePath();

            pdf_file = new File(file_name);
        } while (pdf_file.exists() == false);
        System.out.println("File :" + file_name);
        PDFParser images1 = new PDFParser(file_name, null, null, null);
    }

 }
