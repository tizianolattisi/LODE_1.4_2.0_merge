package it.unitn.LODE.gui.lode2bind;

import it.unitn.LODE.MP.IF.ServiceProviderIF;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.MP.factories.ServiceProviderFactory;
import it.unitn.lode2.IOC;
import it.unitn.lode2.camera.Camera;
import it.unitn.lode2.camera.ipcam.CameraIPBuilder;
import it.unitn.lode2.camera.ipcam.Cmds;
import it.unitn.lode2.recorder.Recorder;
import it.unitn.lode2.recorder.ipcam.IPRecorderBuilder;
import it.unitn.lode2.recorder.ipcam.IPRecorderProtocol;
import it.unitn.lode2.projector.Projector;
import it.unitn.lode2.projector.raster.RasterProjectorBuilder;
import it.unitn.lode2.projector.raster.RasterProjectorImpl;
import it.unitn.lode2.xml.XMLHelper;
import it.unitn.lode2.xml.ipcam.CameraIPConf;
import it.unitn.lode2.xml.lecture.Lecture;
import it.unitn.lode2.xml.slides.LodeSlides;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * User: tiziano
 * Date: 14/12/14
 * Time: 12:26
 */
public class LODE2Runner {

    private final static String CAMERA_CONF = "/Users/tiziano/Projects/LODE2/confs/ipcamera/FOSCAM.XML";

    public static void initAndShowGUI() {

        // Nome del file di output
        String fileName = LODEConstants.MOVIE_FILE;
        ServiceProviderIF spif = new ServiceProviderFactory().getServiceProvider();
        String workingDirectory = spif.getCurrentLectureAcquisitionPath();
        int version = 0;
        String fileURI = workingDirectory + fileName + version + LODEConstants.MOVIE_EXTENSION;


        // Inizializzo le utilità in IOC

        // read ip camera configuration
        CameraIPConf cameraIPConf = XMLHelper.build(CameraIPConf.class).unmarshal(new File(CAMERA_CONF));

        // Camera
        Camera camera = CameraIPBuilder.create()
                .user(cameraIPConf.getUser())
                .password(cameraIPConf.getPassword())
                .host(cameraIPConf.getHost())
                .port(cameraIPConf.getCgiPort())
                .template(Cmds.ZOOMIN, cameraIPConf.getZoomIn())
                .template(Cmds.ZOOMOUT, cameraIPConf.getZoomOut())
                .template(Cmds.ZOOMSTOP, cameraIPConf.getZoomStop())
                .template(Cmds.PANLEFT, cameraIPConf.getPanLeft())
                .template(Cmds.PANRIGHT, cameraIPConf.getPanRight())
                .template(Cmds.PANSTOP, cameraIPConf.getPanStop())
                .template(Cmds.TILTUP, cameraIPConf.getTiltUp())
                .template(Cmds.TILTDOWN, cameraIPConf.getTiltDown())
                .template(Cmds.TILTSTOP, cameraIPConf.getTiltStop())
                .template(Cmds.SNAPSHOT, cameraIPConf.getSnapshot())
                .template(Cmds.PRESET, cameraIPConf.getPreset())
                .build();
        IOC.registerUtility(camera, Camera.class);

        // Recorder
        Recorder recorder = IPRecorderBuilder.create()
                .protocol(IPRecorderProtocol.valueOf(cameraIPConf.getStreamProtocol()))
                .host(cameraIPConf.getHost())
                .port(cameraIPConf.getStreamPort())
                .url(cameraIPConf.getStreamUrl())
                .user(cameraIPConf.getUser())
                .password(cameraIPConf.getPassword())
                .output(fileURI)
                .build();
        IOC.registerUtility(recorder, Recorder.class);

        // Lecture and Slide configuration
        Lecture lecture = XMLHelper.build(Lecture.class).unmarshal(new File(workingDirectory + "LECTURE.XML"));
        LodeSlides lodeSlides = XMLHelper.build(LodeSlides.class).unmarshal(new File(workingDirectory + "SLIDES.XML"));

        RasterProjectorBuilder projectorBuilder = RasterProjectorBuilder.create();
        /*for( LodeSlide slide: lodeSlides.getSlides().getSlides() ){
            URL url = null;
            try {
                url = (new File(workingDirectory + slide.getFileName())).toURI().toURL();
                projectorBuilder = projectorBuilder.slide(new RasterSlideImpl(url, slide.getTitle(), ""));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }*/
        RasterProjectorImpl projector = projectorBuilder.build();
        IOC.registerUtility(projector, Projector.class);

        // Inizializzo il frame che conterrà la scena fx
        JFrame frame = new JFrame("Cam controller");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(1024, 768);
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
            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add("/it/unitn/lode2/ui/skin/style.css");
            fxPanel.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
