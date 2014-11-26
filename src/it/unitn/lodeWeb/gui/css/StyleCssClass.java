package it.unitn.lodeWeb.gui.css;

//--------------------------------------------------------------------------
import java.awt.Color;
import java.awt.Font;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * class contains the  css properties
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class StyleCssClass {

    public Table table;
    public Body body;
    public Link link;

    //--------------------------------------------------------------------------
    /**
     *
     */
    //--------------------------------------------------------------------------
    public class Link {

        private Color link = null;
        private Color link_hover = null;

        //--------------------------------------------------------------------------
        /**
         *
         * Class Constructor
         *
         */
        //--------------------------------------------------------------------------
        Link() {
        }

        /**
         *
         *
         * @param c
         */
        public void setLinkColor(Color c) {
            this.link = c;
        }

        /**
         *
         * @param c - Color
         */
        public void setLinkHColor(Color c) {
            this.link_hover = c;
        }

        /**
         *
         * @return Color
         */
        public Color getLinkColor() {
            return this.link;
        }

        /**
         *
         * @return Color
         */
        public Color getLinkHColor() {
            return this.link_hover;
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     */
    //--------------------------------------------------------------------------
    public class Body {

        private Color background = Color.WHITE;
        private String background_image;
        private Font font = null;
        private String textAlign = "left";
        private Color BGColor_container = Color.WHITE;
        private boolean border_container = true;

        //--------------------------------------------------------------------------
        /**
         *
         * Class Constructor
         *
         */
        //--------------------------------------------------------------------------
        Body() {
        }

        /**
         *
         * @return String
         */
        public String getAlign() {
            return this.textAlign;
        }

        /**
         *
         * @param al - String
         */
        public void setAlign(String al) {
            this.textAlign = al;
        }

        /**
         *
         * @return Color
         */
        public Color getColorBackground() {
            return this.background;
        }

        /**
         *
         * @param c - Color
         */
        public void setColorBackground(Color c) {
            this.background = c;
        }

        /**
         *
         * @return String
         */
        public String getBGImage() {
            return this.background_image;
        }

        /**
         *
         * @param image_path - String
         */
        public void setBGImage(String image_path) {
            this.background_image = image_path;
        }

        /**
         *
         * @return Font
         */
        public Font getFont() {
            return this.font;
        }

        /**
         *
         * @param c Font
         */
        public void setFont(Font c) {
            this.font = c;
        }

        /**
         *
         * @return Color
         */
        public Color getBGColor_container() {
            return this.BGColor_container;
        }

        /**
         *
         * @param c Color
         */
        public void setBGColor_container(Color c) {
            this.BGColor_container = c;
        }

        /**
         *
         * @return boolean
         */
        public boolean getBorderContainer() {
            return this.border_container;
        }

        /**
         *
         * @param flag boolean
         */
        public void setBorderContainer(boolean flag) {
            this.border_container = flag;
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     */
    //--------------------------------------------------------------------------
    public class Table {

        private Color table_header_color = Color.WHITE;
        private Color table_footer_color = Color.WHITE;
        private Color table_body_color = Color.WHITE;
        private Font font_header;
        private Font font_body;
        private Color color_text_header = Color.BLACK;
        private Color color_text_body = Color.BLACK;
        private Color color_hover_rows = Color.WHITE;
        private Font font_text_hover_rows;
        private Color color_text_hover_rows = Color.BLACK;
        private boolean border = true;
        private boolean border_row = true;
        private String textAlign = "left";

        //--------------------------------------------------------------------------
        /**
         *
         * Class Constructor
         *
         */
        //--------------------------------------------------------------------------
        Table() {
        }

        /**
         *
         * @return Font
         */
        public Font getFontHoverRows() {
            return this.font_text_hover_rows;
        }

        /**
         *
         * @param f - Font
         */
        public void setFontHoverRows(Font f) {
            this.font_text_hover_rows = f;
        }

        /**
         *
         * @return Color
         */
        public Color getColorTextHoverRows() {
            return this.color_text_hover_rows;
        }

        /**
         *
         * @param c - Color
         */
        public void setColorTextHoverRows(Color c) {
            this.color_text_hover_rows = c;
        }

        /**
         *
         * @return - String
         */
        public String getAlign() {
            return this.textAlign;
        }

        /**
         *
         * @param al - String
         */
        public void setAlign(String al) {
            this.textAlign = al;
        }

        /**
         *
         * @return - Color
         */
        public Color getColorTableHeader() {
            return this.table_header_color;
        }

        /**
         *
         * @return - Color
         */
        public Color getColorTableFooter() {
            return this.table_footer_color;
        }

        /**
         *
         * @return -Color
         */
        public Color getColorTableBody() {
            return this.table_body_color;
        }

        /**
         *
         * @return - boolean
         */
        public boolean getBorder() {
            return this.border;
        }

        /**
         *
         * @return - Font
         */
        public Font getFontHeader() {
            return this.font_header;
        }

        /**
         *
         * @return Font
         */
        public Font getFontBody() {
            return this.font_body;
        }

        /**
         *
         * @return Color
         */
        public Color getColorTxtBody() {
            return this.color_text_body;
        }

        /**
         *
         * @return Color
         */
        public Color getColorTxtHeader() {
            return this.color_text_header;
        }

        /**
         *
         * @return Color
         */
        public Color getColorHoverRows() {
            return this.color_hover_rows;
        }

        /**
         *
         * @return boolean
         */
        public boolean getBorderRows() {
            return this.border_row;
        }
        //------------------

        /**
         *
         * @param c - COlor
         */
        public void setColorTableHeader(Color c) {
            this.table_header_color = c;
        }

        /**
         *
         * @param c - Color
         */
        public void setColorTableFooter(Color c) {
            this.table_footer_color = c;
        }

        /**
         *
         * @param c -Color
         */
        public void setColorTableBody(Color c) {
            this.table_body_color = c;
        }

        /**
         *
         * @param b - boolean
         */
        public void setBorder(boolean b) {
            this.border = b;
        }

        /**
         *
         * @param font -Font
         */
        public void setFontHeader(Font font) {
            this.font_header = font;
        }

        /**
         *
         * @param font - FOnt
         */
        public void setFontBody(Font font) {
            this.font_body = font;
        }

        /**
         *
         * @param c - Color
         */
        public void setColorTxtHeader(Color c) {
            this.color_text_header = c;
        }

        /**
         *
         * @param c - Color
         */
        public void setColorTxtBody(Color c) {
            this.color_text_body = c;
        }

        /**
         *
         * @param c - Color
         */
        public void setColorHoverRows(Color c) {
            this.color_hover_rows = c;
        }

        /**
         *
         * @param br - boolean
         */
        public void setBorderRows(boolean br) {
            this.border_row = br;
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     */
    //--------------------------------------------------------------------------
    public StyleCssClass() {
        this.body = new Body();
        this.table = new Table();
        this.link = new Link();
    }
}
