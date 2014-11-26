package it.unitn.lodeWeb.gui;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.create.CreateSitePanel;
import it.unitn.lodeWeb.gui.css.CssPanel;
import it.unitn.lodeWeb.gui.manage.ManagePanel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * Main Frame
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class MainFrame extends JFrame {

    private JPanel panelCreateSite;
    private ManagePanel managePanel;
    private JTabbedPane tabbedPane;

    //--------------------------------------------------------------------------
    /**
     * Initializes the frame
     *
     */
    //--------------------------------------------------------------------------
    private void initFrame() {

        // dimensione del frame
        setdimFrame(700, 678);
        setCenterPosition();


        // icona della finestra
        ImageIcon immagine = new ImageIcon("Images_LodeWeb" + File.separator + "logo.jpg");
        setIconImage(immagine.getImage());
        // titolo finestra
        setTitle("LODE Web");
        // crea e aggiungi la menu bar
        setJMenuBar(getMenuBar1());

        tabbedPane = new JTabbedPane();

        // pannello creazione del del sito LODE web
        panelCreateSite = new CreateSitePanel(this);

        tabbedPane.addTab("Create website", null, panelCreateSite, "");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        managePanel = new ManagePanel();

        tabbedPane.addTab("Manager (WebApp)", null, managePanel, "");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        CssPanel cssEditorPanel = new CssPanel();


        ImageIcon immagineEditorCss = new ImageIcon("Images" + File.separator + "css" + File.separator + "css_blue.png");
        tabbedPane.addTab(" Editor", immagineEditorCss, cssEditorPanel, "");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        this.getContentPane().add(tabbedPane);


        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });

        setVisible(true);
        pack();
    }

    //--------------------------------------------------------------------------
    /**
     * Creates new form MainFrame
     *
     */
    //--------------------------------------------------------------------------
    public MainFrame() {
        initFrame();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Set frame dimensions
     *
     * @param w - width
     * @param h - height
     *
     */
    //--------------------------------------------------------------------------
    private void setdimFrame(int w, int h) {
        // setta le dimensioni del frame
        //Dimension frameSize = new Dimension(w, h);
        //setSize(frameSize);
        setSize(w, h);
        setMinimumSize(new Dimension(w, h));

    }

    private void setCenterPosition() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (d.width - getWidth()) / 2;
        int h = (d.height - getHeight()) / 2;
        setLocation(w - 60, h);
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Create Menu bar
     *
     * @return JMenuBar - menu bar
     */
    //--------------------------------------------------------------------------
    private JMenuBar getMenuBar1() {


        // Create the menu bar
        final JMenuBar menuBar = new JMenuBar();

        // Create a menu
        JMenu file = new JMenu("File");
        JMenu help = new JMenu("Help");

        menuBar.add(file);
        menuBar.add(help);




        JMenuItem _CreateSite = new JMenuItem("Create WebSite");
        _CreateSite.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                tabbedPane.setSelectedIndex(0);
            }
        });
        file.add(_CreateSite);


        JMenuItem _Manage = new JMenuItem("Manage WebSite");
        _Manage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                tabbedPane.setSelectedIndex(1);
            }
        });
        file.add(_Manage);

        JMenuItem _CssEditor = new JMenuItem("Editor Css");
        _CssEditor.setIcon(new ImageIcon("Images" + File.separator + "css" + File.separator + "css_blue.png"));
        _CssEditor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                tabbedPane.setSelectedIndex(2);
            }
        });
        file.add(_CssEditor);


        file.add(new JSeparator());

                JMenuItem _Config = new JMenuItem("Config");
        _Config.setIcon(new ImageIcon("Images" +File.separator + "Utilities.png"));
        _Config.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                DialoConfig d = new DialoConfig(true);
            }
        });
        file.add(_Config);


        file.add(new JSeparator());
        JMenuItem _exit = new JMenuItem("Exit");
        _exit.setIcon(new ImageIcon("Images" + File.separator + "close.png"));
        _exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        file.add(_exit);



        JMenuItem _help = new JMenuItem("Help");
        _help.setIcon(new ImageIcon("Images" + File.separator + "help.png"));
        _help.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
            }
        });
        help.add(_help);

        help.add(new JSeparator());

        JMenuItem _about = new JMenuItem("About");
        _about.setIcon(new ImageIcon("Images" + File.separator + "info.png"));
        _about.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JDialog jDialog1 = new DialogAbout();
            }
        });


        help.add(_about);

        return menuBar;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Execution main
     *
     * @param args the command line arguments
     */
    //--------------------------------------------------------------------------
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

                UIManager.put("swing.boldMetal", Boolean.FALSE);

                new MainFrame();
            }
        });
    }
}
