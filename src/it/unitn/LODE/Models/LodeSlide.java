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
/**
 * A LodeSlide is the representation of a slide without temporal marking
 * @author ronchet
 */
@Root(name="SLIDE")
public class LodeSlide extends StorableAsXML {
    @Element(name="FILENAME")
    private String fileName=null;
    @Element(name="SEQ_NUM")
    private int seqNum=-1;
    @Element(name="TITLE")
    private String title=null;
    @Element(name="TEXT")
    private String text=null;

    public LodeSlide(){} //needed to be a bean!
     
    public LodeSlide(int seqNum, String title, String text,String fileName) {
        this.seqNum=seqNum;
        if (title==null || title.trim().equals("")) title="-";
        this.title=title;
        if (text==null || text.trim().equals("")) text="-";
        this.text=text;
        this.fileName=fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
    
}
