package it.unitn.LODE.itunesuMetadata.model.components;
import org.simpleframework.xml.*;

/**
 *
 * @author ronchet
 */
@Root(name="category")
@Namespace(reference="http://www.itunes.com/dtds/podcast-1.0.dtd")
public class Itunes_category {
    @Attribute(name="text")
    private String text=null;


   public Itunes_category(){
       text="DEFAULT TEXT";
   }

   public Itunes_category(String text){
       this.text=text;
   }
}

