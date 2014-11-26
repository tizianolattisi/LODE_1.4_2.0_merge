package it.unitn.LODE.itunesuMetadata.model.components;
import org.simpleframework.xml.*;


public class Atom_link {

   @Attribute(name="href")
   private String href="ERROR -NO URL PROVIDED!";


   @Attribute(name="rel")
   private String rel="self";

   @Attribute(name="type")
   private String type="application/rss+xml";

   public Atom_link(){}

   public Atom_link(String url){
       if (url!=null && !url.equals(""))
            href=url;
   }
}
