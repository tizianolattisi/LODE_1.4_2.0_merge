package it.unitn.LODE.services.slides;

import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.gui.ProgressBar;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import it.unitn.LODE.utils.FileSystemManager;
import java.util.ArrayList;

/**
 *
 * @author ronchet
 */
public class PDFTextParser {


    /* BASED ON:
     * ===========================================
     * Java Pdf Extraction Decoding Access Library
     * ===========================================
     *
     * Project Info:  http://www.jpedal.org
     * Project Lead:  Mark Stephens (mark@idrsolutions.com)
     *
     * (C) Copyright 2002, IDRsolutions and Contributors.
     *
     * ---------------
     * ExtractTextAsWordlist.java
     * ---------------
     * (C) Copyright 2002, by IDRsolutions and Contributors.
     *
     * Original Author:  Mark Stephens (mark@idrsolutions.com)
     * Contributor(s):
     */
//JFC
    /**
     *
     * Sample code showing how jpedal library can be used with
     * pdf files  to extract text from a specified Rectangle as a set of words
     * <br>Scope:<b>(Ent only)</b>
     *
     * This example is based on extractTextInRectangle.java
     *
     * These can then be entered into an index engine such as Lucene
     */
    /**flag to show if we print messages*/
    public static boolean outputMessages = true;
    /**output where we put files*/
    private String user_dir = System.getProperty("user.dir");
    /**correct separator for OS */
    String separator = System.getProperty("file.separator");
    /**the decoder object which decodes the pdf and returns a data object*/
    PdfDecoder decodePdf = null;
    /**flag to show if file or byte array*/
    private boolean isFile = true;
    /**byte array*/
    private byte[] byteArray = null;
    /**used in our regression tests to limit to first 10 pages*/
    public static boolean isTest = false;

    public PDFTextParser() {
    }

    /**example method to open a file and extract the raw text*/
    public void DEMO(String file_name) {

        if (outputMessages) {
            System.out.println("processing " + file_name);
        }


        /**
         * if file name ends pdf, do the file otherwise
         * do every pdf file in the directory. We already know file or
         * directory exists so no need to check that, but we do need to
         * check its a directory
         */
        if (file_name.toLowerCase().endsWith(".pdf")) {
            decodeFile(file_name,null); //will generate null pointer exception on progressbar?
        } else {

            /**
             * get list of files and check directory
             */
            String[] files = null;
            File inputFiles = null;

            /**make sure name ends with a deliminator for correct path later*/
            if (!file_name.endsWith(separator)) {
                file_name = file_name + separator;
            }

            try {
                inputFiles = new File(file_name);

                if (!inputFiles.isDirectory()) {
                    System.err.println(
                            file_name + " is not a directory. Exiting program");
                }
                files = inputFiles.list();
            } catch (Exception ee) {
                LogWriter.writeLog(
                        "Exception trying to access file " + ee.getMessage());
            }

            /**now work through all pdf files*/
            long fileCount = files.length;

            for (int i = 0; i < fileCount; i++) {

                if (files[i].toLowerCase().endsWith(".pdf")) {
                    if (outputMessages) {
                        System.out.println(file_name + files[i]);
                    }

                    decodeFile(file_name + files[i],null); //will generate null pointer exception on progressbar?
                }
            }
        }
    }

    /**example method to open a file and extract the raw text*/
    /*
    public PDFTextParser(byte[] array) {

        if (outputMessages) {
            System.out.println("processing byte array");
        }

        //check output dir has separator
        if (user_dir.endsWith(separator) == false) {
            user_dir = user_dir + separator;
        }


        //set values
        this.byteArray = array;
        isFile = false;

        //routine will open from array (is otherwise identical)
        decodeFile("byteArray");

    }
     *
     */

    /**
     * routine to decode a file
     */
    public ArrayList<String> decodeFile(String file_name,ProgressBar progressBarFrame) {

        ArrayList<String> stringArray = new ArrayList<String>();
        /**if you do not require XML content, pure text extraction
         * is much faster.
         */
        PdfDecoder.useTextExtraction();
        /**/

        /**get just the name of the file without
         * the path to use as a sub-directory or .pdf
         */
        String name = "demo"; //set a default just in case

        int pointer = file_name.lastIndexOf(separator);

        if (pointer != -1) {
            name = file_name.substring(pointer + 1, file_name.length() - 4);
        }


        //PdfDecoder returns a PdfException if there is a problem
        try {
            decodePdf = new PdfDecoder(false);
            decodePdf.setExtractionMode(PdfDecoder.TEXT); //extract just text
            decodePdf.init(true);
            //make sure widths in data CRITICAL if we want to split lines correctly!!


            //always reset to use unaltered co-ords - allow use of rotated or unrotated
            // co-ordinates on pages with rotation (used to be in PdfDecoder)
            PdfGroupingAlgorithms.useUnrotatedCoords = false;

            /**
             * open the file (and read metadata including pages in  file)
             */
            if (outputMessages) {
                System.out.println("Opening file :" + file_name);
            }

            if (isFile) {
                decodePdf.openPdfFile(file_name);
            } else {
                decodePdf.openPdfArray(byteArray);
            }
        } catch (PdfSecurityException e) {
            System.err.println("Exception " + e + " in pdf code for wordlist" + file_name);
        } catch (PdfException e) {
            System.err.println("Exception " + e + " in pdf code for wordlist" + file_name);

        } catch (Exception e) {
            System.err.println("Exception " + e + " in pdf code for wordlist" + file_name);
            e.printStackTrace();
        }

        /**
         * extract data from pdf (if allowed).
         */
        if (!decodePdf.isExtractionAllowed()) {
            if (outputMessages) {
                System.out.println("Text extraction not allowed");
            }
        } else if (decodePdf.isEncrypted() && !decodePdf.isPasswordSupplied()) {
            if (outputMessages) {
                System.out.println("Encrypted settings");
                System.out.println("Please look at SimpleViewer for code sample to handle such files");
            }
        } else {
            //page range
            int start = 1, end = decodePdf.getPageCount();

            //limit to 1st ten pages in testing
            if ((end > 10) && (isTest)) {
                end = 10;
            }

            /**
             * extract data from pdf
             */
            try {
                for (int page = start; page < end + 1; page++) { //read pages
                    progressBarFrame.showProgress("Extracting text from pdf", page, end);
                //decode the page
                    try {
                        decodePdf.decodePage(page);
                    } catch (RuntimeException ex) {
                        System.out.println("Runtime exception: "+ex.getMessage() +" in page "+page);
                        continue;
                    } catch (Exception e) {
                        System.out.println("PDFTextParser - page "+page+" cannot be decoded");
                        continue;
                    }
                    try {
                        /** create a grouping object to apply grouping to data*/
                        PdfGroupingAlgorithms currentGrouping = decodePdf.getGroupingObject();

                        /**use whole page size for  demo - get data from PageData object*/
                        PdfPageData currentPageData;
                            currentPageData = decodePdf.getPdfPageData();

                        int x1 = currentPageData.getMediaBoxX(page);
                        int x2 = currentPageData.getMediaBoxWidth(page) + x1;

                        int y2 = currentPageData.getMediaBoxX(page);
                        int y1 = currentPageData.getMediaBoxHeight(page) - y2;


                        /**Co-ordinates are x1,y1 (top left hand corner), x2,y2(bottom right) */
                        /**The call to extract the list*/
                        List words = null;

                        /**new 7th October 2003 - define punctuation*/
                        try {
                            words = currentGrouping.extractTextAsWordlist(
                                    x1,
                                    y1,
                                    x2,
                                    y2,
                                    page,
                                    //false,
                                    true, "&:=()!;.,\\/\"\"\'\'");
                        } catch (Exception e) { // was: (PdfException e)
                            //decodePdf.closePdfFile();
                            System.out.println("PDFTextParser - Problems parsing page "+page+" in "+ file_name);
                        }

                        if (words == null) {
                            if (outputMessages) //System.out.println("========= BOP "+page+" ===============");
                            //System.out.println("========= EOP "+page+" ===============");
                            // empty text - add a placeholder
                            {
                                stringArray.add("Slide " + page);
                            }
                        } else {

                            //System.out.println("========= BOP "+page+" ===============");
                            /**
                             * output the data
                             */
                            StringBuffer s = new StringBuffer();
                            Iterator wordIterator = words.iterator();
                            while (wordIterator.hasNext()) {

                                String currentWord = (String) wordIterator.next();
                                s.append(currentWord + " ");
                                // skip the info about the words'bounding box
                                wordIterator.next();
                                wordIterator.next();
                                wordIterator.next();
                                wordIterator.next();
                                //System.out.print(currentWord+" ");
                            }
                            stringArray.add(s.toString());
                        }
                        //remove data once written out
                    } catch (Exception e) {
                        System.err.println("Skipping text extraction on page " + page + " in " + file_name + " [ " + e.getMessage());
                        e.printStackTrace();
                    }
                    decodePdf.flushObjectValues(false);
                }
            } catch (Exception e) {
                decodePdf.closePdfFile();
                System.err.println("Exception " + e + " in " + file_name);
                e.printStackTrace();
            }

            /**
             * flush data structures - not strictly required but included
             * as example
             */
            decodePdf.flushObjectValues(true); //flush any text data read

        }

        /**close the pdf file*/
        decodePdf.closePdfFile();

        decodePdf = null;
        //System.exit(0);
        return stringArray;

    }
    //////////////////////////////////////////////////////////////////////////

    /**
     * main routine which checks for any files passed and runs the demo
     */
    public static void main(String[] args) {
        try {

            //set to default
            String file_name = "";

            file_name = ControllersManager.getinstance().getFileSystemManager().selectAFile("", "pdf");

            //check file exists
            File pdf_file = new File(file_name);

            //if file exists, open and get number of pages
            if (pdf_file.exists() == false) {
                System.err.println("File " + file_name + " not found");
            }
            PDFTextParser text1 = new PDFTextParser();
            text1.DEMO(file_name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
