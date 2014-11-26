package it.unitn.lodeWeb.gui;

//--------------------------------------------------------------------------
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
class DialogAbout extends JDialog {

    private static final long serialcarVersionUID = 1L;

    //--------------------------------------------------------------------------
    /**
     *
     * set frame dimensions
     *
     * @param w - frame width
     * @param h - frame height
     *
     * @return void
     */
    //--------------------------------------------------------------------------
    private void setdimFrame(int w, int h) {
        // setta le dimensioni del frame
        Dimension frameSize = new Dimension(w, h);
        setSize(frameSize);

        // centra la finestra
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
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
    public DialogAbout() {

        ImageIcon immagine = new ImageIcon("Images" + File.separator + "logo.jpg");
        this.setIconImage(immagine.getImage());

        JPanel principale = new JPanel();
        principale.setLayout(new BorderLayout());


        JPanel imagePanel = new JPanel();

        JTextArea areaTesto = new JTextArea();
        areaTesto.setFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 16));
        areaTesto.setEditable(false);
        areaTesto.append("Product Version: LODEWeb 1.0\n\n");

        areaTesto.append("Java: \n");
        String[] pro = {
            "java.version", "java.vm.version", "java.runtime.version"
        };

        Properties properties = System.getProperties();
        for (int i = 0; i < pro.length; i++) {
            areaTesto.append("                 " + pro[i] + " : " + properties.getProperty(pro[i]) + "\n");
        }
        areaTesto.append("\nCreated by : Colombari Mattia\n");

        JButton closebutton = new JButton("Close");
        closebutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        imagePanel.setBackground(Color.WHITE);
        imagePanel.add(new JLabel(immagine), BorderLayout.NORTH);
        principale.setBackground(Color.WHITE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(principale);
        principale.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(191, 191, 191).addComponent(closebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(areaTesto, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE).addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(areaTesto, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(closebutton).addContainerGap()));

        this.getContentPane().add(principale);
        this.setVisible(true);

        this.setModal(true);
        this.setdimFrame(456, 456);
        setCenterPosition();
        this.setTitle("Help");
        this.setResizable(false);

        this.pack();

    }
}
