package it.unitn.LODE.services;

import it.unitn.LODE.gui.ProcessLoggerWindow;
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.utils.Messanger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;

/**
 *
 * @author ronchet
 */
public class ProcessLogger {

    Process p; // process to be logged
    ProcessLoggerWindow processLoggerWindow; //Window where to put the output

    /**
     * Factory method. It starts a new thread into which the actual ProcessLogger is launched
     * @param p the process to which the Process logger will be attached
     * @return the created instance of ProcessLogger
     */
    public static ProcessLogger getProcessLogger(final Process p) {
        class ProcessLoggerThread implements Runnable {

            ProcessLogger pl = null;

            @Override
            public void run() {
                pl = new ProcessLogger();
                pl._exec(p);
            }

            ProcessLogger getProcessLogger() {
                return pl;
            }
        }
        ProcessLoggerThread plt = new ProcessLoggerThread();
        //java.awt.EventQueue.invokeLater(plt);
        new Thread(plt).start();
        return plt.getProcessLogger();
    }

    private ProcessLogger() { // do nothing but ensure that "new" is not called on this class.
    }

    private void _exec(final Process p) {
        processLoggerWindow = ControllersManager.getinstance().getProcessLoggerWindow();

        final Document d = processLoggerWindow.getDocument();
        final Style outStyle = processLoggerWindow.getOutStyle();
        final Style errStyle = processLoggerWindow.getErrStyle();
        processLoggerWindow.setVisible(true);
        Thread inReader = new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                try {
                    while (true) {
                        String s = in.readLine();
                        if (s == null) {
                            break;
                        }
                        try {
                            d.insertString(d.getLength(), s + "\n", outStyle);
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (IOException ex) {
                    Messanger m = ControllersManager.getinstance().getMessanger();
                    ex.printStackTrace(m.getLogger());
                    m.w("Error while writing output of Process " + p.toString(), LODEConstants.MSG_ERROR);
                }
                try {
                    d.insertString(d.getLength(), "\nProcess ended\n", outStyle);
                } catch (BadLocationException ex) {
                    Logger.getLogger(ProcessLoggerWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                /*
                try {
                InputStream in = p.getInputStream();
                int c;
                while ((c = in.read()) != -1) {
                jTextArea1.append(""+(char) c);
                jTextArea1.setCaretPosition( jTextArea1.getDocument().getLength() );
                }
                jTextArea1.append("\nProcess ended\n");
                in.close();
                } catch (IOException ex) {
                Messanger m=Messanger.getInstance();
                ex.printStackTrace(m.getLogger());
                m.w("Error while writing output of Process "+p.toString(),LODEConstants.MSG_ERROR);
                }*/
                /*
                try {
                InputStream in = p.getInputStream();
                int c;
                while ((c = in.read()) != -1) {
                jTextArea1.append(""+(char) c);
                jTextArea1.setCaretPosition( jTextArea1.getDocument().getLength() );
                }
                jTextArea1.append("\nProcess ended\n");
                in.close();
                } catch (IOException ex) {
                Messanger m=Messanger.getInstance();
                ex.printStackTrace(m.getLogger());
                m.w("Error while writing output of Process "+p.toString(),LODEConstants.MSG_ERROR);
                }*/
            }
            //});
        };
        Thread errReader = new Thread() {
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                try {
                    while (true) {
                        String s = in.readLine();
                        if (s == null) {
                            break;
                        }
                        d.insertString(d.getLength(), s + "\n", errStyle);
                        //jTextArea1.getDocument().append(s+"\n");
                        //jTextArea1.setCaretPosition( jTextArea1.getDocument().getLength() );
                    }
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    Messanger m = ControllersManager.getinstance().getMessanger();
                    ex.printStackTrace(m.getLogger());
                    m.w("Error while writing output of Process " + p.toString(), LODEConstants.MSG_ERROR);
                }
                //jTextArea1.append("\nProcess ended\n");
                int pos = d.getLength();
                try {
                    d.insertString(pos, "\nProcess ended\n", null);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                /*
                try {
                InputStream in = p.getErrorStream();
                int c;
                while ((c = in.read()) != -1) {
                jTextArea1.append(""+(char) c);
                jTextArea1.setCaretPosition( jTextArea1.getDocument().getLength() );
                }
                jTextArea1.append("\nProcess ended\n");
                in.close();
                } catch (IOException ex) {
                Messanger m=Messanger.getInstance();
                ex.printStackTrace(m.getLogger());
                m.w("Error while writing output of Process "+p.toString(),LODEConstants.MSG_ERROR);
                }*/


                /*
                try {
                InputStream in = p.getErrorStream();
                int c;
                while ((c = in.read()) != -1) {
                jTextArea1.append(""+(char) c);
                jTextArea1.setCaretPosition( jTextArea1.getDocument().getLength() );
                }
                jTextArea1.append("\nProcess ended\n");
                in.close();
                } catch (IOException ex) {
                Messanger m=Messanger.getInstance();
                ex.printStackTrace(m.getLogger());
                m.w("Error while writing output of Process "+p.toString(),LODEConstants.MSG_ERROR);
                }*/
            }
            //});
        };
        inReader.start();
        errReader.start();
    }
}
