package it.unitn.LODE.Models;

import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.services.Scripter;
import it.unitn.LODE.utils.Messanger;
import it.unitn.LODE.MP.utils.WordUtils;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author ronchet
 */
/*
 * <lecture> <course></course> <teacher></teacher> <date></date>
 * <sequenceNumber></sequenceNumber> <title></title> </lecture>
 */
@Root(name = "LECTURE")
public class Lecture extends StorableAsXML implements Serializable {
    // using Simple XML Serialization
    // http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php

    private Set<String> lecturers = null;
    @Element(name = "NAME")
    private String lectureName = null;
    @Element(name = "DATE")
    private Date date = null;
    @Element(name = "SEQUENCE_NUMBER")
    private int sequenceNumber = -1; //-1 means not initialized
    private String acquisitionPath = null; //Home per i file acquisiti
    private String distributionPath = null; //Home per la distribuzione della lezione
    private String iTunesuDistributionPath = null; //Antonio  Home per la distribuzione della lezione in formato per iPhone
    @Element(name = "COURSE_HOME")
    private String courseHome = null; //Home per il corso
    private String courseRef = null; //Home per il corso
    @Element(name = "LECTURE_HOME")
    private String dirName = null; // Nome della directory del corso - ce n'e' una in acquisitionPath e una in distributionPath 
    private Course course = null;
    @Element(name = "LECTURER", required = false)
    private String lecturer = null;
    @Element(name = "VIDEO", required = false)
    private String videoFileName = null;
    @Element(name = "VIDEO_LENGTH", required = false)
    private String videoLenght = null; //null = Video does not exist
    @Element(name = "HAS_POST_PROCESSING")
    private boolean hasPostProcessing = false;
    @Element(name = "HAS_POST_PROCESSING_4_ITUNESU", required = false) //Antonio
    private boolean hasPostProcessing4itunesu = false;
    private LodeSlides slides = null;
    private TimedSlides timedSlides = null;

    public Lecture() { // needed to be a bean for XMLSerialization
        slides = new LodeSlides();
        timedSlides = new TimedSlides();
    }

    @Override
    public void postResume() {
        // compatibility with the old version that stored the absolute path of coursess
        if (courseHome.startsWith(LODEConstants.FS)) {
            this.courseHome = (courseHome.substring(1 + courseHome.lastIndexOf(File.separator)));
        }
        String lecturePath=File.separator +dirName+LODEConstants.FS; //path relativa
        courseRef = LODEConstants.COURSES_HOME + LODEConstants.FS + courseHome;
        //acquisitionPath=courseRef+LODEConstants.FS+LODEConstants.ACQUISITION_SUBDIR+lecturePath;
        acquisitionPath=courseRef+LODEConstants.ACQUISITION_SUBDIR+lecturePath;
        distributionPath=courseRef+LODEConstants.FS+LODEConstants.DISTRIBUTION_SUBDIR+lecturePath;
        iTunesuDistributionPath=courseRef+LODEConstants.FS+LODEConstants.ITUNESU_DISTRIBUTION_SUBDIR+lecturePath;
        String timedSlidesPath=acquisitionPath + File.separator + LODEConstants.TIMED_SLIDES_XML;
        String slidesPath=acquisitionPath + File.separator + LODEConstants.SLIDES_XML;
        try {
            slides = (LodeSlides) (new LodeSlides()).resume(new File(slidesPath), LodeSlides.class);
        } catch (Exception ex) {
            System.err.println("Unable to resume slides from "+slidesPath);
            Logger.getLogger(Lecture.class.getName()).log(Level.SEVERE, null, ex);
            slides = new LodeSlides();
        }
        try {
            timedSlides = (TimedSlides) (new TimedSlides()).resume(new File(timedSlidesPath), TimedSlides.class);
        } catch (Exception ex) {
            System.err.println("Unable to resume timedslides from "+timedSlidesPath);
            Logger.getLogger(Lecture.class.getName()).log(Level.SEVERE, null, ex);
            timedSlides = new TimedSlides();
        }
        if (timedSlides==null) timedSlides = new TimedSlides();
        System.out.println("Lecture acquisitionPath ="+acquisitionPath);
        System.out.println("Lecture distributionPath="+distributionPath);
    }

    public Lecture(Course course, String lectureName, String lecturer, Date date, int seqNum) {
        slides = new LodeSlides();
        timedSlides = new TimedSlides();
        this.course = course;
        this.lectureName = lectureName;
        this.lecturer = lecturer;
        this.date = date;
        this.sequenceNumber = seqNum;
        this.courseRef = course.getFullPath();
        this.courseHome = (courseRef.substring(1 + courseRef.lastIndexOf(File.separator)));
    }

    public final void addTimedSlide(int slide_number, String time) {
        LodeSlide s = slides.get(slide_number - 1);
        timedSlides.addSlide(time, s);
        timedSlides.persist(new File(acquisitionPath + File.separator + LODEConstants.TIMED_SLIDES_XML));
    }

    public final void removeTimedSlide(int slide_number, String time) {
        LodeSlide s = slides.get(slide_number - 1);
        timedSlides.removeSlide(time, s);
        timedSlides.persist(new File(acquisitionPath + File.separator + LODEConstants.TIMED_SLIDES_XML));
    }

    public final void addSlide(int i, String title, String text, String outFileName) {
        slides.addSlide(i, title, text, outFileName);
    }

    public final void addSlidesGroup(String fileName, int first, int last) {
        slides.addSlidesGroup(fileName, first, last);
    }

    /*
     * public void addFakeSlides() { slides.addFakeSlides();
    }
     */
    public final void zapSlides() {
        if (slides == null) {
            // this may happen when reopening a lecture that does not have slides
            slides=new LodeSlides();
            timedSlides = new TimedSlides();
        }
        slides.zapSlides();
    }

    public final boolean hasSlides() {
        if (slides == null) {
            return false;
        }
        return !slides.isEmpty();
    }

    public final int getSlideCount() {
        return slides.getSlideCount();
    }

    public final boolean hasVideo() {
        if (videoLenght == null) {
            return false;
        }
        try {
            if (Integer.parseInt(videoLenght) <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public final boolean hasBeenPostProcessed() {
        return hasPostProcessing;
    }

    public final void setHasBeenPostProcessed(boolean b) {
        hasPostProcessing = b;
    }

    public final boolean hasBeenPostProcessedForItunesu() { //Antonio
        return hasPostProcessing4itunesu;
    }

    public final void setHasBeenPostProcessed4utunesu(boolean b) { //Antonio
        hasPostProcessing4itunesu = b;
    }

    public final void send() {
        //create zip of the lecture
        Thread t = new Thread() {

            @Override
            public void run() {
                Scripter s = new Scripter();
                //s.demo();
                //if(true) return;
                String filename = course.getCourseName() + dirName + ".zip";
                String fullyQualifiedName = LODEConstants.TEMP_DIR + LODEConstants.FS + filename;
                s.localLaunch(new String[]{
                            "#/bin/sh",
                            "cd " + getAcquisitionPath() + "/..",
                            "echo \"COMPRESSING LECTURE...\"",
                            "/usr/bin/zip -r " + fullyQualifiedName + "  " + dirName + " ../COURSE.XML",
                            "echo \"COMPRESSION FINISHED\""
                        }, true);
                s.remoteCopy(fullyQualifiedName,
                        s.REMOTE_INBOX + LODEConstants.FS + filename,
                        true);
            }
        };
        t.start();

    }
    // ============== ACCESS METHODS ===========================================

    public final Course getCourse() {
        return course;
    }

    public final void setVideoLength(String videoDuration) {
        this.videoLenght = videoDuration;
    }

    public final String getVideoLength() {
        return videoLenght;
    }

    public final void setCourse(Course course) {
        this.course = course;
        this.courseRef = course.getFullPath();
    }

    public final String getCourseRef() {
        return courseRef;
    }

    public final Date getDate() {
        return date;
    }

    public final void setDate(Date date) {
        this.date = date;
    }

    public final String getDirName() {
        return dirName;
    }

    public final String getAcquisitionPath() {
        return acquisitionPath;
    }

    private void _setAcquisitionPath(String acquisitionPath) {
        this.acquisitionPath = acquisitionPath;
        this.dirName = (acquisitionPath.substring(1 + acquisitionPath.lastIndexOf(File.separator)));
        _setDistributionPath(getCourse().getFullPath() + LODEConstants.DISTRIBUTION_SUBDIR + File.separator + dirName);
        _setiTunesuDistributionPath(getCourse().getFullPath() + LODEConstants.ITUNESU_DISTRIBUTION_SUBDIR+ File.separator + dirName);
    }

    public final String getDistributionPath() {
        return distributionPath;
    }

    private void _setDistributionPath(String distributionPath) {
        this.distributionPath = distributionPath;
    }

    public final String getiTunesuDistributionPath() { //Antonio
        return iTunesuDistributionPath;
    }

    private void _setiTunesuDistributionPath(String distributionPath) { //Antonio
        this.iTunesuDistributionPath = distributionPath;
    }

    public final String getLecturer() {
        return lecturer;
    }

    public final void setLecturer(String lecturer) {
        if (lecturer == null) {
            lecturer = "-";
        }
        this.lecturer = lecturer;
    }

    public final String getLectureName() {
        return lectureName;
    }

    public final void setLectureName(String title) {
        this.lectureName = title;
    }

    // ============== PERSISTENCE METHODS ======================================
    @Override
    public final Lecture resume(File f, Class<? extends StorableAsXML> c) {
        Lecture lecture = null;
        try {
            lecture = (Lecture) super.resume(f, c);
        } catch (Exception ex) {
            ex.printStackTrace();
            Messanger.getInstance().w("Error while deserializing XML file "+f.getAbsolutePath(), LODEConstants.MSG_ERROR);
            Messanger.getInstance().w(ex.getMessage(), LODEConstants.MSG_ERROR);
        }
        Course cc = lecture.getCourse();
        if (cc == null) {
            try {
                cc = (Course) ((new Course()).resume(new File(lecture.getCourseRef() + File.separator + LODEConstants.SERIALIZED_COURSE), Course.class));
            } catch (Exception ex) {
                ex.printStackTrace();
                Messanger.getInstance().w("Error while deserializing XML file "+f.getAbsolutePath(), LODEConstants.MSG_ERROR);
                Messanger.getInstance().w(ex.getMessage(), LODEConstants.MSG_ERROR);
            }
            lecture.setCourse(cc);
        }
        Messanger m = Messanger.getInstance();
        m.log("Resuming lecture from " + cc.getFullPath());
        System.out.println("Resuming lecture from " + cc.getFullPath());
        System.out.println(lecture.getAcquisitionPath());
        if (lecture.getAcquisitionPath() == null) {
            lecture._setAcquisitionPath(cc.getFullPath() + LODEConstants.ACQUISITION_SUBDIR + File.separator + lecture.getDirName());
            lecture._setDistributionPath(cc.getFullPath() + LODEConstants.DISTRIBUTION_SUBDIR + File.separator + lecture.getDirName());
            lecture._setiTunesuDistributionPath(cc.getFullPath() + LODEConstants.ITUNESU_DISTRIBUTION_SUBDIR + File.separator + lecture.getDirName()); //Antonio
        }
        System.out.println(lecture.getAcquisitionPath());

        if (new File(lecture.getAcquisitionPath() + File.separator + LODEConstants.SLIDES_XML).exists()) {
            lecture.resumeSlides();
        }
        return lecture;
    }

    private String _makeUniqueDirName() {
        String normalizedName = "";
        String base = "";
        String courseSeqNum = null;
        if (sequenceNumber < 10) {
            courseSeqNum = "0" + sequenceNumber;
        } else {
            courseSeqNum = "" + sequenceNumber;
        }
        if (lectureName != null && (!lectureName.equals(""))) {
            normalizedName = (WordUtils.capitalizeFully(lectureName)).replace(" ", "_");
        }
        base = course.getFullPath() + LODEConstants.ACQUISITION_SUBDIR + File.separator + courseSeqNum + "_" + normalizedName;
        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd");
        String dateAsString = sdfOutput.format(date);
        String fn = base + "_" + dateAsString;
        File f = new File(fn);
        int seq = 0;
        while (f.exists()) {
            seq++;
            if (seq < 10) {
                fn = base + "_0" + seq + "_" + dateAsString;
            } else {
                fn = base + "_" + seq + "_" + dateAsString;
            }
            f = new File(fn);
        }
        if (seq != 0) {
            if (seq < 10) {
                lectureName = lectureName + "_0" + seq;
            } else {
                lectureName = lectureName + "_" + seq;
            }
        }
        return fn;
    }

    public final void save(String path) {
        if (path == null) {
            path = _makeUniqueDirName();
        }
        ControllersManager.getinstance().getFileSystemManager().createFolder(new File(path));
        _setAcquisitionPath(path);
        //writeInfo(new File(path+File.separator+"LECTURE.XML"));
        persist(new File(path + File.separator + LODEConstants.SERIALIZED_LECTURE));
        course.addLecture(this);
        course.update();
    }

    public final LodeSlides getLodeSlides() { //Antonio
        return slides;
    }

    public final TimedSlides getTimedSlides() { //Antonio
        return timedSlides;
    }

    public final int getSequenceNumber() { //Antonio
        return sequenceNumber;
    }

    public final void persistSlides() {
        slides.persist(new File(acquisitionPath + File.separator + LODEConstants.SLIDES_XML));
    }

    public final void resumeSlides() {
        try {
            slides = (LodeSlides) (slides.resume(new File(acquisitionPath + File.separator + LODEConstants.SLIDES_XML), LodeSlides.class));
        } catch (Exception ex) {
            Messanger.getInstance().w("Error while deserializing XML file", LODEConstants.MSG_ERROR);
            Messanger.getInstance().w(ex.getMessage(), LODEConstants.MSG_ERROR);
        }
    }
}
