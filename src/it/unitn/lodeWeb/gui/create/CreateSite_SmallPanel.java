package it.unitn.lodeWeb.gui.create;

//--------------------------------------------------------------------------
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.lodeWeb.gui.*;
import java.awt.event.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class CreateSite_SmallPanel extends JPanel {

    static final String newline = System.getProperty("line.separator");
    String acquisitionDir, distributionDir;
    private JTextArea log = null;
    // panel
    private JPanel panelOut;
    //---------------
    private static IOPump ioPump;
    private ProgressJDialog jfp;
    //-------
    private JFrame mainFrame;

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     */
    //--------------------------------------------------------------------------
    public CreateSite_SmallPanel() {

        super(new BorderLayout(5, 5));

        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        //----------------------------------------------------------------------

        // creazione pannello output SOUTH

        // crea text area per log (pompa che stampa output system)

        log = new JTextArea(6, 5);
        log.setEditable(false);
        try {
            ioPump = new IOPump(log.getDocument());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.setOut(ioPump.getOutputStream());
        ioPump.start();

        JScrollPane logScrollPane = new JScrollPane(log);

        logScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        logScrollPane.setBackground(Color.WHITE);

        JButton jButtonClean = new JButton("Clean");
        jButtonClean.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                log.setText("");
            }
        });

        JButton jButtonSaveLog = new JButton("Save Log");
        jButtonSaveLog.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!log.getText().equals("")) {
                    if (!_saveLogFile(log.getText())) {
                        _setLogError("Error saving");
                    }
                }

            }
        });

        JPanel jpanelButtons2 = new JPanel();
        jpanelButtons2.setLayout(new BoxLayout(jpanelButtons2, BoxLayout.Y_AXIS));
        jpanelButtons2.add(jButtonClean);
        jpanelButtons2.add(jButtonSaveLog);

        panelOut = new JPanel();
        panelOut.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        GroupLayout layout = new GroupLayout(panelOut);
        panelOut.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().
                addComponent(jpanelButtons2));
        hGroup.addGroup(layout.createParallelGroup().
                addComponent(logScrollPane));
        layout.setHorizontalGroup(hGroup);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(Alignment.LEADING).
                addComponent(logScrollPane).addComponent(jpanelButtons2));
        layout.setVerticalGroup(vGroup);
        add(panelOut);
        jfp = ControllersManager.getinstance().getProgressJDialog();
    }

    public void display() {
        jfp.reset("Creating web site...");
        mainFrame = new JFrame();
        mainFrame.setSize(700, 300);
        mainFrame.setContentPane(this);
        mainFrame.setVisible(true);
    }
    //--------------------------------------------------------------------------
    /**
     * start creation
     */
    //--------------------------------------------------------------------------
    public void start() {
        mainFrame.setEnabled(false);
        jfp.buttonOk.setEnabled(false);
        Toolkit.getDefaultToolkit().beep();
        // cursore caricamento
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(hourglassCursor);
    }
    //--------------------------------------------------------------------------

    /**
     * 	end creation
     */
    //--------------------------------------------------------------------------
    public void done() {
        mainFrame.setEnabled(true);
        jfp.buttonOk.setEnabled(true);
        Toolkit.getDefaultToolkit().beep();
        setCursor(null);
    }
    //--------------------------------------------------------------------------

    /**
     * set progress bar to start
     *
     * @param action
     */
    //--------------------------------------------------------------------------
    public void startProgressBar(String action) {
        jfp.progressBar.setValue(0);
        jfp.taskOutput.setText(action + "...");
        jfp.taskOutput.repaint();
    }
    //--------------------------------------------------------------------------

    /**
     * set progress bar to end
     *
     */
    //--------------------------------------------------------------------------
    public void doneProgressBar() {
        jfp.progressBar.setValue(100);
        jfp.taskOutput.setText(jfp.taskOutput.getText() + "... OK");
        jfp.taskOutput.repaint();
    }
    //--------------------------------------------------------------------------

    /**
     *
     * set progress
     *
     * @param progress - int progress
     *
     */
    //--------------------------------------------------------------------------
    public void setProgress(int progress) {
        if (progress <= jfp.progressBar.getMaximum()) {
            jfp.progressBar.setValue(progress);
        } else {
            setProgress(0);
        }

    }
    //--------------------------------------------------------------------------

    /**
     *
     * Get the progress bar
     *
     * @return int
     */
    //--------------------------------------------------------------------------
    public int getProgress() {
        return jfp.progressBar.getValue();
    }
    //--------------------------------------------------------------------------

    /**
     *
     * Set log output
     *
     * @param row - row to write
     * @param c - color of the new row
     */
    //--------------------------------------------------------------------------
    private void _setLog(String row, Color c) {
        if (log != null) {
            log.setForeground(c);
            log.append(row + newline);
            log.repaint();
        }
    }
    //--------------------------------------------------------------------------

    /**
     *
     * Set log error
     *
     * @param err - row error to write
     */
    //--------------------------------------------------------------------------
    private void _setLogError(String err) {
        this._setLog("Error_CreateSiteSmallPanel: " + err, Color.RED);
    }
    //--------------------------------------------------------------------------
    /**
     *
     * Save log in a file
     *
     * @param content - content of the new file log
     * @return boolean - if not error return true
     */
    //--------------------------------------------------------------------------
    boolean _saveLogFile(String content) {
        File file = new File("log.txt");
        JFileChooser fc = new JFileChooser();
        javax.swing.filechooser.FileFilter filter = null;
        fc.setCurrentDirectory(new File("."));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        filter = fc.getFileFilter();
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "txt files (*.txt)";
            }
        });
        fc.setSelectedFile(file);
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) {
            return true;
        } else if (result == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return false;
                }
            }
            return _writeFile(file, content);
        } else {
            return false;
        }
    }
    //--------------------------------------------------------------------------

    /**
     * Write a file
     * @param file - file to write
     * @param dataString - data to write
     *
     * @return boolean - if not error return true
     */
    //--------------------------------------------------------------------------
    private boolean _writeFile(File file, String dataString) {
        try {
            PrintWriter out =
                    new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.print(dataString);
            out.flush();
            out.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    //--------------------------------------------------------------------------
    /**
     * Check course dir
     *
     * @param dir - course directory
     * @return boolean - if the course directory is correct, returns true
     */
    //--------------------------------------------------------------------------
    /* THIS STUFF IS NEVER USED ================================================
    private boolean checkDirCourse(String dir) {

        File dirF = new File(dir);
        if (!dirF.exists() || !dirF.isDirectory()) {
            return false;
        }
        File[] lf = dirF.listFiles();

        boolean flag = false;
        for (int i = 0; i < lf.length; i++) {
            File f = lf[i];
            if (f.isDirectory()) {
                if (f.getName().equals(Constants.acquisNameDir)) {
                    this.acquisitionDir = f.getPath();
                    flag = true;
                }
            }
        }
        boolean flag2 = false;
        for (int i = 0; i < lf.length; i++) {
            File f = lf[i];
            if (f.isDirectory()) {
                if (f.getName().equals(Constants.distNameDir)) {
                    this.distributionDir = f.getPath();
                    flag2 = true;
                }
            }
        }
        boolean flag3 = false;
        for (int i = 0; i < lf.length; i++) {
            File f = lf[i];
            if (f.isFile()) {
                if (f.getName().equals(Constants.courseXmlName)) {
                    flag3 = true;
                }
            }
        }

        if (flag && flag2 && flag3) {
            return true;
        } else {
            return false;
        }

    }
     THIS STUFF IS NEVER USED ================================================
     */
}
