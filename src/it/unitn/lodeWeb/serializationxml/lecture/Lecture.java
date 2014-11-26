package it.unitn.lodeWeb.serializationxml.lecture;

import org.simpleframework.xml.*;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root(name = "LECTURE")
public class Lecture {

    //--------------------------------------------------------------------------
    /**
     *Class Constructor
     */
    //--------------------------------------------------------------------------
    public Lecture() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param name
     * @param date
     * @param sequenceN
     * @param courseHomeURL
     * @param lectureHome
     * @param lecturer
     * @param videoLength
     * @param hpp
     */
    //--------------------------------------------------------------------------
    public Lecture(String name, String date, int sequenceN, String courseHomeURL,
            String lectureHome, String lecturer, int videoLength, boolean hpp) {
        this.name = name;
        this.date = date;
        this.sequenceN = sequenceN;
        this.courseHomeURL = courseHomeURL;
        this.lectureHome = lectureHome;
        this.lecturer = lecturer;
        this.videoLength = videoLength;
        this.hpp = hpp;
    }
    @Element(name = "NAME")
    private String name;
    @Element(name = "DATE")
    private String date;
    @Element(name = "SEQUENCE_NUMBER")
    private int sequenceN;
    @Element(name = "COURSE_HOME")
    private String courseHomeURL;
    @Element(name = "LECTURE_HOME")
    private String lectureHome;
    @Element(name = "LECTURER")
    private String lecturer;
    @Element(name = "VIDEO_LENGTH")
    private int videoLength;
    @Element(name = "HAS_POST_PROCESSING")
    private boolean hpp;
    @Element(name = "HAS_POST_PROCESSING_4_ITUNESU")
    private boolean hipp;
    /**
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @return int
     */
    public int getSequenceN() {
        return sequenceN;
    }

    /**
     *
     * @param sn
     */
    public void setSequenceN(int sn) {
        this.sequenceN = sn;
    }

    /**
     *
     * @return String
     */
    public String getCourseHomeURL() {
        return courseHomeURL;
    }

    /**
     *
     * @return String
     */
    public String getLectureHome() {
        return lectureHome;
    }

    /**
     *
     * @return String
     */
    public String getLecturer() {
        return lecturer;
    }

    /**
     *
     * @return int
     */
    public int getVideoLength() {
        return videoLength;
    }

    /**
     *
     * @return boolean
     */
    public boolean getHpp() {
        return hpp;
    }
}
