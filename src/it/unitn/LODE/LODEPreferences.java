package it.unitn.LODE;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.gui.MenuManager;
import it.unitn.LODE.utils.Messanger;
import org.simpleframework.xml.*;
import it.unitn.QT.QT_LODEConstants;
import java.io.File;

/**
 *
 * @author ronchet
 */
@Root (name="PREFERENCES")
public class LODEPreferences extends LODEStorableValues {
    static String UNDEFINED="none";
    // here we keep all the names of the properties
    public static final String preferredCamera="Preferred Camera";
    public static final String fallbackCamera="Fallback Camera";
    public static final String preferredMicrophone="Preferred Microphone";
    public static final String fallbackMicrophone="Fallback Microphone";
    public static final String debug="Debug";
    public static final String passwordProtected="Preferences are password protected";
    public static final String password="Password";
    public static final String course="Last used course";
    public static final String course2="Last used course-2";
    public static final String course3="Last used course-3";
    static LODEPreferences instance=null;
    // ==== HERE ARE THE PREFS =================================================
    //
    // http://stackoverflow.com/questions/5789233/how-to-set-hidden-proprerty-of-a-file
    private LODEPreferences(){
            prefsFile=new File(LODEConstants.PREFS_FILE);
    }
    public static synchronized LODEPreferences getInstance() {
        if (instance == null) {
            instance=new LODEPreferences(){};
            if (instance.prefsFile.exists()) try {
                instance = (LODEPreferences) (new LODEPreferences()).resume(instance.prefsFile, LODEPreferences.class);
                instance.loadValuesIntoMap();
            } catch (Exception ex) {
                Messanger.getInstance().w("Error while deserializing preference file",LODEConstants.MSG_ERROR);
            } 
            instance.readDefaults(); // there might be some undefined value in the prefs file!
            instance.cleanup();
            instance.loadValuesIntoMap();
            // since the prefs file did not exist - or might have changed due to new defaults
            // we recreate it
            instance.savePreferences(); 
        }
        return instance;
    }
    void readDefaults() {
        if (prefsMap.get(preferredCamera)==null) addProperty(preferredCamera,
                    QT_LODEConstants.EXTERNAL_DV,
                    "First choice for the video acquisition device",
                    "I/O DEVICES");
        if (prefsMap.get(fallbackCamera)==null) addProperty(fallbackCamera,
                     UNDEFINED,
                     "Second choice for the video acquisition device",
                    "I/O DEVICES");
        if (prefsMap.get(preferredMicrophone)==null) addProperty(preferredMicrophone,
                     QT_LODEConstants.DV_MICROPHONE,
                     "First choice for the audio acquisition device",
                    "I/O DEVICES");
        if (prefsMap.get(fallbackMicrophone)==null) addProperty(fallbackMicrophone,
                     QT_LODEConstants.INTERNAL_MICROPHONE,//UNDEFINED,//
                     "Second choice for the audio acquisition device",
                    "I/O DEVICES");
        if (prefsMap.get(debug)==null) addProperty(debug,
                     "NO",
                     "yes if debugging is enabled, no otherwise",
                    "GENERIC");
        if (prefsMap.get(passwordProtected)==null) addProperty(passwordProtected,
                     "NO",
                     "Should preferences be password protected [YES-NO]",
                    "PASSWORD PROTECTION");
        if (prefsMap.get(password)==null) addProperty(password,
                     "",
                     "Password",
                    "PASSWORD PROTECTION");
        if (prefsMap.get(course)==null) addProperty(course,
                     "",
                     "Course",
                    "LAST USED COURSE");
        if (prefsMap.get(course2)==null) addProperty(course2,
                     "",
                     "Course2",
                    "LAST USED COURSE");
        if (prefsMap.get(course3)==null) addProperty(course3,
                     "",
                     "Course3",
                    "LAST USED COURSE");        
    }
    public void addCourse(String thisCourse){
        String course1_value=prefsMap.get(course);
        if (thisCourse.equals(course1_value)) {
            // nothing to do:
            //last course is already the current course
            return;
        }
        String course2_value=prefsMap.get(course2);
        if (thisCourse.equals(course2_value)) {
            // this course was second in list:
            // swap first and second
            changeProperty(LODEPreferences.course2, course1_value);
            changeProperty(LODEPreferences.course, thisCourse);
            savePreferences();
            MenuManager.getInstance().updateRecentCourses();
            return;
        }
        // insert this course at first place, and push down the next two
        changeProperty(LODEPreferences.course3, course2_value);
        changeProperty(LODEPreferences.course2, course1_value);
        changeProperty(LODEPreferences.course, thisCourse);
        savePreferences();
        MenuManager.getInstance().updateRecentCourses();
        return;
    }
}
