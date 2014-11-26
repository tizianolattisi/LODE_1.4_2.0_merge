package it.unitn.lodeWeb.serializationxml.data;

import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root(name = "Lezione")
public class Lezione {

    @ElementList(inline = true, name = "slide")
    private List<Slide> slides;
    @Element(name = "video")
    private Video video;
    @Element(name = "info")
    private Info info;

    //--------------------------------------------------------------------------
    /**
     * Class Constructor
     */
    //--------------------------------------------------------------------------
    public Lezione() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param slides
     * @param video
     * @param info
     */
    //--------------------------------------------------------------------------
    public Lezione(List slides, Video video, Info info) {
        this.info = info;
        this.slides = slides;
        this.video = video;
    }

    /**
     *
     * @return Info
     */
    public Info getInfo() {
        return info;
    }

    /**
     *
     * @return Video
     */
    public Video getVideo() {
        return video;
    }

    /**
     * 
     * @return List<Slide>
     */
    public List<Slide> getSlides() {
        return slides;
    }
}


