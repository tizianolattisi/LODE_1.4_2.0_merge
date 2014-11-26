package it.unitn.lodeWeb.serializationxml.slides;

import java.util.List;
import org.simpleframework.xml.*;

//--------------------------------------------------------------------------
/**
 *
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root(name = "SLIDES")
public class Slides {

    //--------------------------------------------------------------------------
    /**
     *
     * Class constructor
     *
     */
    //--------------------------------------------------------------------------
    public Slides() {
        super();
    }
    @ElementList(inline = true, name = "SLIDE")
    private List<Slide> slides;

    //--------------------------------------------------------------------------
    /**
     *
     * CLass constroctor
     *
     * @param slides
     */
    //--------------------------------------------------------------------------
    public Slides(List slides) {
        this.slides = slides;
    }

    /**
     *
     * @return List<Slide>
     */
    public List<Slide> getSlides() {
        return slides;
    }
}
