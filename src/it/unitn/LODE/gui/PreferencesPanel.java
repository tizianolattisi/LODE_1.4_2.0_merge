/*
 * PreferencesPanel.java
 *
 * Created on 3-ott-2008, 16.53.12
 */

package it.unitn.LODE.gui;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.LODEPreferences;
//import it.unitn.QT.QTWrapper;
import it.unitn.science.JCQLibrary.CaptureDevice;
import it.unitn.science.JCQLibrary.JCQFactory;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author ronchet
 */
public class PreferencesPanel extends javax.swing.JPanel {
    //String newPassword=null;
    static final String PASSWORD_WRONG_MSG="<HTML><FONT COLOR=RED>Enter password to unlock preferences</FONT>";
    static final String PASSWORD_RIGHT_MSG="The password is correct - preferences are unlocked";
    static final String PASSWORD_DISABLED_MSG="Choose YES to prevent preferences modifications";
    LODEPreferences prefs;
    // when executing updateList the Combo boxes events should have no effect
    boolean isUpdating=false; 
    JFrame f=null;
    static PreferencesPanel instance=null;
    public static PreferencesPanel getInstance() {
        if (instance==null) instance=new PreferencesPanel();
        return instance;
    }
    /** Creates new form PreferencesPanel */
    private PreferencesPanel() {
        prefs=LODEPreferences.getInstance();
        boolean passwordProtected=(prefs.get(LODEPreferences.passwordProtected).compareToIgnoreCase("YES")==0);
        initComponents();
        isUpdating=true;
        passwordComboBox.removeAllItems();
        passwordComboBox.addItem("NO");
        passwordComboBox.addItem("YES");
        protect(passwordProtected);
        isUpdating=false;
        f=new JFrame("Preferences");
        f.add(this);
        f.pack();
    }
    private void protect(boolean passwordProtected){
        String pw=null;
        if (! passwordProtected) {
            passwordComboBox.setSelectedItem("NO");
            passwordField.setBackground(Color.LIGHT_GRAY);
            passwordField.setText("");
            passwordField.setEnabled(false);
            passwordHintLabel.setText(PASSWORD_DISABLED_MSG);
            enableFields(true);
        } else {
            passwordComboBox.setSelectedItem("YES");
            passwordField.setBackground(Color.WHITE);
            passwordField.setEnabled(true);
            pw=prefs.get(LODEPreferences.password);
            boolean passwordMatches=passwordField.getText().equals(pw);
            if (passwordMatches) passwordHintLabel.setText(PASSWORD_RIGHT_MSG);
            else passwordHintLabel.setText(PASSWORD_WRONG_MSG);
            enableFields(passwordMatches);
        }
    }
    private void enableFields(boolean enable){
        audioFirstComboBox.setEnabled(enable);
        audioSecondComboBox.setEnabled(enable);
        videoFirstComboBox.setEnabled(enable);
        videoSecondComboBox.setEnabled(enable);
        passwordComboBox.setEnabled(enable);
        if (enable) {
            passwordField.setEnabled(false);
        } else {}
    }
    public void setPassword(){
        /*ImageIcon ICON_LODE=null;
        java.net.URL imgURL = PreferencesPanel.class.getResource(LODEConstants.IMGS_PREFIX + "LODE.gif");
        if (imgURL != null) {
            ICON_LODE=new ImageIcon(imgURL, "LODE");
        }*/

        String storedPw=prefs.get(LODEPreferences.password);
        String newPassword=null;
        if (storedPw==null ||  storedPw.equals("")) {
            // define new password
            Object[] message=new Object[2];
            message[0]="Password";
            message[1]=new JPasswordField("");
            String[] options={"OK"};
            while (newPassword==null || newPassword.equals("")) {
                JOptionPane.showOptionDialog(this, message, "Enter new password", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,LODEConstants.ICON_LODE , options, options[0]);
                newPassword=new String(((JPasswordField)message[1]).getPassword());
            }
            prefs.changeProperty(LODEPreferences.password, newPassword);
            //prefs.savePreferences();
        }
    }
    public void showPrefs(){
        updateList();
        f.setVisible(true);
    }
    public void hidePrefs(){
        f.setVisible(false);
    }
    public final void updateList(){
        // when executing updateList the Combo boxes events should have no effect
        isUpdating=true; 
        String firstMic=prefs.get(LODEPreferences.preferredMicrophone);
        String secondMic=prefs.get(LODEPreferences.fallbackMicrophone);
        String firstVid=prefs.get(LODEPreferences.preferredCamera);
        String secondVid=prefs.get(LODEPreferences.fallbackCamera);
        //QTWrapper qtw=QTWrapper.getInstance();
        Map<String, Boolean> hm=null;
        audioFirstComboBox.removeAllItems();
        audioFirstComboBox.addItem("none");
        audioSecondComboBox.removeAllItems();
        audioSecondComboBox.addItem("none");
        //QT// hm=qtw.getAudioSourceList();
        JCQFactory jcq=JCQFactory.getInstance();
        ArrayList<CaptureDevice> al=jcq.getMicrophoneDevices(false);
        Iterator<CaptureDevice> iter;
        iter=al.iterator();
        while (iter.hasNext()) {
            CaptureDevice s=iter.next();
        //for (CaptureDevice s : hm.keySet()) {
            audioFirstComboBox.addItem(s.toString());
            audioSecondComboBox.addItem(s.toString());
            System.out.println(s);
        }
        System.err.println(firstMic);
        audioFirstComboBox.setSelectedItem(firstMic);//prefs.get(LODEPreferences.preferredMicrophone));
        audioSecondComboBox.setSelectedItem(secondMic);//(prefs.get(LODEPreferences.fallbackMicrophone));
        //System.out.println("PREFERRED : "+prefs.get(LODEPreferences.preferredMicrophone));
        // VIDEO
        videoFirstComboBox.removeAllItems();
        videoFirstComboBox.addItem("none");
        videoSecondComboBox.removeAllItems();
        videoSecondComboBox.addItem("none");
        
        //QT// hm=qtw.getVideoSourceList();
        al=jcq.getCameraDevices(false);
        //for (String s : hm.keySet()) {
        iter=al.iterator();
        while (iter.hasNext()) {
            CaptureDevice s=iter.next();
            videoFirstComboBox.addItem(s.toString());
            videoSecondComboBox.addItem(s.toString());
        }
        videoSecondComboBox.setSelectedItem(secondVid);//(prefs.get(LODEPreferences.fallbackCamera));
        videoFirstComboBox.setSelectedItem(firstVid);//(prefs.get(LODEPreferences.preferredCamera));
        repaint();
        isUpdating=false; 
        /*       
        */

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        audioPanel = new javax.swing.JPanel();
        audioFirstLabel = new javax.swing.JLabel();
        audioFirstComboBox = new javax.swing.JComboBox();
        audioSecondLabel = new javax.swing.JLabel();
        audioSecondComboBox = new javax.swing.JComboBox();
        videoPanel = new javax.swing.JPanel();
        videoFirstLabel = new javax.swing.JLabel();
        videoFirstComboBox = new javax.swing.JComboBox();
        videoSecondLabel = new javax.swing.JLabel();
        videoSecondComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        passwordPanel = new javax.swing.JPanel();
        passwordLabel1 = new javax.swing.JLabel();
        passwordComboBox = new javax.swing.JComboBox();
        passwordLabel2 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        passwordHintLabel = new javax.swing.JLabel();

        audioPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        audioPanel.setName("audioPanel"); // NOI18N

        audioFirstLabel.setText("First choice");
        audioFirstLabel.setName("audioFirstLabel"); // NOI18N

        audioFirstComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none" }));
        audioFirstComboBox.setName("audioFirstComboBox"); // NOI18N
        audioFirstComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                audioFirstComboBoxActionPerformed(evt);
            }
        });

        audioSecondLabel.setText("Second choice");
        audioSecondLabel.setName("audioSecondLabel"); // NOI18N

        audioSecondComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none" }));
        audioSecondComboBox.setName("audioSecondComboBox"); // NOI18N
        audioSecondComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                audioSecondComboBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout audioPanelLayout = new org.jdesktop.layout.GroupLayout(audioPanel);
        audioPanel.setLayout(audioPanelLayout);
        audioPanelLayout.setHorizontalGroup(
            audioPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, audioPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(audioPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(audioSecondLabel)
                    .add(audioFirstLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(audioPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(audioFirstComboBox, 0, 361, Short.MAX_VALUE)
                    .add(audioSecondComboBox, 0, 361, Short.MAX_VALUE))
                .addContainerGap())
        );
        audioPanelLayout.setVerticalGroup(
            audioPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(audioPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(audioPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(audioFirstLabel)
                    .add(audioFirstComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(audioPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(audioSecondLabel)
                    .add(audioSecondComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        videoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        videoPanel.setName("videoPanel"); // NOI18N

        videoFirstLabel.setText("First choice");
        videoFirstLabel.setName("videoFirstLabel"); // NOI18N

        videoFirstComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none" }));
        videoFirstComboBox.setName("videoFirstComboBox"); // NOI18N
        videoFirstComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                videoFirstComboBoxActionPerformed(evt);
            }
        });

        videoSecondLabel.setText("Second choice");
        videoSecondLabel.setName("videoSecondLabel"); // NOI18N

        videoSecondComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none" }));
        videoSecondComboBox.setName("videoSecondComboBox"); // NOI18N
        videoSecondComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                videoSecondComboBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout videoPanelLayout = new org.jdesktop.layout.GroupLayout(videoPanel);
        videoPanel.setLayout(videoPanelLayout);
        videoPanelLayout.setHorizontalGroup(
            videoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(videoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(videoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(videoSecondLabel)
                    .add(videoFirstLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(videoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(videoFirstComboBox, 0, 364, Short.MAX_VALUE)
                    .add(videoSecondComboBox, 0, 364, Short.MAX_VALUE))
                .addContainerGap())
        );
        videoPanelLayout.setVerticalGroup(
            videoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(videoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(videoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoFirstLabel)
                    .add(videoFirstComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(videoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoSecondLabel)
                    .add(videoSecondComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("Select preferred AUDIO IN component");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText("Select preferred VIDEO IN component");
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText("<HTML><FONT COLOR=\"RED\">NOTE: your choices will be active at the next start of the program</FONT>");
        jLabel3.setName("jLabel3"); // NOI18N

        passwordPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        passwordPanel.setName("passwordPanel"); // NOI18N

        passwordLabel1.setText("Should preference panel be password-protected?");
        passwordLabel1.setName("passwordLabel1"); // NOI18N

        passwordComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none" }));
        passwordComboBox.setSelectedItem("NO");
        passwordComboBox.setName("passwordComboBox"); // NOI18N
        passwordComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordComboBoxActionPerformed(evt);
            }
        });

        passwordLabel2.setText("Password");
        passwordLabel2.setName("passwordLabel2"); // NOI18N

        passwordField.setName("passwordField"); // NOI18N
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });

        passwordHintLabel.setText("<HTML><FONT COLOR=\"RED\">Enter the password to unlock the preferences</FONT>");
        passwordHintLabel.setName("passwordHintLabel"); // NOI18N

        org.jdesktop.layout.GroupLayout passwordPanelLayout = new org.jdesktop.layout.GroupLayout(passwordPanel);
        passwordPanel.setLayout(passwordPanelLayout);
        passwordPanelLayout.setHorizontalGroup(
            passwordPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(passwordPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(passwordPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(passwordPanelLayout.createSequentialGroup()
                        .add(passwordPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(passwordPanelLayout.createSequentialGroup()
                                .add(passwordLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(passwordComboBox, 0, 144, Short.MAX_VALUE))
                            .add(passwordPanelLayout.createSequentialGroup()
                                .add(passwordLabel2)
                                .add(68, 68, 68)
                                .add(passwordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)))
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, passwordPanelLayout.createSequentialGroup()
                        .add(passwordHintLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 353, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(69, 69, 69))))
        );
        passwordPanelLayout.setVerticalGroup(
            passwordPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(passwordPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(passwordPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordLabel1)
                    .add(passwordComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(passwordPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordLabel2)
                    .add(passwordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(passwordHintLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(videoPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(92, 92, 92)
                        .add(jLabel2))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(92, 92, 92)
                        .add(jLabel1))
                    .add(audioPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(passwordPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(9, 9, 9)
                .add(passwordPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(audioPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(videoPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void audioSecondComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_audioSecondComboBoxActionPerformed
        if (!isUpdating) prefs.changeProperty(LODEPreferences.fallbackMicrophone, audioSecondComboBox.getSelectedItem().toString());
}//GEN-LAST:event_audioSecondComboBoxActionPerformed

    private void videoSecondComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoSecondComboBoxActionPerformed
        if (!isUpdating) prefs.changeProperty(LODEPreferences.fallbackCamera, videoSecondComboBox.getSelectedItem().toString());
}//GEN-LAST:event_videoSecondComboBoxActionPerformed

    private void audioFirstComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_audioFirstComboBoxActionPerformed
        if (!isUpdating) prefs.changeProperty(LODEPreferences.preferredMicrophone, audioFirstComboBox.getSelectedItem().toString());
}//GEN-LAST:event_audioFirstComboBoxActionPerformed

    private void videoFirstComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoFirstComboBoxActionPerformed
        if (!isUpdating) prefs.changeProperty(LODEPreferences.preferredCamera, videoFirstComboBox.getSelectedItem().toString());
    }//GEN-LAST:event_videoFirstComboBoxActionPerformed

    private void passwordComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordComboBoxActionPerformed
        String value=(String)passwordComboBox.getSelectedItem();
        if (value==null) return;
        if (isUpdating) return;
        if (value.equalsIgnoreCase("YES")) {
            setPassword();
            prefs.changeProperty(LODEPreferences.passwordProtected, "YES");
            protect(true);
            prefs.savePreferences();
        } else {
            protect(false);
            prefs.changeProperty(LODEPreferences.passwordProtected, "NO");
            prefs.changeProperty(LODEPreferences.password, "");
            prefs.savePreferences();
        }
}//GEN-LAST:event_passwordComboBoxActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        isUpdating=true;
        String pw=passwordField.getText();
        if (pw.equals(prefs.get(LODEPreferences.password))){
             protect(false);
             passwordComboBox.setSelectedItem("YES");
             passwordField.setEnabled(true);
        }
        isUpdating=false;
    }//GEN-LAST:event_passwordFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox audioFirstComboBox;
    private javax.swing.JLabel audioFirstLabel;
    private javax.swing.JPanel audioPanel;
    private javax.swing.JComboBox audioSecondComboBox;
    private javax.swing.JLabel audioSecondLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox passwordComboBox;
    private javax.swing.JTextField passwordField;
    private javax.swing.JLabel passwordHintLabel;
    private javax.swing.JLabel passwordLabel1;
    private javax.swing.JLabel passwordLabel2;
    private javax.swing.JPanel passwordPanel;
    private javax.swing.JComboBox videoFirstComboBox;
    private javax.swing.JLabel videoFirstLabel;
    private javax.swing.JPanel videoPanel;
    private javax.swing.JComboBox videoSecondComboBox;
    private javax.swing.JLabel videoSecondLabel;
    // End of variables declaration//GEN-END:variables

}
