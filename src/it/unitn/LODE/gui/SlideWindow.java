package it.unitn.LODE.gui;


import it.unitn.LODE.MP.constants.LODEConstants;
import java.awt.*;
import javax.swing.*;
/**
 * The window showing a full-size slide
 */
class SlideWindow extends JFrame {
    private JPanel topPanel;

    public JLabel slide;
    
    private AcquisitionWindow gui;


    public SlideWindow(AcquisitionWindow g) {
        int border=20;
        int window_width=LODEConstants.FULL_SIZE_SLIDE_WIDTH+border;
        int window_heigth=LODEConstants.FULL_SIZE_SLIDE_HEIGTH+border;
        gui=g;
        setTitle("Slide");
        setSize(window_width, window_heigth);
        //setBackground( Color.gray );

        topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(window_width, window_heigth));
        topPanel.setOpaque(true);
        getContentPane().add(topPanel);
        slide = new JLabel();
        topPanel.add(slide);

    }

    public void setSlidePath(String slide_path) {
        ImageIcon slide_icon = gui.__createScaledImageIcon(
                slide_path,
                LODEConstants.FULL_SIZE_SLIDE_WIDTH,
                LODEConstants.FULL_SIZE_SLIDE_HEIGTH);
        slide.setIcon(slide_icon);
    }

    public void closeWindow() {
        //System.exit(0);
        this.dispose();
    }

    public static void main(String args[]) {
        // Create an instance of the test application
        ProgressBar mainFrame = new ProgressBar();
        mainFrame.setVisible(true);
        mainFrame.pack();
    }
}


