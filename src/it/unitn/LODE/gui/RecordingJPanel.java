/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RecordingJPanel.java
 *
 * Created on 9-mar-2012, 17.22.15
 */

package it.unitn.LODE.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ronchet
 */
public class RecordingJPanel extends javax.swing.JPanel {

    /** Creates new form RecordingJPanel */
    public RecordingJPanel() {
        JLabel l=new JLabel();
        l.setText("<HTML><FONT size='18' COLOR='RED'>RECORDING</FONT></HTML>");
        this.add(l);
    }

    public static void main(String a[]){
        JFrame f=new JFrame();
        f.setSize(100,100);
        JPanel p=new RecordingJPanel();
        f.getContentPane().add(p);
        f.setVisible(true);
        p.setVisible(true);

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
/*/
}
