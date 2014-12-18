package it.unitn.LODE.gui.lode2bind;

import it.unitn.lode2.IOC;
import it.unitn.lode2.cam.Camera;
import it.unitn.lode2.cam.ipcam.CameraIPBuilder;
import it.unitn.lode2.cam.ipcam.Cmds;
import it.unitn.lode2.recorder.Recorder;
import it.unitn.lode2.recorder.ipcam.IPRecorderBuilder;
import it.unitn.lode2.recorder.ipcam.IPRecorderProtocol;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.apache.poi.hslf.record.Record;

import javax.swing.*;
import java.io.IOException;

/**
 * User: tiziano
 * Date: 14/12/14
 * Time: 12:26
 */
public class LODE2Runner {

    private final static String HOST = "192.168.1.143";
    private final static Integer PORT = 88;
    private final static String USER = "admin";
    private final static String PASSWORD = "admin";


    public static void initAndShowGUI() {

        // Inizializzo le utilità in IOC

        // Camera
        Camera camera = CameraIPBuilder.create()
                .user(USER)
                .password(PASSWORD)
                .host(HOST)
                .port(PORT)
                .template(Cmds.ZOOMIN, "/cgi-bin/CGIProxy.fcgi?cmd=zoomIn&usr=${user}&pwd=${password}")
                .template(Cmds.ZOOMOUT, "/cgi-bin/CGIProxy.fcgi?cmd=zoomOut&usr=${user}&pwd=${password}")
                .template(Cmds.ZOOMSTOP, "/cgi-bin/CGIProxy.fcgi?cmd=zoomStop&usr=${user}&pwd=${password}")
                .template(Cmds.PANLEFT, "/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveLeft&usr=${user}&pwd=${password}")
                .template(Cmds.PANRIGHT, "/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveRight&usr=${user}&pwd=${password}")
                .template(Cmds.PANSTOP, "/cgi-bin/CGIProxy.fcgi?cmd=ptzStopRun&usr=${user}&pwd=${password}")
                .template(Cmds.TILTUP, "/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveUp&usr=${user}&pwd=${password}")
                .template(Cmds.TILTDOWN, "/cgi-bin/CGIProxy.fcgi?cmd=ptzMoveDown&usr=${user}&pwd=${password}")
                .template(Cmds.TILTSTOP, "/cgi-bin/CGIProxy.fcgi?cmd=ptzStopRun&usr=${user}&pwd=${password}")
                .template(Cmds.SNAPSHOT, "/cgi-bin/CGIProxy.fcgi?cmd=snapPicture2&usr=${user}&pwd=${password}")
                .build();
        IOC.registerUtility(camera, Camera.class);

        // Recorder
        Recorder recorder = IPRecorderBuilder.create()
                .protocol(IPRecorderProtocol.RTSP)
                .host(HOST)
                .port(PORT)
                .url("/videoMain")
                .user(USER)
                .password(PASSWORD)
                .build();
        IOC.registerUtility(recorder, Recorder.class);

        // Inizializzo il frame che conterrà la scena fx
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
