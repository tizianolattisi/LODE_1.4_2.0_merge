package it.unitn.LODE.Models;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.utils.Messanger;
import java.io.File;
import java.io.Serializable;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
/**
 *
 * @author ronchet
 */
public class StorableAsXML implements Serializable {
    // ==== SERIALIZATION/DESERIALIZATION PRIMITIVES ===========================
    public void persist(File f){
        Format format = new Format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Serializer serializer = new Persister(format);
        try {
            serializer.write(this, f);
        } catch (Exception ex) {
            Messanger m=Messanger.getInstance();
            ex.printStackTrace(m.getLogger());
            m.w("Error while serializing XML file "+f.getAbsolutePath(),LODEConstants.MSG_ERROR);
            m.w(ex.getMessage(), LODEConstants.MSG_ERROR);
        }

        /* older Java Serialization implementation
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;
        try {
            fos=new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
        } catch (IOException ex) {
            Logger.getLogger(Lecture.class.getName()).log(Level.SEVERE, null, ex);
        } 
        */
    }
    public StorableAsXML resume(File f, Class<? extends StorableAsXML> c) throws Exception{
        StorableAsXML retval = null;
        if (!f.exists()) {
            System.err.println("File "+f.getPath()+" does not exist");
            return null;
        }
        try {
            Serializer serializer = new Persister();
            retval = (StorableAsXML)serializer.read(c, f);
        } catch (Exception ex) {
            Messanger m=Messanger.getInstance();
            ex.printStackTrace(m.getLogger());
            throw ex;
        }
        retval.postResume();
        /* older Java Serialization implementation
        FileInputStream fis=null;
        ObjectInputStream ois=null;
        try {
            fis=new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            retval=(StorableAsXML)ois.readObject();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Lecture.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            retval=null;
        } catch (IOException ex) {
            Logger.getLogger(Lecture.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
        return retval;
    }
    /**
     * A do-nothing method for post creation.
     * Can be used by subclasses
     */
    protected void postResume() {};
     /**
     * A do-nothing method for data synchronization.
     * Can be used by subclasses
     */
    public void update() {};
}
