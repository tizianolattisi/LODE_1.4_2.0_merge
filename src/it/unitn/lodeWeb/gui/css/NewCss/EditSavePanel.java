package it.unitn.lodeWeb.gui.css.NewCss;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.css.*;
import it.unitn.lodeWeb.util.Constants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class EditSavePanel extends JPanel {

    private StyleCssClass styleCss;
    private JLabel jLabel;
    private JTextField jTextField;
    public JTextArea jTextArea;
    private JScrollPane jScrollPane;

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param styleCss
     */
    //--------------------------------------------------------------------------
    EditSavePanel(final StyleCssClass styleCss) {

        super();

        this.styleCss = styleCss;

        setSize(new Dimension(300, 300));

        setLayout(new BorderLayout());

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("SAVE"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        jLabel = new javax.swing.JLabel();
        jTextField = new javax.swing.JTextField();
        jTextArea = new javax.swing.JTextArea();

        jTextArea.setEditable(false);
        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setViewportView(jTextArea);


        jLabel.setText("Css name :");

        jTextField.setText(Constants.myCss);
        jTextField.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel).addComponent(jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE).addContainerGap()));

    }
}
