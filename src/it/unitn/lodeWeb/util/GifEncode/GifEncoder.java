package it.unitn.lodeWeb.util.GifEncode;

import it.unitn.lodeWeb.util.Row;
import it.unitn.lodeWeb.util.Util;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class GifEncoder {

    //--------------------------------------------------------------------------
    /**
     *
     * create an animated gif from a set of images
     *
     * @param outFile - gif image
     * @param dir - directory that contains images
     * @param delay - delay for the gif image
     *
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     * 
     */
    //--------------------------------------------------------------------------
    public void createAnimatedGif(String outFile, String dir, int delay) throws FileNotFoundException, IOException {
        OutputStream output = null;

        File file = new File(outFile);
        if (!file.exists()) {
            output = new BufferedOutputStream(new FileOutputStream(file));
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.start(output);
            e.setDelay(delay);
            e.setRepeat(0);

            File dirImg = new File(dir);
            File aImg[] = dirImg.listFiles();

            for (int i = 0; i < aImg.length; i++) {
                BufferedImage image = ImageIO.read(aImg[i]);
                e.addFrame(image);
            }
            e.finish();
            output.close();
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Create an animated gif from a set of images
     *
     * @param distDir - distribution directory
     * @param postDir - output directory
     * @param imgsDir - directory that contains images
     * @param r - Row
     *
     * @throws java.io.IOException
     */
    //--------------------------------------------------------------------------
    public void createGifs(String distDir, String postDir, String imgsDir, Row r) throws IOException {
        Util util = new Util();
        util.createDir(postDir + File.separator + imgsDir);
        util.createDir(postDir + File.separator + imgsDir + File.separator + r.getNameCourseHome());
        File outDirGif = new File(postDir + File.separator + imgsDir + File.separator + r.getNameCourseHome());
        String imgDir = distDir + File.separator + r.getNameDir() + File.separator + "content" +
                File.separator + "img" + File.separator;
        File img1 = new File(imgDir + "1.jpg");
        File img1D = new File(outDirGif + File.separator + r.getNameDir() + "_Small.jpg");
        if (img1.exists()) {
            util.copyFile(img1, img1D);
        }
        createAnimatedGif(outDirGif + File.separator + r.getNameDir() + ".gif", imgDir, 5000);

    }
}
