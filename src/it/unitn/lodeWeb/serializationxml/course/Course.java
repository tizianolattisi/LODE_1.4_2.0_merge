package it.unitn.lodeWeb.serializationxml.course;

//--------------------------------------------------------------------------
import java.util.List;
import java.util.TreeSet;
import org.simpleframework.xml.*;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root(name = "COURSE")
public class Course {

    @Element(name = "NAME")
    private String nameCourse;
    @Element(name = "COURSE_HOME")
    private String courseHome;
    @Element(name = "YEAR")
    private int year;
    @ElementList(name = "LECTURES", type = Lecture.class)
    private TreeSet lectureNames;
    @ElementList(name = "TEACHERS", type = Teacher.class)
    private TreeSet teachers;

    /**
     *
     * @return List
     */
    public List getLectures() {
        return (List) lectureNames;
    }

    /**
     *
     * @return List
     */
    public List getTeachers() {
        return (List) teachers;
    }

    /**
     *
     * @return String
     */
    public String getCourseHome() {
        return courseHome;
    }

    /**
     *
     * @return String
     */
    public String getNameCourse() {
        return nameCourse;
    }

    /**
     *
     * @return int
     */
    public int getYear() {
        return year;
    }
}

