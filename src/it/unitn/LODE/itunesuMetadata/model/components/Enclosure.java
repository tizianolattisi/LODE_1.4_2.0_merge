package it.unitn.LODE.itunesuMetadata.model.components;
import org.simpleframework.xml.*;


public class Enclosure {

   @Attribute(name="href")
   private String href="ERROR -NO URL PROVIDED!";


   Enclosure(){}

   Enclosure(String url){
       if (url!=null && !url.equals(""))
            href=url;
   }
}
