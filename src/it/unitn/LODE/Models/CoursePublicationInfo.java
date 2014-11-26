package it.unitn.LODE.Models;
import org.simpleframework.xml.*;
import java.io.Serializable;
/**
 *
 * @author ronchet
 */
@Root(name="CoursePublicationLocation")
public class CoursePublicationInfo extends StorableAsXML implements Serializable {
    @Element(name="PublicationRootDir")
    private String publicationRootDir="";

    public CoursePublicationInfo(){} // needed for deserialization!

    public CoursePublicationInfo(String publicationRootDir){
        this.publicationRootDir=publicationRootDir;
    }
    public String getPublicationRootDir() {return publicationRootDir;}
    public void setPublicationRootDir(String publicationRootDir) {this.publicationRootDir=publicationRootDir;}
}
