package it.unitn.LODE.itunesuMetadata.model.templates;
import it.unitn.LODE.itunesuMetadata.model.components.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.simpleframework.xml.*;

public class ChannelTemplateForLecture {

    Item theItem=null;

    @ElementList(name="item", inline=true)
    private List<Item> itemList=null;

    //String props[]={"url","imageUrl","description","language","summary","subtitle","author","owner-name","owner-email","category-1","category-2"};
    public ChannelTemplateForLecture(){
        /*
        String url="http://latemar.science.unitn.it/itunes/feeds/festivaleconomia2010/indexfestecon2010-3giu.html";
        setUrl(url);
        String imageUrl="http://latemar.science.unitn.it/itunes/feeds/festivaleconomia2010/im.jpg";
        setImageUrl(imageUrl,"IMAGE_TITLE");
        setDescription("DESCRIPTION");
        setLastBuildDate(TimeStamp.now());
        setLanguage("it-it");
        setSy_updatePeriod("hourly");
        setSy_updateFrequency("1");
        setItunes_summary("SUMMARY");
        setItunes_subtitle("SUBTITLE");
        setItunes_author("AUTHOR");
        setItunes_explicit("no");
        setItunes_owner("NOME","EMAIL");
         *
         */
        createLectureTemplate();
    }
    private void createLectureTemplate(){
        theItem=new Item();
        Item[] items={theItem};
        setItems(items);
        setDefaults();
        setvars();
    }

    public void setItems(Item[] items){
        itemList=new ArrayList<Item>();
        itemList.addAll(Arrays.asList(items));
    }
   
    public HashMap<String, String> getPropsMap(){
        return theItem.getPropsMap();
    }

    public void setDefaults(){
        theItem.setDefaults();
    }

    public void setvars(){
        theItem.setvars();
    }
}
