package it.unitn.lodeWeb.serializationxml.notes;

import java.util.List;
import org.simpleframework.xml.*;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root(name = "NOTES")
public class Notes {

    @ElementList(inline = true, name = "NOTES")
    private List<Note> notes;

    //--------------------------------------------------------------------------
    /**
     * Class constructor
     */
    //--------------------------------------------------------------------------
    public Notes() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     * Class constructor
     *
     * @param notes
     */
    //--------------------------------------------------------------------------
    public Notes(List notes) {
        this.notes = notes;
    }

    /**
     *
     * @return List<Note>
     */
    public List<Note> getNotes() {
        return notes;
    }
}

