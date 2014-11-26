package it.unitn.lodeWeb.gui.css.NewCss;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.css.*;
import it.unitn.lodeWeb.util.Constants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class NewCssPanel extends JDialog implements ActionListener {

    private JButton buttonNext;
    private JButton buttonBack;
    //----------------------
    // pannelli
    private EditPagePanel pagePanel = null;
    private EditTablePanel tablePanel = null;
    private EditSavePanel savePanel = null;
    private EditOtherPanel otherPanel = null;
    // navigazione
    private JPanel[] arrayPanel = null;
    private int currentPanelIndex = 0;
    private static int maxPanel = 4;
    private JPanel currentPanel = null;
    // classe OUTPUT DI STILE CHE VIENE MODIFICATO E SCAMBIATO TRA I VARI PANNELLI
    //--------------------------
    StyleCssClass styleCss;
    //---------------------------
    String returnCss;

    //--------------------------------------------------------------------------
    /**
     *
     * show current panel
     *
     */
    //--------------------------------------------------------------------------
    private void showPanel() {
        // ottengo il css data la classe e lo stampo nella text area
        savePanel.jTextArea.setText(new GenerateCss(styleCss).GetCss());

        arrayPanel[currentPanelIndex].setVisible(true);
        currentPanel = arrayPanel[currentPanelIndex];
        this.add(currentPanel, BorderLayout.CENTER);
        this.repaint();

        currentPanel.repaint();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * exit New css panel 
     *
     * @param returnCss
     */
    //--------------------------------------------------------------------------
    private void exit(String returnCss) {
        this.returnCss = returnCss;
        styleCss = null;
        this.dispose();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * return css choosed
     *
     * @return String - css
     */
    //--------------------------------------------------------------------------
    public String getCss() {
        return this.returnCss;
    }


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
     */
    //--------------------------------------------------------------------------
    public NewCssPanel() {


        ImageIcon immagine = new ImageIcon("Images" + File.separator + "css" + File.separator + "css_add.png");
        setIconImage(immagine.getImage());

        setTitle("New Css");

        setSize(600, 500);
        setCenterPosition();
        setResizable(false);
        setModal(true);
        setAlwaysOnTop(true);

        buttonNext = new JButton("Next");
        buttonNext.addActionListener(this);
        buttonBack = new JButton("Back");
        buttonBack.addActionListener(this);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(buttonBack);

        buttonPanel.add(buttonNext);

        if (currentPanelIndex == 0) {
            buttonBack.setEnabled(false);
        }

        styleCss = new StyleCssClass();

        pagePanel = new EditPagePanel(styleCss);
        tablePanel = new EditTablePanel(styleCss);
        otherPanel = new EditOtherPanel(styleCss);
        savePanel = new EditSavePanel(styleCss);


        arrayPanel = new JPanel[maxPanel];
        arrayPanel[0] = pagePanel;
        arrayPanel[1] = tablePanel;
        arrayPanel[2] = otherPanel;
        arrayPanel[3] = savePanel;


        currentPanel = arrayPanel[currentPanelIndex];
        this.add(currentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    //--------------------------------------------------------------------------
    /**
     *
     * actionPerformed
     *
     * @param e - ActionEvent
     *
     */
    //--------------------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {

        if (((JButton) (e.getSource())).getText().equals("Next")) {
            if (currentPanelIndex >= 0 && currentPanelIndex < maxPanel) {
                arrayPanel[currentPanelIndex].setVisible(false);
                currentPanelIndex++;
            }
        } else if (((JButton) (e.getSource())).getText().equals("Back")) {
            if (currentPanelIndex > 0 && currentPanelIndex <= maxPanel) {
                arrayPanel[currentPanelIndex].setVisible(false);
                currentPanelIndex--;
            }
        }
        showPanel();


        if (currentPanelIndex == 0) {
            buttonBack.setEnabled(false);
        } else {
            buttonBack.setEnabled(true);
        }
        if (currentPanelIndex == (maxPanel - 1)) {
            buttonNext.setText("OK");
            buttonNext.removeActionListener(this);
            buttonNext.addActionListener(new ActionListener() {

                private FileWriter filestream;
                private BufferedWriter output;

                public void actionPerformed(ActionEvent e) {
                    if (savePanel.jTextArea != null) {
                        if (!savePanel.jTextArea.getText().equals("")) {
                            try {
                                filestream = new FileWriter(Constants.dirCss + Constants.myCss);
                                output = new BufferedWriter(filestream);
                                output.write(savePanel.jTextArea.getText());
                                output.close();
                                exit(Constants.dirCss + Constants.myCss);
                            } catch (IOException ex) {
                                System.out.print("errore: creazione del file css");
                            }
                        }
                    }
                }
            });
            buttonBack.setText("Cancel");
            buttonBack.removeActionListener(this);
            buttonBack.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    exit(null);
                }
            });
        }

    }
}
