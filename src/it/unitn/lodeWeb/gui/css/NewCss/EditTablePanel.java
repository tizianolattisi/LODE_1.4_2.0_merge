package it.unitn.lodeWeb.gui.css.NewCss;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.css.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class EditTablePanel extends JPanel {

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private MyGraphicsPanel gp;
    StyleCssClass styleCss;
    private JSeparator jSeparator1;
    private JCheckBox jCheckBox1;
    private JLabel jLabel4;
    private JButton jButton4;
    private JLabel jLabel5;
    private JButton jButton5;
    private JScrollPane jScrollPane;
    private JLabel jLabel6;
    private JButton jButton6;
    private JLabel jLabel7;
    private JButton jButton7;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JLabel jLabel8;
    private JComboBox jComboBox1;

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param styleCss
     */
    //--------------------------------------------------------------------------
    EditTablePanel(final StyleCssClass styleCss) {

        super();

        this.styleCss = styleCss;


        setSize(new Dimension(300, 300));

        setLayout(new BorderLayout());

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("TABLE"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        jScrollPane = new javax.swing.JScrollPane();

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jLabel4 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        jLabel5 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        jLabel6 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();

        jLabel7 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();


        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();

        jSeparator1 = new javax.swing.JSeparator();
        jCheckBox1 = new javax.swing.JCheckBox();

        jTextField1 = new javax.swing.JTextField("table header");
        jTextField2 = new javax.swing.JTextField("table body");

        jCheckBox1.setText("Border");
        jCheckBox1.setSelected(true);

        jCheckBox1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (jCheckBox1 != null && gp != null) {
                    styleCss.table.setBorder(jCheckBox1.isSelected());
                    gp.repaint(styleCss);
                    gp.repaint();
                }


            }
        });

        jLabel1.setText("Color header");
        jLabel1.setLabelFor(jButton1);
        jLabel2.setText("Color body");
        jLabel2.setLabelFor(jButton2);
        jLabel3.setText("Color footer");
        jLabel3.setLabelFor(jButton3);
        jButton1.setText("select...");
        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditTablePanel.this,
                        "Color header", Color.cyan);

                if (color != null && gp != null) {
                    styleCss.table.setColorTableHeader(color);
                    jTextField1.setBackground(color);
                    gp.repaint(styleCss);
                    gp.repaint();
                }
            }
        });

        jButton2.setText("select...");
        jButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditTablePanel.this,
                        "Color body", Color.cyan);

                if (color != null && gp != null) {
                    styleCss.table.setColorTableBody(color);
                    jTextField2.setBackground(color);
                    gp.repaint(styleCss);
                    gp.repaint();
                }
            }
        });

        jButton3.setText("select...");
        jButton3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditTablePanel.this,
                        "Color footer", Color.cyan);

                if (color != null && gp != null) {
                    styleCss.table.setColorTableFooter(color);
                    gp.repaint(styleCss);
                    gp.repaint();
                }
            }
        });


        jLabel4.setText("Font header/footer");
        jLabel4.setLabelFor(jButton4);
        jButton4.setText("select...");
        jButton4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FontChooser fc = new FontChooser();
                if (fc.getSelectedFont() != null) {
                    styleCss.table.setFontHeader(fc.getSelectedFont());
                    jTextField1.setFont(fc.getSelectedFont());
                    gp.repaint(styleCss);
                    gp.repaint();
                }

            }
        });

        jLabel5.setText("Font body");
        jLabel5.setLabelFor(jButton5);
        jButton5.setText("select...");
        jButton5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FontChooser fc = new FontChooser();
                if (fc.getSelectedFont() != null) {
                    styleCss.table.setFontBody(fc.getSelectedFont());
                    jTextField2.setFont(fc.getSelectedFont());
                    gp.repaint(styleCss);
                    gp.repaint();
                }

            }
        });


        jLabel6.setText("Color text header/footer");
        jLabel6.setLabelFor(jButton6);
        jButton6.setText("select...");
        jButton6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditTablePanel.this,
                        "Color text header/footer", Color.cyan);
                if (color != null && gp != null) {
                    styleCss.table.setColorTxtHeader(color);
                    jTextField1.setForeground(color);
                    gp.repaint(styleCss);
                    gp.repaint();
                }
            }
        });

        jLabel7.setText("Color text body");
        jLabel7.setLabelFor(jButton7);
        jButton7.setText("select...");
        jButton7.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditTablePanel.this,
                        "Color text body", Color.cyan);
                if (color != null && gp != null) {
                    styleCss.table.setColorTxtBody(color);
                    jTextField2.setForeground(color);
                    gp.repaint(styleCss);
                    gp.repaint();
                }
            }
        });

        jLabel8.setText("text-align table");
        jLabel8.setLabelFor(jComboBox1);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"center", "inherit", "justify", "left", "right"}));

        jComboBox1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                styleCss.table.setAlign(jComboBox1.getSelectedItem().toString());
                gp.repaint(styleCss);
                gp.repaint();
            }
        });



        //styleCss = new StyleCssClass(Color.BLUE, Color.getColor("e8edff"), Color.getColor("d0dafd"), Color.WHITE);

        gp = new MyGraphicsPanel(styleCss);
        gp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        gp.repaint(styleCss);
        gp.repaint();

        jScrollPane.setViewportView(gp);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton3)).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton1)).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton2)).addComponent(jCheckBox1).addGroup(layout.createSequentialGroup().addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton4)).addGroup(layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton5)).addGroup(layout.createSequentialGroup().addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton6)).addGroup(layout.createSequentialGroup().addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton7))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE).addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE).addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jButton1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jButton2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jButton3)).addGap(18, 18, 18).addComponent(jCheckBox1).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jButton4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(jButton5)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(jButton6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(jButton7))).addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }
}
