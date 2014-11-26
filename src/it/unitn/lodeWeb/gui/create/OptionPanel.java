package it.unitn.lodeWeb.gui.create;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.css.CssPanel;
import it.unitn.lodeWeb.util.Constants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * Option panel
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class OptionPanel extends JPanel implements ActionListener {

    JPanel panelGen;
    JPanel panelcss;
    JComboBox jComboBoxContext;
    private CssPanel cssPanel;
    private JDialog cssDialog;
    public JCheckBox jCheckBox1;
    public JCheckBox jCheckBox2;
    public JCheckBox jCheckBox3;
    public JCheckBox jCheckBox4;
    public JCheckBox jCheckBox5;
    public JCheckBox jCheckBox6;
    private JButton jButtonCss;
    private JLabel jLabelCss;
    // css path
    public String css_path = Constants.dirCss + Constants.cssList[4];
    // password
    private JButton jButtonPsw;
    private JTextField jTextField;
    private JPasswordField jPasswordField;
    private JPanel jPanelPsw;

    //--------------------------------------------------------------------------
    /**
     *
     * Get username
     *
     * @return String - username
     */
    //--------------------------------------------------------------------------
    public String getUsername() {
        if (jTextField != null) {
            return jTextField.getText();
        }
        return null;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Get Password
     *
     * @return String - username
     */
    //--------------------------------------------------------------------------
    public String getPassword() {
        if (jPasswordField != null) {
            return String.valueOf(jPasswordField.getPassword());
        }
        return null;
    }

    //--------------------------------------------------------------------------
    /**
     * Class Constructor
     */
    //--------------------------------------------------------------------------
    public OptionPanel() {

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        setLayout(new BorderLayout(10, 10));
        //----------------------------------------------------------------------
        panelGen = new JPanel();
        panelGen.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));

        jCheckBox1 = new JCheckBox("Images from video");
        jCheckBox2 = new JCheckBox("Lectures distribution");
        jCheckBox3 = new JCheckBox("Zip file lectures");
        jCheckBox4 = new JCheckBox("File notes");
        jCheckBox5 = new JCheckBox("Slides Sources");
        jCheckBox6 = new JCheckBox("Images from slides");
        jCheckBox6.setEnabled(false);
        jCheckBox6.setSelected(false);
        jCheckBox1.setEnabled(false);
        jCheckBox1.setSelected(false);

        jCheckBox2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (jCheckBox6 != null && jCheckBox2 != null) {
                    if (jCheckBox2.isSelected()) {
                        jCheckBox6.setEnabled(true);
                        jCheckBox6.setSelected(true);
                        jCheckBox1.setEnabled(true);
                        jCheckBox1.setSelected(true);
                    } else {
                        jCheckBox6.setEnabled(false);
                        jCheckBox6.setSelected(false);
                        jCheckBox1.setEnabled(false);
                        jCheckBox1.setSelected(false);
                    }

                }
            }
        });



        jTextField = new JTextField(10);
        jTextField.setText(Constants.usernameDefault);
        jTextField.setEditable(false);

        jPasswordField = new JPasswordField(10);
        jPasswordField.setText(Constants.pswDefault);
        jPasswordField.setEditable(false);

        jButtonPsw = new JButton("Edit");
        jButtonPsw.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new Password(jPasswordField, jTextField);
            }
        });

        jPanelPsw = new JPanel();
        jPanelPsw.setBorder(javax.swing.BorderFactory.createTitledBorder("Password"));

        jComboBoxContext = new JComboBox();
        jComboBoxContext.addActionListener(this);
        jComboBoxContext.addItem(Constants.ContextWeb.STATIC);
        jComboBoxContext.addItem(Constants.ContextWeb.DYNAMIC);


        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanelPsw);
        jPanelPsw.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jButtonPsw, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPasswordField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE).addComponent(jTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jButtonPsw, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE).addComponent(jTextField, javax.swing.GroupLayout.Alignment.LEADING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panelGen);
        panelGen.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(24, 24, 24).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox2).addGroup(layout.createSequentialGroup().addGap(12, 12, 12).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox6).addComponent(jCheckBox1)))).addGap(18, 18, 18).addComponent(jPanelPsw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(jCheckBox5).addComponent(jCheckBox4).addComponent(jCheckBox3))).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jComboBoxContext, 0, 422, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jComboBoxContext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jCheckBox2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox4)).addComponent(jPanelPsw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPasswordField.setText("");
        jTextField.setText("");
        for (int i = 0; i < jPanelPsw.getComponents().length; i++) {
            jPanelPsw.getComponents()[i].setEnabled(false);
        }


        this.add(panelGen, BorderLayout.CENTER);
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        panelcss = new JPanel();

        panelcss.setBorder(BorderFactory.createTitledBorder("Css editor"));
        jButtonCss = new JButton("Choose Css");
        jButtonCss.setIcon(new ImageIcon("Images" + File.separator + "css" + File.separator + "css.png"));

        jButtonCss.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // creo il pannello editor css
                cssPanel = new CssPanel();
                // pannello bottoni di conferma
                //--------------------------------------------------------------
                JPanel panelButtonsEditor = new JPanel();
                JButton buttonOk = new JButton("Save");
                buttonOk.setIcon(new ImageIcon("Images" + File.separator + "css" + File.separator + "css_ok.png"));
                buttonOk.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (cssDialog != null) {

                            if (cssPanel.getCssPath() != null) {
                                css_path = cssPanel.getCssPath();
                                jLabelCss.setText(css_path);

                            }
                            cssDialog.dispose();
                        }

                    }
                });
                JButton buttonCancel = new JButton("Cancel");
                buttonCancel.setIcon(new ImageIcon("Images" + File.separator + "css" + File.separator + "css_no.png"));
                buttonCancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (cssDialog != null) {
                            cssDialog.dispose();
                        }
                    }
                });

                panelButtonsEditor.add(buttonOk);
                panelButtonsEditor.add(buttonCancel);

                cssPanel.add(panelButtonsEditor, BorderLayout.SOUTH);

                // creo la finestra Jdialog per l'editor
                createJdialog(cssPanel);
            }
        });

        jLabelCss = new JLabel("Default style :" + Constants.cssList[4]);
        panelcss.add(jButtonCss);
        panelcss.add(jLabelCss);
        this.add(panelcss, BorderLayout.SOUTH);
    //----------------------------------------------------------------------
    }

   

    //--------------------------------------------------------------------------
    /**
     * Create new Jdialog
     *
     * @param p - JPanel contentPane
     */
    //--------------------------------------------------------------------------
    private void createJdialog(JPanel p) {
        cssDialog = new JDialog();
        ImageIcon immagine = new ImageIcon("Images" + File.separator + "css" + File.separator + "css_blue.png");
        cssDialog.setIconImage(immagine.getImage());
        cssDialog.setTitle("Cascading Style Sheet Editor");

        // pannello css
        cssDialog.setContentPane(p);

        cssDialog.setSize(new Dimension(800, 500));
        cssDialog.setMinimumSize(new Dimension(800, 500));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (d.width - cssDialog.getWidth()) / 2;
        int h = (d.height - cssDialog.getHeight()) / 2;
        cssDialog.setLocation(w, h);
        cssDialog.setModal(true);
        cssDialog.setVisible(true);
        cssDialog.pack();

    }

    //--------------------------------------------------------------------------
    /**
     *
     * actionPerformed
     *
     * @param e
     */
    //--------------------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {
        if (jComboBoxContext != null) {
            if (jComboBoxContext.getSelectedItem().equals(Constants.ContextWeb.DYNAMIC)) {
                jCheckBox3.setSelected(false);
                jCheckBox4.setSelected(false);
                jCheckBox3.setEnabled(false);
                jCheckBox4.setEnabled(false);
                for (int i = 0; i < jPanelPsw.getComponents().length; i++) {
                    jPanelPsw.getComponents()[i].setEnabled(true);
                }
                jPasswordField.setText(Constants.pswDefault);
                jTextField.setText(Constants.usernameDefault);

            } else {
                jCheckBox3.setEnabled(true);
                jCheckBox4.setEnabled(true);
                for (int i = 0; i < jPanelPsw.getComponents().length; i++) {
                    jPanelPsw.getComponents()[i].setEnabled(false);
                }
                jPasswordField.setText("");
                jTextField.setText("");
            }
        }

    }
}
