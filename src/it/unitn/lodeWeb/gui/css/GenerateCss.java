package it.unitn.lodeWeb.gui.css;

//--------------------------------------------------------------------------
import java.awt.Color;
import javax.lang.model.type.NullType;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class GenerateCss {

    StyleCssClass sCC;

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param sCC - StyleCssClass
     *
     */
    //--------------------------------------------------------------------------
    public GenerateCss(StyleCssClass sCC) {
        if (sCC != null) {
            this.sCC = sCC;
        }
    }

    //--------------------------------------------------------------------------
    /**
     * convert color from Color to a hexadecimal value (html)
     *
     * @param colore
     * @return
     */
    //--------------------------------------------------------------------------
    private String getHtmlColorString(Color colore) {
        return (Integer.toHexString(colore.getRGB() & 0x00ffffff)).toString();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * return css String
     * 
     * @return String - css
     */
    //--------------------------------------------------------------------------
    public String GetCss() {


        String[] fontStyles = {
            "font-style: PLAIN;",
            "font-weight: BOLD;",
            "font-style: ITALIC;",
            "font-style: italic;font-weight: bold;"};


        String css =
                "html, body {\n";
        if (sCC.body.getFont() != null) {
            css = css +
                    "    font-family: " + sCC.body.getFont().getFamily() + ";\n" +
                    "    font-size: " + sCC.body.getFont().getSize() + "px;\n";
        }
        css = css +
                // per immagine di sfondo
                //"background: url(Img/blue-white.jpg) repeat-x;"+
                "    background-color: #" + getHtmlColorString(sCC.body.getColorBackground()) + ";\n" +
                "    text-align: " + sCC.body.getAlign() + ";\n";
        try {
            if (sCC.body.getFont().getStyle() > 0 && sCC.body.getFont().getStyle() <= 3) {
                css = css + "  " + fontStyles[sCC.body.getFont().getStyle()] + "\n";
            }
        } catch (Exception e) {
        }

        css = css + "}\n";
        // LINK
        if (sCC.link.getLinkColor() != null) {
            css = css +
                    "a {\n" +
                    "    color : #" + getHtmlColorString(sCC.link.getLinkColor()) + ";\n" +
                    "}\n";
        }
        if (sCC.link.getLinkHColor() != null) {
            css = css +
                    "a:hover {\n" +
                    "    color: #" + getHtmlColorString(sCC.link.getLinkHColor()) + ";\n" +
                    "}\n";
        }
        //----------------------
        // messaggi errrore
        css = css + "#error {" + "\n" +
                "    text-align : center;" + "\n" +
                "    width : 100%;" + "\n" +
                "    margin : auto;" + "\n" +
                "    top : 20px;" + "\n" +
                "    position : relative;" + "\n" +
                "    border-width: 0px;" + "\n" +
                "    border-style: solid;" + "\n" +
                "    border-color: #000000;" + "\n" +
                "    background-color: #ff0022;" + "\n" +
                "    color: #fefefe;" + "\n" +
                "}" + "\n";
        //------------------------
        // css per menu lista link
        css = css +
                "#navigation ul" + "\n" +
                "{" + "\n" +
                "display: inline" + "\n" +
                "	list-style: none;" + "\n" +
                "	margin: 4px;" + "\n" +
                "	padding: 0;" + "\n" +
                "}" + "\n" +
                "#navigation ul li" + "\n" +
                "{" + "\n" +
                "    display: inline;" + "\n" +
                "}" + "\n" +
                "#navigation li a" + "\n" +
                "{" + "\n" +
                "	font-size: 15px;" + "\n" +
                "	line-height: 16px;" + "\n" +
                "	display: inline;" + "\n" +
                "	padding-left: 10px;" + "\n" +
                "	background-position:left;" + "\n" +
                "	background-repeat:no-repeat;" + "\n" +
                "	text-decoration: none;" + "\n" +
                "	" + "\n" +
                "}" + "\n" +
                "li.selected " + "\n" +
                "{" + "\n" +
                "	font-size: 10px;" + "\n" +
                "	line-height: 16px;" + "\n" +
                "	display: inline;" + "\n" +
                "	padding-left: 10px;" + "\n" +
                "	background-position:left;" + "\n" +
                "	background-repeat:no-repeat;" + "\n" +
                "	text-decoration: none;" + "\n" +
                "	color: #0099cc;" + "\n" +
                "}" + "\n" +
                "" + "\n" +
                "#navigation li a:hover" + "\n" +
                "{" + "\n" +
                "	padding-left: 10px;" + "\n" +
                "	background-position:left;" + "\n" +
                "	background-repeat:no-repeat;" + "\n" +
                "	color: #0099cc;" + "\n" +
                "	text-decoration: underline;" + "\n" +
                "}\n";
        //------------------------
        css = css +
                "#container {\n" +
                "    text-align : " + sCC.body.getAlign() + ";\n" +
                "    width : 1100px;\n" +
                "    height : 100%;\n" +
                "    margin : auto;\n" +
                "    top : 20px;\n" +
                "    position : relative;\n" +
                "    padding : 10px 10px 100px 10px;\n";
        if (sCC.body.getBorderContainer()) {
            css = css +
                    "    border-width: 1px;\n" +
                    "    border-style: solid;\n" +
                    "    border-color: #000000;\n";
        }

        css = css +
                "    background-color: #" + getHtmlColorString(sCC.body.getBGColor_container()) + ";\n" +
                "}\n" +
                "#header{\n" +
                //"    color : black;\n" +
                "    position : relative;\n" +
                "    margin : auto;\n" +
                "    width : 1100px;\n" +
                "    height : 60px;\n" +
                "}\n" +
                "#footer{\n" +
                //"    color : black;\n" +
                "    position : absolute;\n" +
                "    bottom : 20px;\n" +
                "    width : 1100px;\n" +
                "    height : 50px;\n" +
                "}\n" +
                "#content {\n" +
                "    position : relative;\n" +
                "    margin : auto;\n" +
                "    left : 0;\n" +
                "    width : 1060px;\n" +
                "    padding : 20px;\n" +
                "}\n" +
                "#infoTable{\n";
        if (sCC.body.getFont() != null) {
            css = css +
                    "    font-family: " + sCC.body.getFont().getFamily() + ";\n" +
                    "    font-size: 20px;\n";
        } else {
            css = css +
                    "    font-family: \"Lucida Sans Unicode\", \"Lucida Grande\", Sans-Serif;\n" +
                    "    font-size: 20px;\n";
        }
        css = css +
                "    width: 50%;" +
                "    margin : auto;" +
                "    text-align: " + sCC.table.getAlign() + ";" +
                "    border-collapse: collapse;" +
                "}" +
                "#infoTable td" +
                "{" +
                "    background-color: #" + getHtmlColorString(sCC.table.getColorTableBody()) + ";\n" +
                "    border-top: 1px solid #fff;\n" +
                "    color: #" + getHtmlColorString(sCC.table.getColorTxtBody()) + ";\n" +
                "}" +
                "#table{\n";
        if (sCC.body.getFont() != null) {
            css = css +
                    "    font-family: " + sCC.body.getFont().getFamily() + ";\n" +
                    "    font-size: " + sCC.body.getFont().getSize() + "px;\n";
        } else {
            css = css +
                    "    font-family: \"Lucida Sans Unicode\", \"Lucida Grande\", Sans-Serif;\n" +
                    "    font-size: 12px;\n";
        }
        css = css +
                "    margin : auto;\n" +
                "    width: 100%;\n" +
                "    text-align: " + sCC.table.getAlign() + ";\n" +
                "    border-collapse: collapse;\n";
        if (sCC.table.getBorder()) {
            css = css +
                    "    border-width: 1px;\n" +
                    "    border-style: solid;\n" +
                    "    border-color: #000000;\n";
        }

        css = css + "}\n" +
                "#tableDownload{\n";
        if (sCC.body.getFont() != null) {
            css = css + "    font-family: " + sCC.body.getFont().getFamily() + ";\n" +
                    "    font-size: " + sCC.body.getFont().getSize() + "px;\n";
        } else {
            css = css + "    font-family: \"Lucida Sans Unicode\", \"Lucida Grande\", Sans-Serif;\n" +
                    "    font-size: 12px;\n";
        }
        css = css +
                "    margin : auto;\n" +
                "    width: 20%;\n" +
                "    text-align: " + sCC.table.getAlign() + ";\n" +
                "    border-collapse: collapse;\n";
        if (sCC.table.getBorder()) {
            css = css +
                    "    border-width: 1px;\n" +
                    "    border-style: solid;\n" +
                    "    border-color: #000000;\n";
        }

        css = css + "}\n" +
                "#table thead th.top-left{\n" +
                "    background-color: #" + getHtmlColorString(sCC.table.getColorTableHeader()) + ";\n" +
                //"    background: #b9c9fe url('Img/left.png') left -1px no-repeat;\n" +
                "}\n" +
                "#table thead th.top-right{\n" +
                "    background-color: #" + getHtmlColorString(sCC.table.getColorTableHeader()) + ";\n" +
                //"    background: #b9c9fe url('Img/right.png') right -1px no-repeat;\n" +
                "}\n" +
                // TABLE HEADER
                "#table th{\n" +
                "    padding: 8px;\n" +
                "    font-weight: normal;\n";

        if (sCC.table.getFontHeader() != null) {
            css = css + "    font-family: " + sCC.table.getFontHeader().getFamily() + ";\n" +
                    "    font-size: " + sCC.table.getFontHeader().getSize() + "px;\n";
        } else {
            css = css + "    font-family: \"Lucida Sans Unicode\", \"Lucida Grande\", Sans-Serif;\n" +
                    "    font-size: 12px;\n";
        }
        try {
            if (sCC.table.getFontHeader().getStyle() > 0 && sCC.table.getFontHeader().getStyle() <= 3) {
                css = css + "  " + fontStyles[sCC.table.getFontHeader().getStyle()] + "\n";
            }
        } catch (Exception e) {
        }
        css = css +
                "    color: #" + getHtmlColorString(sCC.table.getColorTxtHeader()) + ";\n" +
                "    background-color: #" + getHtmlColorString(sCC.table.getColorTableHeader()) + ";\n" +
                "}\n" +
                // TABLE BODY
                "#table td{\n" +
                "    padding: 8px;\n";

        if (sCC.table.getFontBody() != null) {
            css = css + "    font-family: " + sCC.table.getFontBody().getFamily() + ";\n" +
                    "    font-size: " + sCC.table.getFontBody().getSize() + "px;\n";
        } else {
            css = css + "    font-family: \"Lucida Sans Unicode\", \"Lucida Grande\", Sans-Serif;\n" +
                    "    font-size: 12px;\n";
        }
        try {
            if (sCC.table.getFontBody().getStyle() > 0 && sCC.table.getFontBody().getStyle() <= 3) {
                css = css + "  " + fontStyles[sCC.table.getFontBody().getStyle()] + "\n";
            }
        } catch (Exception e) {
        }

        css = css +
                "    color: #" + getHtmlColorString(sCC.table.getColorTxtBody()) + ";\n" +
                "    background-color: #" + getHtmlColorString(sCC.table.getColorTableBody()) + ";\n" +
                "    border-top: 1px solid #fff;\n" +
                "}\n" +
                "#table tfoot td{\n" +
                "    background-color: #" + getHtmlColorString(sCC.table.getColorTableFooter()) + ";\n" +
                //"    background: #"+getHtmlColorString(sCC.table.getColorTableFooter())+" url('Img/botleft.png') left bottom no-repeat;\n" +
                "}\n" +
                "#table tfoot td.foot-left{\n" +
                "    background-color: #" + getHtmlColorString(sCC.table.getColorTableFooter()) + ";\n" +
                //"    background: #"+getHtmlColorString(sCC.table.getColorTableFooter())+" url('Img/botleft.png') left bottom no-repeat;\n" +
                "}\n" +
                "#table tfoot td.foot-right{\n" +
                "    background-color: #" + getHtmlColorString(sCC.table.getColorTableFooter()) + ";\n" +
                //"    background: #"+getHtmlColorString(sCC.table.getColorTableFooter())+" url('Img/botright.png') right bottom no-repeat;\n" +
                "}\n" +
                "#table tbody td{\n";
        if (sCC.table.getBorderRows()) {
            css = css +
                    "    border-width: 1px;\n" +
                    "    border-style: solid;\n" +
                    "    border-color: #000000;\n";
        }
        css = css + "}\n" +
                "#table tbody tr:hover td{\n" +
                "    background-color: #" + getHtmlColorString(sCC.table.getColorHoverRows()) + ";\n" +
                "    color: #" + getHtmlColorString(sCC.table.getColorTextHoverRows()) + ";\n";
        if (sCC.table.getBorderRows()) {
            css = css +
                    "    border-width: 1px;\n" +
                    "    border-style: solid;\n" +
                    "    border-color: #000000;\n";
        }
        if (sCC.table.getFontHoverRows() != null) {
            css = css + "    font-family: " + sCC.table.getFontHoverRows().getFamily() + ";\n" +
                    "    font-size: " + sCC.table.getFontHoverRows().getSize() + "px;\n";
        } else {
            css = css + "    font-family: \"Lucida Sans Unicode\", \"Lucida Grande\", Sans-Serif;\n" +
                    "    font-size: 12px;\n";
        }
        try {
            if (sCC.table.getFontHoverRows().getStyle() > 0 && sCC.table.getFontHoverRows().getStyle() <= 3) {
                css = css + "  " + fontStyles[sCC.table.getFontHoverRows().getStyle()] + "\n";
            }
        } catch (Exception e) {
        }

        css = css + "}\n";

        return css;
    }
}
