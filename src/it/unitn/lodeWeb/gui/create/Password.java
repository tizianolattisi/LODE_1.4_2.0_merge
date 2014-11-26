package it.unitn.lodeWeb.gui.create;

//--------------------------------------------------------------------------
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class Password extends JPanel implements ActionListener {

    private JDialog jDialog;
    private JPasswordField password;
    private JTextField username;
    private JPasswordField passwordExt;
    private JTextField usernameExt;

    //--------------------------------------------------------------------------
    /**
     *
     *  create new Frame
     *
     */
    //--------------------------------------------------------------------------
    private void createFramePsw() {

        JDialog dialog = new JDialog();

        this.jDialog = dialog;
        dialog.setTitle("Password");
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setAlwaysOnTop(true);
        dialog.setSize(200, 100);
        //dialog.setResizable(false);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (d.width  - dialog.getWidth())/2;
        int h = (d.height  - dialog.getHeight())/2;
        dialog.setLocation(w, h);


        dialog.setContentPane(this);

        dialog.pack();
        dialog.setVisible(true);

    }

    //--------------------------------------------------------------------------
    /**
     *
     * Class Constructor
     *
     * @param passwordExt
     * @param usernameExt
     */
    //--------------------------------------------------------------------------
    public Password(JPasswordField passwordExt, JTextField usernameExt) {
        this.passwordExt = passwordExt;
        this.usernameExt = usernameExt;

        password = new JPasswordField(10);
        if (String.valueOf(this.passwordExt.getPassword()) != null && !String.valueOf(this.passwordExt.getPassword()).equals("")) {
            password.setText(String.valueOf(this.passwordExt.getPassword()));
        }

        password.setActionCommand("OK");
        password.addActionListener(this);

        username = new JTextField(10);
        username.setText(this.usernameExt.getText());

        JLabel label2 = new JLabel("Enter username admin: ");
        label2.setLabelFor(username);
        JLabel label = new JLabel("Enter the password: ");
        label.setLabelFor(password);

        JComponent buttonPane = createButtonPanel();

        JPanel textPane = new JPanel(new GridLayout(2, 2));
        textPane.add(label2);
        textPane.add(username);
        textPane.add(label);
        textPane.add(password);


        add(textPane);
        add(buttonPane);

        createFramePsw();
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Create button panel
     *
     * @return JComponent
     */
    //--------------------------------------------------------------------------
    protected JComponent createButtonPanel() {
        JPanel p = new JPanel(new GridLayout(0, 1));
        JButton okButton = new JButton("OK");
        JButton helpButton = new JButton("Help");

        okButton.setActionCommand("OK");
        helpButton.setActionCommand("Help");
        okButton.addActionListener(this);
        helpButton.addActionListener(this);

        p.add(okButton);
        p.add(helpButton);

        return p;
    }

    //--------------------------------------------------------------------------
    /**
     * actionPerformed
     *
     * @param e - ActionEvent
     */
    //--------------------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("OK")) {

            boolean correct = true;

            char[] input = password.getPassword();
            if (input.length <= 6) {
                JOptionPane.showMessageDialog(jDialog,
                        "Invalid password (Min 6 chars). Try again.",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);
                password.selectAll();
                password.requestFocusInWindow();

                correct = false;
            }
            if (username == null || username.getText().equals("")) {
                JOptionPane.showMessageDialog(jDialog,
                        "Invalid username. Try again.",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);

                username.selectAll();
                username.requestFocusInWindow();

                correct = false;
            }

            Arrays.fill(input, '0');


            if (correct) {
                usernameExt.setText(username.getText());
                passwordExt.setText(String.valueOf(password.getPassword()));
                jDialog.dispose();
            }

        } else {
            JOptionPane.showMessageDialog(jDialog,
                    "\n" + "\n" + "\n" + "");
        }
    }
}
