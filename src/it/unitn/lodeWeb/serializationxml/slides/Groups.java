package it.unitn.lodeWeb.serializationxml.slides;

import java.util.List;
import org.simpleframework.xml.*;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
@Root
public class Groups {

    //--------------------------------------------------------------------------
    /**
     *  Class constructor
     */
    //--------------------------------------------------------------------------
    public Groups() {
        super();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class constructor
     *
     * @param slidesGroup
     */
    //--------------------------------------------------------------------------
    /*public Groups(SlidesGroup slidesGroup) {
        this.slidesGroup = slidesGroup;
    }*/
    @ElementList(inline=true)
    //(name = "slidesGroup")
    private List<SlidesGroup> slidesGroup;

    /**
     *
     * @return SlidesGroup
     */
    public List<SlidesGroup> getSlidesGroup() {
        return slidesGroup;
    }
}
