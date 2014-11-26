package it.unitn.lodeWeb.util;

//--------------------------------------------------------------------------
import it.exprivia.cnos.opencloud.Cloud;
import it.exprivia.cnos.opencloud.Tag;
import it.unitn.lodeWeb.serializationxml.course.Course;
import it.unitn.lodeWeb.serializationxml.notes.Note;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * Functions for creating and writing html file
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class WriteOnHTML {

    private FileWriter filestream;
    private BufferedWriter output;
    //--------------------------------------------------------------------------
    // HEADER HTML
    /*new SWFObject(swf, id, width, height, version, background-color [, quality, xiRedirectUrl, redirectUrl, detectKey])
    *  swf – Il percorso ed il nome del vostro file swf.
    * id – L’ID del vostro oggetto o embed tag. L’embed tag avrà questo valore impostato come nome per i file che sfrutteranno l’opzione swliveconnect.
    * width – La larghezza del vostro filmato Flash.
    * height – L’altezza del vostro filmato Flash.
    * version – La versione del player richiesta per riprodurre il vostro filmato Flash. Questa può essere una stringa nel formato 'majorVersion.minorVersion.revision'. Un esempio potrebbe essere: “6.0.65”. Oppure potete semplicemente richiedere la versione maggiore, come “6”.
    * background color – Questo è il valore esadecimale del colore dello sfondo del vostro filmato Flash.
Argomenti opzionali:
    * quality – La qualità con la quale riprodurre il filmato Flash. Il default è “high”.
    * xiRedirectUrl –Se desiderate redirezionare gli utenti che completano l’aggiornamento ExpressInstall, potete specificare un URL alternativo in questo parametro.
    * redirectUrl – Se desiderate redirezionare gli utenti che non hanno la versione corretta del plugin, usate questo parametro inserendo un URL alternativo.
    * detectKey – Questo è l’URL che lo script SWFObject cercherà quando il rilevamento è bypassato. Il default è ‘detectflash’. Esempio: per aggirare il rilevamento del plugin Flash e semplicemente scrivere il filmato Flash nella pagina, potete aggiungere ?detectflash=false all’URL del documento contenente il filmato Flash.
     */
    private static String scriptFlashTagClouds =
            "   <script type=\"text/javascript\" src=\"js/swfobject.js\"></script>\n" +
            "   <script type=\"text/javascript\">\n" +
            "       var so = new SWFObject(\"tagcloud.swf\", \"tagcloud\", \"150\", \"100\", \"7\", \"#ffffff\");\n" +
            "       so.addParam(\"wmode\", \"transparent\");\n" +
            "       so.addVariable(\"mode\", \"tags\");\n" +
            "       so.addVariable(\"tcolor\", \"0x3333CC\");\n" +
            "       so.addVariable(\"tcolor2\", \"0x333333\");\n" +
            "       so.addVariable(\"hicolor\", \"0xff0000\");\n" +
            "       so.addVariable(\"tspeed\", \"120\");\n" + // it was 200
            "       so.addVariable(\"distr\", \"true\");\n" +
            "   </script>\n" +
            "   <script type=\"text/javascript\">\n" +
            "       var so2 = new SWFObject(\"tagcloud.swf\", \"tagcloud\", \"700\", \"400\", \"7\", \"#ffffff\");\n" +
            "       so2.addParam(\"wmode\", \"transparent\");\n" +
            "       so2.addVariable(\"mode\", \"tags\");\n" +
            "       so2.addVariable(\"tcolor\", \"0x3333CC\");\n" +
            "       so2.addVariable(\"tcolor2\", \"0x333333\");\n" +
            //"       so2.addVariable(\"hicolor\", \"0xff0000\");\n" +
            "       so2.addVariable(\"tspeed\", \"250\");\n" + //it was 200
            "       so2.addVariable(\"distr\", \"true\");\n" +
            "   </script>\n";
    private static String scriptFadeEffects =
            "<script type=\"text/javascript\" language=\"JavaScript\">" + "\n" +
            "<!--" + "\n" +
            "   function high(which2){" + "\n" +
            "       theobject=which2" + "\n" +
            "       highlighting=setInterval(\"highlightit(theobject)\",50)" + "\n" +
            "   }" + "\n" +
            "   function low(which2){" + "\n" +
            "       clearInterval(highlighting)" + "\n" +
            "       if (which2.style.MozOpacity)" + "\n" +
            "           which2.style.MozOpacity=0.3" + "\n" +
            "       else if (which2.filters)" + "\n" +
            "               which2.filters.alpha.opacity=30" + "\n" +
            "   }" + "\n" +
            "   function highlightit(cur2){" + "\n" +
            "       if (cur2.style.MozOpacity<1)" + "\n" +
            "           cur2.style.MozOpacity=parseFloat(cur2.style.MozOpacity)+0.1" + "\n" +
            "       else if (cur2.filters&&cur2.filters.alpha.opacity<100)" + "\n" +
            "               cur2.filters.alpha.opacity+=10" + "\n" +
            "           else if (window.highlighting)" + "\n" +
            "                   clearInterval(highlighting)" + "\n" +
            "   }" + "\n" +
            "//-->" + "\n" +
            "</script>";
    private static String scriptScrollerPreview =
            "<script type=\"text/javascript\" language=\"JavaScript\">\n" +
            "<!--\n" +
            "function oscroller_init() {\n" +
            "}\n" +
            "function stop() {\n" +
            "}\n" +
            "function show(i) {\n" +
            "\n}" +
            "function run() {\n" +
            "}\n" +
            "//-->\n" +
            "</script>\n" +
            "<script type=\"text/javascript\" src=\"js/Scroller/osscroller.js\"></script>\n";
    private static String scriptCss =
            "   <script type=\"text/javascript\" src=\"js/switchCSS.js\"></script>\n";
    private static String scriptFckEditor =
            "   <script type=\"text/javascript\" src=\"fckeditor/fckeditor.js\"></script>\n";
    private static String Jqueryfancyzoom =
            "<script type=\"text/javascript\" src=\"js/Jquery/jquery-1.3.2.min.js\"></script>\n" +
            "<script type=\"text/javascript\" charset=\"utf-8\">\n" +
            "var dirImgFancyZoom = \"images/imagesFancyZoom\";\n" +
            "</script>\n" +
            "<script type=\"text/javascript\" src=\"js/FancyZoom/fancyzoom.min.js\"></script>\n";
    private static String criptJqueryfancyZoom =
            "<script type=\"text/javascript\" charset=\"utf-8\">\n" +
            "   $(document).ready(function() {\n" +
            "       $('div.photo a').fancyZoom({scaleImg: true, closeOnClick: true});\n" +
            "       $('div.note a').fancyZoom({width:400, height:300});\n" +
            "       $('#medium_box_link').fancyZoom({width:400, height:300});\n" +
            "       $('#large_box_link').fancyZoom();\n" +
            "       $('#flash_box_link').fancyZoom();\n" +
            "       $('#note_box_link').fancyZoom();\n" +
            "   });\n" +
            "</script>\n";
    private static String cssStyleSheets =
            "   <link rel=\"stylesheet\" type=\"text/css\" href=\"Css/style.css\" title=\"default\"/>\n" +
            "   <link rel=\"alternate stylesheet\" type=\"text/css\" href=\"Css/Style2.css\" title=\"stile2\"/>\n" +
            "   <link rel=\"alternate stylesheet\" type=\"text/css\" href=\"Css/Style3.css\" title=\"stile3\"/>\n" +
            "   <link rel=\"alternate stylesheet\" type=\"text/css\" href=\"Css/Style4.css\" title=\"stile4\"/>\n" +
            "   <link rel=\"alternate stylesheet\" type=\"text/css\" href=\"Css/Style5.css\" title=\"stile5\"/>\n";
    // --------------------------------------------------------------------------
    public static String topHtmlGeneric =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n" +
            "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n" +
            "<head>\n" +
            "<body>\n";
    public static String pageHtml =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n" +
            "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n";
    public static String topHtml =
            "<head>\n" +
            "   <title>LODE</title>\n" +
            "   <meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\">\n" +
            "   <meta name=\"generator\" content=\"HAPedit 3.0\">\n" +
            "   <link rel=\"stylesheet\" type=\"text/css\" href=\"Css/" + Constants.fileCssWebApplication + "\"/>\n" +
            //scriptCss +
            scriptFlashTagClouds +
            Jqueryfancyzoom +
            criptJqueryfancyZoom +
            scriptFadeEffects +
            "</head>\n" +
            "<body>\n";
    public static String bottomHtml =
            "</body>\n" +
            "</html>\n";
    //--------------------------------------------------------------------------
    // TABLE
    String tableH =
            "<table id=\"table\">\n" +
            "   <caption></caption>\n" +
            "   <thead>\n" +
            "       <tr>\n" +
            "           <th width=5%; class=\"top-left\"></th>\n" +
            "           <th width=15% align='center'>Date<br><br>Lenght</th>\n" +
            "           <th width=15% align='center'>Lecturer<br><br>Title</th>\n" +
            "           <th width=15% align='center'>Contents<br>(Tag Cloud)</th>\n" +
            "           <th width=15% align='center'>Slides on line<br>(Click to browse)</th>\n" +
            "           <th width=10% align='center'>Downloadable slides<br>(Click to download)</th>\n" +
            "           <th width=15% align='center'>Video on line<br>(Click to view)</th>\n" +
            "           <th width=10% align='center'>Zipped video<br>(Click to download)</th>\n" +
            //"           <th width=20% align='center'>Image from video</th>\n" +
            "           <th align='center'>Notes<br>(Click to expand)</th>\n" +
            "           <th class=\"top-right\"></th>\n" +
            "       </tr>\n" +
            "   </thead>\n";
    private static String tableF =
            "   <tfoot>\n" +
            "       <tr>\n" +
            "           <td class=\"foot-left\" colspan=\"9\"></td>\n" +
            "           <td class=\"foot-right\">  </td>\n" +
            "       </tr>\n" +
            "   </tfoot>\n" +
            "   <tbody>\n";
    private static String endTable = "   </tbody>\n</table>\n";
    private static String SSW =
            "<div id=\"linkList\"><div id=\"styleswitch\">\n" +
            "   <h3></h3>\n" +
            "   <ul>\n" +
            "       <li><a href=\"javascript:setStyle('default')\">Stile base</a></li>\n" +
            "       <li><a href=\"javascript:setStyle('stile2')\">stile2</a></li>\n" +
            "       <li><a href=\"javascript:setStyle('stile3')\">stile3</a></li>\n" +
            "       <li><a href=\"javascript:setStyle('stile4')\">stile4</a></li>\n" +
            "       <li><a href=\"javascript:setStyle('stile5')\">stile5</a></li>\n" +
            "   </ul>\n" +
            "</div></div>\n";
    //-------------------------------------------------------------------------------
    // jsp per pagina index dinamica
    public static String jspVar = "<%" +
            "ServletContext context = getServletContext();" +
            "String webRoot = context.getRealPath(\"/\");" +
            "File xmlNote = null;" +
            "File xml_ref = null;" +
            "List<Note> ls = null;\n" +
            "List<Note> ls_ref = null;\n" +
            "Iterator i= null;\n" +
            "Note n= null;\n" +
            "%>";
    public static String pageJsp =
            "<%@page contentType=\"text/html\"\n" +
            "pageEncoding=\"UTF-8\"\n" +
            "import =\"net.fckeditor.*\"\n" +
            "import=\"org.simpleframework.xml.*\"\n" +
            "import=\"it.unitn.lodeweb.serializationxml.notes.*\"\n" +
            "import=\"java.io.File\"\n" +
            "import=\"java.util.List\"\n" +
            "import=\"java.util.ArrayList\"\n" +
            "import=\"java.util.Iterator\"\n" +
            "import=\"org.simpleframework.xml.load.Persister\"%>\n";
    //-------------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    /**
     *
     * get jsp code
     *
     * @param courseHome - directory name of the course
     * @return String - jsp code
     *
     */
    //--------------------------------------------------------------------------
    public String getJspCode_note(String courseHome) {

        String code = jspVar +
                // xml generato di controllo sul numero di lezioni e sul titolo
                "<%\n" +
                "xmlNote = new File(webRoot + \"" + Constants.dirEditNoteXMLs + "\" + File.separator + \"" + courseHome + "\" +" +
                "File.separator + \"" + courseHome + "\" + \"_notes.xml\");\n" +
                " xml_ref = new File(webRoot + \"" + Constants.dirEditNoteXMLs + "\" + File.separator + \"" + courseHome + "\" +" +
                "File.separator + \"" + courseHome + "\" + \".xml\");\n" +
                "if (xml_ref.exists()) {\n" +
                "   Persister serializer = new Persister();\n" +
                "   if (xmlNote.exists()) {\n" +
                "       Notes ns = serializer.read(Notes.class, xmlNote);\n" +
                "       ls = ns.getNotes();\n " +
                "   }\n" +
                "   Notes ns_ref = serializer.read(Notes.class, xml_ref);\n" +
                "   ls_ref = ns_ref.getNotes();\n" +
                "   if (ls != null) {\n" +
                "       i = ls.iterator();\n" +
                "   }\n" +
                "}%>";
        return code;
    }


    //--------------------------------------------------------------------------
    /**
     *
     * 	Write html file
     *
     * @param path - output path
     * @param file - file output
     * @param content - contents of the file (String)
     *
     * @throws java.io.IOException
     */
    //--------------------------------------------------------------------------
    public void writeHTML(String path, String file, String content)  {
        try {
            this.filestream = new FileWriter(path + File.separator + file);
            this.output = new BufferedWriter(filestream);
            this.write(content);
            this.output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(WriteOnHTML.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //--------------------------------------------------------------------------
    /**
     * 
     * write a string on BufferedWriter
     * 
     * @param str - string
     */
    //--------------------------------------------------------------------------
    public void write(String str) throws IOException {
        this.output.write(str);
    }

    //--------------------------------------------------------------------------
    /**
     *
     * 	"Builds" the html text (body html)
     *
     * @param course_year
     * @param course_name
     * @param teachers
     * @param courseHome
     * @return String - body html
     *
     */
    //-------------------------------------------------------------------------
    public String buildBody(String content, int course_year, String course_name, String[] teachers, String courseHome) {


        String body =
                "<script type='text/javascript' src='js/wz_tooltip/wz_tooltip.js'></script>\n" +
                "<div id=\"container\">\n" +
                "   <div id=\"header\">";
        if (course_year != 0 && course_name != null && teachers != null) {
            body = body +
                    "<table id=\"infoTable\">" +
                    "   <tr>" +
                    "       <td class=\"top-left\"></td>" +
                    "       <td>Course name: </td>" +
                    "       <td>" + course_name + "</td>" +
                    "       <td class=\"top-right\"></td>" +
                    "   </tr>" +
                    "   <tr>" +
                    "       <td class=\"foot-left\"></td>" +
                    "       <td>Year: </td>" +
                    "       <td>" + course_year + "</td>" +
                    "       <td class=\"foot-right\"></td>" +
                    "   </tr>" +
                    "</table>";

        }
        body = body +
                "   </div>\n " +
                "   <div id=\"footer\">";
        if (courseHome != null) {
            body = body + this.getLinkEditor(courseHome);
        }
        body = body +
                "</div>\n " +
                "   <div id=\"content\">\n" +
                content +
                "   </div>\n" +
                "</div>\n";
        return body;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * build table
     *
     * @param rows - table rows
     * @return String - table
     *
     */
    //--------------------------------------------------------------------------
    public String buildTable(String rows) {
        return tableH + tableF + rows + endTable;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * 	Conversion.. time from int to string
     *
     * @param time - time in milliseconds
     * @return String - time string : h m s
     *
     */
    //--------------------------------------------------------------------------
    private String intToStr_Time(int time) {
        if (time != 0) {
            int secondi = time % 60;
            int minuti = ((time - secondi) / 60) % 60;
            int ore = (time - secondi - (minuti * 60)) / 3600 % 3600;
            return ore + "h " + minuti + "m " + secondi + "s ";
        } else {
            return null;
        }

    }

    //--------------------------------------------------------------------------
    /**
     *
     * get fck editor script
     *
     * @param note - note
     * @param noteId - Id note
     * @param toolBarName -
     *
     * @return String - fck editor script
     */
    //--------------------------------------------------------------------------
    private String getScriptFckEditor(String note, int noteId, String toolBarName) {
        String script =
                "<script type=\"text/javascript\">\n" +
                "   var oFCKeditor = new FCKeditor( 'FCKeditor_" + noteId + "' ) ;\n" +
                "   oFCKeditor.BasePath = '/LODE/fckeditor/' ;\n" +
                "   oFCKeditor.Height = 100 ;\n" +
                "   oFCKeditor.Config[ 'ToolbarLocation' ] = 'Out:" + toolBarName + "' ;\n" +
                "   oFCKeditor.Value = '" + note + "' ;\n" +
                "   oFCKeditor.Create() ;\n" +
                "</script>\n";
        return script;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * 	Returns the script for the flash tagcloud
     *
     * @param cloud - tab cloud Cloud
     * @param id_tagcloud - lecture id (id tagCloud)
     * @param URL - video url (link sui tag)
     * @return String - script tag cloud
     *
     */
    //--------------------------------------------------------------------------
    public String getScriptTagCloud(
            Cloud cloud, int id_tagcloud, String URL) {

        String script =
                "       <script type=\"text/javascript\">\n";

        String so = "           so.addVariable(\"tagcloud\", \"<tags> ";
        String so2 = "           so2.addVariable(\"tagcloud\", \"<tags> ";
        String tags = "";
        String tags2 = "";
        if (cloud != null) {
            Iterator i = cloud.tags().iterator();
            Tag t = null;
            while (i.hasNext()) {
                t = ((Tag) i.next());

                tags =
                        tags + "<a style='font-size: " + t.getWeight() + "pt;'>" + t.getName() + "</a>";
                tags2 =
                        tags2 + "<a href='" + Constants.dirCoursesWEB + File.separator + URL + "index.html" + "' style='font-size: " + t.getWeight() + "pt;'>" + t.getName() + "</a>";
            }

        }
        so = so + tags + "</tags>\");";
        so2 =
                so2 + tags2 + "</tags>\");";

        script =
                script + so + so2 +
                "  $(document).ready(function() {$('#flash_tcloud_link_" + id_tagcloud + "').fancyZoom();});      " +
                "           so.write(\"tcloud" + id_tagcloud + "\");\n" +
                "           so2.write(\"tcloud" + id_tagcloud + "_zoom\");\n" +
                "       </script>\n";

        return script;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * 	Returns an html link, String format, (page managing notes)
     *
     * @param courseName - course name
     *
     * @return String - html link 
     *
     */
    //--------------------------------------------------------------------------
    public String getLinkEditor(String courseName) {
        String edit = "<div class=\"login\">\n" +
                "<a href=\"#login\" id=\"large_box_link\">ADMIN</a>\n" +
                "<div id=\"login\">\n" +
                "    <iframe frameborder=\"0\" style=\"overflow-x: hidden;\" align=\"center\" src=\"adminJsp/Login.jsp?cn=" + courseName + "\" width=\"500\" height=\"240\"></iframe>\n" +
                "</div>\n" +
                "</div>\n";
        return edit;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Write static notes on files
     *
     * @param ln - lista di note
     * @param outdir - directory output
     *
     */
    //--------------------------------------------------------------------------
    public void writeNotes(List<Note> ln, String outdir) {

        Iterator it = ln.iterator();
        (new Util()).createDir(outdir);
        // per ogni lezione
        while (it.hasNext()) {

            Note n = (Note) it.next();

            String htmlNote = buildBody(n.getNote(), 0, null, null, null);

            if (!new File(outdir + File.separator + n.getTitle() + ".html").exists()) {
                writeHTML(outdir, n.getTitle() + ".html", topHtmlGeneric + htmlNote + bottomHtml);
            }

        }


    }

    //--------------------------------------------------------------------------
    /**
     *
     * Write one static note
     *
     * @param note - note
     * @param outdir - directory output
     *
     */
    //--------------------------------------------------------------------------
    public void writeSinleNote(Note note, String outdir) throws IOException {
        (new Util()).createDir(outdir);
        String htmlNote = buildBody(note.getNote(), 0, null, null, null);
        writeHTML(outdir, note.getTitle() + ".html", topHtmlGeneric + htmlNote + bottomHtml);

    }

    //--------------------------------------------------------------------------
    /**
     *
     * "Build" one row of the table
     *
     * @param r - Row
     * @param cw - context STATIC or DYNAMIC
     * @param courseInfo - information about the course from the file COURSE.xml
     * @param oc - option class
     * 
     * @return String - row
     *
     */
    //--------------------------------------------------------------------------
    public String getRow(Row r, Constants.ContextWeb cw, Course courseInfo, OptionClass oc) {


        // DA FARE : distinzione tra contesto statico e dinamico
        // DA FARE : materiale didattico


        String dirCourseWeb = Constants.dirCoursesWEB + File.separator + r.getNameCourseHome();

        //----------------------------------------------------------------------------------
        // CONTESTO STATICO
        //----------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------

        // ottengo la stringa per la lunghezza video
        String lengthVideo = intToStr_Time(r.getTime()) + "\n";
        //----------------------------------------------------------------------------------
        // link alla lezione
        String url = "<a href=\"" + Constants.dirCoursesWEB + File.separator + r.getUrl() + "index.html\">video on line</a>\n";
        //----------------------------------------------------------------------------------


        String imgHtml = "";
        if (oc.getImageFromSlidesFlag()) {// se vengono copiate le lezioni allora slideshow

            // flag per controllare l'esistenza delle imagini di preview
            if (r.getNSlide() > 0) {

                String imgSmall = Constants.dirCoursesWEB + File.separator + r.getUrl() + Constants.imgDirDistribution + File.separator + "1.jpg";
                // codice html per la preview con zoom
                imgHtml =
                        "<div class=\"photo\">\n" +
                        "   <a href=\"#" + r.getNameDir() + "_img2\">\n" +
                        "<img WIDTH=\"100\" src=\"" + imgSmall + "\" style=\"-moz-opacity:0.3\" onmouseover=\"Tip('Click to preview the content of the slides')\" onmouseout=\"UnTip()\" />\n" + //onMouseover=\"high(this)\" onMouseout=\"low(this)\"  />\n" +
                        "</a>\n" +
                        "</div>\n" +
                        "<div id=\"" + r.getNameDir() + "_img2\">\n" +
                        "   <iframe frameborder=\"0\" scrolling=\"No\" align=\"center\" width=\"600\" height=\"450\" src=\"slideshow.html?ns=" + r.getNSlide() + "&cn=" + r.getUrl() + "\"></iframe>\n" +
                        "</div>\n";
            }
        }

        //----------------------------------------------------------------------------------
        // tagCloud zoom
        String tagCloud = "";
        if (r.getCloud() != null) {
            tagCloud = "<a href=\"#tcloud" + r.getSequenceN() + "_zoom\" id =\"flash_tcloud_link_" + r.getSequenceN() + "\">" +
                    "<div id=\"tcloud" + r.getSequenceN() + "\">\n" +
                    "</div>\n" +
                    "</a>\n" +
                    "<div id=\"tcloud" + r.getSequenceN() + "_zoom\"/>\n";
        }
        //----------------------------------------------------------------------------------


        // codice per la preview video

        String videoHtmlPreview = "";
        if (oc.getPreviewVideoFlag()) {

            String dirPreview = dirCourseWeb + File.separator + Constants.dirPreviewsWEB + File.separator +
                    r.getNameDir();
            videoHtmlPreview =
                    "<script type=\"text/javascript\" language=\"JavaScript\">\n" +
                    "   num_immagini = \"4\";\n" +
                    "   oscroller_init();\n" +
                    "</script>\n";
            // videoHtmlPreview = videoHtmlPreview + "<img WIDTH=\"100\" src=\"" + dirPreview + File.separator + Constants.nameImgPreview + i + ".jpg" + "\" style=\"filter:alpha(opacity=30);-moz-opacity:0.3\" onMouseover=\"high(this)\" onMouseout=\"low(this)\"  />\n";
            videoHtmlPreview = videoHtmlPreview + "<img name=\"partenza" + r.getSequenceN() + "\" WIDTH=\"100\" src=\"" +
                    dirPreview + File.separator + Constants.nameImgPreview +
                    "11" + ".jpg" + "\"  onMouseover=\"run(this,'" + dirPreview + File.separator + Constants.nameImgPreview + "')\" onMouseout=\"stop(); return false;\"  />\n";

            videoHtmlPreview =
                    "<div class=\"photo\">\n" +
                    "   <a href=\"#" + r.getNameDir() + "_videoPrev\">\n" +
                    "<img WIDTH=\"100\" src=\"" +
                    dirPreview + File.separator + Constants.nameImgPreview + "11" + ".jpg" +
                    "\" style=\"-moz-opacity:0.3\" onMouseover=\"high(this)\" onMouseout=\"low(this)\"  />\n" +
                    "</a>\n" +
                    "</div>\n" +
                    "<div id=\"" + r.getNameDir() + "_videoPrev\">\n" +
                    "   <img src=\"" + dirPreview + File.separator + r.getNameDir() + ".gif\"/>\n" +
                    "</div>\n";

            url = "<a href=\"" + Constants.dirCoursesWEB + File.separator + r.getUrl() +
                    "index.html\"><img width=\"100\" src=\""+dirPreview +
                    File.separator + r.getNameDir() + ".gif\" alt=\"Click to start viewing the video on line\" onmouseover=\"Tip('Click to start viewing the video on line')\" onmouseout=\"UnTip()\" ></a>\n";
        }
        //----------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------
        // CONTESTO DINAMICO / statico
        //--------------------------------------
        //----------------------------------------------------------------------------------

        String noteHtml = "";
        String download = "";


        if (cw.equals(Constants.ContextWeb.DYNAMIC)) {
            // link per dowload
            download = "<a href=\"DownloadServlet?op=zip&lectureUrl=" +
                    Constants.dirCoursesWEB + File.separator + r.getUrl() +
                    "&courseName=" + r.getNameCourseHome() + "\">download</a>\n";


            // aggiunta delle note con il titolo della lezione
            String notes = "<iframe frameborder=\"0\" style=\"overflow-x: hidden;\" align=\"center\" src=\"editNoteJsp/nota.jsp?id=" +
                    r.getSequenceN() + "&CourseName=" + r.getNameCourseHome() + "\" width=\"100%\" height=\"400\">" +
                    "</iframe>\n";
            // codice html per le notes zoom


            // visualizza parte delle note se esistono altrimenti ridirigi verso lo zoom delle note intere
            String jspNote = "<%" +
                    "xmlNote = new File(webRoot + \"" + Constants.dirEditNoteXMLs + "\" + File.separator + \"" + r.getNameCourseHome() + "\" +\n" +
                    "   File.separator + \"" + r.getNameCourseHome() + "\" + \"_notes.xml\");\n" +
                    "xml_ref = new File(webRoot + \"" + Constants.dirEditNoteXMLs + "\" + File.separator + \"" + r.getNameCourseHome() + "\" +\n" +
                    "   File.separator + \"" + r.getNameCourseHome() + "\" + \".xml\");\n" +
                    "if (xml_ref.exists()) {\n" +
                    "   Persister serializer = new Persister();\n" +
                    "   if (xmlNote.exists()) {\n" +
                    "       Notes ns = serializer.read(Notes.class, xmlNote);\n" +
                    "       ls = ns.getNotes();\n" +
                    "   }\n" +
                    "   Notes ns_ref = serializer.read(Notes.class, xml_ref);\n" +
                    "   ls_ref = ns_ref.getNotes();\n" +
                    "   if (ls != null) {\n" +
                    "       if (ls.get(" + r.getSequenceN() + ").getNote() == null) {\n" +
                    "           out.print(\"nessuna nota\");\n" +
                    "       } else {\n" +
                    "           out.print(ls.get(" + r.getSequenceN() + ").getNote() + \"...\");\n" +
                    "       }\n" +
                    "   }\n" +
                    "}\n" + "%>";

            String jspNote1 = "<%\n" +
                    "if (ls != null) {\n" +
                    "   if(i!=null){\n" +
                    "       if(i.hasNext()){\n" +
                    "           n = (Note) i.next();\n" +
                    "           if (n.getNote() == null) {\n" +
                    "               out.print(\" - \");\n" +
                    "           } else {\n" +
                    "               if (n.getNote().length() > 10) {\n" +
                    "                   out.print(n.getNote().substring(0, 9) + \"...\");\n" +
                    "               } else {\n" +
                    "                   out.print(n.getNote());\n" +
                    "               }\n" +
                    "           }\n" +
                    "       }\n" +
                    "   }\n" +
                    "}\n" + "\n%>\n";
            jspNote = "nota";


            noteHtml = "<div class=\"note\">\n" +
                    "<a href=\"#note_box" + r.getSequenceN() + "\" id=\"note_box_link\" span onmouseover=\"Tip('Click to start viewing the video on line')\" onmouseout=\"UnTip()\">"+ jspNote1 +"</a>\n" +
                    "<div id=\"note_box" + r.getSequenceN() + "\">\n" + notes +
                    "</div>\n" +
                    "</div>\n";

        } else if (cw.equals(Constants.ContextWeb.STATIC)) {

            if (oc.getNotesFlag()) // codice html per le notes zoom
            {

                // note statiche ... collegamento alla pagina statica di ogni nota
                String notes = "<iframe frameborder=\"0\" scrolling=\"No\" align=\"center\" src=\"" +
                        dirCourseWeb + File.separator + Constants.dirNoteWEB + File.separator +
                        r.getTitle() + ".html\" height=\"400\">\n" +
                        "</iframe>";

                noteHtml = "<div class=\"note\"  onmouseover=\"Tip('Click to view the note')\" onmouseout='UnTip()'>" +
                        "<a href=\"#note_box" + r.getSequenceN() + "\" id=\"note_box_link\"><div name=\"note_" + r.getTitle() + "\">" + r.getNote() + "</div></a>\n" +
                        "<div id=\"note_box" + r.getSequenceN() + "\">\n" + notes +
                        "</div>\n" +
                        "</div>\n";
            }

            if (oc.getZipFlag()) {

                // link per dowload diretto // zip gia creato
                String pathZip = dirCourseWeb + File.separator +
                        Constants.dirZipsWEB +
                        File.separator + r.getNameDir() + ".zip";

                File zipFile = new File(pathZip);
                long dim = zipFile.length();
                download = "<a href=\"" + pathZip + "\"><img src='images/box.gif' alt='Click to download video' onmouseover=\"Tip('Click to download the video')\" onmouseout='UnTip()'><br>"+zipFile.getName()+"</a>\n";

            }
        }


        //-------------------------------------
        // source slides --- materiale didattico
        String sourcesSlides = "";

        if (oc.getSlidesSourcesFlag()) { // se slide sources richieste

            if (r.getSsExist()) {
                String outDirSsWeb = dirCourseWeb +
                        File.separator + Constants.ssDirAquisition + File.separator + r.getNameDir();
                String[] lfn = r.getSsList();
                if (lfn != null) {
                    for (int i = 0; i < lfn.length; i++) {
                        if (lfn[i].equalsIgnoreCase(".DS_Store")) continue; //skip mac system file
                        String fileImage="images/file.jpg";
                        if (lfn[i].toLowerCase().endsWith(".ppt")) fileImage="images/ppt.jpg";
                        else if (lfn[i].toLowerCase().endsWith(".pdf")) fileImage="images/pdf.jpg";
                        sourcesSlides = sourcesSlides + 
                                "<a href=\"" + outDirSsWeb + File.separator + lfn[i] + "\" ><img src=\""+fileImage+
                                "\" alt=\"download slides\" width=\"50\" onmouseover=\"Tip('Click to download the slides')\" onmouseout='UnTip()'><br>"+
                                lfn[i] + "</a><br>\n";
                    }
                }


            }
        }
        //-------------------------------------

        // codice html per una riga della tabella
        String tableR =
                "       <tr>\n" +
                "           <td>" + r.getSequenceN() + "</td>\n" +
                "           <td align='center'>" + r.getDate() + "<br><br>" + lengthVideo +"</td>\n" +
                "           <td align='center'>" + r.getLecturer() + "<br><br>" + r.getTitle() + "</td>\n" +
                "           <td align='center'>" + tagCloud + "</td>\n" +
                "           <td align='center'>" + imgHtml + "</td>\n" +
                "           <td align='center'>" + sourcesSlides + "</td>\n" +
                "           <td align='center'>" + url + "</td>\n" +
                "           <td align='center'>" + download + "</td>\n" + //(" + dim + " Mb)
                "           <td>" + noteHtml + "</td>\n" +
                //"           <td>" + videoHtmlPreview + "</td>\n" +
                "           <td></td>\n" +
                "       </tr>\n";
        return tableR;
    }
}

