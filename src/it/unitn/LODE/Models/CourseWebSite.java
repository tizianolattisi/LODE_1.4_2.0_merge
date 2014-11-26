/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.Models;
import org.simpleframework.xml.*;
import java.io.Serializable;

/**
 *
 * @author ronchet
 */
@Root(name="CourseWebSite")
public class CourseWebSite extends StorableAsXML implements Serializable {
    @Element(name="PublicationDir")
    public String publicationDir="";

    public CourseWebSite(){} // needed for deserialization!

    public CourseWebSite(String publicationDir){
        this.publicationDir=publicationDir;
    }
}
