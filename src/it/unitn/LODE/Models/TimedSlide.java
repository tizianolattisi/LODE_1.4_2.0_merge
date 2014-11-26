/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.Models;
import org.simpleframework.xml.*;

/**
 *
 * @author ronchet
 */
@Root(name="slide")
public class TimedSlide {
    @Element(name="tempo")
    public String time="";
    @Element(name="titolo")
    public String slideTitle="...";
    @Element(name="immagine")
    public String fileName=null;

    public TimedSlide(){} // needed for deserialization!
    
    public TimedSlide(String time,String title,String fileName){
        this.time=time;
        this.slideTitle=title;
        this.fileName=fileName;
    }

    @Override
    // equals is needed to ensure a correct removal of instances from an arraylist
    public boolean equals(Object arg0) {
        if (! (arg0 instanceof TimedSlide)) return false;
        TimedSlide other=(TimedSlide) arg0;
        if (Integer.parseInt(time.trim())!=Integer.parseInt(other.time.trim())) return false;
        if (! other.fileName.equals(fileName)) return false;
        if (! other.slideTitle.equals(slideTitle)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.time != null ? this.time.hashCode() : 0);
        hash = 67 * hash + (this.slideTitle != null ? this.slideTitle.hashCode() : 0);
        hash = 67 * hash + (this.fileName != null ? this.fileName.hashCode() : 0);
        return hash;
    }
}
