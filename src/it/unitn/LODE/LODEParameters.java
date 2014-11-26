package it.unitn.LODE;

import it.unitn.LODE.MP.IF.LODEParametersIF;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.utils.Messanger;
import org.simpleframework.xml.*;
import java.io.File;


@Root (name="PARAMETERS")
 public class LODEParameters extends LODEStorableValues implements LODEParametersIF {
/**
 * This class defines some parameters for LODE not defined in the preferences
 * @author ronchet
 */
    public static int SOUND_YELLOW_THRESHOLD;
    public static int SOUND_ORANGE_THRESHOLD;
    public static int SOUND_GREEN_THRESHOLD;
    public static int SOUND_RED_THRESHOLD;

    static private final String sound_YELLOW_THRESHOLD="SOUND_YELLOW_THRESHOLD";
    static private final String sound_ORANGE_THRESHOLD="SOUND_ORANGE_THRESHOLD";
    static private final String sound_GREEN_THRESHOLD="SOUND_GREEN_THRESHOLD";
    static private final String sound_RED_THRESHOLD="SOUND_RED_THRESHOLD";

    static LODEParameters instance=null;
    // ==== HERE ARE THE PREFS =================================================
    /*
    @ElementMap(entry="SECTION", key="NAME", attribute=true,inline=true)
    HashMap<String,Properties> parametersMap=new HashMap<String,Properties>();
    HashMap<String,String> paramsMap=new HashMap<String,String>();
    */
     //
    private LODEParameters(){
            prefsFile=new File(LODEConstants.PARAMS_FILE);
    }
    public static LODEParameters getInstance() {
        if (instance == null) {
            instance=new LODEParameters(){};
            if (instance.prefsFile.exists()) try {
                instance = (LODEParameters) (new LODEParameters()).resume(instance.prefsFile, LODEParameters.class);
                instance.loadValuesIntoMap();
            } catch (Exception ex) {
                Messanger.getInstance().w("Error while deserializing parameters file",LODEConstants.MSG_ERROR);
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
        if (prefsMap.get(sound_RED_THRESHOLD)==null) addProperty(sound_RED_THRESHOLD,
                     "95",
                     "Threshold for red color",
                     "SOUND PANEL THRESHOLDS");
        SOUND_RED_THRESHOLD=Integer.parseInt(prefsMap.get(sound_RED_THRESHOLD));
        //
        if (prefsMap.get(sound_GREEN_THRESHOLD)==null) addProperty(sound_GREEN_THRESHOLD,
                     "50",
                     "Threshold for green color",
                     "SOUND PANEL THRESHOLDS");
        SOUND_GREEN_THRESHOLD=Integer.parseInt(prefsMap.get(sound_GREEN_THRESHOLD));
        //
        if (prefsMap.get(sound_ORANGE_THRESHOLD)==null) addProperty(sound_ORANGE_THRESHOLD,
                     "25",
                     "Threshold for orange color",
                     "SOUND PANEL THRESHOLDS");
        SOUND_ORANGE_THRESHOLD=Integer.parseInt(prefsMap.get(sound_ORANGE_THRESHOLD));
        //
        if (prefsMap.get(sound_YELLOW_THRESHOLD)==null) addProperty(sound_YELLOW_THRESHOLD,
                     "10",
                     "Threshold for yellow color",
                     "SOUND PANEL THRESHOLDS");
        SOUND_YELLOW_THRESHOLD=Integer.parseInt(prefsMap.get(sound_YELLOW_THRESHOLD));
    }
    public int getSOUND_YELLOW_THRESHOLD() {return SOUND_YELLOW_THRESHOLD;}
    public int getSOUND_ORANGE_THRESHOLD() {return SOUND_ORANGE_THRESHOLD;}
    public int getSOUND_GREEN_THRESHOLD() {return SOUND_GREEN_THRESHOLD;}
    public int getSOUND_RED_THRESHOLD() {return SOUND_RED_THRESHOLD;}
}
