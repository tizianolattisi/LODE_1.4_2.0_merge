package it.unitn.lodeWeb.gui.css.NewCss;

//--------------------------------------------------------------------------
import it.unitn.lodeWeb.gui.css.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class MyGraphicsPanel extends JPanel {

    StyleCssClass style;

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param styleCss
     */
    //--------------------------------------------------------------------------
    public MyGraphicsPanel(StyleCssClass styleCss) {
        this.style = styleCss;
        setSize(700, 700);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        setVisible(true);
    }

    //--------------------------------------------------------------------------
    /**
     *
     * paint graphics
     *
     * @param g
     */
    //--------------------------------------------------------------------------
    @Override
    public void paint(Graphics g) {


        // background
        g.setColor(style.body.getColorBackground());
        g.fillRect(0, 0, 310, 375);
        // font body
        g.setFont(style.body.getFont());
        g.setColor(Color.BLACK);
        g.drawString("text body", 20, 40);
        
        int y_table = 70;
        int x_max = 220;

        // container
        g.setColor(style.body.getBGColor_container());
        g.fillRect(20, y_table - 20, x_max + 40, 300 - 50);
        if (style.body.getBorderContainer()) {
            g.setColor(Color.BLACK);
            g.drawRect(20, y_table - 20, x_max + 40, 300 - 50);
        }

        // header table
        g.setColor(style.table.getColorTableHeader());
        g.fillRect(40, y_table, x_max, 25);
        // border header table
        if (style.table.getBorder()) {
            g.setColor(Color.BLACK);
            g.drawRect(40, y_table, x_max, 25);
        }

        y_table = y_table + 28;

        // body table
        g.setColor(style.table.getColorTableBody());
        g.fillRect(40, y_table, x_max, 50);
        // border body table
        if (style.table.getBorder()) {
            g.setColor(Color.BLACK);
            g.drawRect(40, y_table, x_max, 50);
        }

        y_table = y_table + 53;

        // footer table
        g.setColor(style.table.getColorTableFooter());
        g.fillRect(40, y_table, x_max, 25);
        // boreder footer table
        if (style.table.getBorder()) {
            g.setColor(Color.BLACK);
            g.drawRect(40, y_table, x_max, 25);
        }
        // fonta header table
        g.setFont(style.table.getFontHeader());
        g.setColor(style.table.getColorTxtHeader());
        g.drawString("table header", 45, 90);
        // font body table
        g.setFont(style.table.getFontBody());
        g.setColor(style.table.getColorTxtBody());
        g.drawString("table body", 45, 130);

    }

    //--------------------------------------------------------------------------
    /**
     *
     * repaint graphics
     *
     * @param styleCss
     */
    //--------------------------------------------------------------------------
    public void repaint(StyleCssClass styleCss) {
        this.style = styleCss;
        this.repaint();
    }
}
