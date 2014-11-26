package it.unitn.LODE.services;

import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.Models.TimedSlide;
import it.unitn.LODE.gui.InspectorWindow;
import it.unitn.LODE.gui.ProgressBar;
import it.unitn.LODE.utils.FileSystemManager;
import it.unitn.LODE.utils.Messanger;
import it.unitn.LODE.utils.ProcessWaiter;
import it.unitn.LODE.utils.RunnableProcess;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author ronchet
 */
public class PostProducer_iTunesU {
    // ============= ANTONIO ===================================================
    Messanger m = Messanger.getInstance();
    FileSystemManager fileSystemManager=FileSystemManager.getInstance();

    // ========== SOLITON PATTERN ==============================================
    static PostProducer_iTunesU instance = null;

    public synchronized static PostProducer_iTunesU getInstance() {
        if (instance == null) {
            instance = new PostProducer_iTunesU();
        }
        return instance;
    }

    private PostProducer_iTunesU() {
    }
    public void createItunesuDistribution(Lecture lecture) { //Antonio
        createItunesuDistribution(lecture, true);
    }

    public void createItunesuDistribution(Lecture lecture, boolean showFinalDialog) { //Antonio
        File destinationDir = null;
        File sourceDir = null;
        String itunesuDistributionPath = lecture.getiTunesuDistributionPath();
        String acquisitionPath = lecture.getAcquisitionPath();
        // throw away existing distribution dir
        File iTunesDistributionDir = new File(itunesuDistributionPath);
        if (iTunesDistributionDir.exists()) {
            fileSystemManager.recursiveDelete(iTunesDistributionDir);
        }
        // create new empty distribution dir
        fileSystemManager.createFolder(iTunesDistributionDir);
        m.w("iTunesU Distribution path " + itunesuDistributionPath + " has been created.", LODEConstants.MSG_LOG);
        m.log("iTunesU Distribution path " + itunesuDistributionPath + " has been created.");

        /*
         * if (lecture.hasSlides()) { // copy images of slides into distribution
         * dir try { destinationDir = new File(iTunesDistributionDir +
         * LODEConstants.DISTRIBUTION_IMG_SUBDIR); sourceDir = new
         * File(acquisitionPath + LODEConstants.ACQUISITION_IMG_SUBDIR);
         * fileSystemManager.copyFiles(destinationDir, sourceDir); } catch
         * (IOException ex) { m.w("CANNOT COPY! " + sourceDir + " -> " +
         * iTunesDistributionDir, LODEConstants.MSG_ERROR); m.log("CANNOT COPY!
         * " + sourceDir + " -> " + iTunesDistributionDir);
         * ex.printStackTrace(m.getLogger()); }
        }
         */

        // copy video into iPhone distribution dir
        String tempmovie_final = LODEConstants.TEMP_DIR + LODEConstants.FS + lecture.getDirName() + LODEConstants.FS + "tempmovie_final.mp4";

        //String command[] = {"cp", tempmovie_final, iTunesDistributionDir + LODEConstants.FS + "movie.mp4"};
        System.err.println("getLectureName =" + lecture.getDirName());
        String command[] = {"cp", tempmovie_final, iTunesDistributionDir + LODEConstants.FS + lecture.getDirName() + ".mp4"};
        final Process p = RunnableProcess.launch(command, false, false);
        //final ProgressBar pb=ProgressBar.showIndeterminateProgressBar("Please wait - executing postprocessing");
        ProcessWaiter pw = new ProcessWaiter(p, "movie copied");
        // create the main info file (data.xml) and copy it into distribution dir

        /*
         * Data dataXml = new Data(lecture, true); File dataXmlFile = new
         * File(lecture.getAcquisitionPath() + LODEConstants.FS +
         * LODEConstants.DATA_XML); if (dataXmlFile.exists()) {
         * dataXmlFile.delete(); }
        dataXml.persist(dataXmlFile);
         */
        pw.start();

        /*
         * String command1[] = {"cp", dataXmlFile.getAbsolutePath(),
         * iTunesDistributionDir + LODEConstants.FS + "content" +
         * LODEConstants.FS + LODEConstants.DATA_XML}; final Process p1 =
         * RunnableProcess.launch(command1, false, false); ProcessWaiter pw1 =
         * new ProcessWaiter(p1, "data.xml copied");
        pw1.start();
         */
        // update lecture state
        lecture.setHasBeenPostProcessed4utunesu(true);
        InspectorWindow.getInstance().update();
        lecture.persist(new File(lecture.getAcquisitionPath() + LODEConstants.FS + LODEConstants.SERIALIZED_LECTURE));
        if (showFinalDialog) {
            // allow the user immediately viewing the postprocessed movie!
            int choice = JOptionPane.showOptionDialog(null,
                    "<HTML><center>Postprocessing for iPhone complete.<BR><BR>"
                    + "You can now find the postprocessed lecture in<BR>"
                    + "<B>" + iTunesDistributionDir + "</B></center><br></HTML>",
                    "Postprocessing complete",
                    JOptionPane.WARNING_MESSAGE,
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    new Object[]{"No thanks", "Yes please, show it"},
                    null);
            if (choice == 1) {
                fileSystemManager.showInBrowser("file://" + iTunesDistributionDir + LODEConstants.FS + lecture.getDirName() + ".mp4", null);
            }
        }
        // if running in a sequence, let the next postprocessing start
        final PostProducer_iTunesU token = (PostProducer_iTunesU) instance;
        synchronized (token) {
            token.notify();
        }
    }

    public final boolean convertVideo4Itunesu(Lecture lec) { //Antonio

        boolean retval = true;
        // Check if postprocessing is needed ===================================

        if (lec.hasBeenPostProcessedForItunesu()) {

            int response = JOptionPane.showConfirmDialog(null,
                    "This lecture seems to be already postprocessed for iTunesU \n"
                    + "Would you like to rebuild this process (yes/no)\n");
            if (response == 0) {
                lec.setHasBeenPostProcessed4utunesu(false);
            } else {
                m.w(" Lecture " + lec.getLectureName() + " does not need iTunesU postprocessing");
                return false;
            }
        }
        // Start postprocessing is needed ===================================

        if (!lec.hasBeenPostProcessedForItunesu() && (lec.hasVideo() || lec.hasSlides())) {
            //String videoPath = lec.getAcquisitionPath() + LODEConstants.FS + LODEConstants.MOVIE_FILE + "0";
            String videoPath = lec.getAcquisitionPath() + LODEConstants.FS + LODEConstants.MOVIE_FILE + "0";
            String tempPath = LODEConstants.TEMP_DIR;
            fileSystemManager.createFolder(new File(tempPath));
            fileSystemManager.createFolder(new File(tempPath + LODEConstants.FS + lec.getDirName()));

            m.log("start execution" + (new Date()));
            Vector<String[]> commands = new Vector<String[]>();

            m.w((new Date()) + " Start processing lecture for iTunesU - " + lec.getLectureName());
            m.log((new Date()) + " Start processing lecture for iTunesU - " + lec.getLectureName());

            if (!(lec.hasVideo())) {
                //==============================================================
                //  CASE 1 : add postprocess with slides only
                m.w(" Lecture " + lec.getLectureName() + " - only slides will be postprocessed for iTunesU");
                throw new UnsupportedOperationException("Not yet implemented");
            } else if (!lec.hasSlides()) {
                //==============================================================
                //  CASE 2: add postprocess with video only
                m.w(" Lecture " + lec.getLectureName() + " - only video will be postprocessed for iTunesU");
                String command0[] = _FFMPEG_ITUNESU_onlyVideo(videoPath + ".mp4");
                commands.add(command0);
                throw new UnsupportedOperationException("Not yet implemented");
            } else {
                //==============================================================
                //CASE 3: process video and slides for  5 seconds
                //1. extract audio only
                //m.w("Extracting audio  from original movie ", LODEConstants.MSG_GENERIC);
                String tempmoviefinal = tempPath + LODEConstants.FS + lec.getDirName() + LODEConstants.FS + "tempmoviefinal.avi";
                String audioonly = tempPath + LODEConstants.FS + lec.getDirName() + LODEConstants.FS + "audioonly.mp3";
                String tempmovie_root = tempPath + LODEConstants.FS + lec.getDirName() + LODEConstants.FS + "tempmovie";

                // extract audio only-----------------------------
                String command[] = _FFMPEG_ITUNESU_extractAudioTrack(videoPath + ".mp4", audioonly);
                //command[2] = videoPath+ ".mp4";
                //command[7] = audioonly;
                //command[7] = tempPath + LODEConstants.FS + lec.getDirName() + LODEConstants.FS + "audioonly.aac";
                commands.add(command);
                //for (int k=0; k<command.length; k++) {System.out.println(command[k]);}
                //_execCommand(command);

                // generate video-fragments from slides ----------
                lec.resumeSlides();
                System.out.println("N:SLIDES IS " + lec.getSlideCount());
                //2. for each slide, split video and insert slide for 5 seconds
                if (lec.hasSlides()) {

                    int contatempvideo = 0;
                    int contaslide = 1;
                    int timeprevslide = -1;

                    //ArrayList<TimedSlide> ts = lec.getTimedSlides().slideList;
                    ArrayList<TimedSlide> ts = lec.getTimedSlides().slideList;
                    System.out.println(ts.size());
                    System.out.flush();
                    TimedSlide onets = ts.get(0);
                    int tempo1;
                    try {
                        tempo1 = Integer.parseInt(onets.time);
                        if (tempo1 < 0) {
                            tempo1 = 0;
                        }

                    } catch (Exception e) {
                        tempo1 = 0;
                    }
                    if (tempo1 == 0) {
                        // create small videos of lenght equal to TIMESLIDE containing the slide
                        String inputFile = lec.getAcquisitionPath() + LODEConstants.FS + LODEConstants.SLIDES_FILE + onets.fileName.substring(1 + onets.fileName.lastIndexOf(File.separator));
                        String outputFile = tempmovie_root + contatempvideo + ".avi";
                        String command2[] = _FFMPEG_ITUNESU_createVideoSlide("" + LODEConstants.ITUNESU_SLIDE_DURATION, inputFile, outputFile);
                        //command2[4] = "" + LODEConstants.ITUNESU_SLIDE_DURATION;
                        //command2[6] = lec.getAcquisitionPath() + LODEConstants.FS + LODEConstants.SLIDES_FILE + onets.fileName.substring(1 + onets.fileName.lastIndexOf(File.separator));
                        //command2[13] = tempmovie_root + contatempvideo + ".avi";
                        commands.add(command2);
                        contatempvideo++;
                        contaslide++;
                        if (ts.size() > 1) {
                            try {
                                int secondslidetime = Integer.parseInt(ts.get(1).time);
                                if (secondslidetime < LODEConstants.ITUNESU_SLIDE_DURATION) {
                                    command2[4] = ts.get(1).time;
                                }
                            } catch (Exception e) {
                                m.log(e.getMessage());
                            }
                        }

                        timeprevslide = 0;
                    }

                    int lastCorrectionTime = 0;
                    double addtime = 0;
                    DecimalFormat myformat = new DecimalFormat("#######.0", new DecimalFormatSymbols(new Locale("en")));

                    for (int i = contaslide; i <= ts.size(); i++) {
                        onets = ts.get(i - 1);

                        int currenttimeslide = 0;
                        try {
                            currenttimeslide = Integer.parseInt(onets.time);
                        } catch (Exception e) {
                            m.log(e.getMessage());
                        }

                        if (!(((currenttimeslide - timeprevslide) <= LODEConstants.ITUNESU_SLIDE_DURATION) && (timeprevslide > -1))) {
                            // generate video without audio
                            int startTime = 0;
                            if (timeprevslide == 0) {
                                //command3[11] = "0:0:5";
                                startTime = LODEConstants.ITUNESU_SLIDE_DURATION;
                            } else if (timeprevslide == -1) {
                                //command3[11] = "0:0:0";
                                startTime = 0;
                            } //else command2[9] = "0:0:"+(5+timeprevslide);
                            else {
                                //command3[11] = "" + (5 + timeprevslide);
                                startTime = (LODEConstants.ITUNESU_SLIDE_DURATION + timeprevslide);
                            }
                            //command3[11] = "" + startTime;

                            int duration = 0;
                            int onetsTime = Integer.parseInt(onets.time);
                            if (timeprevslide == 0) {
                                duration = onetsTime - (timeprevslide + LODEConstants.ITUNESU_SLIDE_DURATION);
                                //command3[9] = "" + (Integer.parseInt(onets.time) - (timeprevslide + LODEConstants.ITUNESU_SLIDE_DURATION));
                            } else if (timeprevslide == -1) {
                                duration = onetsTime;
                                //command3[9] = onets.time;
                            } else {
                                //command3[9] = "" + (Integer.parseInt(onets.time) - (timeprevslide + LODEConstants.ITUNESU_SLIDE_DURATION));
                                duration = onetsTime - (timeprevslide + LODEConstants.ITUNESU_SLIDE_DURATION);
                            }
                            // WHAT IS THIS? IT OVERWRITES command3[11]...
                            /*
                             * MARCO TO BE FIXED!!!! if (lastCorrectionTime >
                             * 230) { addtime = addtime + ((lastCorrectionTime /
                             * 230) * 0.1); //command3[11] =
                             * myformat.format(Float.parseFloat(command3[11])+addtime);
                             * lastCorrectionTime = 0; } else {
                             * lastCorrectionTime = lastCorrectionTime +
                             * Integer.parseInt(command3[9]); } command3[11] =
                             * myformat.format(Double.parseDouble(command3[11])
                             * + addtime); MARCO ===============
                             */
                            //command3[12] = tempmovie_root + contatempvideo + ".avi";
                            String outputFile = tempmovie_root + contatempvideo + ".avi";
                            String command3[] = _FFMPEG_ITUNESU_cutVideoFragment(videoPath, "" + startTime, "" + duration, outputFile);

                            commands.add(command3);
                            contatempvideo++;
                        }
                        // =====================================================
                        String inputFile = lec.getAcquisitionPath() + LODEConstants.FS + LODEConstants.SLIDES_FILE + onets.fileName.substring(1 + onets.fileName.lastIndexOf(File.separator));
                        String outputFile = tempmovie_root + contatempvideo + ".avi";

                        //command4[4] = "" + LODEConstants.ITUNESU_SLIDE_DURATION;
                        //command4[6] = lec.getAcquisitionPath() + LODEConstants.FS + LODEConstants.SLIDES_FILE + onets.fileName.substring(1 + onets.fileName.lastIndexOf(File.separator));
                        //command4[13] = tempmovie_root + contatempvideo + ".avi";
                        int slideDuration = LODEConstants.ITUNESU_SLIDE_DURATION;
                        if (i < ts.size()) {
                            try {
                                int nextslidetime = Integer.parseInt(ts.get(i).time);
                                if ((nextslidetime - currenttimeslide) < LODEConstants.ITUNESU_SLIDE_DURATION) {
                                    slideDuration = nextslidetime - currenttimeslide;
                                }
                            } catch (Exception e) {
                                m.log(e.getMessage());
                            }
                        } else if ((Integer.parseInt(lec.getVideoLength()) - currenttimeslide) < LODEConstants.ITUNESU_SLIDE_DURATION) {
                            slideDuration = (Integer.parseInt(lec.getVideoLength()) - currenttimeslide);
                        }
                        lastCorrectionTime = lastCorrectionTime + slideDuration;
                        String command4[] = _FFMPEG_ITUNESU_createVideoSlide("" + slideDuration, inputFile, outputFile);
                        commands.add(command4);
                        contatempvideo++;
                        //contaslide++;
                        timeprevslide = Integer.parseInt(onets.time);

                    }
                    //==========================================================
                    //3. create the very last piece of video (after last slide)
                    if ((Integer.parseInt(lec.getVideoLength()) - timeprevslide) > LODEConstants.ITUNESU_SLIDE_DURATION) {
                        int startTime = 0;
                        if (timeprevslide > -1) {
                            //command5[9] = "" + (LODEConstants.ITUNESU_SLIDE_DURATION + timeprevslide);
                            startTime = LODEConstants.ITUNESU_SLIDE_DURATION + timeprevslide;
                        } /*
                         * else { command5[9] = "0:0:0"; }
                         */
                        String outputFile = tempmovie_root + contatempvideo + ".avi";
                        String command5[] = _FFMPEG_ITUNESU_cutLastVideoFragment(videoPath, "" + startTime, outputFile);

                        /*
                         * if (timeprevslide > -1) { command5[9] = "" +
                         * (Integer.parseInt(onets.time) - timeprevslide + 5); }
                         * else { command5[9] = onets.time;
                        }
                         */
                        //command5[10] = tempmovie_root + contatempvideo + ".avi";
                        commands.add(command5);
                    } else {
                        contatempvideo--;
                    }
                    // =========================================================
                    //4. merge avi's files with avimerge

                    Vector<String> allmoviesvec = new Vector<String>();
                    allmoviesvec.add(LODEConstants.FFMPEG_AVI_MERGE); //MAC ONLY
                    allmoviesvec.add("-o");
                    allmoviesvec.add(tempmoviefinal);
                    System.err.println(tempmoviefinal);
                    allmoviesvec.add("-i");
                    for (int i = 0; i <= contatempvideo; i++) {
                        String tempmovie = tempmovie_root + i + ".avi";
                        System.err.println(tempmovie);
                        allmoviesvec.add(tempmovie);

                    }
                    // mencoder -ovc copy -oac copy -o <output filename>.avi <input file1>.avi <input file2>.avi etc.
                    /*
                     * Vector<String> allmoviesvec = new Vector<String>();
                     * allmoviesvec.add(LODEConstants.FFMPEG_MENCODER); //MAC
                     * ONLY allmoviesvec.add("-ovc"); allmoviesvec.add("copy");
                     * allmoviesvec.add("-oac"); allmoviesvec.add("copy");
                     * allmoviesvec.add("-o"); allmoviesvec.add(tempmoviefinal);
                     * System.err.println(tempmoviefinal);
                     * allmoviesvec.add("-i"); for (int i = 0; i <=
                     * contatempvideo; i++) { String tempmovie=tempmovie_root +
                     * i + ".avi"; System.err.println(tempmovie);
                     * allmoviesvec.add(tempmovie);
                     *
                     * }
                     *
                     */
                    String[] command6 = new String[allmoviesvec.size()];
                    for (int i = 0; i < allmoviesvec.size(); i++) {
                        command6[i] = allmoviesvec.elementAt(i);

                    }
                    commands.add(command6);

                    // =========================================================
                    //5. join audio and video streams
                    String itunesuMovie = tempmovie_root + "_final.mp4";
                    String command7[] = _FFMPEG_ITUNESU_joinAudioVideo(tempmoviefinal, audioonly, itunesuMovie);
                    //command7[16] = videoPath + ".mp4";
                    commands.add(command7);
                }

            }
//MARCO            final Vector<Process> vect_process = new Vector<Process>();
//MARCO            final Vector<ProcessLoggerWindow> vect_process_logger = new Vector<ProcessLoggerWindow>();

            //ProgressBar pb = ProgressBar.showIndeterminateProgressBar("Please wait - executing iPhone postprocessing... it may takes a long time");
            ProgressBar pb = ProgressBar.getProgressBar();

            for (int i = 0; i < commands.size(); i++) {
                /*
                 * Process depproc =
                 * RunnableProcess.launch(commands.elementAt(i), true, false);
                 * try { depproc.waitFor(); vect_process.add(depproc);
                 * ProcessLoggerWindow depproclog =
                 * ProcessLoggerWindow.getProcessLogger(vect_process.get(i));
                 *
                 * vect_process_logger.add(depproclog); } catch (Exception e)
                 * {System.out.println(e.getMessage());e.printStackTrace(); }
                 */
                pb.showProgress("Postprocessing for iTunesU (it may take a while)", i + 1, commands.size());
                _execCommand(commands.elementAt(i));

            }


            //final ProgressBar pb = ProgressBar.showIndeterminateProgressBar("Please wait - executing iPhone convert");

            //final Process p = RunnableProcess.launch(megaarray, true, false);
            //final ProcessLoggerWindow logger = ProcessLoggerWindow.getProcessLogger(p);
            //final ProgressBar pb = ProgressBar.showIndeterminateProgressBar("Please wait - executing iPhone convert");
            ////ProcessWaiter waitForProcess=new ProcessWaiter(p,"ffmpeg pass is complete");


            /*
             * Thread waitForProcess = new Thread(new Runnable() {
             *
             * public void run() { try { for (int i = 0; i <
             * vect_process.size(); i++) { vect_process.elementAt(i).waitFor();
             * }
             *
             * } catch (InterruptedException ex) {
             * ex.printStackTrace(m.getLogger()); m.w("Error while writing
             * output of Process of iPhone convert" , LODEConstants.MSG_ERROR);
             * } pb.closeWindow(); //logger.setVisible(false);
             * //addFlvMetadata(videoPath + ".flv", createDistribution,
             * showFinalDialog); } ;
            });
             */
            //waitForProcess.start();

            //6. remove temporary files TODO
            //MARCO TEST fileSystemManager.recursiveDelete(new File(tempPath));
            createItunesuDistribution(lec);
            m.w("end execution of postprocessing for iPhone" + (new Date()));
            m.log("end execution of postprocessing for iPhone" + (new Date()));
            pb.closeWindow();
            retval = true;
        }

        /*
         * synchronized (instance) { try { // this lock will be released at the
         * end of createDistribution // that is launched in another thread
         * generated by compressVideo above. System.out.println((new Date()) + "
         * Starting to wait for postprocessing to be completed"); wait();
         * System.out.println((new Date()) + " Finished waiting - postprocessing
         * is complete"); } catch (InterruptedException ex) {
         * ex.printStackTrace(Messanger.getInstance().getLogger()); } } }
        }
         */


        return retval;
    }

    private void _execCommand(String[] cmd) {
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                m.log(line);
            }
            int exitValue = process.waitFor();

        } catch (Exception e) {
            m.w(e.getMessage());
            m.log(e.getMessage());
        }

    }


    /*
     * @Override public final boolean convertVideo4Ituneu_allSlides(Lecture lec)
     * { //Antonio
     *
     * boolean retval = true;
     *
     * if (lec.hasBeenPostProcessedForItunesu()) {
     *
     * int response = JOptionPane.showConfirmDialog(null, "This lecture seems to
     * be already postprocessed for iTunesU \n" + "Would you like to rebuild
     * this process (yes/no)\n"); if (response == 0) {
     * lec.setHasBeenPostProcessed4utunesu(false); } else { m.w(" Lecture " +
     * lec.getLectureName() + " does not need iTunesU postprocessing"); } }
     *
     * if ((lec.hasVideo() || lec.hasSlides()) &&
     * !lec.hasBeenPostProcessedForItunesu()) { String videoPath =
     * lec.getAcquisitionPath() + LODEConstants.FS + LODEConstants.MOVIE_FILE +
     * "0"; String tempPath = LODEConstants.TEMP_DIR;
     * fileSystemManager.createFolder(new File(tempPath));
     * fileSystemManager.createFolder(new File(tempPath + LODEConstants.FS +
     * lec.getDirName()));
     *
     * m.log("start execution" + (new Date())); Vector<String[]> commands = new
     * Vector<String[]>();
     *
     * m.w((new Date()) + " Start processing lecture for iTunesU - " +
     * lec.getLectureName()); m.log((new Date()) + " Start processing lecture
     * for iTunesU - " + lec.getLectureName());
     *
     * if (!(lec.hasVideo())) { m.w(" Lecture " + lec.getLectureName() + " -
     * only slides will be postprocessed for iTunesU"); } // TODO CASE 1 : add
     * postprocess with slides only else if (!lec.hasSlides()) { // CASE 2: add
     * postprocess with video only m.w(" Lecture " + lec.getLectureName() + " -
     * only video will be postprocessed for iTunesU"); String command0[] = {
     * LODEConstants.FFMPEG_COMMAND, //0 "-i", videoPath, //1-2 "-y", //3
     * "-vcodec", "h264", //4-5 "-acodec", "aac", //6-7 "-b",
     * LODEConstants.ITUNESU_BIT_RATE,//8-9 "-r",
     * LODEConstants.ITUNESU_FRAME_RATE,//10-11 "OUTPUT_FILE" //12 };
     * command0[12] = videoPath + ".mp4"; commands.add(command0); } else {
     * //CASE 3: process video and slides for 5 seconds
     *
     * //1. extract audio only //m.w("Extracting audio from original movie ",
     * LODEConstants.MSG_GENERIC); String command[] = {
     * LODEConstants.FFMPEG_COMMAND, //0 "-i", "INPUT_FILE", //1-2 "-y", //3
     * "-f", "mp3", //4-5 //"-acodec","aac", "-vn", "OUTPUT_FILE" //6-7 };
     * command[2] = videoPath; command[7] = tempPath + LODEConstants.FS +
     * lec.getDirName() + LODEConstants.FS + "audioonly.mp3"; //command[7] =
     * tempPath + LODEConstants.FS + lec.getDirName() + LODEConstants.FS +
     * "audioonly.aac"; commands.add(command);
     *
     *
     * lec.resumeSlides(); //2. for each slide, split video and insert slide for
     * 5 seconds if (lec.hasSlides()) {
     *
     * int contatempvideo = 0; int contaslide = 1; int timeprevslide = -1;
     *
     * ArrayList<TimedSlide> ts = lec.getTimedSlides().slideList; TimedSlide
     * onets = ts.get(0); int tempo1; try { tempo1 =
     * Integer.parseInt(onets.time); if (tempo1 < 0) { tempo1 = 0; }
     *
     * } catch (Exception e) { tempo1 = 0; } if (tempo1 == 0) { String
     * command2[] = { LODEConstants.FFMPEG_COMMAND, //0 "-f", "mjpeg", //1-2
     * "-itsoffset", "TIMESLIDE", //3-4 "-i", "INPUT_FILE", //5-6 "-y", //7
     * "-s", "640x480", //8-9 "-an", //10 "-r",
     * LODEConstants.ITUNESU_FRAME_RATE,//11-12 //"-vcodec","h264",//13-14
     * "OUTPUT_FILE" //13 }; command2[4] = LODEConstants.ITUNESU_SLIDE_DURATION;
     * command2[6] = lec.getAcquisitionPath() + LODEConstants.FS +
     * LODEConstants.SLIDES_FILE + onets.fileName.substring(1 +
     * onets.fileName.lastIndexOf(File.separator)); command2[13] = tempPath +
     * LODEConstants.FS + lec.getDirName() + LODEConstants.FS + "tempmovie" +
     * contatempvideo + ".avi"; commands.add(command2); contatempvideo++;
     * contaslide++; if (ts.size() > 1) { try { int secondslidetime =
     * Integer.parseInt(ts.get(1).time); // if (secondslidetime <
     * Integer.parseInt(LODEConstants.ITUNESU_SLIDE_DURATION)) { command2[4] =
     * ts.get(1).time; //} } catch (Exception e) { m.log(e.getMessage()); } }
     *
     * timeprevslide = 0; }
     *
     * int lastCorrectionTime = 0; double addtime = 0; DecimalFormat myformat =
     * new DecimalFormat("#######.0", new DecimalFormatSymbols(new
     * Locale("en")));
     *
     * for (int i = contaslide; i <= ts.size(); i++) { onets = ts.get(i - 1);
     *
     * int currenttimeslide = 0; try { currenttimeslide =
     * Integer.parseInt(onets.time); } catch (Exception e) {
     * m.log(e.getMessage()); }
     *
     * /* if (!(((currenttimeslide - timeprevslide) <=
     * Integer.parseInt(LODEConstants.ITUNESU_SLIDE_DURATION)) && (timeprevslide
     * > -1))) { String command3[] = {
     * "/Applications/ffmpegX.app/Contents/Resources/ffmpeg", //0 "-i",
     * videoPath, //1-2 "-y", //3 "-sameq", "-an", //4-5 "-r",
     * LODEConstants.ITUNESU_FRAME_RATE, //6-7 //"-b", "250", "-t", "TIME",
     * //8-9 "-ss", "TIME2", //10-11 "OUTPUT_FILE" //12 };
     *
     * if (timeprevslide == 0) {
     *
     * //command3[11] = "0:0:5"; command3[11] =
     * LODEConstants.ITUNESU_SLIDE_DURATION; } else if (timeprevslide == -1) {
     * //command3[11] = "0:0:0"; command3[11] = "0"; } //else command2[9] =
     * "0:0:"+(5+timeprevslide); else { //command3[11] = "" + (5 +
     * timeprevslide); command3[11] = "" +
     * (Integer.parseInt(LODEConstants.ITUNESU_SLIDE_DURATION) + timeprevslide);
     * }
     *
     * if (timeprevslide == 0) { command3[9] = "" +
     * (Integer.parseInt(onets.time) - (timeprevslide +
     * Integer.parseInt(LODEConstants.ITUNESU_SLIDE_DURATION))); } else if
     * (timeprevslide == -1) { command3[9] = onets.time; } else { command3[9] =
     * "" + (Integer.parseInt(onets.time) - (timeprevslide +
     * Integer.parseInt(LODEConstants.ITUNESU_SLIDE_DURATION))); }
     *
     *
     *
     * if (lastCorrectionTime > 230) {
     * addtime=addtime+((lastCorrectionTime/230)*0.1);
     *
     *
     * //command3[11] = myformat.format(Float.parseFloat(command3[11])+addtime);
     *
     * lastCorrectionTime = 0; } else { lastCorrectionTime = lastCorrectionTime
     * + Integer.parseInt(command3[9]); } command3[11] =
     * myformat.format(Double.parseDouble(command3[11])+addtime);
     *
     * command3[12] = tempPath + LODEConstants.FS + lec.getDirName() +
     * LODEConstants.FS + "tempmovie" + contatempvideo + ".avi";
     * commands.add(command3); contatempvideo++; }
     *
     * /
     * String command4[] = { LODEConstants.FFMPEG_COMMAND, //0 "-f", "mjpeg",
     * //1-2 "-itsoffset", "TIMESLIDE", //3-4 "-i", "INPUT_FILE", //5-6 "-y",
     * //7 "-s", "640x480", //8-9 "-an", //10 "-r",
     * LODEConstants.ITUNESU_FRAME_RATE, //11-12 //"-vframes","100",//13-14
     * "OUTPUT_FILE" //13 }; command4[4] = LODEConstants.ITUNESU_SLIDE_DURATION;
     * command4[6] = lec.getAcquisitionPath() + LODEConstants.FS +
     * LODEConstants.SLIDES_FILE + onets.fileName.substring(1 +
     * onets.fileName.lastIndexOf(File.separator)); command4[13] = tempPath +
     * LODEConstants.FS + lec.getDirName() + LODEConstants.FS + "tempmovie" +
     * contatempvideo + ".avi";
     *
     * if (i < ts.size()) { try { int nextslidetime =
     * Integer.parseInt(ts.get(i).time); // if ((nextslidetime -
     * currenttimeslide) <
     * Integer.parseInt(LODEConstants.ITUNESU_SLIDE_DURATION)) { command4[4] =
     * Integer.toString(nextslidetime - currenttimeslide); //} } catch
     * (Exception e) { m.log(e.getMessage()); } } else //if
     * ((Integer.parseInt(lec.getVideoLength()) - currenttimeslide) <
     * Integer.parseInt(LODEConstants.ITUNESU_SLIDE_DURATION)) { { command4[4] =
     * Integer.toString(Integer.parseInt(lec.getVideoLength()) -
     * currenttimeslide); } //} lastCorrectionTime = lastCorrectionTime +
     * Integer.parseInt(command4[4]); commands.add(command4); contatempvideo++;
     * //contaslide++; timeprevslide = Integer.parseInt(onets.time);
     *
     * }
     *
     * //3. create the very last piece of video (after last slide)
     *
     * /* if ((Integer.parseInt(lec.getVideoLength()) - timeprevslide) >
     * Integer.parseInt(LODEConstants.ITUNESU_SLIDE_DURATION)) { String
     * command5[] = { "/Applications/ffmpegX.app/Contents/Resources/ffmpeg", //0
     * "-i", videoPath, //1-2 "-y", //3 "-sameq", "-an", //4-5 "-r",
     * LODEConstants.ITUNESU_FRAME_RATE, //6-7 //"-b", "250",//6-7 //"-t",
     * "TIME", //8-9 "-ss", "TIME2", //8-9 "OUTPUT_FILE" //20 };
     *
     * if (timeprevslide > -1) { command5[9] = "" +
     * (Integer.parseInt(LODEConstants.ITUNESU_SLIDE_DURATION) + timeprevslide);
     * } else { command5[9] = "0:0:0"; }
     *
     *
     * command5[10] = tempPath + LODEConstants.FS + lec.getDirName() +
     * LODEConstants.FS + "tempmovie" + contatempvideo + ".avi";
     * commands.add(command5); } else { contatempvideo--; }* /
     *
     * //4. merge avi's files with avimerge contatempvideo--; Vector<String>
     * allmoviesvec = new Vector<String>();
     * allmoviesvec.add(LODEConstants.FFMPEG_AVI_MERGE); allmoviesvec.add("-o");
     * allmoviesvec.add(tempPath + LODEConstants.FS + lec.getDirName() +
     * LODEConstants.FS + "tempmoviefinal.avi"); allmoviesvec.add("-i"); for
     * (int i = 0; i <= contatempvideo; i++) { allmoviesvec.add(tempPath +
     * LODEConstants.FS + lec.getDirName() + LODEConstants.FS + "tempmovie" + i
     * + ".avi");
     *
     * }
     *
     * String[] command6 = new String[allmoviesvec.size()]; for (int i = 0; i <
     * allmoviesvec.size(); i++) { command6[i] = allmoviesvec.elementAt(i);
     *
     * }
     * commands.add(command6);
     *
     * //5. join audio and video streams
     *
     * String command7[] = { LODEConstants.FFMPEG_COMMAND, //0 "-i", tempPath +
     * LODEConstants.FS + lec.getDirName() + LODEConstants.FS +
     * "tempmoviefinal.avi", //1-2 "-i", tempPath + LODEConstants.FS +
     * lec.getDirName() + LODEConstants.FS + "audioonly.mp3", //3-4 "-s",
     * "640x480",//5-6 "-r", LODEConstants.ITUNESU_FRAME_RATE,//7-8 "-b",
     * LODEConstants.ITUNESU_BIT_RATE,//9-10 "-vcodec", "mpeg4",//11-12
     * "-acodec", "aac",//13-14 "-y", //15 "OUTPUT_FILE" //16 }; command7[16] =
     * videoPath + ".mp4"; commands.add(command7);
     *
     *
     *
     * }
     *
     * }
     * //MARCO final Vector<Process> vect_process = new Vector<Process>();
     * //MARCO final Vector<ProcessLoggerWindow> vect_process_logger = new
     * Vector<ProcessLoggerWindow>();
     *
     * //ProgressBar pb = ProgressBar.showIndeterminateProgressBar("Please wait
     * - executing iPhone postprocessing... it may takes a long time");
     * ProgressBar pb = ProgressBar.getProgressBar();
     *
     * for (int i = 0; i < commands.size(); i++) { /* Process depproc =
     * RunnableProcess.launch(commands.elementAt(i), true, false); try {
     * depproc.waitFor(); vect_process.add(depproc); ProcessLoggerWindow
     * depproclog = ProcessLoggerWindow.getProcessLogger(vect_process.get(i));
     *
     * vect_process_logger.add(depproclog); } catch (Exception e)
     * {System.out.println(e.getMessage());e.printStackTrace(); } /
     *
     * try { pb.showProgress("Postprocessing for iTunesU (it may take a while) -
     * STEP ", i + 1, commands.size()); ProcessBuilder builder = new
     * ProcessBuilder(commands.elementAt(i)); builder.redirectErrorStream(true);
     * Process process = builder.start();
     *
     * InputStream is = process.getInputStream(); InputStreamReader isr = new
     * InputStreamReader(is); BufferedReader br = new BufferedReader(isr);
     * String line; while ((line = br.readLine()) != null) { m.log(line); }
     *
     *
     * } catch (Exception e) { m.w(e.getMessage()); m.log(e.getMessage()); }
     *
     * }
     *
     *
     * //final ProgressBar pb = ProgressBar.showIndeterminateProgressBar("Please
     * wait - executing iPhone convert");
     *
     * //final Process p = RunnableProcess.launch(megaarray, true, false);
     * //final ProcessLoggerWindow logger =
     * ProcessLoggerWindow.getProcessLogger(p); //final ProgressBar pb =
     * ProgressBar.showIndeterminateProgressBar("Please wait - executing iPhone
     * convert"); ////ProcessWaiter waitForProcess=new ProcessWaiter(p,"ffmpeg
     * pass is complete");
     *
     *
     * /*
     * Thread waitForProcess = new Thread(new Runnable() {
     *
     * public void run() { try { for (int i = 0; i < vect_process.size(); i++) {
     * vect_process.elementAt(i).waitFor(); }
     *
     * } catch (InterruptedException ex) { ex.printStackTrace(m.getLogger());
     * m.w("Error while writing output of Process of iPhone convert" ,
     * LODEConstants.MSG_ERROR); } pb.closeWindow(); //logger.setVisible(false);
     * //addFlvMetadata(videoPath + ".flv", createDistribution,
     * showFinalDialog); } ; });* / //waitForProcess.start();
     *
     * //6. remove temporary files TODO fileSystemManager.recursiveDelete(new
     * File(tempPath)); createItunesuDistribution(lec); m.w("end execution of
     * postprocessing for iTunesU" + (new Date())); m.log("end execution of
     * postprocessing for iTunesU" + (new Date())); pb.closeWindow(); retval =
     * true; }
     *
     * /*
     * synchronized (instance) { try { // this lock will be released at the end
     * of createDistribution // that is launched in another thread generated by
     * compressVideo above. System.out.println((new Date()) + " Starting to wait
     * for postprocessing to be completed"); wait(); System.out.println((new
     * Date()) + " Finished waiting - postprocessing is complete"); } catch
     * (InterruptedException ex) {
     * ex.printStackTrace(Messanger.getInstance().getLogger()); } } } }* /
     *
     *
     * return retval; }
     *
     */
// =============================================================================
// DEFINIZIONE DEI COMANDI FFMPEG USATI
// =============================================================================

    private String[] _FFMPEG_ITUNESU_onlyVideo(String videoPath) {
        String cmd[] = {
            LODEConstants.FFMPEG_COMMAND, //0
            "-i", videoPath, //1-2 -i: input file name
            "-y", //3 -y:Overwrite output files
            "-vcodec", "h264", //4-5 -vcodec: Force video codec to codec
            "-acodec", "aac", //6-7 -acodec: Force audio codec to codec.
            "-b", LODEConstants.ITUNESU_BIT_RATE,//8-9 -b: Set the video bitrate in bit/s (default = 200 kb/s)
            "-r", LODEConstants.ITUNESU_FRAME_RATE,//10-11 -r: Set frame rate (Hz value, fraction or abbreviation), (default = 25)
            "OUTPUT_FILE" //12
        };
        printcmd(cmd);
        return cmd;

    }

    private String[] _FFMPEG_ITUNESU_extractAudioTrack(String inputFile, String outputFile) {
        String cmd[] = {
            LODEConstants.FFMPEG_COMMAND, //0
            "-i", inputFile, //1-2 -i: input file name
            "-y", //3 -y:Overwrite output files
            "-f", "mp3", //4-5 -f: force format
            //"-acodec","aac",
            "-vn", //6 Disable video recording.
            outputFile //7
        };
        printcmd(cmd);
        return cmd;
    }

    private String[] _FFMPEG_ITUNESU_createVideoSlide(String timeslide, String inputFile, String outputFile) {
        String cmd[] = {
            LODEConstants.FFMPEG_COMMAND, //0
            "-f", "mjpeg", //1-2  -f: force format
            "-itsoffset", timeslide, //3-4 Set the input time offset in seconds. This option affects all the input files that follow it. The offset is added to the timestamps of the input files. Specifying a positive offset means that the corresponding streams are delayed by 'offset' seconds.
            "-i", inputFile, //5-6 -i: input file name
            "-y", //7 -y: Overwrite output files.
            "-s", "640x480", //8-9 -s: Set frame size
            "-an", //10 -an: Disable audio recording
            "-r", LODEConstants.ITUNESU_FRAME_RATE,//11-12 -r: Set frame rate (Hz value, fraction or abbreviation), (default = 25).
            //"-vcodec","h264",//13-14
            //"-vframes","100",//13-14
            outputFile //13
        };
        printcmd(cmd);
        return cmd;
    }

    private String[] _FFMPEG_ITUNESU_cutVideoFragment(String videoPath, String startTime, String duration, String outputFile) {
        String cmd[] = {
            LODEConstants.FFMPEG_COMMAND, //0
            "-i", videoPath + ".mp4", //1-2 -i: input file name
            "-y", //3 -y: Overwrite output files.
            "-sameq", //4 Use same video quality as source
            "-s", "640x480",// 12-13   "320x240", -s: Set frame size. The format is wxh
            "-an", //5 no audio
            "-r", LODEConstants.ITUNESU_FRAME_RATE, //6-7 -r: Set frame rate (Hz value, fraction or abbreviation), (default = 25)
            //"-b", "250",
            "-t", duration, //8-9 Restrict the transcoded/captured video sequence to the duration specified in seconds.
            "-ss", startTime, //10-11 Seek to given time position in seconds
            outputFile //12
        };
        System.out.println(" => STARTTIME = " + startTime + " DURATION = " + duration);
        printcmd(cmd);
        return cmd;
    }

    private String[] _FFMPEG_ITUNESU_cutLastVideoFragment(String videoPath, String startTime, String outputFile) {
        String cmd[] = {
            LODEConstants.FFMPEG_COMMAND, //0
            "-i", videoPath + ".mp4", //1-2 -i: input file name
            "-y", //3 -y: Overwrite output files.
            "-sameq", //4 Use same video quality as source
            "-s", "640x480",// 12-13   "320x240", -s: Set frame size. The format is wxh
            "-an", //5 no audio
            "-r", LODEConstants.ITUNESU_FRAME_RATE, //6-7 -r: Set frame rate (Hz value, fraction or abbreviation), (default = 25)
            //"-b", "250",
            //"-t", "TIME", //8-9
            "-ss", startTime, //8-9 Seek to given time position in seconds
            outputFile //10
        };
        printcmd(cmd);
        return cmd;
    }

    private String[] _FFMPEG_ITUNESU_joinAudioVideo(String inputfile, String audioonly, String outputFile) {
        String cmd[] = {
            LODEConstants.FFMPEG_COMMAND, //0
            "-i", inputfile, //1-2 -i: input file name
            "-i", audioonly, //3-4 -i: input file name
            "-s", "640x480",//5-6 -s: Set frame size
            "-r", LODEConstants.ITUNESU_FRAME_RATE,//7-8 -r: Set frame rate (Hz value, fraction or abbreviation), (default = 25)
            "-b", LODEConstants.ITUNESU_BIT_RATE,//9-10 -b: Set the video bitrate in bit/s (default = 200 kb/s)
            "-vcodec", "mpeg4",//11-12 -vcodec: Force video codec to codec
            "-acodec", "aac",//13-14 -acodec: Force audio codec to codec.
            "-y", //15 -y: Overwrite output files
            outputFile //16
        };
        printcmd(cmd);
        return cmd;
    }

    private void printcmd(String a[]) {
        for (int k = 0; k < a.length; k++) {
            System.out.print(a[k] + " ");
        }
        System.out.println(a);
        System.out.flush();
    }
}
