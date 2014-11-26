package it.unitn.LODE.itunesuMetadata.model.components;
import org.simpleframework.xml.*;

/**
 *
 * @author ronchet
 */
public class Itunesu_category {


   @Attribute(name="itunesu:code")
   private String code="NO CODE PROVIDED!";

   public Itunesu_category(){}

   public Itunesu_category(String aCode){
         code=aCode;
   }
}
