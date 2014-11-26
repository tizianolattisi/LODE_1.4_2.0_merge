package it.unitn.LODE.Models;

import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.MP.utils.WordUtils;
import it.unitn.LODE.utils.Messanger;
import java.io.File;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Data structure representing a course, with 
 * <li> - access methods
 * <li> - persistence methods
 * @author ronchet
 */
    @Root(name="COURSE")
public class Course extends StorableAsXML implements Serializable {
    @Element(name="NAME")
    private String courseName=null;
    @Element(name="YEAR")
    private String year=null;
    @Element(name="COURSE_HOME")
    private String courseHome=null;
    private String fullPath=null;
    @ElementList(name="LECTURES",entry="LECTURE")
    private Set<String> lectures=new TreeSet<String>();
    @ElementList(name="TEACHERS",entry="TEACHER_NAME")
    private Set<String> teachers=new TreeSet<String>();
    
    public Course(){
    } // needed to be a bean for XMLSerialization
    public void postResume() {
        // compatibility with the old version that stored the absolute path of coursess
        if (courseHome.startsWith(LODEConstants.FS)) 
                    this.courseHome=(courseHome.substring(1+courseHome.lastIndexOf(File.separator))); 
        fullPath=LODEConstants.COURSES_HOME+LODEConstants.FS+courseHome;
    }
    public Course(String courseName,String year,String dirName) {
        this.courseName=courseName;
        this.setYear(year); // perform year normalization!
        if (dirName==null) dirName=makeUniqueDirName();
        this.fullPath=dirName;
        this.courseHome=(fullPath.substring(1+fullPath.lastIndexOf(File.separator)));
    }
    // ============== ACCESS METHODS ===========================================
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String dirName) {
        this.fullPath = dirName;
    }
    
    public String getYear() {
        return year;
    }

    final public void setYear(String year) {
        //if (year.indexOf(File.separator)==-1) year.replaceAll(File.separator, "-2");
        this.year = year;
    }
    
    public int getNumberOfLectures() {return lectures.size();}
    
    public Set<String> getLecturers(){return teachers; }
    
    public void addLecture(Lecture lecture){
        lectures.add(lecture.getDirName());
    }
    // ============== PERSISTENCE METHODS ======================================
    final public String makeUniqueDirName() {
        String normalizedName=(WordUtils.capitalizeFully(courseName)).replace(" ", "_");
        String fn=LODEConstants.COURSES_HOME+File.separator+normalizedName;
        if (fn.indexOf(year)==-1) fn=fn+"_"+year;
        File f=new File(fn);
        int seq=0;
        while (f.exists()) {
            fn=LODEConstants.COURSES_HOME+File.separator+normalizedName+"_"+(++seq)+"_"+year;
            f=new File(fn);
        }
        if (seq!=0) courseName=courseName+"_"+seq;
        return fn;
    }
    public void update(){
        persist(new File(this.getFullPath()+File.separator+LODEConstants.SERIALIZED_COURSE));
        //writeInfo(new File(fullPath+File.separator+"COURSE.XML"));
    }
    public void save(String path) {
        if (path==null) path=makeUniqueDirName();
        ControllersManager.getinstance().getFileSystemManager().createFolder(new File(path));
        setFullPath(path);
        //writeInfo(new File(fullPath+File.separator+"COURSE.XML"));
        persist(new File(path+File.separator+LODEConstants.SERIALIZED_COURSE));
    }
    
    public void writeInfo(File xmlFile){ 
        // using Simple XML Serialization
        // http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php

        //===========================================================
        // Create a file as course marker
        File f = new File(fullPath+File.separator+".LoC");
        String content[]={"This is a marker file for a LODE Course directory."};
        ControllersManager.getinstance().getFileSystemManager().createFile(f, content);
         //===========================================================
        Serializer serializer = new Persister();
        try {
            serializer.write(this, xmlFile);
        } catch (Exception ex) {
            Messanger m=Messanger.getInstance();
            ex.printStackTrace(m.getLogger());
            m.w("Error while serializing XML file "+xmlFile.getAbsolutePath(),LODEConstants.MSG_ERROR);
        }

        /* Implemantation with XMLEncoder - bad xml!
        try {
            File outFile = new File(fullPath+File.separator+"COURSE.XML");
            FileOutputStream fos=new FileOutputStream(outFile);
            XMLEncoder encoder = new XMLEncoder(fos);
            encoder.writeObject(this);
            encoder.close(); 
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
// ================= XML STRUCTURE =============================================
/*
 * <course>
 *     <course_name>name</course_name>     [1]
 *     <course_year>year</course_year>     [1]
 *     <lecture_id>lecture_id</lecture_id> [N]
 *     <teacher_id>name<teacher_id>        [N]
 * </course>
 */
 }
