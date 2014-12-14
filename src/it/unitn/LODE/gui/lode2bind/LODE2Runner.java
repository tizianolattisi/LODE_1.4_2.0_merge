package it.unitn.LODE.gui.lode2bind;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.io.IOException;

/**
 * User: tiziano
 * Date: 14/12/14
 * Time: 12:26
 */
public class LODE2Runner {

    public static void initAndShowGUI() {
        JFrame frame = new JFrame("Cam controller");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }

    private static void initFX(JFXPanel fxPanel) {
        try {
            Parent root = FXMLLoader.load(LODE2Runner.class.getResource("/it/unitn/lode2/ui/camctrl.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add("/it/unitn/lode2/ui/skin/style.css");
            fxPanel.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
