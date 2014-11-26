package it.unitn.lodeWeb.gui.css;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.css.NewCss.MyGraphicsPanel;
import it.unitn.lodeWeb.gui.css.NewCss.NewCssPanel;
import it.unitn.lodeWeb.util.Constants;
import it.unitn.lodeWeb.util.Util;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class CssPanel extends JPanel {

    private JButton buttonSelect;
    private JButton buttonEdit;
    private JButton buttonViewStr;
    private JFileChooser fc;
    private JEditorPane jedp;
    private ButtonGroup buttonGroup;
    private JRadioButtonMenuItem jRadioCss;
    private JButton viewPageButton;
    private JButton changeViewButton;
    private JPanel pc;
    private String cssPath;
    private String cssPathSelect;
    private int cssSelected = -1;
    private JRadioButtonMenuItem jRadioCssSelected;


    //--------------------------------------------------------------------------
    /**
     * Class Constructor
     */
    //--------------------------------------------------------------------------
    public CssPanel() {

        super();

        setLayout(new BorderLayout(5, 5));
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // output preview css
        jedp = new JEditorPane();
        jedp.setOpaque(false);
        jedp.setEditable(false);
        jedp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)));
        JScrollPane jScrollPane = new JScrollPane(jedp);

        jScrollPane.setPreferredSize(new Dimension(250, 145));
        jScrollPane.setMinimumSize(new Dimension(10, 10));
        jedp.setContentType("text/html");


        changeViewButton = new JButton("View Css code");
        changeViewButton.setEnabled(false);
        //-------------------------------------
        // action listener selezione css
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                if (viewPageButton != null) {
                    viewPageButton.setEnabled(true);
                    changeViewButton.setEnabled(true);
                }
                JRadioButtonMenuItem jrb = (JRadioButtonMenuItem) actionEvent.getSource();
                boolean selected = jrb.isSelected();
                if (selected) {
                    cssSelected = Integer.parseInt(jrb.getName());
                    if (cssSelected == -1) {
                        cssPath = cssPathSelect;
                    } else {
                        cssPath = Constants.dirCss + Constants.cssList[cssSelected];
                    }
                    showPreviewCss();
                }
            }
        };
        //-------------------------------------
        // pannello selezione css gallery
        pc = new JPanel();
        pc.setOpaque(false);
        pc.setLayout(new BoxLayout(pc, BoxLayout.X_AXIS));
        pc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Css gallery"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        buttonGroup = new ButtonGroup();
        ImageIcon immagine = new ImageIcon("Images" + File.separator + "css" + File.separator + "cssIco.png");

        // aggiungo i vari css precreati
        for (int i = 0; i < Constants.n_css; i++) {
            jRadioCss = new JRadioButtonMenuItem("", immagine);
            jRadioCss.setName("" + i);
            //jRadioCss.setOpaque(false);
            //jRadioCss.setBackground(Color.WHITE);
            jRadioCss.setOpaque(false);
            jRadioCss.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Style " + (i + 1)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            jRadioCss.addActionListener(actionListener);
            buttonGroup.add(jRadioCss);
            pc.add(jRadioCss);
        }

        // radiobutton per il css edit
        immagine = new ImageIcon("Images" + File.separator + "css" + File.separator + "cssIco.png");
        jRadioCssSelected = new JRadioButtonMenuItem("", immagine);

        jRadioCssSelected.setOpaque(false);
        //jRadioCsSelected.setVisible(false);
        // jRadioCssSelected.setBackground(Color.WHITE);
        jRadioCssSelected.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(" "),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jRadioCssSelected.addActionListener(actionListener);
        jRadioCssSelected.setEnabled(false);
        buttonGroup.add(jRadioCssSelected);

        // aggiunta nuovo css da file
        buttonSelect = new JButton(new ImageIcon("Images" + File.separator + "css" + File.separator + "Css2.png"));
        buttonSelect.setText("Select Css...");
        buttonSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cssChooser();
            }
        });

        // creazione di un css da applicazione
        buttonEdit = new JButton(new ImageIcon("Images" + File.separator + "css" + File.separator + "css_add.png"));
        buttonEdit.setText("New Css...");
        buttonEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                newCss();
            }
        });

        // visualizza la pagina struttura
        buttonViewStr = new JButton("View HTML");
        buttonViewStr.setIcon(new ImageIcon("Images" + File.separator + "html2.png"));
        buttonViewStr.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showPreviewStructure();
            }
        });

        // visualizza la pagina scelta
        viewPageButton = new JButton("View result page");
        viewPageButton.setIcon(new ImageIcon("Images" + File.separator + "Globe-16x16.png"));
        viewPageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showBrowserCss();
            }
        });

        viewPageButton.setEnabled(false);


        // pannello bottoni per modifica
        JPanel pEdit = new JPanel();
        pEdit.setOpaque(false);
        pEdit.setLayout(new BorderLayout(20, 20));

        JPanel pButtonEdit = new JPanel(new GridLayout(4, 1, 2, 2));

        pButtonEdit.add(buttonSelect);
        pButtonEdit.add(buttonEdit);
        pButtonEdit.add(buttonViewStr);
        pButtonEdit.add(viewPageButton);
        pEdit.add(pButtonEdit, BorderLayout.EAST);
        pEdit.add(jRadioCssSelected, BorderLayout.CENTER);

        pc.add(pEdit);
        //-------------------------------------------

        // css gallery
        add(pc, BorderLayout.NORTH);
        // pannello preview




        JPanel p = new JPanel(new BorderLayout(5, 5));


        changeViewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (cssPath != null) {
                    if (changeViewButton.getText().equals("View Css")) {
                        try {
                            String css = new Util().leggiFile(cssPath);
                            jedp.setEditorKit(null);
                            jedp.setContentType("text");
                            jedp.setText(css);

                            changeViewButton.setText("View page");

                        } catch (IOException ex) {
                        }
                    } else {
                        showPreviewCss();
                        changeViewButton.setText("View Css");
                    }
                }
            }
        });

        p.add(changeViewButton, BorderLayout.NORTH);
        p.add(jScrollPane, BorderLayout.CENTER);
        //add(jScrollPane, BorderLayout.CENTER);
        add(p, BorderLayout.CENTER);

    }

    //--------------------------------------------------------------------------
    /**
     * Get css path
     *
     * @return String - css path
     */
    //--------------------------------------------------------------------------
    public String getCssPath() {
        return this.cssPath;
    }
    private String htmlStructure =
            "<html>\n" +
            "<head>\n" +
            "    <title> \n" +
            "    </title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div id=\"container\">\n" +
            "        <div id=\"header\">\n" +
            "            <table id=\"infoTable\">\n" +
            "                <tr>\n" +
            "                    <td class=\"top-left\"></td>\n" +
            "                    <td>Course name: </td>\n" +
            "                    <td>....</td>\n" +
            "                    <td class=\"top-right\"></td>\n" +
            "                </tr>   <tr>\n" +
            "                   <td class=\"foot-left\"></td>\n" +
            "                    <td>Year: </td>\n" +
            "                    <td>xxxx</td>\n" +
            "                    <td class=\"foot-right\"></td>\n" +
            "                </tr>\n" +
            "            </table>\n" +
            "        </div>\n" +
            "        <div id=\"footer\">\n" +
            "        </div>\n" +
            "        <div id=\"content\">\n" +
            "            <table id=\"table\">\n" +
            "                <caption></caption>\n" +
            "                <thead>\n" +
            "                    <tr>\n" +
            "                        <th class=\"top-left\"></th>\n" +
            "                 	      ...\n" +
            "                        <th></th>\n" +
            "                         ...\n" +
            "                        <th class=\"top-right\"></th>\n" +
            "                    </tr>\n" +
            "                </thead>\n" +
            "                <tfoot>\n" +
            "                    <tr>\n" +
            "                        <td class=\"foot-left\" colspan=\"\"></td>\n" +
            "                        <td></td>\n" +
            "                        <td class=\"foot-right\"> </td>\n" +
            "                    </tr>\n" +
            "                </tfoot>\n" +
            "                <tbody>\n" +
            "                    ....\n" +
            "                    <tr>\n" +
            "                        ...\n" +
            "                        <td></td>\n" +
            "                        ...\n" +
            "                    </tr>\n" +
            "                    ....\n" +
            "                </tbody>\n" +
            "            </table>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

    //--------------------------------------------------------------------------
    /**
     * Show preview Css in jEditorPane
     *
     */
    //--------------------------------------------------------------------------
    public void showPreviewCss() {
        changeViewButton.setText("View Css");
        jedp.setEditorKit(new MyEditorKit());
        try {
            File file = new File(Constants.dirCss + "test.html");
            if (file.exists()) {
                jedp.setPage(file.toURI().toURL());
            }
        } catch (IOException ex) {
            System.out.println("errore: lettura del file preview css");
            Logger.getLogger(CssPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.repaint();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Show preview html structure in JDialog
     *
     */
    //--------------------------------------------------------------------------
    private void showPreviewStructure() {


        JDialog structureDialog = new JDialog();
        structureDialog.setSize(new Dimension(600, 500));
        structureDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (d.width - structureDialog.getWidth()) / 2;
        int h = (d.height - structureDialog.getHeight()) / 2;
        structureDialog.setLocation(w, h);
        structureDialog.setMinimumSize(new Dimension(300, 350));

        ImageIcon immagine = new ImageIcon("Images" + File.separator + "html.png");
        structureDialog.setIconImage(immagine.getImage());
        structureDialog.setTitle("HTML page structure");


        JEditorPane jedpS = new JEditorPane();
        jedpS.setMaximumSize(new Dimension(600, 500));
        jedpS.setFont(new Font("Serif", Font.PLAIN, 13));
        jedpS.setForeground(Color.BLUE);
        jedpS.setEditable(false);
        jedpS.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)));
        JScrollPane jScrollPane2 = new JScrollPane(jedpS);
        jedpS.setBackground(Color.white);
        jedpS.setText(htmlStructure);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("HTML", null, jScrollPane2, "");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        StyleCssClass scss = new StyleCssClass();
        MyGraphicsPanel g = new MyGraphicsPanel(scss);
        g.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        g.repaint(scss);
        g.repaint();
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(g);
        jScrollPane.setBackground(Color.WHITE);
        jScrollPane.setSize(new Dimension(200, 200));

        tabbedPane.addTab("Graphics", null, jScrollPane, "");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        ImageIcon immagineEditorCss = new ImageIcon("Images" + File.separator + "css" + File.separator + "css_blue.png");

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        GroupLayout layout = new GroupLayout(structureDialog.getContentPane());
        structureDialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE).addContainerGap()));

        structureDialog.pack();


        structureDialog.setAlwaysOnTop(true);
        structureDialog.setModal(true);
        structureDialog.setResizable(true);
        structureDialog.setVisible(true);
        structureDialog.pack();


    }

    //--------------------------------------------------------------------------
    /**
     *
     * JavaFilter for .css file
     *
     */
    //--------------------------------------------------------------------------
    private class JavaFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".css") || f.isDirectory();
        }

        public String getDescription() {
            return "Css files (*.css)";
        }
    }

    //--------------------------------------------------------------------------
    /**
     * 	Choose css from an external file
     */
    //--------------------------------------------------------------------------
    private void cssChooser() {

        if (fc == null) {
            fc = new JFileChooser();



            javax.swing.filechooser.FileFilter filter = null;
            filter = fc.getFileFilter();
            fc.addChoosableFileFilter(filter);
            fc.setFileFilter(new JavaFilter());
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setAcceptAllFileFilterUsed(false);

            fc.setCurrentDirectory(new java.io.File("."));
        }

        int returnVal = fc.showDialog(CssPanel.this, "Select css");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            String name = file.getName();

            // controllo l'estensione del file selezionato

            String s = name.substring(name.length() - 3, name.length());
            if (s != null) {
                if (s.equals("css") || s.equals("Css") || s.equals("CSS")) {


                    name = file.getName();

                    // aggiorno il path del css
                    cssPath = (file.getPath());
                    cssPathSelect = cssPath;
                    showPreviewCss();
                    fc.setSelectedFile(null);

                    // seleziono il radiobutton
                    jRadioCssSelected.setVisible(true);
                    jRadioCssSelected.setEnabled(true);
                    // -1 vuol dire che il path del css è stato selezionato dall'esterno
                    jRadioCssSelected.setName("-1");
                    jRadioCssSelected.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createTitledBorder(name),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                    jRadioCssSelected.setSelected(true);
                    // per pagina preview
                    if (viewPageButton != null) {
                        viewPageButton.setEnabled(true);
                        changeViewButton.setEnabled(true);
                    }


                }
            }


        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * create a new css
     *
     */
    //--------------------------------------------------------------------------
    private void newCss() {
        NewCssPanel ncp = new NewCssPanel();
        if (ncp.getCss() != null) {

            cssPath = new File(ncp.getCss()).getPath();

            //fc.setSelectedFile(null);

            String name = new File(ncp.getCss()).getName();

            // seleziono il radiobutton
            jRadioCssSelected.setVisible(true);
            jRadioCssSelected.setEnabled(true);
            jRadioCssSelected.setName("5");
            jRadioCssSelected.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(name),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            jRadioCssSelected.setSelected(false);

            jRadioCssSelected.doClick();

        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * 	Displays the example page in the default browser
     *
     */
    //--------------------------------------------------------------------------
    private void showBrowserCss() {
        File index = null;
        if (cssSelected == -1) {
            index = new File(Constants.dirCss + Constants.genericHtml);
        }
        if (cssSelected >= 0) {
            if (cssSelected == 5) {
                index = new File(Constants.dirCss + Constants.myCssHtml);
            } else {
                int i = cssSelected + 1;
                index = new File(Constants.dirCss + "test_" + i + ".html");
            }
        }

        try {
            Desktop.getDesktop().browse(index.toURI());
        } catch (IOException ex) {
            System.out.println("error: apertura file preview");
            Logger.getLogger(CssPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //--------------------------------------------------------------------------
    /**
     * 
     */
    //--------------------------------------------------------------------------
    private class MyEditorKit extends HTMLEditorKit {

        private StyleSheet styleSheet;

        //--------------------------------------------------------------------------
        /**
         * @see javax.swing.text.html.HTMLEditorKit#getContentType()
         */
        //--------------------------------------------------------------------------
        @Override
        public String getContentType() {
            return "text/html";
        }

        //--------------------------------------------------------------------------
        /**
         * Overridden to allow us to have another stylesheet as oposed to text/html content type.
         */
        //--------------------------------------------------------------------------
        @Override
        public StyleSheet getStyleSheet() {
            if (styleSheet == null) {
                styleSheet = loadStyleSheet();
            }
            return styleSheet;
        }

        //--------------------------------------------------------------------------
        /**
         * Load the style sheet.
         * @return The style sheet.
         */
        //--------------------------------------------------------------------------
        private StyleSheet loadStyleSheet() {
            StyleSheet result = new StyleSheet();
            try {
                if (new File(cssPath).exists() && new File(cssPath).isFile()) {
                    Reader reader = new FileReader(new File(cssPath));
                    result.loadRules(reader, null);
                }

            } catch (Exception ex) {
                System.err.println("An error occurred while loading the style sheet.");
                ex.printStackTrace();
            }

            return result;
        }
    }
}




