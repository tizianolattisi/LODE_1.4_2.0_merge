package it.unitn.lodeWeb.gui.css.NewCss;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.css.StyleCssClass;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class EditOtherPanel extends JPanel {

    private StyleCssClass styleCss;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanelLink;
    private javax.swing.JPanel jPanel2;
    private MyGraphicsPanelTable pg;
    private JLinkButton lb;
    private JCheckBox jCheckBox1;

    //--------------------------------------------------------------------------
    /**
     *
     */
    //--------------------------------------------------------------------------
    class MyGraphicsPanelTable extends JPanel {

        StyleCssClass style;

        //--------------------------------------------------------------------------
        /**
         *
         * Class Constructor
         *
         * @param styleCss - StyleCssClass
         */
        //--------------------------------------------------------------------------
        MyGraphicsPanelTable(StyleCssClass styleCss) {
            this.style = styleCss;
            setBackground(styleCss.body.getBGColor_container());
            setSize(700, 700);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(""),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            setVisible(true);
        }

        //--------------------------------------------------------------------------
        @Override
        /**
         *
         */
        //--------------------------------------------------------------------------
        public void paint(Graphics g) {

            g.setColor(styleCss.body.getBGColor_container());
            g.fillRect(0, 0, 250, 60);
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, 250, 60);

            g.setColor(styleCss.table.getColorTableBody());
            g.fillRect(10, 10, 200, 30);

            if (styleCss.table.getBorderRows()) {
                g.setColor(Color.BLACK);
                g.drawRect(10, 10, 200, 30);
            }

            // font hover rows
            g.setFont(style.table.getFontBody());
            g.setColor(style.table.getColorTxtBody());
            g.drawString("Example text", 15, 30);


            g.setColor(styleCss.table.getColorHoverRows());
            g.fillRect(30, 20, 210, 30);

            if (styleCss.table.getBorderRows()) {
                g.setColor(Color.BLACK);
                g.drawRect(30, 20, 210, 30);
            }

            // font hover rows
            g.setFont(style.table.getFontHoverRows());
            g.setColor(style.table.getColorTextHoverRows());
            g.drawString("Example text", 35, 40);
        }

        //--------------------------------------------------------------------------
        /**
         *
         * @param styleCss
         */
        //--------------------------------------------------------------------------
        public void repaint(StyleCssClass styleCss) {
            this.style = styleCss;
            this.repaint();
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * @param styleCss
     */
    //--------------------------------------------------------------------------
    EditOtherPanel(final StyleCssClass styleCss) {

        super();

        this.styleCss = styleCss;

        setSize(new Dimension(300, 300));

        setLayout(new BorderLayout());


        jPanelLink = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        //----------------
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        //-------------------

        jPanelLink.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("LINK"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        jLabel1.setText("link color");

        jLabel2.setText("link:hover color");

        jButton1.setText("select...");
        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditOtherPanel.this,
                        "Link Color", Color.cyan);
                if (color != null) {
                    styleCss.link.setLinkColor(color);
                    lb.setLinkColor(color);
                }
            }
        });
        jLabel1.setLabelFor(jButton1);

        jButton2.setText("select...");
        jButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditOtherPanel.this,
                        "Link:hover Color", Color.cyan);
                if (color != null) {
                    styleCss.link.setLinkHColor(color);
                    lb.setActiveLinkColor(color);
                }
            }
        });
        jLabel2.setLabelFor(jButton2);

        lb = new JLinkButton("Prova link");
        lb.setFont(styleCss.table.getFontBody());
        lb.setLinkColor(styleCss.link.getLinkColor());

        lb.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
                lb.setLinkColor(styleCss.link.getLinkHColor());
            }

            public void mouseExited(MouseEvent e) {
                lb.setLinkColor(styleCss.link.getLinkColor());

            }
        });



        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanelLink);
        jPanelLink.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel2)).addGap(27, 27, 27).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton2).addComponent(jButton1)).addGap(18, 18, 18).addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jButton1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jButton2)))).addContainerGap()));
        //-------------------------------------------


        pg = new MyGraphicsPanelTable(styleCss);

        jPanel2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("TABLE"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        jLabel3.setText("Table_row:hover Color");

        jButton3.setText("select...");

        jButton3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditOtherPanel.this,
                        "Table_row:hover Color", Color.cyan);
                if (color != null) {
                    styleCss.table.setColorHoverRows(color);
                    pg.repaint(styleCss);
                    pg.repaint();
                }
            }
        });
        jLabel3.setLabelFor(jButton3);

        jCheckBox1.setText("Border rows");
        jCheckBox1.setSelected(styleCss.table.getBorderRows());
        jCheckBox1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (jCheckBox1 != null) {
                    styleCss.table.setBorderRows(jCheckBox1.isSelected());
                    pg.repaint(styleCss);
                    pg.repaint();
                }
            }
        });



        JButton jButton5 = new JButton();
        JLabel jLabel5 = new JLabel();
        jLabel5.setText("Font");
        jLabel5.setLabelFor(jButton5);

        jButton5.setText("select...");
        jButton5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FontChooser fc = new FontChooser();
                if (fc.getSelectedFont() != null) {
                    styleCss.table.setFontHoverRows(fc.getSelectedFont());
                    pg.repaint(styleCss);
                    pg.repaint();
                }

            }
        });

        JButton jButton6 = new JButton();
        JLabel jLabel6 = new JLabel();
        jLabel6.setText("Color text header/footer");
        jLabel6.setLabelFor(jButton6);
        jButton6.setText("select...");
        jButton6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(EditOtherPanel.this,
                        "Color text", Color.cyan);
                if (color != null && pg != null) {
                    styleCss.table.setColorTextHoverRows(color);
                    pg.repaint(styleCss);
                    pg.repaint();
                }
            }
        });




        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(pg);
        pg.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 416, Short.MAX_VALUE));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox1).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton6)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton5)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jLabel3).addGap(29, 29, 29).addComponent(jButton3)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(pg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jButton3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(jButton5)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(jButton6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox1))).addContainerGap(155, Short.MAX_VALUE)));


        add(jPanelLink, BorderLayout.NORTH);
        add(jPanel2, BorderLayout.CENTER);
    }
}
