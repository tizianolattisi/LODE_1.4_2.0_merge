
package it.unitn.LODE.Models;

import it.unitn.LODE.MP.constants.LODEConstants;
import java.util.ArrayList;
import org.simpleframework.xml.*;


@Root(name="LODE_SLIDES")
public class LodeSlides extends StorableAsXML{
   @ElementList(inline=false, name="GROUPS")
   ArrayList<SlidesGroup> slideGroupList=new ArrayList<SlidesGroup>();
   @ElementList(inline=false, name="SLIDES")
   ArrayList<LodeSlide> slideList=new ArrayList<LodeSlide>();
   public final void addSlidesGroup(String fileName, int first,int last){
       slideGroupList.add(new SlidesGroup(fileName, first, last));
   }
   public final void addSlide(int seqNum, String title, String text,String fileName){
       slideList.add(new LodeSlide(seqNum, title, text, fileName));
   }
   /**
    * throws away all slides
    */
   public final void zapSlides(){
       slideList=new ArrayList<LodeSlide>();
       slideGroupList=new ArrayList<SlidesGroup>();
   }
   public final int getSlideCount(){
        return slideList.size();
   }
   public final LodeSlide get(int i){
        return slideList.get(i);
   }
   public final boolean isEmpty() { return slideList.isEmpty();}
}

   class SlidesGroup{
        @Element
        String fileName=null;
        @Element
        int firstSlide=-1;
        @Element
        int lastSlide=-1;
        public SlidesGroup(){} //needed to be a bean
        public SlidesGroup(String fileName,int firstSlide,int lastSlide){
            this.firstSlide=firstSlide;
            this.lastSlide=lastSlide;
            this.fileName=fileName;
        }
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setFirstSlide(int firstSlide) {
            this.firstSlide = firstSlide;
        }

        public void setLastSlide(int lastSlide) {
            this.lastSlide = lastSlide;
        }
   }
