package it.unitn.lodeWeb.gui;
//--------------------------------------------------------------------------
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import it.unitn.lodeWeb.util.Constants;
import it.unitn.lodeWeb.util.Util;
import java.awt.Dimension;
import java.awt.Toolkit;
import it.unitn.LODE.MP.constants.LODEConstants;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class DialoConfig extends JDialog {

    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldPathVlc;
    //---------------------
    private String pathVLC = LODEConstants.VLC_PATH_ON_WINDOWS;

    private void setCenterPosition() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (d.width - getWidth()) / 2;
        int h = (d.height - getHeight()) / 2;
        setLocation(w, h);
    }

    /**
     *
     * Class Constructor
     *
     */
    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param modal - modal flag
     */
    //--------------------------------------------------------------------------
    DialoConfig(boolean modal) {


        super();

        ImageIcon immagine = new ImageIcon("Images" + File.separator + "Utilities.png");
        setIconImage(immagine.getImage());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButtonSave = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jTextFieldPathVlc = new javax.swing.JTextField();


        jButtonSave.setText("Save and exit");
        jButtonSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                // salvataggio su file
                String content = "";
                if (new File(pathVLC).exists() && new File(pathVLC).isFile()) {
                    content = "pathVLC=\"" + pathVLC + "\"";
                } else {
                    content = "pathVLC=\"" + LODEConstants.VLC_PATH_ON_WINDOWS + "\"";
                }

                Util u = new Util();
                u.writeFile(Constants.pathConfigFile, Constants.configFile, content);


                dispose();
            }
        });
        jButtonClose.setText("Exit");
        jButtonClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        jLabel1.setText("VideoLAN vlc installed path :");

        jButton3.setText("Select..");

        jButton3.addActionListener(new ActionListener() {

            private File selectedfile;

            class JavaFilter extends javax.swing.filechooser.FileFilter {

                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".exe") || f.isDirectory();
                }

                public String getDescription() {
                    return "Vlc exe file (*.exe)";
                }
            }

            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                javax.swing.filechooser.FileFilter filter = null;
                filter = fileChooser.getFileFilter();
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setFileFilter(new JavaFilter());
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogTitle("vlc");

                int result = fileChooser.showOpenDialog(DialoConfig.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedfile = fileChooser.getSelectedFile();
                    ImageIcon _img = new ImageIcon(selectedfile.getAbsolutePath());
                    jTextFieldPathVlc.setText(selectedfile.getPath());
                    pathVLC = (selectedfile.getPath());
                }
            }
        });
        Util util = new Util();
        try {
            jTextFieldPathVlc.setText(util.getMaching(util.leggiFile(Constants.pathConfigFile + File.separator + Constants.configFile), "pathVLC=\"(.*?)\""));
        } catch (IOException ex) {
            jTextFieldPathVlc.setText(this.pathVLC);
        }
        jTextFieldPathVlc.setEditable(false);



        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(18, 18, 18).addComponent(jButton3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jTextFieldPathVlc, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jButton3).addComponent(jTextFieldPathVlc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(258, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jButtonClose).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonSave))).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonSave).addComponent(jButtonClose)).addContainerGap()));

        setSize(new Dimension(600, 400));
        setResizable(false);
        setModal(modal);
        setCenterPosition();
        this.setVisible(true);
        pack();
    }
}
