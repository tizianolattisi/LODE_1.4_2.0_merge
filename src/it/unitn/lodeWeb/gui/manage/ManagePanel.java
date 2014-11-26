package it.unitn.lodeWeb.gui.manage;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.manage.notes.ViewNotesPanel;
import it.unitn.lodeWeb.gui.ChooserPanel;
import it.unitn.lodeWeb.gui.manage.css.EditCss;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class ManagePanel extends JPanel {

    private JTabbedPane tabbedPaneManager;
    private ViewNotesPanel vnp;
    private EditCss edcss;
    public ChooserPanel chooserPanel;

    //--------------------------------------------------------------------------
    /**
     *
     * Get chooser panel (ChooserPanel class)
     *
     * @return JPanel - ChooserPanel
     */
    //--------------------------------------------------------------------------
    public ChooserPanel getChooserPanel() {
        return this.chooserPanel;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     */
    //--------------------------------------------------------------------------
    public ManagePanel() {

        super(new BorderLayout());

        setSize(650, 700);


        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        chooserPanel = new ChooserPanel();

        chooserPanel.jLabel2.setText("Web dir");

        chooserPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        add(chooserPanel, BorderLayout.NORTH);

        vnp = new ViewNotesPanel();
        vnp.setChooser(chooserPanel);

        edcss = new EditCss();
        edcss.setChooser(chooserPanel);

        tabbedPaneManager = new JTabbedPane();
        tabbedPaneManager.addTab(" Editor notes", new ImageIcon("Images"+ File.separator + "editNote.png"), vnp, "");
        tabbedPaneManager.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPaneManager.addTab(" Css", new ImageIcon("Images"+ File.separator + "css"+ File.separator + "css.png"), edcss, "");
        tabbedPaneManager.setMnemonicAt(1, KeyEvent.VK_2);


        add(tabbedPaneManager, BorderLayout.CENTER);


    }

    //--------------------------------------------------------------------------
    /**
     *
     */
    //--------------------------------------------------------------------------
    class HyperLinkViewer extends JEditorPane implements HyperlinkListener {

        //--------------------------------------------------------------------------
        /**
         * Class Constructor
         */
        //--------------------------------------------------------------------------
        public HyperLinkViewer() {
            super();
            addHyperlinkListener(this);
        }

        //--------------------------------------------------------------------------
        /**
         *
         * @param e - HyperlinkEvent
         */
        //--------------------------------------------------------------------------
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {

                if (e instanceof HTMLFrameHyperlinkEvent) {
                    HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                    HTMLDocument doc = (HTMLDocument) this.getDocument();
                    doc.processHTMLFrameHyperlinkEvent(evt);
                } else {
                    try {
                        setPage(e.getURL());
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    }
}
