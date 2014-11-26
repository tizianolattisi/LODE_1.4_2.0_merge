package it.unitn.lodeWeb.util.Vlc;

//--------------------------------------------------------------------------
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.MP.utils.CorrectPathFinder;
import it.unitn.lodeWeb.util.GifEncode.GifEncoder;
import it.unitn.lodeWeb.util.ProcessRuntime;
import it.unitn.lodeWeb.util.Row;
import it.unitn.lodeWeb.util.Util;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import it.unitn.lodeWeb.util.Constants;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
//--------------------------------------------------------------------------

//--------------------------------------------------------------------------
/**
 *
 * @author Colombari Mattia
 */
//--------------------------------------------------------------------------
public class VlcImages {

    //--------------------------------------------------------------------------
    //vlc -V image --start-time=0 --stop-time=1 --image-out-format=jpg --image-out-ratio=24 --image-out-prefix=snap test.mpg vlc://quit
    /**
     *
     * Create n images (snarpshots) from a video
     *
     * @param outdir - output directory for snarpshots
     * @param movie - video path
     * @param lenght - video lenght
     * @param Nsnap - number of images
     *
     * @throws IOException
     * @throws InterruptedException
     *
     */
    //--------------------------------------------------------------------------
    private void createSnapshots(File outdir, String movie, int lenght, String format, int Nsnap) throws InterruptedException, IOException, Exception {
        //System.out.println("MARCO - ENTERING createSnapshots");

        ProcessRuntime proc = new ProcessRuntime();

        int offset = 0; // time at which the snapshot is taken
        int N = 4; // number of snapshots per movie

        Process p = null;
        //Process[] aPr = new Process[N];
        String comandWIN = "";
        for (int j = 0; j < N; j++) {
            // convert the offset in hh:mm:ss format
            int hours=offset/3600;
            int t=offset%3600;
            int min=t/60;
            int sec=t%60;
            String time="";
            if (hours<10) time="0";
            time=time+hours+":";
            if (min<10) time=time+"0";
            time=time+min+":";
            if (sec<10) time=time+"0";
            time=time+sec;
            //System.out.println("MARCO - ENTERING loop "+j);

            if ((proc.getSo() == ProcessRuntime.Sop.MAC) ||(proc.getSo() == ProcessRuntime.Sop.WINDOWS)) {
                //String actualFFMPEGX_PATH=CorrectPathFinder.getJarPath(LODEConstants.FFMPEG_COMMAND);
                String actualFFMPEGX_PATH=LODEConstants.FFMPEG_COMMAND;
                //String fileProgramVLC = LODEConstants.FFMPEG_COMMAND; //Constants.VLC_PATH_ON_MAC;
                String fileProgramVLC = actualFFMPEGX_PATH; ;
                String comandMAC = "";
                System.err.println(offset);
                if (new File(fileProgramVLC).exists() && new File(fileProgramVLC).isFile()) {
                    comandMAC = fileProgramVLC + " -y -i "+movie+
                            " -vframes 1 -ss "+time+" -an -s 320x240 -qmin 20 "+Constants.nameImgPreview+j+"%d.jpg";                   
                }
                System.out.println("EXECUTING "+comandMAC);
                p = proc.execute(comandMAC, null, outdir); //"/bin/sh -c "+

                InputStream is = p.getErrorStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                //System.out.println("MARCO - STARTED PROCESS ");

                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                //Thread.sleep(10000);
                p.waitFor();
                System.out.println("MARCO - ENDED IMAGE EXTRACTION PROCESS n."+j+" for movie "+movie);
                //aPr[j] = p;
            } else if (proc.getSo() == ProcessRuntime.Sop.LINUX) {
                throw new Exception("LINUX is presently not supported");
            } /*else if (proc.getSo() == ProcessRuntime.Sop.WINDOWS) {
                throw new Exception("WINDOWS is presently not supported");
            }*/
            offset = offset + (lenght / 10) - 1;
        }
        /*
        // attendo che termino gli N processi per la creazione delle N preview per ogni lezione
        for (int i = 0; i < N; i++) {
                System.out.println("MARCO - WAITING PROCESS "+i);
                aPr[i].waitFor();
                System.out.println("MARCO - TERMINATED PROCESS "+i);

        }
         */
    }
        //--------------------------------------------------------------------------
/*    THIS IS THE OLD METHOD THAT USED VLC

        public String fileVLC = Constants.pathFileVlcWINDOWS;

    private void createSnapshots_orig(File outdir, String movie, int lenght, String format, int Nsnap) throws InterruptedException, IOException, Exception {

        ProcessRuntime proc = new ProcessRuntime();

        //--image-out-replace   --no-sout-audio
        //You can specify --image-out-replace. In that case Vlc produces the file 'snap.jpg'. This will prevent VLC from creating multiple images.

        // Use this interface when you want no interface! That is you just want to give a self-contained command-line string
        //For example : % vlc --intf dummy video

        int offset = 0;
        int N = 4;
        if (proc.getSo() == ProcessRuntime.Sop.WINDOWS) {
            N = 2;
        }

        Process p = null;
        Process[] aPr = new Process[N];
        String comandWIN = "";
        for (int j = 0; j < N; j++) {

            if (proc.getSo() == ProcessRuntime.Sop.MAC) {
                String fileProgramVLC = Constants.VLC_PATH_ON_MAC;
                String comandMAC = "";
                if (new File(fileProgramVLC).exists() && new File(fileProgramVLC).isFile()) {
                    comandMAC = fileProgramVLC + " --intf dummy --no-audio -V image " +
                        "--start-time=" + offset + " --stop-time=" + (offset + 2) + " --image-out-ratio=4 " +
                        "--image-out-format=" + format + " --image-out-replace --image-out-prefix=" + Constants.nameImgPreview + j + " " +
                        movie + " vlc://quit";
                }
                p = proc.execute(comandMAC, null, outdir);
                aPr[j] = p;
            } else if (proc.getSo() == ProcessRuntime.Sop.LINUX) {
                String comandLINUX = "";
                comandLINUX = "vlc --intf dummy --no-audio -V image " +
                        "--start-time=" + offset + " --stop-time=" + (offset + 2) + " --image-out-ratio=4 " +
                        "--image-out-format=" + format + " --image-out-replace --image-out-prefix=" + Constants.nameImgPreview + j + " " +
                        movie + " vlc://quit";
                p = proc.execute(comandLINUX, null, outdir);
                aPr[j] = p;
            } else if (proc.getSo() == ProcessRuntime.Sop.WINDOWS) {
                String fileProgramVLC = Constants.pathFileVlcWINDOWS;;
                if (new File(fileProgramVLC).exists() && new File(fileProgramVLC).isFile()) {
                    comandWIN = fileProgramVLC + " --intf dummy --no-audio -V image " +
                            "--start-time " + offset + " --stop-time " + (offset + 1) + " --image-out-ratio 4 " +
                            "--image-out-format " + format + " --image-out-replace --image-out-prefix " + Constants.nameImgPreview + j + " " +
                            movie + " vlc://quit ";

                    //------------------------------------------
                    //Attendo la fine di vlc
                    if (p != null) {
                        p.destroy();
                    }
                    p = proc.execute(comandWIN, null, outdir);
                    aPr[j] = p;


                    InputStream is = p.getErrorStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);


                    String line;
                    while ((line = br.readLine()) != null) {
                        //System.out.print();
                        //MOMENTO IN CUI ATTENDE
                    }
                    br.close();
                    p.waitFor();
                //------------------------------------------
                } else {
                    throw new Exception("VideoLAN vlc not installed, path: " + Constants.pathFileVlcWINDOWS);
                }
            }

            offset = offset + (lenght / 10) - 1;
        }

        // attendo che termino gli N processi per la carezione delle N preview per ogni lezione
        for (int i = 0; i < N; i++) {
            if (proc.getSo() == ProcessRuntime.Sop.LINUX) {
                aPr[i].waitFor();
            }
            if (proc.getSo() == ProcessRuntime.Sop.MAC) {
                aPr[i].waitFor();
            }
            if (proc.getSo() == ProcessRuntime.Sop.WINDOWS) {
                aPr[i].waitFor();
            }
        }
        for (int i = 0; i < N; i++) {
            if (proc.getSo() == ProcessRuntime.Sop.WINDOWS) {
                aPr[i].destroy();
            }
        }

    }
/*
    //--------------------------------------------------------------------------
    /**
     *
     *  create preview video
     *
     * @param dist_Dir - distribution directory
     * @param lecturesList - list of lectures
     * @param outDir - output directory
     * @param type - type of images
     *
     * @throws InterruptedException
     * @throws IOException
     *
     */
    //--------------------------------------------------------------------------
    public boolean createPreviewVideos(String dist_Dir, List<Row> lecturesList, String outDir, String type) throws IOException, InterruptedException, Exception {

        if (new File(dist_Dir).exists()) {

            Iterator it = lecturesList.iterator();

            while (it.hasNext()) {

                Row row = (Row) it.next();

                String dir = dist_Dir + File.separator + row.getNameDir() + File.separator + Constants.contentDirDistribution;

                String videoPath = dir + File.separator + Constants.movieName;

                if (new File(dir).exists() && new File(videoPath).exists()) {

                    String out = outDir + File.separator + row.getNameDir();

                    String outGif = outDir + File.separator + row.getNameDir() + File.separator + row.getNameDir() + ".gif";

                    if (!new File(outGif).exists() || !new File(outGif).isFile()) {
                        new Util().createDir(out);
                        createSnapshots(new File(out), videoPath, row.getTime(), type, 10);

                    }

                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * Delete preview dir
     *
     * @param dir - directory to delete
     * @param esclusion - file exclusion
     */
    //--------------------------------------------------------------------------
    private void deletePreviewVideo(String dir, File esclusion) {
        File dirF = new File(dir);
        if (dirF.exists() && dirF.isDirectory()) {
            File[] lf = dirF.listFiles();
            for (int i = 0; i < lf.length; i++) {
                if (lf[i].isFile()) {
                    if (!lf[i].equals(esclusion) && !lf[i].equals(new File(dir + File.separator + Constants.nameImgPreview+"11.jpg"))) {
                        lf[i].delete();
                    }
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     *
     * create preview video (gif)
     *
     * @param lecturesList - list of lecture
     * @param outDir - output dir
     * @param type - tyle of image
     *
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     * @throws java.lang.Exception
     *
     */
    //--------------------------------------------------------------------------
    public void createPreviewVideosGif(List<Row> lecturesList, String outDir, String type) throws IOException, InterruptedException, Exception {
        Iterator it = lecturesList.iterator();
        while (it.hasNext()) {

            Row row = (Row) it.next();
            String out = outDir + File.separator + row.getNameDir();
            File outImgGif = new File(out + File.separator + row.getNameDir() + type);
            if (!outImgGif.exists() || !outImgGif.isFile()) {
                (new GifEncoder()).createAnimatedGif(outImgGif.getPath(), out, 2000);
                this.deletePreviewVideo(out, outImgGif);
            }
        }
    }
}
