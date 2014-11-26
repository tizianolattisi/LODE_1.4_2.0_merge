package it.unitn.LODE.itunesuMetadata.model.templates;
import it.unitn.LODE.itunesuMetadata.model.components.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.simpleframework.xml.*;

public class ChannelTemplateForCourse {

    HashMap<String,String> propsMap=new HashMap<String,String>();

    @Element(name="atom:link")
    private Atom_link atom_link=null;

    @Element(name="link")
    private String link=null;

    @Element(name="description")
    private String description=null;

    @Element(name="lastBuildDate")
    private String lastBuildDate=null;

    @Element(name="language")
    private String language=null;

    @Element(name="updateFrequency")
    @Namespace(reference="http://purl.org/rss/1.0/modules/syndication/")
    private String sy_updateFrequency=null;

    @Element(name="updatePeriod")
    @Namespace(reference="http://purl.org/rss/1.0/modules/syndication/")
    private String sy_updatePeriod=null;

    @Element(name="image")
    private Image image=null;

    @Element(name="summary")
    @Namespace(reference="http://www.itunes.com/dtds/podcast-1.0.dtd")
    private String itunes_summary=null;

    @Element(name="subtitle")
    @Namespace(reference="http://www.itunes.com/dtds/podcast-1.0.dtd")
    private String itunes_subtitle=null;

    @Element(name="author")
    @Namespace(reference="http://www.itunes.com/dtds/podcast-1.0.dtd")
    private String itunes_author=null;

    @Element(name="itunes:image")
    private String itunes_image=null;

    @ElementList(name="categoryList", inline=true)
    @Namespace(reference="http://www.itunes.com/dtds/podcast-1.0.dtd")
    private List<Itunes_category> itunes_categoryList=null;

    @Element(name="explicit")
    @Namespace(reference="http://www.itunes.com/dtds/podcast-1.0.dtd")
    private String itunes_explicit=null;

    @Element(name="owner")
    @Namespace(reference="http://www.itunes.com/dtds/podcast-1.0.dtd")
    private Itunes_owner itunes_owner=null;

    //String props[]={"url","imageUrl","description","language","summary","subtitle","author","owner-name","owner-email","category-1","category-2"};
    public ChannelTemplateForCourse(){
        /*
         * view http://www.feedforall.com/itune-tutorial-tags.htm#category for an explanation of the tags
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
        createCourseTemplate();
    }
    public void createCourseTemplate(){
        setDefaults();
        setvars();
        String cats[]={"Education","Higher Education"};
        setCategories(cats);
    }
    public void setUrl(String url){
        atom_link=new Atom_link(url);
        link=url;
    }
    public void setImageUrl(String imageUrl, String imageTitle){
        this.image=new Image(imageUrl,"IMAGE_TITLE");
        this.itunes_image=imageUrl;
    }
    public void setCategories(String[] categories){
        itunes_categoryList=new ArrayList<Itunes_category>();
        for (String s : categories){
            Itunes_category itunes_category=new Itunes_category(s);
            itunes_categoryList.add(itunes_category);            
        }
    }

    public HashMap<String, String> getPropsMap(){
        return propsMap;
    }

    public void setDefaults(){
        propsMap.put("url","http://###URL-UNDEFINED###");
        propsMap.put("imageUrl","http://###IMAGE-URL-UNDEFINED###");
        propsMap.put("description"," ");
        propsMap.put("lastBuildDate",TimeStamp.now());
        propsMap.put("language","it");
        propsMap.put("itunes:summary"," ");
        propsMap.put("itunes:subtitle"," ");
        propsMap.put("itunes:author","###ITUNES-AUTHOR-UNDEFINED###");
        propsMap.put("itunes_owner_name","Progetto LODE Unitn");
        propsMap.put("itunes_owner_email","marco.ronchetti@unitn.it");
        //===================
        propsMap.put("itunes:explicit","no");
        propsMap.put("sy:updatePeriod","1");
        propsMap.put("sy:updateFrequency","hourly");
    }

    public void setvars(){
        String url=propsMap.get("url");
        atom_link=new Atom_link(url);
        link=url;
        //===============================
        String imageUrl=propsMap.get("imageUrl");
        image=new Image(imageUrl,"IMAGE_TITLE");
        itunes_image=imageUrl;
        //===============================
        description=propsMap.get("description");
        lastBuildDate=propsMap.get("lastBuildDate");
        language=propsMap.get("language");
        itunes_summary=propsMap.get("itunes:summary");
        itunes_subtitle=propsMap.get("itunes:subtitle");
        itunes_author=propsMap.get("itunes:author");
        itunes_owner=new Itunes_owner(propsMap.get("itunes_owner_name"),propsMap.get("itunes_owner_email"));
        itunes_explicit=propsMap.get("itunes:explicit");
        sy_updatePeriod=propsMap.get("sy:updatePeriod");
        sy_updateFrequency=propsMap.get("sy:updateFrequency");
    }
}
