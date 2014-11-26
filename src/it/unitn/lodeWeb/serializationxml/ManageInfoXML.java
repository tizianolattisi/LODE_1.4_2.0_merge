package it.unitn.lodeWeb.serializationxml;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.serializationxml.course.Course;
import it.unitn.lodeWeb.serializationxml.lecture.Lecture;
//import it.unitn.LODE.Models.Lecture;
import it.unitn.lodeWeb.serializationxml.notes.Notes;
import it.unitn.lodeWeb.serializationxml.slides.LodeSlides;
import it.unitn.lodeWeb.serializationxml.timeslide.TimedSlides;
import java.io.File;
import org.simpleframework.xml.core.Persister;
import it.unitn.lodeWeb.util.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class ManageInfoXML {

    //--------------------------------------------------------------------------
    /**
     *
     * Get info Slides from xml file
     *
     * @param fileName - xml filename
     * @param path - path xml file
     *
     * @return Slides - Slides class
     */
    //--------------------------------------------------------------------------
    public LodeSlides getInfoSlides(String fileName, String path) {

        /*To serialize an instance of the above object a Persister is required. 
        The persister object is then given an instance of the annotated object and an output result, 
        which is a file in this example. Other output formats are possible with the persister object.
         */
        // serializzazione della classe Lezione su pagina XML
        File result = new File(path + File.separator + fileName);
        if (!result.exists()) {
            return null;
        }
        Persister serializer = new Persister();
        LodeSlides ss = null;
        if (fileName.substring(0, fileName.length() - 4).equals(Constants.slideXmlName.substring(0, fileName.length() - 4))) {
            try {
                ss = serializer.read(LodeSlides.class, result);
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(ManageInfoXML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ss;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Get info Lecture from xml file
     *
     *
     * @param fileName -  xml filename
     * @param path - path xml file
     *
     * @return Lecture - Lecture class
     */
    //--------------------------------------------------------------------------
    public Lecture getInfoLecture(String fileName, String path) {

        File result = new File(path + File.separator + fileName);
        if (!result.exists()) {
            return null;
        }
        Persister serializer = new Persister();
        Lecture ll = null;
        if (fileName.substring(0, fileName.length() - 4).equals(Constants.lectureXmlName.substring(0, fileName.length() - 4))) {
            try {
                ll = serializer.read(Lecture.class, result);
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(ManageInfoXML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ll;
    }


    //--------------------------------------------------------------------------
    /**
     *
     *  Get timeslide info from xml file
     *
     * @param fileName -  xml filename
     * @param path - path xml file
     *
     * @return TimedSlides - TimedSlides class
     */
    //--------------------------------------------------------------------------
    public TimedSlides getInfoTimeSlide(String fileName, String path) {

        File result = new File(path + File.separator + fileName);
        if (!result.exists()) {
            return null;
        }
        Persister serializer = new Persister();
        TimedSlides ts = null;
        if (fileName.substring(0, fileName.length() - 4).equals(Constants.timeSlidesXmlName.substring(0, fileName.length() - 4))) {
            try {
                ts = serializer.read(TimedSlides.class, result);
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(ManageInfoXML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ts;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * get info data from xml file
     *
     * @param fileName -  xml filename
     * @param path - path xml file
     *
     * @return data - data class
     */
    //--------------------------------------------------------------------------
    public it.unitn.lodeWeb.serializationxml.data.Lezione getInfoData(String fileName, String path) throws Exception {

        File result = new File(path + File.separator + fileName);
        if (!result.exists()) {
            return null;
        }
        Persister serializer = new Persister();
        it.unitn.lodeWeb.serializationxml.data.Lezione l = null;
        if (fileName.substring(0, fileName.length() - 4).equals(Constants.dataXmlName.substring(0, fileName.length() - 4))) {
            l = serializer.read(it.unitn.lodeWeb.serializationxml.data.Lezione.class, result);
        }
        return l;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * get course details from xml file
     *
     * @param fileName -  xml filename
     * @param path - path xml file
     *
     * @return Course - Course class
     */
    //--------------------------------------------------------------------------
    public Course getInfoCourse(String fileName, String path) {

        File result = new File(path + File.separator + fileName);
        if (!result.exists()) {
            return null;
        }
        Persister serializer = new Persister();
        Course c = null;
        if (fileName.substring(0, fileName.length() - 4).equals(Constants.courseXmlName.substring(0, fileName.length() - 4))) {
            try {
                c = serializer.read(Course.class, result);
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(ManageInfoXML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return c;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * write note in a xml file
     *
     * @param c - notes
     * @param f - file to write
     *
     */
    //--------------------------------------------------------------------------
    public void writeXmlnotes(Notes c, File f) {
        if (f != null) {
            Persister serializer = new Persister();
            try {
                serializer.write(c, f);
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(ManageInfoXML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
