package it.unitn.LODE.itunesuMetadata.model.components;
import org.simpleframework.xml.*;

/**
 *
 * @author ronchet
 */
public class Itunes_owner {
    @Element(name="itunes:name")
    private String itunes_name=null;

    @Element(name="itunes:email")
    private String itunes_email=null;


   public Itunes_owner(){
       itunes_name="Progetto Lode";
       itunes_email="marco.ronchetti@unitn.it";
   }
   public Itunes_owner(String name, String email){
       itunes_name=name;
       itunes_email=email;
   }
}
