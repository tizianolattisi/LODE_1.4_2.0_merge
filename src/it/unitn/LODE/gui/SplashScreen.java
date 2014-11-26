/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.gui;

/**
 *
 * @author ronchet
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


public class SplashScreen extends JWindow
{
   /**
    * Constructor
    * @param icon The icon image to display
    */
   public SplashScreen(ImageIcon icon) {
       JPanel mainPanel = new JPanel(new BorderLayout());
       mainPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
       mainPanel.setBackground(Color.white);

       if(icon != null) {
           JLabel label = new JLabel(icon);
           mainPanel.add(label, BorderLayout.CENTER);
           getContentPane().add(mainPanel);
           int picWidth = icon.getIconWidth();
           int picHeight = icon.getIconHeight();
           centreWindow(this, picWidth, picHeight);
           this.setAlwaysOnTop(true);
           setVisible(true);
           setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       }
   }

   
   /**
    * Close and dispose of the SplashScreen
    */
   public void close() {
       dispose();
   }

   /**
   * Centres a window on the screen given the size of window.
   * It does not show the window.
   * @param window The Window to centre
   * @param width The width of the Window
   * @param height The height of the Window
   */
   private static void centreWindow(Window window, int width, int height) {
     Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
     int screenWidth = screenSize.width / 2;
     int screenHeight = screenSize.height / 2;
     window.setSize(width, height);
     window.setLocation(screenWidth - width / 2, screenHeight - height / 2);
   }

}


