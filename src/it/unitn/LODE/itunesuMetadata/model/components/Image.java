/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.itunesuMetadata.model.components;

import org.simpleframework.xml.*;

/**
 *
 * @author ronchet
 */
public class Image {
    @Element(name="title")
    private String title=null;

    @Element(name="url")
    private String url="URL NOT DEFINED";

   public Image(){
        title="TITLE";
        url="a";
   }

   public Image(String url, String title){
        this.url=url;
        this.title=title;
   }

}
