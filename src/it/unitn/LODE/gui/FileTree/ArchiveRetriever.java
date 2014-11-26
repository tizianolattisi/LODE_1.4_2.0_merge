/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.gui.FileTree;

//==============================================================================

import it.unitn.LODE.MP.constants.LODEConstants;
import java.io.File;
import java.util.*;
/**
 * A library of static methods to get list of courses and lectures contained in the 
 * LODE library.
 * @author ronchet
 */
public class ArchiveRetriever {
    /**
     * Get the list of courses in the LODE library
     * @return an array of directories - one for each course
     */   
    public static File [] getCourses() {
        LodeFileNode lfn = new LodeFileNode(new File(LODEConstants.COURSES_HOME));
        return lfn.listFiles();
    }  
    /**
     * Get the list of lecture in a course
     * @param f the directory of the course
     * @return an array of directories - one for each lecture
     */
    public static File [] getAllLecturesInCourse(File f) {
        if (!f.exists()) return null;
        LodeFileNode lfn = new LodeFileNode(f);
        if (!lfn.isCourse()) return null;
        return lfn.listFiles();
    }
    /**
     * Gets the whole list of lectures contained in the Lode library
     * @return an array of directories - one for each lecture
     */
    public static File [] getAllLectures() {
        File [] courses=getCourses();
        Collection<File> allLectures=new ArrayList<File>();
        for (File c:courses){
            allLectures.addAll((List<File>)Arrays.asList(getAllLecturesInCourse(c)));
        }
        return (File[]) (allLectures.toArray(new File[0]));
    }
}