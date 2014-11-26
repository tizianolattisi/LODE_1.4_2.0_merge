package it.unitn.lodeWeb.serializationxml.slides;

import org.simpleframework.xml.*;

//--------------------------------------------------------------------------
/**
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root
public class SlidesGroup {

    //--------------------------------------------------------------------------
    /**
     *  Class constroctor
     */
    //--------------------------------------------------------------------------
    public SlidesGroup() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class constructor
     *
     * @param filename
     * @param firstSlide
     * @param lastSlide
     */
    //--------------------------------------------------------------------------
    public SlidesGroup(String filename, int firstSlide, int lastSlide) {
        this.filename = filename;
        this.firstSlide = firstSlide;
        this.lastSlide = lastSlide;
    }
    @Element(name = "fileName")
    private String filename;
    @Element(name = "firstSlide")
    private int firstSlide;
    @Element(name = "lastSlide")
    private int lastSlide;

    /**
     *
     * @return String
     */
    public String getName() {
        return filename;
    }

    /**
     *
     * @return int
     */
    public int getFirstSlide() {
        return firstSlide;
    }

    /**
     * 
     * @return int
     */
    public int getLastSlide() {
        return lastSlide;
    }
}