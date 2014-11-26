package it.unitn.LODE.Models;
import it.unitn.LODE.MP.constants.LODEConstants;
import java.util.ArrayList;
import java.util.Iterator;
import org.simpleframework.xml.*;

/**
 *
 * @author ronchet
 */
@Root(name="TIMED_SLIDES")
public class TimedSlides extends StorableAsXML {
   @ElementList(inline=true)
   public ArrayList<TimedSlide> slideList=new ArrayList<TimedSlide>();
   
   public TimedSlides(){}
   
   public void addSlide(String time, LodeSlide ls){
       String fileName=null;
       String path=ls.getFileName();
       int idx=path.lastIndexOf(LODEConstants.FS);
       if (idx==-1) fileName=LODEConstants.DISTRIBUTION_IMG_DIR+LODEConstants.FS+path;
       else fileName=LODEConstants.DISTRIBUTION_IMG_DIR+LODEConstants.FS+path.substring(idx+1);
       String title=ls.getTitle();
       slideList.add(new TimedSlide(time,title,fileName));
   }
   public Object removeSlide(String time, LodeSlide ls){
       String fileName=null;
       String path=ls.getFileName();
       int idx=path.lastIndexOf(LODEConstants.FS);
       if (idx==-1) fileName=LODEConstants.DISTRIBUTION_IMG_DIR+LODEConstants.FS+path;
       else fileName=LODEConstants.DISTRIBUTION_IMG_DIR+LODEConstants.FS+path.substring(idx+1);
       String title=ls.getTitle();
       return slideList.remove(new TimedSlide(time,title,fileName));
   }
   public void rebuildSlideTitles(){
       Iterator<TimedSlide> it=slideList.iterator();
       while (it.hasNext()){
        TimedSlide ts=it.next();
        // timed slides need to be changed. They need to keep a pointer to the slideSequence
        //ts.
       }
   }

}