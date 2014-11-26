package it.unitn.LODE.itunesuMetadata.gui;
/**
 *
 * @author ronchet
 */
public class SchemaCreator {
    public void createCourseTemplate(){
         CourseTablePanel ctp=new CourseTablePanel();

    }
    public void createLectureTemplate(){
         LectureTablePanel ctp=new LectureTablePanel();
    }
    public static void main(String a[]){
        SchemaCreator sc=new SchemaCreator();
        //sc.createLectureTemplate();
        sc.createCourseTemplate();
    }
}
