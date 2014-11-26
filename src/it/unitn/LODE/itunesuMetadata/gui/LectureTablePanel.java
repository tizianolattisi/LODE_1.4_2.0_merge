package it.unitn.LODE.itunesuMetadata.gui;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.itunesuMetadata.model.templates.ChannelTemplateForLecture;
import it.unitn.LODE.itunesuMetadata.model.templates.LectureTemplate;

/**
 *
 * @author ronchet
 */
public class LectureTablePanel extends TablePanel {
    ChannelTemplateForLecture channel=null;

    public LectureTablePanel(){
        rss=new LectureTemplate();
        channel=((LectureTemplate)rss).getChannel();
        propsMap=channel.getPropsMap();
        //initComponentsAndShow();
    }

    public void getLectureData(Lecture l){
        filename=l.getiTunesuDistributionPath()+LODEConstants.ITUNES_LECTURE_METADATA_FILENAME;
        int time=Integer.parseInt(l.getVideoLength());
        int ss=time%60;
        int mm=time/60;
        int hh=mm/60;
        mm=mm%60;
        propsMap.put("itunes:duration",""+hh+":"+mm+":"+ss);
        propsMap.put("title",l.getLectureName());
        propsMap.put("itunes:author",l.getLecturer());
        propsMap.put("dc:creator",l.getLecturer());
        propsMap.put("enclosure",l.getiTunesuDistributionPath());
        propsMap.put("pubDate",l.getDate().toString());
        //channel.setvars();
    }
}
