package it.unitn.lodeWeb.gui.create;

//--------------------------------------------------------------------------
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.lodeWeb.gui.*;
import it.unitn.lodeWeb.execution.Exec;
import it.unitn.lodeWeb.util.Constants;
import it.unitn.lodeWeb.util.OptionClass;
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
public class CreateSitePanel extends JPanel implements ActionListener {

    static final String newline = System.getProperty("line.separator");
    String acquisitionDir, distributionDir;
    private JTextArea log = null;
    // panels
    public ChooserPanel chooserPanel;
    private OptionPanel optionPanel;
    // panel
    private JPanel panelOut;
    //---------------
    private JButton startButton;
    private static IOPump ioPump;
    private ProgressJDialog jfp;
    //-------
    private JFrame mainFrame;

    //--------------------------------------------------------------------------
    /**
     * 	end creation
     */
    //--------------------------------------------------------------------------
    public void done() {
        mainFrame.setEnabled(true);
        jfp.buttonOk.setEnabled(true);
        startButton.setEnabled(true);
        Toolkit.getDefaultToolkit().beep();
        setCursor(null);
    }

    //--------------------------------------------------------------------------
    /**
     * start creation
     */
    //--------------------------------------------------------------------------
    public void start() {
        mainFrame.setEnabled(false);
        startButton.setEnabled(false);
        jfp.buttonOk.setEnabled(false);
        Toolkit.getDefaultToolkit().beep();
        // cursore caricamento
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(hourglassCursor);
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
    public void setLog(String row, Color c) {
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
    public void setLogError(String err) {
        this.setLog("Error_CreateSitePanel: " + err, Color.RED);
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Write a file
     *
     * @param file - file to write
     * @param dataString - data to write
     *
     * @return boolean - if not error return true
     */
    //--------------------------------------------------------------------------
    public static boolean writeFile(File file, String dataString) {
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
     *
     * Filter txt file
     *
     */
    //--------------------------------------------------------------------------
    private class JavaFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
        }

        public String getDescription() {
            return "txt files (*.txt)";
        }
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
    boolean saveLogFile(String content) {
        File file = new File("log.txt");
        JFileChooser fc = new JFileChooser();
        javax.swing.filechooser.FileFilter filter = null;
        fc.setCurrentDirectory(new File("."));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        filter = fc.getFileFilter();
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(new JavaFilter());
        fc.setSelectedFile(file);

        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) {
            return true;
        } else if (result == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Overwrite existing file?", "Confirm Overwrite",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return false;
                }
            }
            return writeFile(file, content);
        } else {
            return false;
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param mainFrame
     */
    //--------------------------------------------------------------------------
    public CreateSitePanel(JFrame mainFrame) {

        super(new BorderLayout(5, 5));

        this.mainFrame = mainFrame;

        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        //----------------------------------------------------------------------

        // Pannello scelta directory course e output per generazione sito NORTH

        chooserPanel = new ChooserPanel();
        add(chooserPanel, BorderLayout.NORTH);

        // Pannello per le opzioni di creazione sito CENTER

        optionPanel = new OptionPanel();
        add(optionPanel, BorderLayout.CENTER);

        //----------------------------------------------------------------------

        // pannello bottoni WEST

        JPanel jPanelButtons = new JPanel();
        jPanelButtons.setLayout(new BoxLayout(jPanelButtons, BoxLayout.Y_AXIS));
        jPanelButtons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        startButton = new JButton("GO!!!");
        startButton.addActionListener(this);

        jPanelButtons.add(startButton);

        add(jPanelButtons, BorderLayout.LINE_START);

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
        JButton jButtonSaveLog = new JButton("Save Log");

        jButtonClean.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                log.setText("");
            }
        });
        jButtonSaveLog.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!log.getText().equals("")) {
                    if (!saveLogFile(log.getText())) {
                        setLogError("Error saving");
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

        add(panelOut, BorderLayout.SOUTH);
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Check course dir
     *
     * @param dir - course directory
     * @return boolean - if the course directory is correct, returns true
     */
    //--------------------------------------------------------------------------
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

    //--------------------------------------------------------------------------
    /**
     *
     * Start creation process
     *
     * @throws java.lang.Exception
     *
     */
    //--------------------------------------------------------------------------
    private void go() throws Exception {


        if (new File(chooserPanel.getOutDir()).isDirectory()) {

            if (checkDirCourse(chooserPanel.getCourseDir())) {

                if (optionPanel.jComboBoxContext != null) {

                    String username = optionPanel.getUsername();
                    String password = optionPanel.getPassword();

                    OptionClass oc = new OptionClass(
                            optionPanel.jCheckBox1.isSelected(),
                            optionPanel.jCheckBox6.isSelected(),
                            LODEConstants.VLC_PATH_ON_WINDOWS,
                            optionPanel.jCheckBox2.isSelected(),
                            optionPanel.jCheckBox3.isSelected(),
                            optionPanel.jCheckBox4.isSelected(),
                            optionPanel.jCheckBox5.isSelected(),
                            optionPanel.jComboBoxContext.getSelectedItem().toString(),
                            optionPanel.css_path, username, password);

                    jfp = ControllersManager.getinstance().getProgressJDialog();
                    jfp.reset("Checking course...");

                    String[] args = {acquisitionDir, distributionDir,
                        chooserPanel.getCourseDir(),
                        chooserPanel.getOutDir(), "1",
                        optionPanel.jComboBoxContext.getSelectedItem().toString()};

                    log.setForeground(Color.BLUE);
                    log.setText("");

                    Runnable prova = new Exec(args, oc, this);
                    new Thread(prova).start();

                }

            } else {
                setLogError("Bad course directory");
            }
        } else {
            setLogError("Bad output directory");
        }
    }

    //--------------------------------------------------------------------------
    /**
     * 
     * actionPerformed
     *
     * @param e
     *
     */
    //--------------------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {
        try {
            go();
        } catch (Exception ex) {
            setLogError(ex.getMessage());
        }
    }
}