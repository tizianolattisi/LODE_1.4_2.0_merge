
package it.unitn.LODE.gui;
/**
 * AWT version, to be used on Macintosh
 * Due to some bugs, working with Swing does not enable Menus in the mac style
 * even when using         
 * System.setProperty("apple.laf.useScreenMenuBar", "true");
 *
 * @author ronchet
 */
import java.awt.MenuBar;
import javax.swing.JMenuBar;
public abstract class MenuManager {
    

    // ========== SOLITON PATTERN ==============================================
    static MenuManager instance=null;
    public static synchronized MenuManager getInstance(){
        if (instance==null) instance=MenuManager_Swing.getInstance();
        return instance;
    }
    protected MenuManager() {
    }
    //==========================================================================
    // methods returnting null menu bars - 
    // either the MenuBar or the JMenuBar version must be implemented by the subclasses
    public MenuBar getMenuBar() {return null;} 
    public JMenuBar getJMenuBar() {return null;} 
    public MenuBar createMenuBar() {return null;} 
    public JMenuBar createJMenuBar() {return null;} 
    //==========================================================================
    public abstract void swapState();
    public abstract void updateMenuState();
    public abstract void updateRecentCourses();
}
