package it.unitn.lodeWeb.util;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class OptionClass {

    private boolean zip;
    private boolean imageFromVideo;
    private boolean imageFromSlides;
    private String pathVlc;
    private boolean notes;
    private boolean lectures;
    private boolean slidesSources;

    private String cssPath;
    private Constants.ContextWeb context;
    private String username;
    private String psw;

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param pVideo - boolean , images from video
     * @param imagefromSlides - boolean , image from slides
     * @param lectures - boolean , copy directory lectures
     * @param zip - boolean, create zip
     * @param notes - boolean, create notes
     * @param slidesSources - boolean, copy slides sources
     * @param context - context web applicazion (STATIC, DYNAMIC)
     * @param cssPath - Cascading Style Sheet path
     * @param username - admin username
     * @param psw - admon password
     *
     */
    //--------------------------------------------------------------------------
    public OptionClass(boolean pVideo, boolean imagefromSlides, String pathVlc, boolean lectures,
            boolean zip, boolean notes, boolean slidesSources, String context,
            String cssPath, String username, String psw) {

        this.zip = zip;
        this.imageFromVideo = pVideo;
        this.imageFromSlides = imagefromSlides;
        this.pathVlc = pathVlc;
        this.notes = notes;
        this.lectures = lectures;
        this.slidesSources = slidesSources;
        this.cssPath = cssPath;
        if (context.equals(Constants.ContextWeb.DYNAMIC.toString())) {
            this.context = Constants.ContextWeb.DYNAMIC;
        } else if (context.equals(Constants.ContextWeb.STATIC.toString())) {
            this.context = Constants.ContextWeb.STATIC;
        }
        this.username = username;
        this.psw = psw;

    }

    /**
     *
     * @return boolean
     */
    public boolean getZipFlag() {
        return this.zip;
    }

    /**
     *
     * @return boolean
     */
    public boolean getPreviewVideoFlag() {
        return this.imageFromVideo;
    }

    /**
     * 
     * @param flag
     */
    public void setPreviewVideoFlag(boolean flag) {
       this.imageFromVideo = flag;
    }


    /**
     *
     * @return boolean
     */
    public boolean getImageFromSlidesFlag() {
        return this.imageFromSlides;
    }

    /**
     *
     * @return boolean
     */
    public boolean getNotesFlag() {
        return this.notes;
    }

    /**
     *
     * @return boolean
     */
    public boolean getLecturesFlag() {
        return this.lectures;
    }

    /**
     *
     * @return boolean
     */
    public boolean getSlidesSourcesFlag() {
        return this.slidesSources;
    }

    /**
     *
     * @return Constants.ContextWeb
     */
    public Constants.ContextWeb getContext() {
        return this.context;
    }

    /**
     *
     * @return String
     */
    public String getCssPath() {
        return this.cssPath;
    }

    /**
     *
     * @return String
     */
    public String getPsw() {
        return this.psw;
    }

    /**
     *
     * @return String
     */
    public String getUsername() {
        return this.username;
    }
    /**
     *
     * @return String
     *
    public String getPathVlc() {
        return this.pathVlc;
    }
    
    /**
     *
     * @param path
     *
    public void setPathVlc(String path){
        this.pathVlc = path;
    }
    */

}
