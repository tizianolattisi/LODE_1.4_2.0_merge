package it.unitn.LODE.services.packaging;

import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.utils.Messanger;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.Models.StorableAsXML;
import it.unitn.LODE.Models.TimedSlide;
import it.unitn.LODE.Models.TimedSlides;
import it.unitn.LODE.utils.FileSystemManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.simpleframework.xml.*;

/**
 *
 * @author ronchet
 */
@Root(name="lezione")
public class Data extends StorableAsXML {

    @ElementList(inline=true,required=false)
    ArrayList<Slide> list=null;
    @Element(name="video")
    Video v=null;
    @Element(name="info")
    Info i=null;
    
    
    public Data(){
        v=new Video();
        i=new Info();
    }    
    
    public Data(Lecture lecture,boolean rebuildTitles){
        // da inserire la gestione di rebuild titles
        list=new ArrayList<Slide>();
        TimedSlides tsl=null;
        if (lecture.hasSlides()) {
            File timedSlidesFile=new File(lecture.getAcquisitionPath()+File.separator+LODEConstants.TIMED_SLIDES_XML);
            if (timedSlidesFile.exists()) {
                try {
                    tsl = (TimedSlides) new TimedSlides().resume(
                        timedSlidesFile,
                        TimedSlides.class);
                } catch (Exception ex) {
                    Messanger.getInstance().w("Error while deserializing XML file "+timedSlidesFile.getAbsolutePath(),LODEConstants.MSG_ERROR);
                }
                Iterator<TimedSlide> iter=tsl.slideList.iterator();
                while (iter.hasNext()){
                    TimedSlide ts=iter.next();
                    list.add(new Slide(ts.time, ts.slideTitle, ts.fileName));
                }
            }
        }
        v=new Video();
        i=new Info();
        i.courseName=lecture.getCourse().getCourseName();
        i.lectureTitle=lecture.getLectureName();
        i.speakerName=lecture.getLecturer();
        v.totaltime=lecture.getVideoLength();
    }
    
    public static void main(String a[]){
        Data d=new Data();
        File tmp=new File(LODEConstants.COURSES_HOME);
        if (!tmp.exists()) ControllersManager.getinstance().getFileSystemManager().createFolder(tmp);
        d.persist(new File(LODEConstants.COURSES_HOME+LODEConstants.FS+"data.xml"));

    }
    /*
    public void persist(File xmlFile){ 
        // using Simple XML Serialization
        // http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php

          Serializer serializer = new Persister();
        try {
            serializer.write(this, xmlFile);
        } catch (Exception ex) {
            Messanger m=Messanger.getInstance();
            ex.printStackTrace(m.getLogger());
            m.w("Error while serializing XML file "+xmlFile.getAbsolutePath(),LODEConstants.MSG_ERROR);
        }
    }*/

}
class Video{
    @Element(name="nome")
    String nome="movie.flv";
    @Element(name="totaltime")
    String totaltime="0";
    @Element(name="starttime")
    String starttime="0";
}

class Info {
    @Element(name="corso")
    String courseName="Course Name";
    @Element(name="titolo")
    String lectureTitle="Lecture Name";
    @Element(name="professore")
    String speakerName="Speaker Name";
    @Element(name="dinamic_url")
    String dinamic_url="http://latemar.science.unitn.it/LODE</dinamic_url";
}

@Root
class Slide{
    @Element(name="tempo")
    String time="0";
    @Element(name="titolo")
    String slideTitle="...";
    @Element(name="immagine")
    String immagine="img/1.JPG";
    
    Slide(String time, String titolo, String immagine){
        this.time=time;
        this.slideTitle=titolo;
        this.immagine=immagine;
    }
}


