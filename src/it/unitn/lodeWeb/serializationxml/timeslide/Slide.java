package it.unitn.lodeWeb.serializationxml.timeslide;

import org.simpleframework.xml.*;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root(name = "slide")
public class Slide {

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     */
    //--------------------------------------------------------------------------
    public Slide() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param time
     * @param title
     * @param image
     */
    //--------------------------------------------------------------------------
    public Slide(int time, String title, String image) {
    }
    @Element(name = "tempo")
    private int time;
    @Element(name = "titolo")
    private String title;
    @Element(name = "immagine")
    private String image;

    /**
     *
     * @return int
     */
    public int getTime() {
        return time;
    }

    /**
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return String
     */
    public String getImage() {
        return image;
    }
}

