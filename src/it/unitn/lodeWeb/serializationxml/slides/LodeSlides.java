package it.unitn.lodeWeb.serializationxml.slides;

import org.simpleframework.xml.*;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root(name = "LODE_SLIDES")
public class LodeSlides {

    @Element(name = "GROUPS")
    private Groups groups;
    @Element(name = "SLIDES")
    private Slides slides;

    //--------------------------------------------------------------------------
    /**
     * Clas constructor
     */
    //--------------------------------------------------------------------------
    public LodeSlides() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class constructor
     *
     * @param groups
     * @param slides
     */
    //--------------------------------------------------------------------------
    public LodeSlides(Groups groups, Slides slides) {
        this.groups = groups;
        this.slides = slides;
    }

    /**
     *
     * @return Groups
     */
    public Groups _getGroups() {
        return groups;
    }

    /**
     *
     * @return Slides
     */
    public Slides _getSlides() {
        return slides;
    }
}
