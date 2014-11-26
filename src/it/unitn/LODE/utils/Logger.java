package it.unitn.LODE.utils;
/*
 * Logger.java
 * 
 * Created on 30-nov-2007, 12.17.03
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ronchet
 */
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.ProgramState;
import java.io.*;
public class Logger {
    ProgramState programState=null;
    // ========== SOLITON PATTERN ==============================================
    static Logger instance=null;
    public synchronized static Logger getInstance(){
        if (instance==null) instance=new Logger();
        return instance;
    }
    private Logger() {
        programState=ProgramState.getInstance();
    }
    // =========================================================================
    public void writeLogLine (int current_slide_number, int current_movie_time) {
        RandomAccessFile raf;
        String path = programState.getCurrentLecture().getAcquisitionPath()+File.separator+LODEConstants.LOG_NAME;
        try {
            //URL url = LODE.class.getResource(LOG_NAME);
            raf = new RandomAccessFile (path, "rw");
            raf.skipBytes( (int)raf.length() );
            raf.writeBytes(current_slide_number + ": " + current_movie_time + "\n");
            raf.close();
        }
        catch (IOException ex) {
            Messanger m=Messanger.getInstance();
            m.log("Unable to write to file "+path);
            ex.printStackTrace(m.getLogger());
            m.w("Unable to write to file "+path,LODEConstants.MSG_ERROR);
        }

    }

    public void writeLogLine (int current_slide_number, String current_movie_time) {
        RandomAccessFile raf;
        String path = programState.getCurrentLecture().getAcquisitionPath()+File.separator+LODEConstants.LOG_NAME;
        try {
            //URL url = LODE.class.getResource(LOG_NAME);
            raf = new RandomAccessFile (path, "rw");
            raf.skipBytes( (int)raf.length() );
            raf.writeBytes(current_slide_number + " - " + current_movie_time + "\n");
            raf.close();
        }
        catch (IOException ex) {
            Messanger m=Messanger.getInstance();
            m.log("Unable to write to file "+path);
            ex.printStackTrace(m.getLogger());
            m.w("Unable to write to file "+path,LODEConstants.MSG_ERROR);
        }

    }

    public void deleteLogLine (String text_to_delete)  {
        String line;
        StringBuffer sb = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(programState.getCurrentLecture().getAcquisitionPath()+File.separator+LODEConstants.LOG_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll(text_to_delete, "");
                if (line.length()>0)
                    sb.append(line + "\n");
            }
            reader.close();
            BufferedWriter out = new BufferedWriter(new FileWriter(programState.getCurrentLecture().getAcquisitionPath()+File.separator+LODEConstants.LOG_NAME));
            out.write(sb.toString());
            out.close();
        }
        catch (Throwable ex) {
            Messanger m=Messanger.getInstance();
            m.log("Error while deleting log line "+text_to_delete);
            ex.printStackTrace(m.getLogger());
            m.w("Error while deleting log line "+text_to_delete,LODEConstants.MSG_ERROR);
        }

    }
}
