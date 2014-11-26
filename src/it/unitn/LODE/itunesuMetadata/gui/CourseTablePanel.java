package it.unitn.LODE.itunesuMetadata.gui;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.Course;
import it.unitn.LODE.itunesuMetadata.model.templates.ChannelTemplateForCourse;
import it.unitn.LODE.itunesuMetadata.model.templates.CourseTemplate;
import java.util.Set;

/**
 *
 * @author ronchet
 */
public class CourseTablePanel extends TablePanel {

    public CourseTablePanel(){
        rss=new CourseTemplate();
        ChannelTemplateForCourse channel=((CourseTemplate)rss).getChannel();
        propsMap=channel.getPropsMap();
        //initComponentsAndShow();
    }

    public void getCourseData(Course c ){
        filename=c.getFullPath()+LODEConstants.ITUNESU_DISTRIBUTION_SUBDIR+
                LODEConstants.FS+LODEConstants.ITUNES_COURSE_METADATA_FILENAME;
        String courseName=c.getCourseName();
        propsMap.put("title",courseName);
        //
        Set<String> lecturerSet=c.getLecturers();
        StringBuilder lecturers=new StringBuilder();
        boolean notFirst=false;
        for (String lecturer:lecturerSet) {
            if (notFirst) lecturers.append(", ");
            lecturers.append(lecturer);
            notFirst=true;
        }
        propsMap.put("itunes:author",lecturers.toString());
    }
}
