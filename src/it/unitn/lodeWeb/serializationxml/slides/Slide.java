package it.unitn.lodeWeb.serializationxml.slides;

import org.simpleframework.xml.*;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root(name = "SLIDE")
public class Slide {

    //--------------------------------------------------------------------------
    /**
     *  Class constructor
     */
    //--------------------------------------------------------------------------
    public Slide() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class constructor
     *
     * @param filename
     * @param sequenceNumber
     * @param title
     * @param text
     *
     */
    //--------------------------------------------------------------------------
    public Slide(String filename, int sequenceNumber, String title, String text) {
        this.filename = filename;
        this.sequenceNumber = sequenceNumber;
        this.title = title;
        this.text = text;
    }
    @Element(name = "FILENAME")
    private String filename;
    @Element(name = "SEQ_NUM")
    private int sequenceNumber;
    @Element(name = "TITLE")
    private String title;
    @Element(name = "TEXT")
    private String text;

    /**
     *
     * @return String
     */
    public String getFileName() {
        return filename;
    }

    /**
     *
     * @return int
     */
    public int getSeqN() {
        return sequenceNumber;
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
    public String getText() {
        return text;
    }
}

