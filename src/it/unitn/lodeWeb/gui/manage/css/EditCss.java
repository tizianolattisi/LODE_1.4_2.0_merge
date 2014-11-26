package it.unitn.lodeWeb.gui.manage.css;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.ChooserPanel;
import it.unitn.lodeWeb.gui.css.CssPanel;
import it.unitn.lodeWeb.serializationxml.ManageInfoXML;
import it.unitn.lodeWeb.serializationxml.course.Course;
import it.unitn.lodeWeb.util.Constants;
import it.unitn.lodeWeb.util.Util;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author colo
 */
//--------------------------------------------------------------------------
public class EditCss extends JPanel {

    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JLabel jlabelSaved;
    private javax.swing.JScrollPane jScrollPane1;
    private JEditorPane jEditorPane;
    private ChooserPanel cp;
    private JButton jButton4;
    private JPanel jPanelButtons;
    private File css;
    private JPanel jPanelCssName;
    private JLabel l;

    //--------------------------------------------------------------------------
    /**
     *
     * @param cp
     */
    //--------------------------------------------------------------------------
    public void setChooser(ChooserPanel cp) {
        this.cp = cp;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * write log output
     *
     * @param log - row to write
     * @param colore - color of the new row
     */
    //--------------------------------------------------------------------------
    private void writeOutput(String log, Color colore) {
        jEditorPane.setForeground(colore);
        jEditorPane.setText(log);
        //output.setForeground(Color.BLACK);
        jEditorPane.repaint();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Set log error
     *
     * @param err - row error to write
     */
    //--------------------------------------------------------------------------
    public void printError(String err) {
        writeOutput(err, Color.RED);

        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     */
    //--------------------------------------------------------------------------
    public EditCss() {

        setOpaque(false);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        jPanelButtons = new javax.swing.JPanel();

        jButton1 = new javax.swing.JButton();

        jButton2 = new javax.swing.JButton();

        jButton3 = new javax.swing.JButton();

        jButton4 = new javax.swing.JButton();

        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane = new javax.swing.JEditorPane();
        jlabelSaved = new JLabel("Saved");
        jlabelSaved.setVisible(false);

        jScrollPane1.setViewportView(jEditorPane);


        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
        jButton1.setText("Show/Refresh");
        jButton1.setIcon(new ImageIcon("Images"+ File.separator + "refresh.png"));

        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (cp.getOutDir() != null && !cp.getOutDir().equals("") && cp.getCourseDir() != null && !cp.getCourseDir().equals("")) {

                    css = new File(cp.getOutDir() + File.separator + Constants.dirCss + Constants.fileCssWebApplication);
                    if (css.exists() && css.isFile()) {
                        try {
                            jButton2.setEnabled(true);
                            jButton3.setEnabled(true);
                            jButton4.setEnabled(true);
                            String testoPagina = new Util().leggiFile(css.getPath());
                            writeOutput(testoPagina, Color.BLACK);
                            l.setText(css.getName());
                        } catch (IOException ex) {
                            printError("Error css");
                        }
                    } else {
                        printError("select valid dir");
                    }
                } else {
                    printError("select valid dir");
                }
            }
        });


        jButton2.setIcon(new ImageIcon("Images" + File.separator + "css" + File.separator + "css_blue.png"));
        jButton2.setText("New Css...");
        jButton2.addActionListener(new ActionListener() {

            private JDialog cssDialog;

            public void actionPerformed(ActionEvent e) {
                // creo il pannello editor css
                final CssPanel cssPanel = new CssPanel();
                // pannello bottoni di conferma
                //--------------------------------------------------------------
                JPanel panelButton = new JPanel();
                JButton buttonOk = new JButton("Ok");
                                buttonOk.setIcon(new ImageIcon("Images" + File.separator + "css" + File.separator + "css_ok.png"));
                buttonOk.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (cssDialog != null) {

                            if (cssPanel.getCssPath() != null) {
                                try {
                                    cssPanel.getCssPath();
                                    String testoPagina = new Util().leggiFile(cssPanel.getCssPath());
                                    writeOutput(testoPagina, Color.BLACK);
                                    l.setText(new File(cssPanel.getCssPath()).getName());
                                    jlabelSaved.setVisible(false);
                                } catch (IOException ex) {
                                    Logger.getLogger(EditCss.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                            cssDialog.dispose();
                        }

                    }
                });
                JButton buttonCancel = new JButton("Cancel");
                buttonCancel.setIcon(new ImageIcon("Images" + File.separator + "css" + File.separator + "css_no.png"));
                buttonCancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (cssDialog != null) {
                            cssDialog.dispose();
                        }
                    }
                });

                panelButton.add(buttonOk);
                panelButton.add(buttonCancel);

                cssPanel.add(panelButton, BorderLayout.SOUTH);
                //--------------------------------------------------------------

                // creo la finestra Jdialog per l'editor
                cssDialog = new JDialog();
                ImageIcon immagine = new ImageIcon("Images" + File.separator + "css" + File.separator + "css_blue.png");
                cssDialog.setIconImage(immagine.getImage());
                cssDialog.setTitle("Cascading Style Sheet Editor");

                // pannello css
                cssDialog.setContentPane(cssPanel);

                cssDialog.setSize(new Dimension(800, 500));
                cssDialog.setMinimumSize(new Dimension(800, 500));
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                int w = (d.width - cssDialog.getWidth()) / 2;
                int h = (d.height - cssDialog.getHeight()) / 2;
                cssDialog.setLocation(w, h);
                cssDialog.setModal(true);
                cssDialog.setVisible(true);
                cssDialog.pack();

            }
        });

        jButton3.setText("View index page");
        jButton3.setIcon(new ImageIcon("Images"+ File.separator + "Globe-16x16.png"));
        jButton3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (cp.getOutDir() != null && !cp.getOutDir().equals("") && cp.getCourseDir() != null && !cp.getCourseDir().equals("")) {
                    if (cp != null) {

                        String dirSite = cp.getOutDir();
                        Course c = null;
                        try {
                            c = (new ManageInfoXML()).getInfoCourse(Constants.courseXmlName, new File(cp.getCourseDir()).getPath());

                        } catch (Exception ex) {
                            printError("Error index page");
                        }
                        String home_Corso = c.getCourseHome();
                        //File nameIndex = new File(home_Corso+"index.html");
                        File nameIndex = new File("index.html");
                        File index = new File(dirSite + File.separator + nameIndex);

                        if (index.isFile() && index.exists()) {
                            try {
                                Desktop.getDesktop().browse(index.toURI());
                            } catch (Exception ex) {
                                printError("Error page index");
                            }
                        }
                    }
                } else {
                    printError("Select valid dir");
                }
            }
        });

        jButton4.setIcon(new ImageIcon("Images" + File.separator + "Save.png"));
        jButton4.setText("Save");
        jButton4.addActionListener(new ActionListener() {

            private FileWriter filestream;
            private BufferedWriter output;

            public void actionPerformed(ActionEvent e) {
                if (jEditorPane != null) {
                    if (!jEditorPane.getText().equals("")) {
                        try {
                            filestream = new FileWriter(css);
                            output = new BufferedWriter(filestream);
                            output.write(jEditorPane.getText());
                            output.close();
                            jlabelSaved.setVisible(true);
                        } catch (IOException ex) {
                            printError("Errore: Css creation");
                        }
                    }
                }
            }
        });



        jEditorPane.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                // non ancora salvato perchè sta per essere modificato
                jlabelSaved.setVisible(false);
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        this.setLayout(new BorderLayout(5, 5));

        jPanelButtons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));

        jPanelButtons.add(jButton1);
        jPanelButtons.add(jButton2);
        jPanelButtons.add(jButton3);
        jPanelButtons.add(jButton4);
        jPanelButtons.add(jlabelSaved);

        add(jPanelButtons, BorderLayout.NORTH);

        jScrollPane1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        add(jScrollPane1, BorderLayout.CENTER);

        jPanelCssName = new JPanel();
        l = new JLabel("");
        jPanelCssName.setOpaque(false);
        jPanelCssName.add(l);

        add(jPanelCssName, BorderLayout.SOUTH);

    }
}
