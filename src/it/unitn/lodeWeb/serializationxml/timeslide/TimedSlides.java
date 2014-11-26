package it.unitn.lodeWeb.serializationxml.timeslide;

import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root(name = "TIMED_SLIDES")
public class TimedSlides {
    
    @ElementList(inline = true, name = "slide")
    private List<Slide> timeslides;

    //--------------------------------------------------------------------------
    /**
     * class constructor
     */
    //--------------------------------------------------------------------------
    public TimedSlides() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class constructor
     *
     * @param timeslides
     */
    //--------------------------------------------------------------------------
    public TimedSlides(List timeslides) {
        this.timeslides = timeslides;
    }

    public List<Slide> getTimeslides() {
        return timeslides;
    }
    
}
