package it.unitn.LODE.itunesuMetadata.model.components;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.simpleframework.xml.*;

public class Channel {

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

    @ElementList(name="item", inline=true)
    private List<Item> itemList=null;

    //String props[]={"url","imageUrl","description","language","summary","subtitle","author","owner-name","owner-email","category-1","category-2"};
    public Channel(){
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
        createCourseTemplate();
    }
    public void createCourseTemplate(){
        setDefaults();
        setvars();
        String cats[]={"CATEGORY 1","CATEGORY 2"};
        setCategories(cats);
        Item[] items={new Item()};
        items[0].setTitle("THIS ITEM IS A PLACEHOLDER");
        setItems(items);
    }
    public void createLectureTemplate(){
        Item[] items={new Item()};
        items[0].setTitle("THIS ITEM IS A PLACEHOLDER");
        setItems(items);
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
    public void setItems(Item[] items){
        itemList=new ArrayList<Item>();
        for (Item i : items){
            itemList.add(i);
        }
    }
    /*
    public void setDescription(String description){
        this.description=description;
    }
    public void setLastBuildDate(String lastBuildDate){
        this.lastBuildDate=lastBuildDate;
    }
    public void setLanguage(String language){
         this.language=language;
    }
    public void setSy_updatePeriod(String sy_updatePeriod){
        this.sy_updatePeriod=sy_updatePeriod;
    }
    public void setSy_updateFrequency(String sy_updateFrequency){
        this.sy_updateFrequency=sy_updateFrequency;
    }

    public void setItunes_summary(String itunes_summary){
        this.itunes_summary=itunes_summary;
    }
    public void setItunes_subtitle(String itunes_subtitle){
        this.itunes_subtitle=itunes_subtitle;
    }
    public void setItunes_author(String itunes_author){
        this.itunes_author=itunes_author;
    }
    public void setItunes_explicit(String itunes_explicit){
        this.itunes_explicit=itunes_explicit;
    }
    public void setItunes_owner(String nome,String email){
        this.itunes_owner=new Itunes_owner(nome,email);
    }
     *
     */
    public HashMap<String, String> getPropsMap(){
        return propsMap;
    }

    public void setDefaults(){
        propsMap.put("url","http://###URL-UNDEFINED###");
        propsMap.put("imageUrl","http://###IMAGE-URL-UNDEFINED###");
        propsMap.put("description","");
        propsMap.put("lastBuildDate",TimeStamp.now());
        propsMap.put("language","it");
        propsMap.put("itunes_summary"," ");
        propsMap.put("itunes_subtitle"," ");
        propsMap.put("itunes_author","###ITUNES-AUTHOR-UNDEFINED###");
        propsMap.put("itunes_owner_name","Progetto LODE Unitn");
        propsMap.put("itunes_owner_email","marco.ronchetti@unitn.it");
        //===================
        propsMap.put("itunes_explicit","NO");
        propsMap.put("sy_updatePeriod","1");
        propsMap.put("sy_updateFrequency","hourly");
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
        itunes_summary=propsMap.get("itunes_summary");
        itunes_subtitle=propsMap.get("itunes_subtitle");
        itunes_author=propsMap.get("itunes_author");
        itunes_owner=new Itunes_owner(propsMap.get("itunes_owner_name"),propsMap.get("itunes_owner_email"));
        itunes_explicit=propsMap.get("itunes_explicit");
        sy_updatePeriod=propsMap.get("sy_updatePeriod");
        sy_updateFrequency=propsMap.get("sy_updateFrequency");
    }
}
