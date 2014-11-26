/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.services;

import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.utils.FileSystemManager;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.Course;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.Models.ProgramState;
import java.io.File;

/**
 *
 * @author ronchet
 */
public class Packager {
    String DATA_XML="data.xml";
    public void makeDataXml(){
        ProgramState programState=ProgramState.getInstance();
        StringBuffer content=new StringBuffer("");
        content.append("<lezione>\n");
        appendSlides(content);
        // append video        
        content.append("\t<video>\n");
        int totaltime=0;
        int starttime=0;
        content.append("\t\t<totaltime>"+totaltime+"\t\t</totaltime>\n");
        content.append("\t\t<starttime>"+starttime+"\t\t</starttime>\n");
        content.append("\t</video>\n");
        // append info
        String courseName=programState.getCurrentCourse().getCourseName()+" "+programState.getCurrentCourse().getYear();
        String lectureName=programState.getCurrentLecture().getLectureName();
        String teacherName=programState.getCurrentLecture().getLecturer();
        content.append("\t<info>\n");
        content.append("\t\t<corso>"+courseName+"\t\t</corso>\n");
        content.append("\t\t<lezione>"+lectureName+"\t\t</lezione>\n");
        content.append("\t\t<professore>"+teacherName+"\t\t</professore>\n");
        content.append("\t\t<dinamic_url>http://latemar.science.unitn.it/LODE</dinamic_url>\n");
        content.append("\t</info>\n");
        content.append("<lezione>\n");
        String contentStrings[]={content.toString()};
        ControllersManager.getinstance().getFileSystemManager().createFile(new File(DATA_XML), contentStrings);
    }
    private void appendSlides(StringBuffer content){
    }
    public void doit(Lecture lecture){
        FileSystemManager fileSystemManager = ControllersManager.getinstance().getFileSystemManager();
        Course course=lecture.getCourse();
        String coursePath=course.getFullPath();
        String FS=LODEConstants.FS;
        String lectureDistributionPath=lecture.getDistributionPath();
        String lectureAcquisitionPath=lecture.getAcquisitionPath();
        // delete existing distibution
        fileSystemManager.recursiveDelete(new File(lectureDistributionPath));

        /*
            try {
            fileSystemManager.deleteFolder(new File(course_lecture_path+File.separator+"~Stand alone"));
            fileSystemManager.copyFiles(new File (course_lecture_path+File.separator+"~Stand alone"), new File(gui.getRealPath()+File.separator+"template"));
            fileSystemManager.copyFiles(new File (course_lecture_path+File.separator+"~Stand alone"+File.separator+"content"+File.separator+"data"+File.separator+"content_frame"), new File(course_lecture_path+"\\Slides"));
            File movie = new File(course_lecture_path+File.separator+"movie.mp4");
            if (movie.exists()) fileSystemManager.copyFiles(new File (course_lecture_path+File.separator+"~Stand alone"+File.separator+"content"+File.separator+"data"+File.separator+"movie.mp4"), movie);
            else {
                //convertVideo(course_lecture_path);
                JOptionPane.showMessageDialog(gui, "No mp4 movie found. Please create it and repeate the post-processing");
                gui.setEnabled(true);
            }
            String[] links = new String[300];
            String line, param_line;
            int i=0;
                                  
            RandomAccessFile index = new RandomAccessFile(course_lecture_path+File.separator+"~Stand alone"+File.separator+"content"+File.separator+"index.html","rw");
            int param_position = 0;

            File outFile = new File("index.html");
            //System.out.println("Out file (index) is "+outFile.getAbsolutePath());

            FileOutputStream fos = new FileOutputStream(outFile);
            PrintWriter out = new PrintWriter(fos);

            while ((line = index.readLine()) != null) {
                //System.out.println("___(index line)"+line);

                if (line.indexOf("<param name=\"totSec\"")>-1) {
                    try {
                        QTSession.open();
                        OpenMovieFile omFile = OpenMovieFile.asRead(new QTFile(movie));
                        Movie m = Movie.fromFile(omFile);
                        int movie_duration = (int) (m.getDuration()/m.getTimeScale());
                        line = "    <param name=\"totSec\"         value=\""+movie_duration+"\">";
                    } catch (Exception e) {
                        System.out.println("postProcess error -> Can not get video time!");
                    }
                }

                if (line.indexOf("param name=\"tickCount\"") > - 1) {
                    //System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^TICKcount");
                    RandomAccessFile log = new RandomAccessFile(course_lecture_path + "\\" + LODEConstants.LOG_NAME, "rw");
                    String log_line;

                    //read the slide titles from the file
                    //System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^Read the slides titles from file");
                    String[] slide_titles = new String[LodeGui.NUM_IMAGES];
                    if ((new File(course_lecture_path+File.separator+"Slides_Titles.txt").exists())) {
                        RandomAccessFile slides_title = new RandomAccessFile(course_lecture_path+File.separator+"Slides_Titles.txt", "rw");
                        String slide_titles_line;
                        int img = 0;
                        while  ((slide_titles_line = slides_title.readLine()) != null) {
                            if (slide_titles_line.length()>40) slide_titles[img] =  slide_titles_line.substring(0,40);
                            else  slide_titles[img] =  slide_titles_line;
                            //System.out.println("         "+ img+": "+ slide_titles_line);
                            img++;
                        }
                        slides_title.close();
                    } else {
                        for (int a=0; a<LodeGui.NUM_IMAGES;a++) {
                            slide_titles[a]="Slide "+(a+1);
                        }
                    }
                    //gui.setSlide_titles(slide_titles);

                    while ((log_line = log.readLine()) != null) {
                        //System.out.println("___(log_line)"+log_line);
                        String slide = log_line.substring(0, log_line.indexOf(' '));
                        int start = log_line.indexOf(" - ") + 3;
                        int time = 0;
                        if (i != 0) {
                            String TIME = log_line.substring(start);
                            String h = TIME.substring(0, TIME.indexOf(':'));
                            String rest = TIME.substring(TIME.indexOf(':') + 1);
                            String m = rest.substring(0, rest.indexOf(':'));
                            rest = rest.substring(rest.indexOf(':') + 1);
                            String s = rest.substring(0, rest.indexOf(':'));
                            time = Integer.parseInt(h) * 3600 + Integer.parseInt(m) * 60 + Integer.parseInt(s);
                        }

                        //System.out.println("slide="+slide+"; time="+time);

                        param_line = "<param name=\"tick" + i + "\" value=\"0;2006;0;" + slide + ";" + time + ";" + slide_titles[(Integer.parseInt(slide)-1)] + ";core.Slide;\">";
                        links[i] = param_line;
                        //System.out.println("i="+i+"; param_line ="+param_line);
                        i++;
                        line = "    <param name=\"tickCount\"      value=\""+i+"\">";

                        //System.out.println("     (log_line)"+line);
                    }
                }

                out.println(line);
                //System.out.println("... Writing in index");

                if (line.indexOf("<!-- ticks definition -->") > -1) {
                    for (int j=0; j<i;j++) {
                        out.println(links[j]);
                    }
                }
            }
            out.flush(); out.close();
            fos.close();
            index.close();
            File index_old = new File(course_lecture_path+File.separator+"~Stand alone"+File.separator+"content"+File.separator+"index.html");
            index_old.delete();
            File index_new = new File(course_lecture_path+File.separator+"~Stand alone"+File.separator+"content"+File.separator+"index.html");
            outFile.renameTo(index_new);

            //change the title.html content
            //System.out.println("Start processing the title.htm");
            RandomAccessFile title = new RandomAccessFile(course_lecture_path+File.separator+"~Stand alone"+File.separator+"content"+File.separator+"data"+File.separator+"title.html","rw");
            File outFile2 = new File("title.html");
            FileOutputStream fos2 = new FileOutputStream(outFile2);
            PrintWriter out2 = new PrintWriter(fos2);
            String line2="";
            String lecture_name="";
            while ((line2 = title.readLine()) != null) {
                //System.out.println("____(title)"+line2);
                if (line2.indexOf("<SPAN class=\"title line1\">")>-1) {

                    //Get the course name
                    String course_name = "";
                    StringTokenizer st = new StringTokenizer(LODEConstants.COURSES_HOME,File.separator);
                    int k = st.countTokens();
                    for (int j=0;j<k-1; j++) {
                        course_name = st.nextToken();
                    }
                    lecture_name = st.nextToken();

                    line2 = "       <SPAN class=\"title line1\">"+course_name+"</SPAN><BR>";
                }
                if (line2.indexOf("<SPAN class=\"title line2\">")>-1) {
                    line2 = "       <SPAN class=\"title line2\">"+lecture_name+"</SPAN><BR>";
                }
                if (line2.indexOf("<SPAN class=\"title line3\">")>-1) {
                    String lecturer_name ="";
                    RandomAccessFile lecturer = new RandomAccessFile(course_lecture_path+File.separator+"Lecturer.txt","rw");
                    lecturer_name = lecturer.readLine();
                    line2 = "       <SPAN class=\"title line3\">"+lecturer_name+"</SPAN><BR>";
                }

                out2.println(line2);
            }
            out2.flush(); out2.close();
            fos2.close();
            title.close();
            File title_old = new File(course_lecture_path+File.separator+"~Stand alone"+File.separator+"content"+File.separator+"data"+File.separator+"title.html");
            title_old.delete();
            File title_new = new File(course_lecture_path+File.separator+"~Stand alone"+File.separator+"content"+File.separator+"data"+File.separator+"title.html");
            outFile2.renameTo(title_new);


        } catch (IOException e) {
            System.out.println("Error during post processing!");
            e.printStackTrace();
        }
         */
    }
     /*   
    <lezione>
    <slide>
    <tempo>0</tempo>
    <titolo>Rappresentazione e manipolazione dell?in
    </titolo>
    <immagine>img/1.JPG</immagine>
    </slide>


    <video>
    <nome>video.flv</nome>
    <totaltime>5182</totaltime>
    <starttime>0</starttime>
    </video>

    <info>
    <corso>Architettura degli Elaboratori</corso>
    <titolo>Lezione 2</titolo>
    <professore>Lorenzo Dematte'</professore>
    <dinamic_url>http://latemar.science.unitn.it/LODE</dinamic_url>
    </info>
    </lezione>
      */

}
