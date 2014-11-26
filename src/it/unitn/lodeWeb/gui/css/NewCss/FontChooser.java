package it.unitn.lodeWeb.gui.css.NewCss;

//--------------------------------------------------------------------------
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class FontChooser extends JDialog {

    protected Font Font;
    protected String Name;
    protected int Size;
    protected boolean isBold;
    protected boolean isItalic;
    protected String ListFont[];
    protected List fontNameList;
    protected List fontSizeList;
    JCheckBox bold, italic;
    protected String fontSizes[] = {"8", "10", "11", "12", "14", "16", "18",
        "20", "24", "30", "36", "40", "48", "60", "72"};
    protected static final int DEFAULT_SIZE = 4;

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
    FontChooser() {

        super();

        setModal(true);
        setAlwaysOnTop(true);

        setSize(new Dimension(400, 220));
        setCenterPosition();

        setResizable(false);

        JPanel top = new JPanel();

        top.setLayout(new FlowLayout());

        fontNameList = new List(8);
        top.add(fontNameList);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        ListFont = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (int i = 0; i < ListFont.length; i++) {
            fontNameList.add(ListFont[i]);
        }
        fontNameList.select(0);

        fontSizeList = new List(8);
        top.add(fontSizeList);

        for (int i = 0; i < fontSizes.length; i++) {
            fontSizeList.add(fontSizes[i]);
        }
        fontSizeList.select(DEFAULT_SIZE);

        add(top, BorderLayout.NORTH);

        JPanel attrs = new JPanel();
        top.add(attrs);
        attrs.setLayout(new GridLayout(0, 1));
        attrs.add(bold = new JCheckBox("Bold", false));
        attrs.add(italic = new JCheckBox("Italic", false));

        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Apply");
        buttonPanel.add(okButton);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Name = fontNameList.getSelectedItem();
                String resultSizeName = fontSizeList.getSelectedItem();
                int resultSize = Integer.parseInt(resultSizeName);
                isBold = bold.isSelected();
                isItalic = italic.isSelected();
                int attrs = Font.PLAIN;
                if (isBold) {
                    attrs = Font.BOLD;
                }
                if (isItalic) {
                    attrs |= Font.ITALIC;
                }
                Font = new Font(Name, attrs, resultSize);

                dispose();
                setVisible(false);
            }
        });



        JButton canButton = new JButton("Cancel");
        buttonPanel.add(canButton);
        canButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Set all values to null. Better: restore previous.
                Font = null;
                Name = null;
                Size = 0;
                isBold = false;
                isItalic = false;
                dispose();
                setVisible(false);
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);

    }

    //--------------------------------------------------------------------------
    /**
     *
     * get name font
     *
     * @return String - name font
     */
    //--------------------------------------------------------------------------
    public String getSelectedName() {
        return Name;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * get selected text size
     *
     * @return int - text size
     */
    //--------------------------------------------------------------------------
    public int getSelectedSize() {
        return Size;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Get selected Font
     *
     * @return Font - text font
     */
    //--------------------------------------------------------------------------
    public Font getSelectedFont() {
        return Font;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * get is bolt
     *
     * @return boolean -
     */
    //--------------------------------------------------------------------------
    public boolean getIsBolt() {
        return isBold;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * get is italic
     *
     * @return boolean -
     */
    //--------------------------------------------------------------------------
    public boolean getIsItalic() {
        return isItalic;
    }
}
