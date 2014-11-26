package it.unitn.lodeWeb.util;
//--------------------------------------------------------------------------
import it.unitn.LODE.MP.constants.LODEConstants;
import java.io.File;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 * Constants collection.
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public interface Constants {
    //--------------------------------------------------------------------------
    // file name and extension
    public final static String FS=File.separator;
    public final static String RS="/";// Separator in resources

    //static String sep = File.separator;
    static String newLine = "\n";
    static String compressFileT = ".zip";
    static String imgT = ".jpg";
    static String nameImgPreview = "snap";
    //--------------------------------------------------------------------------
    // XMLS
    static String dataXmlName = "data.xml";
    static String slideXmlName = "SLIDES.XML";
    static String lectureXmlName = "LECTURE.XML";
    static String timeSlidesXmlName = "TIMED_SLIDES.XML";
    static String courseXmlName = "COURSE.XML";
    //--------------------------------------------------------------------------
    // LODEWeb directory
    static String outFile = "index.jsp";
    static String imgsDir = "images";
    static String postDirStatic =  File.separator+LODEConstants.RESOURCE_ROOT +RS+"toPost" + RS + "STATIC";
    static String postDirDynamic = File.separator+LODEConstants.RESOURCE_ROOT +RS+ "toPost" + RS + "DYNAMIC";
    //--------------------------------------------------------------------------
    // Directory web
    static String dirEditNoteXMLs = "editNoteJsp" + File.separator + "xmls";
    static String dirCoursesWEB = "courses";
    static String dirZipsWEB = "zips";
    static String dirNoteWEB = "notes";
    //--------------------------------------------------------------------------
    // Preview video
    static String dirPreviewsWEB = "previews";
    //--------------------------------------------------------------------------
    static String configFile = "config.txt";
    static String pathConfigFile = ".";//../"+File.separator;
    //--------------------------------------------------------------------------
    // directory distribution
    static String distNameDir = "Distribution";
    static String contentDirDistribution = "content";
    static String imgDirDistribution = "content" + File.separator + "img";
    static String movieName = "movie.flv";
    //--------------------------------------------------------------------------
    // directory acquisition
    static String acquisNameDir = "Acquisition";
    static String slidesDirAcquisition = "Slides";
    static String ssDirAquisition = "Slides_Sources";
    //--------------------------------------------------------------------------
    // script path
    static String criptPathZip = "bashScript" + File.separator + "CreatezipDir.sh";
    //--------------------------------------------------------------------------
    // context enum

    public enum ContextWeb {

        STATIC, DYNAMIC, NULL
    }
    //--------------------------------------------------------------------------
    // CSS editor
    static int n_css = 5;
    static String[] cssList = {
        "Style1.css",
        "Style2.css",
        "Style3.css",
        "Style4.css",
        "Style5.css",
        "MyCss.css"};
    static String dirCss = File.separator+LODEConstants.RESOURCE_ROOT+RS+"css" + RS;
    static String myCss = "MyCss.css";
    static String myCssHtml = "test_MyCss.html";
    static String genericHtml = "test.html";
    //--------------------------------------------------------------------------
    // to post dir CSS
    static String postCssDirDynamic =File.separator+LODEConstants.RESOURCE_ROOT+RS+"toPost" + RS + "DYNAMIC" +
            RS + "Css" + RS;
    static String postCssDirStatic =File.separator+LODEConstants.RESOURCE_ROOT+RS+"toPost" + RS + "STATIC" +
            RS + "Css" + RS;
    //--------------------------------------------------------------------------
    // css web application
    static String fileCssWebApplication = "Style.css";
    //--------------------------------------------------------------------------
    // PASSWORD ADMIN
    // path per il salvataggio del file con password per sito dinamico
    static String pathFilePassword = postDirDynamic + File.separator + "WEB-INF";
    // username e password di default
    static String usernameDefault = "admin";
    static String pswDefault = "admin";
    //--------------------------------------------------------------------------

    static class message{
        
        static String badOutputDir = "Bad course directory";
        static String badCourseDir = "";


    }
}
