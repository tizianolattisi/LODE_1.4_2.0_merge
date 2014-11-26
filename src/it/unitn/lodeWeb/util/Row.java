package it.unitn.lodeWeb.util;

import it.exprivia.cnos.opencloud.Cloud;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class Row {

    private int sequenceN;
    private String date;
    private String lecturer;
    private String title;
    private String note;
    private int time;
    private String url;
    private String nameDir;
    private Cloud tagCloud;
    private boolean hasPost;
    // viene utilizzato per vedere se esistono immagini per la preview
    private int nSlides;
    private String nameCourseHome;
    // materiale didattico slidesources
    private boolean ssExist;
    // lista dei file slidesource, pososnoe ssere piu di uno
    private String[] ssListFile;

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param sN - lecture sequence number
     * @param lecturer - lecturer name
     * @param date - lecture date
     * @param title - lecture title
     * @param note - lecture note
     * @param time - length of the video lesson
     * @param nameCourseHome - directory name of the course
     * @param url - url of the course
     * @param nameDir - directory name of the lecture
     * @param tc - tag Clouds
     * @param nSlides - number of slides
     * @param ssExist - slides sources
     * @param ssListFile - array of file, slides sources
     */
    //--------------------------------------------------------------------------
    public Row(
            int sN,
            String lecturer,
            String date,
            String title,
            String note,
            int time,
            String nameCourseHome,
            String url,
            String nameDir,
            Cloud tc,
            int nSlides,
            boolean ssExist,
            String[] ssListFile) {

        this.sequenceN = sN;
        this.lecturer = lecturer;
        this.date = date;
        this.title = title;
        this.note = note;
        this.time = time;
        this.nameCourseHome = nameCourseHome;
        this.url = url;
        this.nameDir = nameDir;
        this.tagCloud = tc;
        this.nSlides = nSlides;
        this.ssExist = ssExist;
        this.ssListFile = ssListFile;

    }

    /**
     *
     * @return String
     */
    public String getNameCourseHome() {
        return this.nameCourseHome;
    }
    /**
     *
     * @return String
     */
    public String getLecturer() {
        return this.lecturer;
    }
    /**
     *
     * @return boolean
     */
    public boolean getHasPost() {
        return this.hasPost;
    }

    /**
     *
     * @param hpp
     */
    public void setHasPost(boolean hpp) {
        this.hasPost = hpp;
    }

    /**
     *
     * @return int
     */
    public int getSequenceN() {
        return this.sequenceN;
    }

    /**
     *
     * @return String
     */
    public String getDate() {
        return this.date;
    }

    /**
     *
     * @return String
     */
    public String getTitle() {
        return this.title;
    }

    /**
     *
     * @return String
     */
    public String getNote() {
        return this.note;
    }

    /**
     *
     * @return int
     */
    public int getTime() {
        return this.time;
    }

    /**
     *
     * @return String
     */
    public String getUrl() {
        return this.url;
    }

    /**
     *
     * @return String
     */
    public String getNameDir() {
        return this.nameDir;
    }

    /**
     *
     * @return Cloud
     */
    public Cloud getCloud() {
        return this.tagCloud;
    }

    /**
     *
     * @return int
     */
    public int getNSlide() {
        return this.nSlides;
    }

    /**
     *
     * @return boolean
     */
    public boolean getSsExist() {
        return this.ssExist;
    }

    /**
     *
     * @return String []
     */
    public String[] getSsList() {
        return this.ssListFile;
    }
}
