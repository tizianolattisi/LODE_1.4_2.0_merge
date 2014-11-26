package it.unitn.lodeWeb.serializationxml.notes;

import org.simpleframework.xml.*;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root(name = "NOTE")
public class Note {

    //--------------------------------------------------------------------------
    /**
     * Class Constructor
     */
    //--------------------------------------------------------------------------
    public Note() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param id
     * @param title
     * @param note
     */
    //--------------------------------------------------------------------------
    public Note(int id, String title, String note) {
        this.id = id;
        this.title = title;
        this.note = note;
    }
    @Element(name = "ID")
    private int id;
    @Element(name = "NOTE")
    private String note;
    @Element(name = "TITLE")
    private String title;

    /**
     *
     * @return int
     */
    public int getID() {
        return id;
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
    public String getNote() {
        return note;
    }
}