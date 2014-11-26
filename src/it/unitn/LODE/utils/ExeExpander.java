/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.LODE.utils;

import java.io.*;

/**
 *
 * @author ronchetti
 */
public class ExeExpander {
    public void expandResource(String rsc, String targetFileName) {
        // input stream
        InputStream is = ExeExpander.class.getResourceAsStream(rsc);
        System.out.println(rsc);
        System.out.println(targetFileName);
        File target = new File(targetFileName);
        //=== COPY STREAM
        try {
            final byte[] buf = new byte[1024];
            OutputStream os = new FileOutputStream(target);
            int len = 0;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
