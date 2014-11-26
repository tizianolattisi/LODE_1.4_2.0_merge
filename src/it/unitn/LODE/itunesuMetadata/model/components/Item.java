    package it.unitn.LODE.itunesuMetadata.model.components;

import java.util.HashMap;
import org.simpleframework.xml.*;

/**
<item>
<title>Inaugurazione</title>
<pubDate>Mon, 17 May 2010 14:04:55 +0000</pubDate>
<dc:creator>Marco Ronchetti</dc:creator>
<enclosure url="http://latemar.science.unitn.it/itunes/archive/video/festivaleconomia2010/3giu/conferenza1.mp4" />
<itunes:author>vari</itunes:author>
<itunes:summary></itunes:summary>
<itunes:subtitle></itunes:subtitle>
<itunes:keywords>festival,economia,universita',trento</itunes:keywords>
<itunes:duration></itunes:duration>
<itunes:explicit>no</itunes:explicit>
</item>

 */
public class Item {
    HashMap<String, String> propsMap = new HashMap<String, String>();
    @Element(name = "title")
    private String title = null;
    @Element(name = "itunesu:category")
    private Itunesu_category itunesu_category = null;
    @Element(name = "pubDate")
    private String pubDate = null;
    @Element(name = "dc:creator")
    private String dc_creator = null;
    @Element(name = "enclosure")
    private Enclosure enclosure = null;
    private String mp4Url = "URL NOT PROVIDED";
    @Element(name = "author")
    @Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd")
    private String itunes_author = null;
    @Element(name = "summary")
    @Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd")
    private String itunes_summary = null;
    @Element(name = "subtitle")
    @Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd")
    private String itunes_subtitle = null;
    @Element(name = "keywords")
    @Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd")
    private String itunes_keywords = null;
    @Element(name = "duration")
    @Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd")
    private String itunes_duration = null;
    @Element(name = "explicit")
    @Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd")
    private String itunes_explicit = null;

    public Item() {
        title = "TITOLO";
        pubDate = TimeStamp.now();
        enclosure = new Enclosure(mp4Url);
        dc_creator = "CREATOR";
        itunes_author = "AUTHOR";
        itunes_summary = "SUMMARY";
        itunes_subtitle = "SUBTITLE";
        itunes_keywords = "KEYWORDS";
        itunes_duration = "DURATION";
        itunes_explicit = "EXPLICIT";
    }

    public Item(String title,
            String code,
            String pubDate,
            String mp4Url,
            String dc_creator,
            String itunes_author,
            String itunes_summary,
            String itunes_subtitle,
            String itunes_keywords,
            String itunes_duration,
            String itunes_explicit) {
        this.title = title;
        this.itunesu_category=new Itunesu_category(code);
        this.pubDate = pubDate;
        enclosure = new Enclosure(mp4Url);
        this.dc_creator = dc_creator;
        this.itunes_author = itunes_author;
        this.itunes_summary = itunes_summary;
        this.itunes_subtitle = itunes_subtitle;
        this.itunes_keywords = itunes_keywords;
        this.itunes_duration = itunes_duration;
        this.itunes_explicit = itunes_explicit;
    }
    // ========== SETTERS =======================================================

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setMp4Url(String mp4Url) {
        enclosure = new Enclosure(mp4Url);
    }

    public void setDc_creator(String dc_creator) {
        this.dc_creator = dc_creator;
    }

    public void setItunes_author(String itunes_author) {
        this.itunes_author = itunes_author;
    }

    public void setItunes_summary(String itunes_summary) {
        this.itunes_summary = itunes_summary;
    }

    public void setItunes_subtitle(String itunes_subtitle) {
        this.itunes_subtitle = itunes_subtitle;
    }

    public void setItunes_keywords(String itunes_keywords) {
        this.itunes_keywords = itunes_keywords;
    }

    public void setItunes_duration(String itunes_duration) {
        this.itunes_duration = itunes_duration;
    }

    public void setItunes_explicit(String itunes_explicit) {
        this.itunes_explicit = itunes_explicit;
    }

    public HashMap<String, String> getPropsMap(){
        return propsMap;
    }

    public void setDefaults() {
        propsMap.put("title", "");
        propsMap.put("itunesu:category", "UNDEFINED");
        propsMap.put("pubDate", TimeStamp.now());
        propsMap.put("enclosure", "###MP4-URL-UNDEFINED###");
        propsMap.put("dc:creator", "###dc_creator-UNDEFINED##");
        propsMap.put("itunes:author", "###itunes_author-UNDEFINED##");
        propsMap.put("itunes:summary", "");
        propsMap.put("itunes:subtitle", "");
        propsMap.put("itunes:keywords", "");
        propsMap.put("itunes:duration", "");
        propsMap.put("itunes:explicit", "no");
    }

    public void setvars() {
        title = propsMap.get("title");
        itunesu_category=new Itunesu_category(propsMap.get("itunesu:category"));
        pubDate = propsMap.get("pubDate");
        enclosure = new Enclosure(propsMap.get("enclosure"));
        dc_creator = propsMap.get("dc:creator");
        itunes_author = propsMap.get("itunes:author");
        itunes_summary = propsMap.get("itunes:summary");
        itunes_subtitle = propsMap.get("itunes:subtitle");
        itunes_keywords = propsMap.get("itunes:keywords");
        itunes_duration = propsMap.get("itunes:duration");
        itunes_explicit = propsMap.get("itunes:explicit");
    }
}
