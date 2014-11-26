/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.LODE.services;

import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.utils.SystemProps;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.utils.FileSystemManager;
import java.io.File;

/**
 *
 * @author ronchet
 */
public abstract class PostProducerIF {

    // ========== SOLITON PATTERN ==============================================
    protected static PostProducerIF instance = null;

    public static synchronized PostProducerIF getInstance() {
        // WHEN CALLED, Acquisition Window must already exist!
        if (instance == null) {
            // if (LODE.WINDOWS) instance=WindowsVideoController.getInstance();
            //else if (LODE.MAC) instance=MacVideoController.getInstance();
            //if (SystemProps.IS_OS_MAC_OSX) instance = PostProducer.getInstance();
            instance = PostProducer.getInstance();
        }
        return instance;
    }

    protected FileSystemManager fileSystemManager=null;
    protected ControllersManager controllersManager=null;

    protected PostProducerIF() {
        controllersManager=ControllersManager.getinstance();
        fileSystemManager=controllersManager.getFileSystemManager();
    }

    abstract public boolean convertVideo(String lectureFolder);
    abstract public Thread convertAllLectures();
    abstract public Thread convertAllLecturesInCourse(final File f);
    abstract public boolean convertVideo(File[] fv);
    abstract public void createDistribution(Lecture lecture);
    abstract public void createDistribution(Lecture lecture,boolean showFinalDialog);
    abstract public void postProcessAllRemotely();
    abstract public void createItunesuDistribution(Lecture lecture); //Antonio
    abstract public void createItunesuDistribution(Lecture lecture, boolean showFinalDialog);  //Antonio
    abstract public boolean convertVideo4Itunesu(Lecture lec); //Antonio
    //abstract public boolean convertVideo4Ituneu_allSlides(Lecture lec); //Antonio
    abstract public  void makeItAccessible(Lecture lecture); //Alberto


}
