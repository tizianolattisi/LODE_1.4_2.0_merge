package it.unitn.lodeWeb.gui.css.NewCss;

import it.unitn.lodeWeb.gui.css.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class EditPagePanel extends JPanel {

    private JLabel jLabel1;
    private JButton jButton1;
    private JLabel jLabel2;
    private JButton jButton2;
    private JScrollPane jScrollPane;
    private JLabel jLabel3;
    private JButton jButton3;
    private JLabel jLabel4;
    private JComboBox jComboBox1;
    StyleCssClass styleCss;
    private JLabel jLabel5;
    private JButton jButton5;
    private JLabel jLabel6;
    private JCheckBox jCheckBox;
    private MyGraphicsPanel gp;

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param styleCss - StyleCssClass
     */
    //--------------------------------------------------------------------------
    EditPagePanel(final StyleCssClass styleCss) {
        super();

        this.styleCss = styleCss;

        setSize(new Dimension(300, 300));

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("PAGE"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox = new javax.swing.JCheckBox();




        gp = new MyGraphicsPanel(styleCss);
        gp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        gp.repaint(styleCss);
        gp.repaint();


        jLabel1.setText("Background-image");
        jLabel1.setLabelFor(jButton1);
        jButton1.setText("select...");

        class JavaFilter extends javax.swing.filechooser.FileFilter {

            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".png") || f.isDirectory();
            }

            public String getDescription() {
                return "Jpg files (*.jpg)";
            }
        }


        jButton1.addActionListener(new ActionListener() {

            private File selectedfile;

            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                javax.swing.filechooser.FileFilter filter = null;
                filter = fileChooser.getFileFilter();
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setFileFilter(new JavaFilter());
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogTitle("Background-image");

                int result = fileChooser.showOpenDialog(EditPagePanel.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedfile = fileChooser.getSelectedFile();

                    ImageIcon _img = new ImageIcon(selectedfile.getAbsolutePath());

                    styleCss.body.setBGImage(selectedfile.getAbsolutePath());
                    //jTextPane.insertIcon(_img);
                    gp.repaint(styleCss);
                    gp.repaint();
                }
            }
        });

        jLabel2.setText("Background-color");
        jLabel2.setLabelFor(jButton2);
        jButton2.setText("select...");

        jButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditPagePanel.this,
                        "Background-color", Color.cyan);

                if (color != null) {
                    MutableAttributeSet attr = new SimpleAttributeSet();

                    StyleConstants.setForeground(attr, color);

                    styleCss.body.setColorBackground(color);
                    gp.repaint(styleCss);
                    gp.repaint();
                }
            }
        });




        jLabel3.setText("Font");
        jLabel3.setLabelFor(jButton3);
        jButton3.setText("select...");

        jButton3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FontChooser fc = new FontChooser();
                if (fc.getSelectedFont() != null) {
                    styleCss.body.setFont(fc.getSelectedFont());
                    gp.repaint(styleCss);
                    gp.repaint();
                }

            }
        });

        jLabel4.setText("text-align");
        jLabel4.setLabelFor(jComboBox1);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"center", "inherit", "justify", "left", "right"}));

        jComboBox1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                styleCss.body.setAlign(jComboBox1.getSelectedItem().toString());
                gp.repaint(styleCss);
                gp.repaint();
            }
        });

        jLabel5.setText("Container-Color");
        jLabel5.setLabelFor(jButton5);
        jButton5.setText("select...");

        jButton5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditPagePanel.this,
                        "Container-Color", Color.cyan);

                if (color != null) {
                    MutableAttributeSet attr = new SimpleAttributeSet();

                    StyleConstants.setForeground(attr, color);

                    //jTextPane.setBackground(color);
                    styleCss.body.setBGColor_container(color);
                    gp.repaint(styleCss);
                    gp.repaint();
                }
            }
        });


        jLabel6.setText("Border container");
        jLabel5.setLabelFor(jCheckBox);
        jCheckBox.setSelected(true);
        jCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                styleCss.body.setBorderContainer(jCheckBox.isSelected());
                gp.repaint(styleCss);
                gp.repaint();
            }
        });

        jScrollPane.setViewportView(gp);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).
                addGroup(layout.createSequentialGroup().
                addContainerGap().
                addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).
                addGroup(layout.createSequentialGroup().
                addComponent(jLabel1).
                addGap(28, 28, 28).
                addComponent(jButton1)).
                addGroup(layout.createSequentialGroup().
                addComponent(jLabel2).
                addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
                addComponent(jButton2)).
                addGroup(layout.createSequentialGroup().
                addComponent(jLabel5).
                addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
                addComponent(jButton5)).
                addGroup(layout.createSequentialGroup().
                addComponent(jLabel6).
                addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
                addComponent(jCheckBox)).
                addGroup(layout.createSequentialGroup().
                addComponent(jLabel3).
                addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
                addComponent(jButton3)).
                addGroup(layout.createSequentialGroup().
                addComponent(jLabel4).
                addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
                addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).
                addGap(18, 18, 18).
                addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE).
                addContainerGap()));

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).
                addGroup(layout.createSequentialGroup().
                addContainerGap().
                addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).
                addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE).
                addGroup(layout.createSequentialGroup().
                addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).
                addComponent(jLabel1).
                addComponent(jButton1)).
                addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).
                addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).
                addComponent(jLabel2).
                addComponent(jButton2)).
                addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).
                addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).
                addComponent(jLabel5).
                addComponent(jButton5)).
                addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).
                addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).
                addComponent(jLabel6).
                addComponent(jCheckBox)).
                addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).
                addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).
                addComponent(jLabel3).
                addComponent(jButton3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).
                addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).
                addComponent(jLabel4).
                addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).
                addContainerGap()));
    }
}
