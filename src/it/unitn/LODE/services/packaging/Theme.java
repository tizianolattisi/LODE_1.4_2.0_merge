package it.unitn.LODE.services.packaging;

import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.StorableAsXML;
import java.awt.Color;
import java.io.File;
import org.simpleframework.xml.*;

/**
 *
 * @author ronchet
 */
@Root(name="theme")
public class Theme extends StorableAsXML {
    @Element(name="fontcolor")
    Fontcolor f=null;
    @Element(name="elementcolor")
    Elementcolor e=null; 
    @Element(name="text")
    Text t=null;
    @Element(name="logo")
    Logo l=null;;
    public Theme(){
        f=new Fontcolor();
        e=new Elementcolor();
        t=new Text();
        l=new Logo();
        String s=f.sliderollover;
        Integer i=string2int(s);
        String s2=int2string(i);
        System.out.println(s);
        System.out.println(s2);
        System.out.println(i);
        Color c=new Color(i);
        System.out.println(c.getRGB());

        System.out.println(rgb2string(c.getRGB()));

    }
    public int string2int(String s){
       return Integer.parseInt(s.substring(2),0x10);
    }
    public String int2string(int i){
       return "0x"+Integer.toHexString(i);
    }
    public String rgb2string(int i){
       // throw away the alpha values
       return "0x"+Integer.toHexString(i).substring(2);
    }
    public static void main(String a[]){
        Theme t=new Theme();
        File tmp=new File(LODEConstants.COURSES_HOME);
        if (!tmp.exists()) ControllersManager.getinstance().getFileSystemManager().createFolder(tmp);
        t.persist(new File(LODEConstants.COURSES_HOME+LODEConstants.FS+"theme.xml"));

    }/*
    public void writeInfo(File xmlFile){ 
        // using Simple XML Serialization
        // http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php

       Serializer serializer = new Persister();
        try {
            serializer.write(this, xmlFile);
        } catch (Exception ex) {
            Messanger m=Messanger.getInstance();
            ex.printStackTrace(m.getLogger());
            m.w("Error while serializing XML file "+xmlFile.getAbsolutePath(),LODEConstants.MSG_ERROR);
        }
    }*/

}
@Root
class Fontcolor {
    @Element
    String sliderollover="0xbb8800";
    @Element
    String slide="0xbbbb00";
    @Element
    String list="0x0000aa";
    @Element
    String lesson="0x000000";
    @Element
    String course="0x000000";
    @Element
    String time="0xbbbb00";
    @Element
    String buttons="0x0000aa";  

}

class Elementcolor {
    @Element
    String loadingbar="0x0000ff";
    @Element
    String timelinebackground="0xbbbb00";
    @Element
    String timelinebar="0x0000ff";
    @Element
    String listselection="0xbbbb00";
    @Element
    String zoomslider="0x0000ff";
    @Element
    String zoomtext="0xbbbb00";
    @Element
    String appbackground="0x444444";
}
	
class Text {
    @Element
    String largevideo="large video";
    @Element
    String largeslide="large slide";
    @Element
    String openslide="Full size slide";
    @Element
    String helpabout="About LODE";
    @Element
    String helpaboutcontent="--- Lectures On DEmand ---\n\n"+
"The LODE system for lecture\n" +
"recording has been developed\n"+
"by Marco Ronchetti at the\n"+
"University of Trento, Italy.\n\n"+
"marco.ronchetti@unitn.it\n\n"+
"For more info see:\n"+
"http://latemar.science.unitn.it/LODE\n\n";
    @Element
    String swapslidevideo="Slide&lt;-&gt;Video";
    @Element
    String zoombar="MUOVI IL CURSORE PER RIDIMENSIONARE IL VIDEO";
    @Element
    String dinamic_url="LODE Home";
}
	
class Logo{
    @Element
    String link="http://latemar.science.unitn.it/LODE";
    @Element
    String image="logo.jpg";
}
