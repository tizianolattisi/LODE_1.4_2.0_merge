package it.unitn.lodeWeb.gui;

//--------------------------------------------------------------------------
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.nio.channels.*;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class IOPump implements Runnable {

    private Thread runner;
//segnale di esaurimento (IOPump è eseguibileuna sola volta)
    private volatile boolean exau = false;
    private volatile boolean execute = false;
    private PrintStream out;
    private BufferedReader lineReader;
    private Document document;

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param doc - output document for IOpump
     *
     * @throws java.io.IOException
     * 
     */
    //--------------------------------------------------------------------------
    public IOPump(Document doc) throws IOException {
        document = doc;
        //crea la connessione tra input e output
        Pipe pipe = Pipe.open();
        InputStream pin = Channels.newInputStream(pipe.source());
        OutputStream pout = Channels.newOutputStream(pipe.sink());
        //attacca un BufferedReader e un PrintStream ai flussi connessi
        out = new PrintStream(pout);
        InputStreamReader inReader = new InputStreamReader(pin);
        lineReader = new BufferedReader(inReader);
    }

    //--------------------------------------------------------------------------
    /**
     *
     * get output Stream
     *
     * @return PrintStream
     */
    //--------------------------------------------------------------------------
    public PrintStream getOutputStream() {
        return out;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Start IOpump
     *
     */
    //--------------------------------------------------------------------------
    public synchronized void start() {
        if (exau) {
            throw new RuntimeException("IOPump cannot be started twice");
        } else {
            execute = exau = true;
            runner = new Thread(this);
            //Daemon, muore "insieme all'evocatore"
            runner.setDaemon(true);
            runner.start();
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Stop IOpump
     *
     */
    //--------------------------------------------------------------------------
    public synchronized void stop() {
        try {
            lineReader.close();
        } catch (IOException ex) {
        } finally {
            execute = false;
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Run IOpump
     *
     */
    //--------------------------------------------------------------------------
    public void run() {
        while (execute) {
            String line = null;
            try {
                line = lineReader.readLine();//legge una linea di testo
            } catch (IOException ex) {
                break;
            }
            if (line != null) {
                swingPrint(line + "\n");
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Append the text using AWT Event Dispatcher Thread
     *
     * @param line
     *
     * @return void
     */
    //--------------------------------------------------------------------------
    private void swingPrint(final String line) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                rawPrint(line);
            }
        });
    }

    //--------------------------------------------------------------------------
    /**
     * 
     * @param line
     *
     * @return void
     */
    //--------------------------------------------------------------------------
    private void rawPrint(String line) {
        try {
            document.insertString(document.getLength(), line, null);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
