package it.unitn.lodeWeb.gui.manage.notes;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.util.Constants;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.MinimalHTMLWriter;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class Editor extends JFrame implements ActionListener {

    // Some required variables to make multiple file handling easy
    File selectedfile;
    private JScrollPane jScrollPane;
    private JEditorPane jEditorPane;
    private String noteURL;
    JTextPane jedp;
    private JTextField textLog;
    private String title = null;
    private String index = null;

    private void setCenterPosition() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (d.width - getWidth()) / 2;
        int h = (d.height - getHeight()) / 2;
        setLocation(w, h);
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param url - note url
     * @param title - note title
     * @param index - page index
     * 
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * @throws javax.swing.text.BadLocationException
     */
    //--------------------------------------------------------------------------
    Editor(String url, String title, String index) throws FileNotFoundException, IOException, BadLocationException {
        this.index = index;
        this.title = title;

        ImageIcon immagine = new ImageIcon("Images" + File.separator + "logo.jpg");
        this.setIconImage(immagine.getImage());

        setSize(800, 600);
        setCenterPosition();
        setVisible(true);
        setTitle("Simple Text ");



        File f = new File(url);

        noteURL = url;

        jedp = new JTextPane();


        jedp.setContentType("text/html");
        // ottengo il documentstyle
        StyledDocument doc = (StyledDocument) jedp.getDocument();
        f = new File(url);
        FileReader reader = new FileReader(f);
        // leggo il file html solo il contenuto
        HTMLEditorKit s = new HTMLEditorKit();
        s.read(reader, doc, 0);

        jedp.setEditable(true);

        jedp.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                // non ancora salvato perchè sta per essere modificato
                textLog.setText("");
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        jScrollPane = new JScrollPane();
        jScrollPane = new JScrollPane(jedp);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setPreferredSize(new Dimension(250, 145));
        jScrollPane.setMinimumSize(new Dimension(10, 10));

        add((new JPanel()).add(jScrollPane), BorderLayout.CENTER);

        JMenuBar menuBar = drawMenu();
        setJMenuBar(menuBar);

        JToolBar toolBar = drawToolBar();

        add(toolBar, BorderLayout.PAGE_START);

    }

    //--------------------------------------------------------------------------
    /**
     *
     * actionPerformed
     *
     * @param e - ActionEvent
     */
    //--------------------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {
        try {
            saveDocument(this.noteURL, jedp);
        } catch (Exception ex) {
            textLog.setText("error save");
        }

    }

    //--------------------------------------------------------------------------
    /**
     *
     * save Document (new note)
     *
     * @param noteURL
     * @param jedp
     *
     * @throws java.io.IOException
     * @throws javax.swing.text.BadLocationException
     */
    //--------------------------------------------------------------------------
    private void saveDocument(String noteURL, JTextPane jedp) throws IOException, BadLocationException {
        if (noteURL != null && jedp != null) {

            File f = new File(noteURL);

            StyledDocument testo = jedp.getStyledDocument();
            // scrivo il file html con la formattazione del testo
            FileWriter fileWriter = new FileWriter(f);
            MinimalHTMLWriter htmlWriter = new MinimalHTMLWriter(
                    fileWriter, testo);
            htmlWriter.write();
            fileWriter.close();

            // ottengo la directory della home del sito web
            File homeWeb = new File(new File(new File(new File(new File(noteURL).getParent()).getParent()).getParent()).getParent());
            jedp.selectAll();
            if (!homeWeb.getName().equals(Constants.dirCoursesWEB) && !homeWeb.getName().equals(Constants.dirNoteWEB) && homeWeb.isDirectory()) {
                (new ProcessIndexNotes()).processIndex(this.title, this.index, homeWeb.getPath(), jedp.getSelectedText());
            }

            textLog.setText("saved");
        } else {
            textLog.setText("error");
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Create our toolbar
     *
     * @return
     */
    //--------------------------------------------------------------------------
    private JToolBar drawToolBar() {
        JToolBar myBar = new JToolBar(
                "Tool Bar");

        JButton save_button = new JButton(
                "Save");
        save_button.setIcon(new ImageIcon("Images" + File.separator + "Save.png"));
        save_button.addActionListener(this);
        myBar.add(save_button);

        JButton fontitalic_button = new JButton("Italic");

        fontitalic_button.addActionListener(new StyledEditorKit.ItalicAction());
        myBar.add(fontitalic_button);

        JButton fontbold_button = new JButton("Bold");

        fontbold_button.addActionListener(
                new StyledEditorKit.BoldAction());
        myBar.add(fontbold_button);

        JButton color_button = new JButton("Color");
        color_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(Editor.this,
                        "Color Chooser", Color.cyan);

                if (color != null) {
                    MutableAttributeSet attr = new SimpleAttributeSet();

                    StyleConstants.setForeground(attr, color);

                    jedp.setCharacterAttributes(attr, false);
                }
            }
        });
        myBar.add(color_button);

        textLog = new JTextField("", 200);
        textLog.setEditable(false);
        myBar.add(textLog);

        return myBar;
    }

    //--------------------------------------------------------------------------
    /**
     *  Our menu bar
     *
     * @return
     */
    //--------------------------------------------------------------------------
    private JMenuBar drawMenu() {

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a menu
        JMenu file = new JMenu("File");
        JMenu insert = new JMenu("Insert");
        JMenu format = new JMenu("Format");
        JMenu fontsize = new JMenu("Size");
        JMenu fontface = new JMenu("Font Face");
        JMenu fontstyle = new JMenu("Font Style");

        menuBar.add(file);
        menuBar.add(insert);
        menuBar.add(format);

        JMenuItem _save = new JMenuItem("Save…");
        _save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    saveDocument(noteURL, jedp);
                } catch (Exception ex) {
                    if (textLog != null) {
                        textLog.setText("error save");
                    }
                }
            }
        });

        JMenuItem _exit = new JMenuItem("Exit");
        _exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
        file.add(_save);
        file.add(_exit);

        JMenuItem _picture = new JMenuItem("Picture");
        _picture.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setDialogTitle("Select an image file");

                int result = fileChooser.showOpenDialog(Editor.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedfile = fileChooser.getSelectedFile();

                    ImageIcon _img = new ImageIcon(selectedfile.getAbsolutePath());

                    jedp.insertIcon(_img);
                }
            }
        });
        insert.add(_picture);

        // Font Sizes
        JMenuItem _size_10 = new JMenuItem("10");
        _size_10.addActionListener(new StyledEditorKit.FontSizeAction("font-size-10", 10));
        JMenuItem _size_12 = new JMenuItem("12");
        _size_12.addActionListener(new StyledEditorKit.FontSizeAction("font-size-12", 12));
        JMenuItem _size_14 = new JMenuItem("14");
        _size_14.addActionListener(new StyledEditorKit.FontSizeAction("font-size-14", 14));
        JMenuItem _size_16 = new JMenuItem("16");
        _size_16.addActionListener(new StyledEditorKit.FontSizeAction("font-size-16", 16));
        fontsize.add(_size_10);
        fontsize.add(_size_12);
        fontsize.add(_size_14);
        fontsize.add(_size_16);
        format.add(fontsize);

        // Font Families
        JMenuItem _sans = new JMenuItem("SansSerif");
        _sans.addActionListener(new StyledEditorKit.FontFamilyAction("font-family-Serif", "Serif"));
        JMenuItem _mono = new JMenuItem("Monospaced");
        _mono.addActionListener(new StyledEditorKit.FontFamilyAction("font-family-Monospaced", "Monospaced"));
        fontface.add(_sans);
        fontface.add(_mono);
        format.add(new JSeparator());
        format.add(fontface);

        // Font Styles
        JMenuItem _bold = new JMenuItem("Bold");
        _bold.addActionListener(new StyledEditorKit.BoldAction());
        JMenuItem _italic = new JMenuItem("Italic");
        _italic.addActionListener(new StyledEditorKit.ItalicAction());
        JMenuItem _underlined = new JMenuItem("Underlined");
        _underlined.addActionListener(new StyledEditorKit.UnderlineAction());
        fontstyle.add(_bold);
        fontstyle.add(_italic);
        fontstyle.add(_underlined);
        format.add(new JSeparator());
        format.add(fontstyle);
        // Font Color
        JMenuItem _color = new JMenuItem("Color");

        _color.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(Editor.this,
                        "Color Chooser", Color.cyan);

                if (color != null) {
                    MutableAttributeSet attr = new SimpleAttributeSet();

                    StyleConstants.setForeground(attr, color);

                    jedp.setCharacterAttributes(attr, false);
                }
            }
        });
        format.add(new JSeparator());
        format.add(_color);

        return menuBar;
    }
}