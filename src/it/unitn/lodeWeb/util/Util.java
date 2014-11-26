package it.unitn.lodeWeb.util;

//--------------------------------------------------------------------------
import it.exprivia.cnos.opencloud.Cloud;
import it.exprivia.cnos.opencloud.Tag;
import it.exprivia.cnos.opencloud.filters.DictionaryFilter;
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.utils.CorrectPathFinder;
import it.unitn.lodeWeb.serializationxml.ManageInfoXML;
import it.unitn.lodeWeb.serializationxml.lecture.Lecture;
//import it.unitn.LODE.Models.Lecture;
import it.unitn.lodeWeb.serializationxml.slides.*;
import it.unitn.lodeWeb.serializationxml.timeslide.TimedSlides;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
//--------------------------------------------------------------------------


//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class Util {

    static Logger cat = Logger.getLogger(Util.class.getName());

    //--------------------------------------------------------------------------
    /**
     * Init Tag cloud
     * 
     * @param maxTag - maximum number of tags to display in the cloud
     * @param maxWeight - maximum weight value of tags
     * @param minWeight - minimum weight value of tags
     * @param threshold - threshold value. Tags with their score under the threshold will not be displayed.
     * @param dictionaryFilter - language word stop
     * @param defaultLink - Format string representing the default link
     * 
     * @return Cloud - Tag cloud.
     */
    //--------------------------------------------------------------------------
    public Cloud initTagCloud(int maxTag, int maxWeight, int minWeight,
            int threshold, String dictionaryFilter, String defaultLink) {

        Cloud tagCloud = new Cloud();

        tagCloud.setMaxTagsToDisplay(maxTag);
        tagCloud.setMaxWeight(maxWeight);
        tagCloud.setMinWeight(minWeight);
        tagCloud.setDefaultLink(defaultLink);

        if (dictionaryFilter.equals("my")) {
            tagCloud.addInputFilter(new MyDictionaryFilter());
        } else {
            tagCloud.addInputFilter(new DictionaryFilter());
        }

        tagCloud.setThreshold(3);

        return tagCloud;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Removing all not alphanumeric characters.
     *
     * @param input - input string
     * @return String - processed string
     */
    //--------------------------------------------------------------------------
    public static String soloAlfanumerici(String input) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            if (Character.isLetterOrDigit(current) || current == ' ') {
                sb.append(current);
            }
        }
        return sb.toString();
    }

    //--------------------------------------------------------------------------
    /**
     * Add tags to an existing tag cloud
     * 
     * @param slides - content of the slides of a lecture
     * @param cloud - tagcloud
     * 
     */
    //--------------------------------------------------------------------------
    private void addTags(LodeSlides slides, Cloud cloud) {
        Iterator i = slides._getSlides().getSlides().iterator();
        while (i.hasNext()) {
            it.unitn.lodeWeb.serializationxml.slides.Slide s = (it.unitn.lodeWeb.serializationxml.slides.Slide) i.next();

            // elimina i caratteri speciali!!!! dal testo

            String st=s.getTitle();
            if (st != null && ! st.startsWith("Slide")) {
                st=soloAlfanumerici(st);
                int WEIGHT = 5;
                for (int c=0; c<WEIGHT; c++) cloud.addText(st); // add several times to give more WEIGHT t the terms; #MARCO
            } else {
                st=s.getText();
                if (st != null && ! st.equals("-")) {
                    System.out.println(st);
                    cloud.addText(soloAlfanumerici(st));
                }
            }

        }
    }

    //--------------------------------------------------------------------------
    /**
     * Add tags url
     */
    //--------------------------------------------------------------------------
    private void addTagsURL(TimedSlides tslides, LodeSlides slides, Cloud cloud) {
        Iterator i_tags = cloud.tags().iterator();
        Tag t = null;
        while (i_tags.hasNext()) {
            t = ((Tag) i_tags.next());
            Iterator i2 = tslides.getTimeslides().iterator();
            while (i2.hasNext()) {
                it.unitn.lodeWeb.serializationxml.timeslide.Slide sl = (it.unitn.lodeWeb.serializationxml.timeslide.Slide) i2.next();
                //if(t.getName().equals(sl.getTitle())){
                t.setLink(sl.getImage());
            //}
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * 	Initializes and creates a new tagCloud
     * 
     * @param slides - content of the slides of a lecture
     * @param tslides - time slide
     * 
     * @return Cloud - new lesson tag clouds
     */
    //--------------------------------------------------------------------------
    public Cloud createTagCloud(LodeSlides slides, TimedSlides tslides) {
        Cloud cloud = this.initTagCloud(10, 70, 0, 20, "my", "");

        this.addTags(slides, cloud);

        //this.addTagsURL(tslides, slides, cloud);
        return cloud;
    }
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    /**
     *
     * Reads the contents of a file
     *
     * @param nomeFile - filename
     * @return String - contents text
     *
     * @throws java.io.IOException
     */
    //--------------------------------------------------------------------------
    public String leggiFile(String nomeFile)
            throws IOException {
        InputStream is = null;
        InputStreamReader isr = null;

        StringBuffer sb = new StringBuffer();
        char[] buf = new char[1024];
        int len;

        try {
            is = new FileInputStream(nomeFile);
            isr = new InputStreamReader(is);

            while ((len = isr.read(buf)) > 0) {
                sb.append(buf, 0, len);
            }

            return sb.toString();
        } finally {
            if (isr != null) {
                isr.close();
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Compiles the given regular expression into a pattern
     * Creates a matcher that will match the given input against this pattern
     * Attempts to find the next subsequence of the input sequence that matches the pattern.
     *
     * @param text - The character sequence to be matched
     * @param pattern - The expression to be compiled
     * @return String - Returns the input subsequence matched
     */
    //--------------------------------------------------------------------------
    public String getMaching(String text, String pattern){
            Pattern patternP = Pattern.compile(pattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher matcher = patternP.matcher(text);
            String ret = "";
            if (matcher.find()) {
                ret = matcher.group(1);
            }
            return ret;
    }



    //--------------------------------------------------------------------------
    /**
     *
     * Write a file
     *
     * @param path - output path
     * @param file - file output
     * @param content - contents of the file (text String)
     * 
     * @throws java.io.IOException
     *
     */
    //--------------------------------------------------------------------------
    public void writeFile(String path, String file, String content)  {
        FileWriter filestream;
        try {
            filestream = new FileWriter(path + File.separator + file);

        BufferedWriter output = new BufferedWriter(filestream);
        output.write(content);
        output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //--------------------------------------------------------------------------
    /**
     * 
     * 	Create directory
     * 
     * @param namedir - directory name
     * @return true - success
     * 
     */
    //--------------------------------------------------------------------------
    public boolean createDir(String namedir) {
        File dir = new File(namedir);
        return dir.mkdirs();
    }

    //--------------------------------------------------------------------------
    /**
     * 	Copy a single file from one directory to another
     * 
     * @param src - source file
     * @param dst - destination directory
     *
     * @throws java.io.IOException
     */
    //--------------------------------------------------------------------------
    public void copyFile(File src, File dst) throws IOException {
        ControllersManager.getinstance().getFileSystemManager().nioCopyFile(src, dst);
        /*
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
        * 
        */
    }

    /**
     * Move a single file from one directory to another
     * 
     * @param src - source file
     * @param dst - destination directory
     */
    public void moveFile(File src, File dst) throws IOException {

        src.renameTo(dst);

    }

    //--------------------------------------------------------------------------
    /**
     * 	copy or move an entire directory
     * 
     * @param sDir - source directory
     * @param dDir - destination directory
     * @param copy - flag , copy:true ; move:false
     * @param flagSameDim - flag , check if the directory has already been copied
     * 
     * @throws java.io.IOException
     */
    //--------------------------------------------------------------------------
    public void copy(String sDir, String dDir, boolean copy, boolean flagSameDim)  {

        File srcDir = new File(sDir);
        File dstDir = new File(dDir);
        //System.err.println ("#UTIL-COPY#"+sDir+" -> "+dDir);

        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
            String[] children = srcDir.list();
            for (int i = 0; i < children.length; i++) {
                String infile=sDir + File.separator + children[i];
                String outfile=dDir + File.separator + children[i];
                copy(infile, outfile, copy, flagSameDim);
            }

        } else {
            try{
            if (flagSameDim) {
                if (dstDir.getTotalSpace() != srcDir.getTotalSpace()) {
                    if (copy) {
                        copyFile(srcDir, dstDir);
                    } else {
                        moveFile(srcDir, dstDir);
                    }
                }
            } else {
                if (copy) {
                    copyFile(srcDir, dstDir);
                } else {
                    moveFile(srcDir, dstDir);
                }
            }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //System.err.println ("#UTIL-COPY"+sDir+"END");System.err.flush();
    }

    //--------------------------------------------------------------------------
    /**
     *
     *  Copy a directory to another
     *
     * @param postDir - source directory
     * @param outDir - destination directory
     * @param flag - flag , copy:true ; move:false
     *
     * @throws java.io.IOException
     */
    //--------------------------------------------------------------------------
    public void postFile(String postDir, String outDir, boolean flag) {
        postDir=CorrectPathFinder.getPath(postDir);
        File dir = new File(postDir);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            System.err.println(i+" - FILE IS -"+file[i].getName());
            String path=postDir + File.separator + file[i].getName();
            this.copy(CorrectPathFinder.getPath(path), outDir + File.separator + file[i].getName(), flag, false);
        }

    }

    //--------------------------------------------------------------------------
    /**
     * 	Create a list of Row (lesson) for each directory in acquisitionDir
     * 
     * @param acquisitionDir -acquisition directory
     * 
     * @return List<Row> - list of Row
     *
     * @throws java.lang.Exception
     */
    //--------------------------------------------------------------------------
    public List<Row> getListRow(String acquisitionDir) {

        File dir = new File(acquisitionDir);
        // creo un array di file/cartelle della directory di acquisizione
        File fl[] = dir.listFiles();

        List<Row> listRows = new LinkedList<Row>();

        if (fl.length > 0) {

            // per ogni cartella lezione
            for (int i = 0; i < fl.length; i++) {
                // se è una directory (numero_nome_data lezione)

                if (fl[i].isDirectory() && fl[i].listFiles().length != 0) { // !!! da fare e soddisfa i requisiti : (01_nome_data) ????

                    // get xml file information da file slide.xml, lecture.xml, timeslide.xml, (data.xml)
                    ManageInfoXML ex = new ManageInfoXML();

                    Lecture lecture = ex.getInfoLecture(Constants.lectureXmlName, fl[i].getPath());
                    //Lezione lezione = ex.getInfoData("data.xml", fl[i].getPath());

                    // possono essere opzionali: es se non esistono slides
                    LodeSlides slides = ex.getInfoSlides(Constants.slideXmlName, fl[i].getPath());
                    TimedSlides tslides = ex.getInfoTimeSlide(Constants.timeSlidesXmlName, fl[i].getPath());

                    //se i file della lezione esistono
                    if (lecture != null) {

                        boolean slideExist = false;
                        Cloud cloud = null;
                        if (slides != null && tslides != null) {
                            // genero la tagcloud dal testo delle slides della lezione
                            cloud = createTagCloud(slides, tslides);
                            slideExist = true;
                        }
                        // controllo se esistono le immagini delle slide
                        // oppure se la cartella slides non continee immagini delle slides
                        File slidesDir = new File(fl[i].getPath() + File.separator + Constants.slidesDirAcquisition);
                        if (!slidesDir.exists() || slidesDir.listFiles().length <= 0) {
                            slideExist = false;
                        }
                        // conto il numero delle slide presenti
                        int n_slide = -1;
                        if (slideExist) {
                            n_slide = slides._getSlides().getSlides().size();
                        }

                        //--------------------------
                        // controllo se esiste la cartella per il materiale didattico
                        boolean ssExist = false;
                        String[] fssNameList = null;
                        File slidesSources = new File(fl[i].getPath() + File.separator + Constants.ssDirAquisition);
                        if (slidesSources.exists() && slidesSources.listFiles().length > 0) {

                            File[] fssList = slidesSources.listFiles();
                            fssNameList = new String[fssList.length];
                            for (int j = 0; j < fssList.length; j++) {
                                fssNameList[j] = fssList[j].getName();
                            }

                            ssExist = true;
                        }

                        //--------------------------

                        // aggiungo alla lista la nuova riga della lezione
                        /*
                        listRows.add(new Row(
                                lecture.getSequenceNumber(),
                                lecture.getLecturer(),
                                lecture.getDate().toString().substring(0, 10),
                                lecture.getLectureName(),
                                lecture.getLectureName(),
                                Integer.parseInt(lecture.getVideoLength()),
                                lecture.getCourseRef(),
                                lecture.getCourseRef() + File.separator + fl[i].getName() + File.separator,
                                fl[i].getName(),
                                cloud,
                                n_slide,
                                ssExist,
                                fssNameList));
                         *
                         */
                        listRows.add(new Row(
                                lecture.getSequenceN(),
                                lecture.getLecturer(),
                                lecture.getDate().substring(0, 10),
                                lecture.getName(),
                                lecture.getName(),
                                lecture.getVideoLength(),
                                lecture.getCourseHomeURL(),
                                lecture.getCourseHomeURL() + File.separator + fl[i].getName() + File.separator,
                                fl[i].getName(),
                                cloud,
                                n_slide,
                                ssExist,
                                fssNameList));
                    } else {
                        System.out.println("errore nelli file xml della cartella: " + fl[i].getName());
                    // cartella che non contine informazioni xml in dir acquisition
                    // non è una cartella lezione!!!
                    }
                } else {
                    // file in dir acquisition
                    // non è una cartella
                }
            }

            // crea il comparator per l'ordinamento della lista
            Comparator<Row> comparator = new Comparator<Row>() {

                public int compare(Row r1, Row r2) {
                    //Confronto il numero di sequenza
                    int value = ((Integer) r1.getSequenceN()).compareTo((Integer) r2.getSequenceN());
                    //Se value è uguale a zero (cioè i numeri di sequenza sono uguali), confronto la data
                    value = value == 0 ? ((String) r1.getDate()).compareTo((String) r2.getDate()) : value;
                    //Se value è uguale a zero (cioè i le date sono uguali), confronto il titolo della lezione
                    return value == 0 ? ((String) r1.getTitle()).compareTo((String) r2.getTitle()) : value;

                }
            };
            // ordina la lista di Row
            Collections.sort(listRows, comparator);

        } else {
            System.err.println("directory Acquisition empty");
        }

        // return lista 
        return listRows;
    }

    //--------------------------------------------------------------------------
    /**
     * 
     * 	Create a list of Row[]
     * 
     * @param lrs -list of ROw
     *
     * @return List<Row[]> - list of ROw[]
     * 
     */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
     *  contiene per ogni elemento delle lista un array con tanti elementi quanti
     *  sono i video per una singola lezione.
     *  NB : Una lezione può contenere piu video inseriti in directory differenti
     *       (controllo sulla data uguale)*/
    //--------------------------------------------------------------------------
    public List<Row[]> getListArrayRow(List<Row> lrs) {

        // istanza una nuova lista di Row[]
        List<Row[]> lars = new LinkedList<Row[]>();

        // per ogni
        for (int i = 0; i < lrs.size(); i++) {

            // istanzio un array di Rows con una dimensione massima
            Row[] arrayRows = new Row[lrs.size()];

            int indexArray = 0;
            arrayRows[indexArray] = lrs.get(i);

            for (int j = i + 1; j < lrs.size(); j++) {
                if (arrayRows[indexArray].getDate().equals(lrs.get(j).getDate())) {
                    indexArray++;
                    i++;
                    arrayRows[indexArray] = lrs.get(j);
                }
            }

            // creo un nuovo array con la dimensione corretta
            Row[] arrayRows2 = new Row[indexArray + 1];
            for (int x = 0; x < indexArray + 1; x++) {
                arrayRows2[x] = arrayRows[x];
            }
            // aggiungo alla lista l'array di Row
            lars.add(arrayRows2);
        }

        return lars;
    }    
}
