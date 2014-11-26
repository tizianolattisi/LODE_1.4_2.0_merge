package it.unitn.lodeWeb.gui.create;

//--------------------------------------------------------------------------
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class ProgressJDialog extends JDialog {

    private JPanel panelProgress;
    // jframeout
    public JTextField taskOutput;
    public JProgressBar progressBar;
    public JButton buttonOk;

    private ProgressJDialog() {}
    static ProgressJDialog instance=null;
    public static ProgressJDialog getInstance() {
        if (instance==null) {
            instance=new ProgressJDialog();
            instance._initFrame();
        }
        return instance;
    }

    private void _setCenterPosition() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (d.width - getWidth()) / 2;
        int h = (d.height - getHeight()) / 2;
        setLocation(w, h);
    }

    //--------------------------------------------------------------------------
    /**
     *
     * initializes the graphics panel
     *
     */
    //--------------------------------------------------------------------------
    private void _initFrame() {

        setSize(400, 200);
        _setCenterPosition();
        setAlwaysOnTop(true);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                //System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        //Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        //setCursor(hourglassCursor);

        setTitle("Progress");
        //ImageIcon immagine = new ImageIcon("Images" + File.separator + "logo.jpg");
        ImageIcon immagine = new ImageIcon("Resources/Template/Content" + File.separator + "logo.jpg");
        System.out.println(immagine.getImage());
        setIconImage(immagine.getImage());
        panelProgress = new JPanel(new GridLayout(3, 0, 10, 10));
        panelProgress.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Progress"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        panelProgress.add(progressBar);
        taskOutput = new JTextField(50);
        taskOutput.setMargin(new Insets(5, 5, 5, 5));
        taskOutput.setEditable(false);
        buttonOk = new JButton("Done");
        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                //dispose();
            }
        });
        panelProgress.add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        panelProgress.add(buttonOk);

        setContentPane(panelProgress);
        setResizable(false);
        //setVisible(true);
        pack();
    }
    public void reset(String title){
        setTitle(title);
        progressBar.setValue(0);
        setVisible(true);
    }

    //--------------------------------------------------------------------------
    /**
     * Class Constructor
     */
    //--------------------------------------------------------------------------

    public static void main(String a[]){
        new ProgressJDialog();
    }
}
