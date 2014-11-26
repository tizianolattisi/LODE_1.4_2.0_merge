package it.unitn.lodeWeb.execution;
//--------------------------------------------------------------------------
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.lodeWeb.gui.create.CreateSitePanel;
import it.unitn.lodeWeb.util.Vlc.VlcImages;
import it.unitn.lodeWeb.util.Zip.ZipDir;
import it.unitn.lodeWeb.serializationxml.ManageInfoXML;
import it.unitn.lodeWeb.serializationxml.course.Course;
import it.unitn.lodeWeb.serializationxml.notes.Note;
import it.unitn.lodeWeb.serializationxml.notes.Notes;
import it.unitn.lodeWeb.util.Row;
import it.unitn.lodeWeb.util.Util;
import it.unitn.lodeWeb.util.WriteOnHTML;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import it.unitn.lodeWeb.util.Constants;
import it.unitn.lodeWeb.util.Constants.ContextWeb;
import it.unitn.lodeWeb.util.OptionClass;
import it.unitn.lodeWeb.util.ProcessRuntime;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.Timer;
import org.apache.log4j.Logger;
import it.unitn.LODE.MP.utils.CorrectPathFinder;
//--------------------------------------------------------------------------

/**
 *
 * Esecution class that creates the web application (STATIC or DYNAMIC)
 *
 * @author Colombari Mattia
 */
public class Exec implements Runnable, ActionListener {

    String[] args = null;
    OptionClass oc = null;
    CreateSitePanel csp = null;
    //----------------------------------------------------------------------
    // util - classe che contiene le funzioni utili
    Util util;
    // WriteOnHTML - classe che contiene le funzioni per la
    //               generazione/stampa della pagina html
    WriteOnHTML wOnHtml;
    //--------------------------------------------------------------------------
    private String acquisitionDir;
    private String distributionDir;
    private String courseDir;
    private String outDir;
    private boolean copy = true;
    private ContextWeb context;
    //----------------------------------------------------------------------
    // password per dinamico
    private String username = null;
    private String psw = null;
    //----------------------------------------------------------------------
    static Logger cat = Logger.getLogger(Exec.class.getName());
    private Timer timer;
    //----------------------------------------------------------------------

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param args - option (acquisition dir, distribution dir, course dir,
     * output dir)
     * @param oc - option class
     * @param panel - graphic panel
     */
    //--------------------------------------------------------------------------
    public Exec(String[] args, OptionClass oc, CreateSitePanel panel) {

        BasicConfigurator.configure();

        timer = new Timer(1000, this);

        this.wOnHtml = new WriteOnHTML();
        this.util = new Util();
        this.args = args;
        this.oc = oc;
        this.csp = panel;
        //--------------------------------------------------------------
        // path della directory di acquisizione dei dati xml
        this.acquisitionDir = args[0];
        // path della directory di distribuzione (post processing)
        this.distributionDir = args[1];
        // path della directory del corso contenente distr, acqu...
        this.courseDir = args[2];
        // path della directory di output (web server)
        this.outDir = args[3];
        //----------------------------------------------------------------------
        // flag di copia o sposta file args[3]
        // "1" -> copia dir distribuzione
        // "2" -> sposta dir distribuzione
        copy = true;
        if (args[4].equals("1")) {
            copy = true;
        } else if (args[4].equals("2")) {
            copy = false;
        }


        //----------------------------------------------------------------------
        // contesto statico o dinamico???
        // STATIC or DyNAMIC
        context = Constants.ContextWeb.NULL;
        String contextPar = args[5];
        if (contextPar.equals("STATIC")) {
            context = (Constants.ContextWeb.STATIC);
        } else if (contextPar.equals("DYNAMIC")) {
            context = (Constants.ContextWeb.DYNAMIC);
        }

        this.username = oc.getUsername();
        this.psw = oc.getPsw();


    }

    //--------------------------------------------------------------------------
    /**
     * Copy slides sources
     *
     * @param row - row of the table
     *
     * @throws IOException
     *
     * @return void
     */
    //--------------------------------------------------------------------------
    private void copySLidesSources(Row row) throws IOException {
        if (oc.getSlidesSourcesFlag()) {
            if (row.getSsExist()) {
                String outDirSs = outDir + File.separator + Constants.dirCoursesWEB + File.separator + row.getNameCourseHome()
                        + File.separator + Constants.ssDirAquisition + File.separator + row.getNameDir();
                util.createDir(outDirSs);
                util.copy(acquisitionDir
                        + File.separator
                        + row.getNameDir()
                        + File.separator
                        + Constants.ssDirAquisition,
                        outDirSs, copy, true);
                //System.out.println("copy slideSources for lecture : " + row.getTitle());
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Create web application
     *
     * @param args - arguments options
     * @param oc - option class
     *
     * @throws Exception
     *
     */
    //--------------------------------------------------------------------------
    public void genera(String[] args, OptionClass oc) throws Exception {
        System.out.println("------------------------------------------");
        System.out.println("Web application : " + oc.getContext());
        System.out.println("Start time : " + Calendar.getInstance().getTime().toGMTString());
        System.out.println("------------------------------------------");

        //----------------------------------------------------------------------
        // creo la dir di output
        util.createDir(outDir);
        Course course = null;
        String courseHome = null;
        String nameCourse = null;
        int courseYear = 0;

        File dirCourse = new File(courseDir);

        // se distribution dir e acquisition dir esistono 
        if (dirCourse.exists() && new File(acquisitionDir).exists() && new File(distributionDir).exists()) {


            // COURSE XML per ottenere nome del corso ecc


            try {

                course = new ManageInfoXML().getInfoCourse(Constants.courseXmlName, dirCourse.getPath());
                nameCourse = course.getNameCourse();
                courseHome = course.getCourseHome();
                courseYear = course.getYear();

            } catch (Exception ex) {
                String err_message = "Error: Acquisition file :" + Constants.courseXmlName + " (" + ex.getMessage() + ")";
                cat.error(err_message);
                throw new Exception(err_message);
                //System.out.println(err_message);
            }
            if (course == null) {
                String err_message = "Error: Acquisition file :" + Constants.courseXmlName;
                throw new Exception(err_message);
            }

            //fare controlli sul numero di lezioni se è giusto --- lezioni da COURSE.xml e quelle presenti nelle cartelle

            List<Row> l_row = null;
            try {
                l_row = (util.getListRow(acquisitionDir));
            } catch (Exception ex) {
                String err_message = "Error: Acquisition files xml (" + ex.getMessage() + ")";
                cat.error(err_message);
                throw new Exception(err_message);
            }


            // test su vlc path
            ProcessRuntime proc = new ProcessRuntime();
            if (oc.getPreviewVideoFlag()) {
                if ((proc.getSo() == ProcessRuntime.Sop.MAC)||(proc.getSo() == ProcessRuntime.Sop.WINDOWS)) {
                    String actualFFMPEGX_PATH=CorrectPathFinder.getJarPath(LODEConstants.FFMPEG_COMMAND);
                    //String pathVlc = LODEConstants.FFMPEGX_PATH;
                    String pathVlc = actualFFMPEGX_PATH;
                    if (!new File(pathVlc).exists()) {
                        cat.error("FFMPEG X non present at: " + LODEConstants.FFMPEGX_PATH);
                        cat.error("==> Disabling generation of video previews");
                        oc.setPreviewVideoFlag(false);
                    }
                } else if (proc.getSo() == ProcessRuntime.Sop.LINUX) {
                    throw new Exception("LINUX is presently not supported");
                } /*else if (proc.getSo() == ProcessRuntime.Sop.WINDOWS) {
                    throw new Exception("WINDOWS is presently not supported");
                }*/
            }
            /*
             * REMOVED VLC VERSIONS, SUBSTITUTED WITH FFMPEG ABOVE if
             * (oc.getPreviewVideoFlag()) { // controllo se il path di vlc è
             * giusto if (new ProcessRuntime().getSo() ==
             * ProcessRuntime.Sop.WINDOWS) { // controllo se esiste il path per
             * vlc String pathVlc = Constants.pathFileVlcWINDOWS;
             *
             * try { pathVlc =
             * util.getMaching(util.leggiFile(Constants.pathConfigFile +
             * File.separator + Constants.configFile), "pathVLC=\"(.*?)\""); if
             * (pathVlc.equals("") || pathVlc == null) { pathVlc =
             * Constants.pathFileVlcWINDOWS; } } catch (Exception e) { pathVlc =
             * Constants.pathFileVlcWINDOWS; }
             *
             * if (new File(pathVlc).exists() && new File(pathVlc).isFile()) {
             * if (new File(pathVlc).getName().equals("vlc.exe")) {
             * oc.setPathVlc(pathVlc); oc.setPreviewVideoFlag(true); } else {
             * cat.error("VideoLAN error path: " +
             * Constants.pathFileVlcWINDOWS); oc.setPathVlc(null);
             * oc.setPreviewVideoFlag(false); }
             *
             * } else { cat.error("VideoLAN error path: " +
             * Constants.pathFileVlcWINDOWS); oc.setPathVlc(null);
             * oc.setPreviewVideoFlag(false); } } else if (new
             * ProcessRuntime().getSo() == ProcessRuntime.Sop.MAC) { //
             * controllo se esiste il path per vlc String pathVlc =
             * Constants.VLC_PATH_ON_MAC;
             *
             * // TOLTO LA LETTURA DELLA CONFIGURAZIONE!!!
             *
             * if (new File(pathVlc).exists() && new File(pathVlc).isFile()) {
             * if (new File(pathVlc).getName().equals("VLC")) {
             * oc.setPathVlc(pathVlc); oc.setPreviewVideoFlag(true); } else {
             * cat.error("VideoLAN error path [1]: " + pathVlc + "[Default: "+
             * Constants.VLC_PATH_ON_MAC + " ]"); oc.setPathVlc(null);
             * oc.setPreviewVideoFlag(false); }
             *
             * } else { cat.error("VideoLAN error path: [2]" + pathVlc +
             * "[Default: "+ Constants.VLC_PATH_ON_MAC + " ]");
             * oc.setPathVlc(null); oc.setPreviewVideoFlag(false); } }
             *
             * }
             */

            // CICLO
            //----------------------------------------------------------------
            //----------------------------------------------------------------
            List<Note> ns = new ArrayList<Note>();

            String rows = "";
            Iterator it = l_row.iterator();

            csp.startProgressBar("acquisition lectures info");
            timer.start();

            // per ogni lezione
            while (it.hasNext()) {
                // ottengo informazioni per una riga
                Row row = (Row) it.next();


                // crezione note per la lezioine
                Note n = new Note(row.getSequenceN(), row.getTitle(), row.getNote());
                // aggiungo note all'arrayList notes
                ns.add(n);


                try {
                    // copia del materiale didattico source slides
                    copySLidesSources(row);
                } catch (IOException ex) {
                    String err_message = "Error: copy slides sources file (" + ex.getMessage() + ")";
                    cat.error(err_message);
                }


                // creazione di una riga della tabella
                String rowS = wOnHtml.getRow(row, context, course, oc);

                // aggiungo la nuova riga 
                rows = rows + rowS;

                // tagCloud
                if (row.getCloud() != null) {
                    rows = rows + wOnHtml.getScriptTagCloud(row.getCloud(), row.getSequenceN(), row.getUrl());
                }

            }
            timer.stop();
            csp.doneProgressBar();
            //----------------------------------------------------------------
            //-------------------------FINE CICLO-----------------------------


            String courseHomeWeb = outDir + File.separator + Constants.dirCoursesWEB + File.separator + courseHome;
            //----------------------------------------------------------------------------------------------
            // creo la directory del corso e copio le lezioni
            if (oc.getLecturesFlag()) {
                csp.startProgressBar("copy lectures");
                timer = new Timer(3, this);
                timer.start();
                util.createDir(courseHomeWeb);
                util.copy(distributionDir, courseHomeWeb, copy, true);
                System.out.println("Copy lectures : OK");
                timer.stop();
                csp.doneProgressBar();
            }
            timer = new Timer(1000, this);
            //----------------------------------------------------------------------------------------------
            // creazione preview videos
            if (oc.getPreviewVideoFlag()) {
                try {
                    util.createDir(courseHomeWeb);

                    csp.startProgressBar("Preview video");
                    timer.start();

                    String outDirImages = courseHomeWeb + File.separator + Constants.dirPreviewsWEB;

                    VlcImages vlc = new VlcImages();
                    //vlc.fileVLC = oc.getPathVlc();
                    if (vlc.createPreviewVideos(distributionDir, l_row, outDirImages, "jpg")) {
                        vlc.createPreviewVideosGif(l_row, outDirImages, ".gif");
                    }

                } catch (Exception ex) {
                    String err_message = "Error: Create preview videos (" + ex.getMessage() + ")";
                    cat.error(err_message);
                } finally {
                    System.out.println("Previews videos : OK");
                    timer.stop();
                    csp.doneProgressBar();
                }
            }
            //----------------------------------------------------------------------------------------------
            csp.startProgressBar("Notes");
            timer.start();
            if (context.equals(Constants.ContextWeb.STATIC)) {
                // creazione delle note statiche
                if (oc.getNotesFlag()) {
                    util.createDir(courseHomeWeb);
                    String outDirNotes = courseHomeWeb + File.separator + Constants.dirNoteWEB;

                    wOnHtml.writeNotes(ns, outDirNotes);

                    // scrivo xml per le note
                        /*
                     * File result = new File(outDirNotes + courseHome +
                     * ".xml"); Serializer serializer = new Persister();
                     * serializer.write(new Notes(ns), result);
                     */
                    // prova scrittura nota singola ok funziona
                    //wOnHtml.writeSinleNote(new Note(1,"casestudy5" , "sdfasfasfasdfsadfasdf"), outDirNotes);
                    System.out.println("Static notes : OK");

                }

            } else if (context.equals(Constants.ContextWeb.DYNAMIC)) {
                // allora creo l'xml che viene utilizzato per la modifica da sito dinamico
                // scrivo xml per le note
                // questo va fatto prima di postare i file dell'applicazione
                String dirOut = outDir + File.separator + Constants.dirEditNoteXMLs + File.separator + courseHome;
                if (new File(dirOut).exists()) {
                    new File(dirOut).delete();
                }
                util.createDir(dirOut);
                File result = new File(dirOut + File.separator + courseHome + ".xml");
                (new ManageInfoXML()).writeXmlnotes(new Notes(ns), result);
                System.out.println("Xml notes : OK");
            }
            timer.stop();
            csp.doneProgressBar();
            //----------------------------------------------------------------------------------------------
            if (oc.getZipFlag()) {
                if (context.equals(Constants.ContextWeb.STATIC)) {
                    try {
                        csp.startProgressBar("Create zip files");
                        timer.start();
                        util.createDir(courseHomeWeb);
                        //---------------------------------------------
                        // crea gli zip per contesto STATICO!!!!!!!
                        //new ZipDir().doZip(new File((dirDistribution.getPath() + File.separator + row.getNameDir())).getPath(), outDir);
                        String outDirZip = courseHomeWeb + File.separator + Constants.dirZipsWEB;
                        (new ZipDir()).zipLectures(distributionDir, l_row, outDirZip);
                        // nota:  se invece è dinamico allora c'è gia il link dinamico alla pagina di creazione dinamica degli zip
                        //------------------------------------------------

                    } catch (Exception ex) {
                        String err_message = "Error: Zip lectures  (" + ex.getMessage() + ")";
                        cat.error(err_message);
                    } finally {
                        System.out.println("Zip lectures : OK");
                        timer.stop();
                        csp.doneProgressBar();
                    }
                }
            }
            //----------------------------------------------------------------------------------------------
            // posto i file per l'applicazione web
            csp.startProgressBar("Post file web application");
            timer = new Timer(3, this);
            timer.start();

            // copio il css scelto in to post dir
            if (oc.getCssPath() != null && !oc.getCssPath().equals("")) {
// MARCO                    File css = new File(oc.getCssPath());
                String cssFile = CorrectPathFinder.getPath(oc.getCssPath());
                File css = new File(cssFile);
                String ext = css.getName().substring(css.getName().length() - 3, css.getName().length());
                if (ext != null) {
                    if (ext.equals("css") || ext.equals("Css") || ext.equals("CSS")) {
                        try {
                            util.copyFile(css, new File(Constants.postCssDirDynamic + Constants.fileCssWebApplication));
                            util.copyFile(css, new File(Constants.postCssDirStatic + Constants.fileCssWebApplication));
                        } catch (IOException e) {
                            String err_message = "Error: post css file  (" + e.getMessage() + ")" + cssFile + css.toString();
                            cat.error(err_message);
                        }
                    }
                }
            }

            if (context.equals(Constants.ContextWeb.STATIC)) {
                util.postFile(Constants.postDirStatic, outDir, true);
            }
            if (context.equals(Constants.ContextWeb.DYNAMIC)) {
                if (this.psw == null || this.psw.equals("")) {
                    this.psw = Constants.pswDefault;
                }
                if (this.username == null || this.username.equals("")) {
                    this.username = Constants.usernameDefault;
                }

                String content = "username = \"" + this.username + "\"\n" + "psw = \"" + this.psw + "\"";
                //creo il file per login
                util.writeFile(Constants.pathFilePassword, "login.txt", content);
                System.out.println("Password file (un = " + this.username + ", psw = " + this.psw + ") : OK");
                // posto file applicazione dinamici
                util.postFile(Constants.postDirDynamic, outDir, true);
            }

            System.out.println("Post file web application : OK");
            timer.stop();
            csp.doneProgressBar();

            //----------------------------------------------------------------------------------------------
            // creo il codice html per la tabella
            // scrivo il file html
            String table = wOnHtml.buildTable(rows);
            timer = new Timer(500, this);
            String editLink = "";
            csp.startProgressBar("Create web application");
            timer.start();

            if (context.equals(Constants.ContextWeb.DYNAMIC)) {

                wOnHtml.writeHTML(outDir, courseHome + "_index.jsp",
                        WriteOnHTML.pageJsp
                        + WriteOnHTML.pageHtml + WriteOnHTML.topHtml
                        + wOnHtml.getJspCode_note(courseHome)
                        + wOnHtml.buildBody(table, courseYear, nameCourse, new String[1], courseHome)
                        + WriteOnHTML.bottomHtml);
            } else if (context.equals(Constants.ContextWeb.STATIC)) {
                //wOnHtml.writeHTML(outDir, courseHome + "_index.html",
                wOnHtml.writeHTML(outDir, "index.html",
                        WriteOnHTML.pageHtml
                        + WriteOnHTML.topHtml
                        + wOnHtml.buildBody(table, courseYear, nameCourse, new String[1], null)
                        + WriteOnHTML.bottomHtml);
            }

            System.out.println("Create web application : OK");
            timer.stop();
            csp.doneProgressBar();


            System.out.println("------------------------------------------");
            System.out.println("end time : " + Calendar.getInstance().getTime().toGMTString());
            System.out.println("------------------------------------------");

        } else {
            cat.error("Course dir error");
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Start creating
     */
    //--------------------------------------------------------------------------
    public void run() {
        try {
            csp.start();
            genera(args, oc);
        } catch (Exception ex) {
            System.out.println("Error(Exec_run): " + ex.getMessage());
        } finally {

            csp.done();
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Set Progress timer
     *
     * @param e - actionEvent
     */
    //--------------------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {
        csp.setProgress(csp.getProgress() + 1);
    }
}