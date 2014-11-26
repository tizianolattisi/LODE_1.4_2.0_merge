package it.unitn.LODE.gui;

import it.unitn.LODE.Controllers.ActionController;
import it.unitn.LODE.Controllers.ControllersManager;
import it.unitn.LODE.MP.IF.Controllers.VideoControllerIF;
import it.unitn.LODE.MP.constants.LODEConstants;
import it.unitn.LODE.MP.factories.VideoControllerFactory;
import it.unitn.LODE.Models.Lecture;
import it.unitn.LODE.Models.ProgramState;
import it.unitn.LODE.utils.FileSystemManager;
import it.unitn.LODE.utils.Messanger;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;

//============= ADAPTERS =======================================================
public final class AcquisitionWindow extends JPanel implements ChangeListener, MouseListener, WindowListener, KeyListener {

    ProgramState programState = null;
    // ========= MISCELLANEA ===================================================
    int current_movie_time;
    int w, h, renderingMode;
    public final String temp_video_path = LODEConstants.TEMP_DIR;
    boolean window_opened = false;
    private final String labelPrefix = "Number of button clicks: ";
    public int NUM_IMAGES = 0;
    private boolean isPanelInited = false;
    private boolean hasExternalWindow = false;

    public boolean hasExternalWindow() {
        return hasExternalWindow;
    }
    // ====== GRAPHIC COMPONENTS ===============================================
    private JFrame externalWindow = null;
    private Rectangle externalWindowRectangle = null;
    private JFrame frame;
    public JButton play_button, rec_button, pause_button, stop_button;
    public JLabel status;
    public JPanel jPVideo = new JPanel();
    public SoundPanel audioPanel = null;
    public JPanel recordingAlert = new JPanel() {

        {
            add(new JLabel("<HTML><FONT size='18' BGCOLOR='YELLOW' COLOR='RED'>RECORDING</FONT></HTML>"));
            setVisible(false);
        }
    };
    JButton add_button, go_button, slide_down_button, slide_up_button;
    JButton b_add_at_time, b_delete_at_time;
    JTextField goto_field;
    JLabel slide_C, slide_P1, slide_P2, slide_P3, slide_N, slide_N2a, slide_N2b;
    JLabel slide_C_label, slide_N_label, slide_P1_label, slide_P2_label, slide_P3_label, slide_N2a_label, slide_N2b_label;
    JSlider slider;
    JTextArea logPresenter;
    private final JLabel label = new JLabel(labelPrefix + "0    ");
    Component videoAsComponent = null;
    Action selectLine;
    ActionController actionController = null;
    // ========= SLIDE MANAGEMENT ==============================================
    ImageIcon[] slides;
    ImageIcon[] slides_big;
    ImageIcon[] slides_fullScreen;
    ImageIcon emptySlide = null;
    private int current_slide_number;
    public int next_slide_number;
    private int p1_slide_number;
    private int p2_slide_number;
    private int p3_slide_number;
    private int n2a_slide_number;
    private int n2b_slide_number;
    //======= USED SINGLETONS ================================================== 
    private FileSystemManager fileSystemManager = null;
    private VideoControllerIF videoController = null;
    // ========== SOLITON PATTERN ==============================================
    static private AcquisitionWindow instance = null;

    public static void createAndShow() {
        if (!ProgramState.getInstance().isLectureDefined) {
            System.err.println("AcquisitionWindow cannot be opened unless a lecture is defined!");
            System.exit(1);
        }
        instance = new AcquisitionWindow();
        // register the acquisition Window with the ControllersManager
        ControllersManager.getinstance().setAcquistionWindow(instance);
        instance.showWindow();
    }

    public void clearInstance() {
        final ProgressBar pb = ProgressBar.showIndeterminateProgressBar("Please wait while the session gets closed...");
        VideoControllerFactory.getInstance().stopPreview();
        if (frame.isVisible()) {
            frame.setVisible(false);
        }
        ControllersManager.getinstance().unsetAcquisitionWindow();
        pb.closeWindow();
        instance = null;
        externalWindow = null;
    }

    public void clear() {
        //VideoController.getInstance().stopAudioLevelMonitor();
    }

    private void _acquireExternalWindow() {
        if (externalWindow != null) {
            externalWindow.setVisible(false);
            externalWindow.dispose();
            externalWindow = null;
        }
        //JFrame f = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice sd[] = ge.getScreenDevices();
        for (GraphicsDevice d : sd) {
            if (d.equals(ge.getDefaultScreenDevice())) {
                continue;
            }

            GraphicsConfiguration[] gc = d.getConfigurations();
            //for (int i = 0; i < gc.length; i++) {
            externalWindowRectangle = d.getDefaultConfiguration().getBounds();
            int xoffs = externalWindowRectangle.width;
            int yoffs = externalWindowRectangle.height;
            externalWindow = new JFrame(d.getDefaultConfiguration());
            externalWindow.setFocusableWindowState(false);
            //Window[] ws = f.getWindows();
            externalWindow.setUndecorated(true);
            JPanel p = new JPanel();
            externalWindow.setContentPane(p);
            p.setSize(externalWindow.getSize());
            p.setLayout(new FlowLayout());
            p.add(new JButton("Welcome to LODE"));
            p.setBackground(Color.BLUE);
            externalWindow.setSize(xoffs, yoffs);
            externalWindow.setVisible(true);
            //}
            //}
            break; // use the first non-default graphic device as the second secreen
        }
        hasExternalWindow = externalWindow != null;
        if (!hasExternalWindow) {
            externalWindowRectangle = new Rectangle(1024, 768);
        }
        //return f;
    }

    public Rectangle getExternalWindowRectangle() {
        if (hasExternalWindow) {
            return externalWindowRectangle;
        } else {
            return null;
        }
    }
    //============== COSTRUTTORE E SUOI HELPERS ================================

    private AcquisitionWindow() {
        super(new GridBagLayout());
        System.out.println("create new Acqusition window");
        fileSystemManager = ControllersManager.getinstance().getFileSystemManager();
        programState = ProgramState.getInstance();
        frame = new JFrame("LODE Video Production System");
        actionController = ControllersManager.getinstance().getActionController();
        isPanelInited = false;
        initializeVariables();
        initVideo();
        initPanel(); //prepare the graphics - but without video yet
    }

    /**
     * add video to the vindow
     */
    public void showWindow() {
        MenuManager menuGenerator = MenuManager.getInstance();
        //frame.setMenuBar(menuGenerator.getMenuBar());
        VideoControllerFactory.getInstance().startPreview(jPVideo,audioPanel);
        programState.setVideoInited(true);
        frame.setMenuBar(menuGenerator.createMenuBar());
        redrawElements();
        showFrame();
        MenuManager.getInstance().updateMenuState();
        this.requestFocus();
    }

    // ==========================================================================
    /*
     * public final void setUpMainPanel() { initializeVariables(); // if it is
     * the first time, initialize panel and frame // else reset its state if
     * (!isPanelInited) { initPanel(); } else { resetVideo();
     * resetRecordingButtons(); logPresenter.setText(""); }
     * programState.setVideoInited(true); redrawElements(); showFrame();
     * MenuManager.getInstance().updateMenuState(); }
     *
     */
    private void initializeVariables() {
        _acquireExternalWindow();
        slides = new ImageIcon[300];
        slides_big = new ImageIcon[300];
        slides_fullScreen = new ImageIcon[300];
        //label.setText("INFO: "+NUM_IMAGES+" slides in "+ slidesPath);
        current_slide_number = 0;
        next_slide_number = 0;
        p1_slide_number = next_slide_number - 3;
        p2_slide_number = next_slide_number - 2;
        p3_slide_number = next_slide_number - 1;
        n2a_slide_number = next_slide_number + 1;
        n2b_slide_number = next_slide_number + 2;
        current_movie_time = 0;
        current_slide_number = 0;
        NUM_IMAGES = 0;
    }

    /**
     * A run-only-once initializaztion of the graphic panel
     */
    private void initPanel() {
        isPanelInited = true;
        emptySlide = __createImageIcon(LODEConstants.IMGS_PREFIX + "image_empty_resize.gif", "Empty picture");
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        _addSlideSlots(c); //================ SLIDESF
        _addSlideButtons(c); //==== ADD and GOTO buttons and field
        _addTimeLogger(c);
        _addSlider(c);
        _addSRecordingAlert(c);

        //INFO label
        c.insets = new Insets(650, 30, 0, 0);
        add(label, c);
        _addSoundPanel(c);
        _addVideo(c); // ============= VIDEO SECTION
        //BACKGROUND IMAGE 
        ImageIcon bgr = __createImageIcon(LODEConstants.IMGS_PREFIX + "bgr.jpg", "The background image");
        c.insets = new Insets(0, 0, 0, 0);
        add(new JLabel(bgr), c); // picture label

        //redrawElements();
        frame.setFocusable(true); // this is needed to be able to capture key events
        frame.addKeyListener(this);
        //listenToKeyPressed();
        // setup and show window
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(this);
//        frame.setMenuBar(MenuManager.getInstance().createMenuBar());
        //else frame.setJMenuBar(jmenuGenerator.createMenuBar());
        setOpaque(true); //content panes must be opaque
        frame.setContentPane(this);
        frame.pack();
        frame.setLocation(275, 0);
        //VideoController.getInstance().startPreview(jPVideo);
    }

    public final void hideFrame() {
        if (frame.isVisible()) {
            frame.setVisible(false);
        }
    }

    public final void showFrame() {
        if (!frame.isVisible()) {
            frame.setVisible(true);
        }
    }
    // -------------------------------------------------------------------------

    public final void redrawElements() {
        //System.out.println("redrawElements-> LODEConstants.COURSES_HOME="+ LODEConstants.COURSES_HOME);
        String slidesPath = programState.getCurrentLecture().getAcquisitionPath() + File.separator + "Slides" + File.separator;
        window_opened = true;
//        numClicks = 0;

        int i, j, slides_number;

        //File slides_dir = new File (LODEConstants.COURSES_HOME+File.separator+"Slides"+File.separator);
        File slides_dir = new File(slidesPath);
        ProgressBar progressBarFrame = new ProgressBar();
        progressBarFrame.setVisible(true);
        progressBarFrame.pack();

        slides_number = NUM_IMAGES;
        if (slides_dir.exists()) {
            String[] new_slides = slides_dir.list();
            slides_number = new_slides.length;
            NUM_IMAGES = slides_number;

            for (i = 0; i < slides_number; i++) {
                System.gc();
                System.runFinalization();
                j = i + 1;
                progressBarFrame.showProgress("Loading images into LODE", i, slides_number);
                slides[i] = __createScaledImageIcon(slidesPath + j + ".jpg", 100, 82);
                slides_big[i] = __createScaledImageIcon(slidesPath + j + ".jpg", 299, 224);
                //if (hasExternalWindow){
                slides_fullScreen[i] = __createScaledImageIcon(slidesPath + j + ".jpg",
                        externalWindowRectangle.width,
                        externalWindowRectangle.height);
                //}
            }
            slides[slides_number] = emptySlide;
            slides[slides_number + 1] = emptySlide;

        }
        progressBarFrame.closeWindow();
        performSlidesMove(0);
        slide_down_button.setEnabled(NUM_IMAGES != 0);
        slide_up_button.setEnabled(NUM_IMAGES != 0);
        add_button.setEnabled(NUM_IMAGES != 0);
        go_button.setEnabled((NUM_IMAGES != 0));
        b_add_at_time.setEnabled((NUM_IMAGES != 0));
        b_delete_at_time.setEnabled((NUM_IMAGES != 0));
        slider.setEnabled(NUM_IMAGES != 0);
        slider.setMaximum(slides_number - 1);
        slider.setValue(0);
        logPresenter.setText("");
        slide_C.setIcon(emptySlide);

        label.setForeground(Color.white);
        label.setText("Number of slides is " + NUM_IMAGES);
    }

    //========================== VIDEO =========================================
    public final void initVideo() {
        VideoControllerFactory.getInstance();
    }

    public final void resetVideo() {
        /*
        //aggiunge al JPVideo l'oggetto contenente il video
        if (videoAsComponent != null) {
        jPVideo.remove(videoAsComponent);
        }
        videoController = VideoController.getInstance();
        videoController.resetVideo();
        //videoController.startPreview(jPVideo);
        videoAsComponent = videoController.getVideoAsGraphicComponent();
        if (videoAsComponent != null) {
        jPVideo.add("Center", videoAsComponent);
        jPVideo.setEnabled(false);
        jPVideo.setFocusable(false);
        videoAsComponent.setEnabled(false);
        videoAsComponent.setFocusable(false);
        }
        videoController.startPreview(jPVideo);
        }

        public final void disableRecording() {
        if (play_button != null) {
        play_button.setEnabled(false);
        }
        if (rec_button != null) {
        rec_button.setEnabled(false);
        }
        if (pause_button != null) {
        pause_button.setEnabled(false);
        }
        if (stop_button != null) {
        stop_button.setEnabled(false);
        }
         *
         */
    }

    //========================== SLIDES ====================================
    /*
     * private final void resetSlideSlots(){ this.remove(slide_C);
     * this.remove(slide_P1); this.remove(slide_P2); this.remove(slide_P3);
     * this.remove(slide_N); this.remove(slide_N2a); this.remove(slide_N2b); //
     * addSlideSlots(c);
    }
     */
    public final void stateChanged(ChangeEvent e) {
        //Slider changed
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            next_slide_number = (int) source.getValue();
            performSlidesMove(next_slide_number);
        }
    }

    public final void performSlidesMove(int next_slide_number) {
        if (NUM_IMAGES == 0) {
            return;
        }
        //in the general case the values of the small slides
        if (next_slide_number < 0) {
            next_slide_number = 0;
        }
        if (next_slide_number >= NUM_IMAGES) {
            next_slide_number = NUM_IMAGES - 1;
        }
        p1_slide_number = next_slide_number - 3;
        p2_slide_number = next_slide_number - 2;
        p3_slide_number = next_slide_number - 1;
        n2a_slide_number = next_slide_number + 1;
        n2b_slide_number = next_slide_number + 2;

//        if (numClicks == 2) {slide_P1.setIcon(slides[p1_slide_number]); slide_P1_label.setText(String.valueOf(p1_slide_number+1));}
//        if (numClicks > 0) { slide_P2.setIcon(slides[p2_slide_number]); slide_P2_label.setText(String.valueOf(p2_slide_number+1));}
        if (p1_slide_number >= 0 && p1_slide_number < NUM_IMAGES) {
            slide_P1.setIcon(slides[p1_slide_number]);
            slide_P1_label.setText(String.valueOf(p1_slide_number + 1));
        } else {
            slide_P1.setIcon(emptySlide);
            slide_P1_label.setText("");
        }
        if (p2_slide_number >= 0 && p2_slide_number < NUM_IMAGES) {
            slide_P2.setIcon(slides[p2_slide_number]);
            slide_P2_label.setText(String.valueOf(p2_slide_number + 1));
        } else {
            slide_P2.setIcon(emptySlide);
            slide_P2_label.setText("");
        }
        if (p3_slide_number >= 0 && p3_slide_number < NUM_IMAGES) {
            slide_P3.setIcon(slides[p3_slide_number]);
            slide_P3_label.setText(String.valueOf(p3_slide_number + 1));
        } else {
            slide_P3.setIcon(emptySlide);
            slide_P3_label.setText("");
        }

        slide_N.setIcon(slides_big[next_slide_number]);
        slide_N_label.setText("Next Slide: " + (next_slide_number + 1));
        slide_N2a.setIcon(slides[n2a_slide_number]);
        slide_N2a_label.setText(String.valueOf(n2a_slide_number + 1));
        slide_N2b.setIcon(slides[n2b_slide_number]);
        slide_N2b_label.setText(String.valueOf(n2b_slide_number + 1));
        slider.setValue(next_slide_number);
    }

    public final void deleteSlideAtTime() {
        String text_to_delete = logPresenter.getSelectedText();
        if (text_to_delete != null) {
            int start = logPresenter.getSelectionStart();
            int end = logPresenter.getSelectionEnd();
            logPresenter.replaceRange("", start, end + 1);
            //logger.deleteLogLine(text_to_delete);
            StringTokenizer st = new StringTokenizer(text_to_delete, "-");
            int seq = Integer.parseInt(st.nextToken().trim());
            String time = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(time, ":");
            int hours = Integer.parseInt(st1.nextToken().trim());
            int min = Integer.parseInt(st1.nextToken().trim());
            int sec = Integer.parseInt(st1.nextToken().trim());
            programState.getCurrentLecture().removeTimedSlide(seq, " " + ((hours * 60 + min) * 60 + sec));
        } else {
            JOptionPane.showConfirmDialog(null, "You did not select a slide...", "Slide not selected", JOptionPane.OK_OPTION);
        }

    }

    public final void editSlide() {
        int r = JOptionPane.showConfirmDialog(null, "Sorry, this option is not yet implemented...", "Not Yet Impmenented", JOptionPane.OK_OPTION);
    }

    public final void addSlide() {
        current_movie_time = videoController.getTime();
        String reformatted_time = reformatTime(current_movie_time);
        current_slide_number = next_slide_number + 1;
        slide_C.setIcon(slide_N.getIcon());
        slide_C_label.setText("Current Slide: " + (current_slide_number));
        _showImageOnSecondaryScreen(current_slide_number - 1);
        programState.getCurrentLecture().addTimedSlide(current_slide_number, "" + current_movie_time);
        //Write BOTH (video_time and current_slide_number) to a log file and to the screen
        //logger.writeLogLine(current_slide_number, reformatted_time);
        logPresenter.append(current_slide_number + " - " + reformatted_time + "\n");
        goToNextSlide(1); // go to next slide
    }

    private final void _showImageOnSecondaryScreen(int n) {
        if (!hasExternalWindow) {
            return;
        }
        JLabel image = new JLabel();
        image.setIcon(slides_fullScreen[n]);
        JPanel p = (JPanel) (externalWindow.getContentPane());
        p.setLayout(new FlowLayout());
        p.removeAll();
        p.setBackground(Color.yellow);
        p.add(image);
        p.validate();
        p.repaint();
    }

    private final String reformatTime(int time_in_seconds) {
        String time = "";
        if (time_in_seconds > 0) {
            int hours, minutes, seconds, milliseconds;
            //seconds = time_in_milliseconds/1000; milliseconds = time_in_milliseconds % 1000;
            seconds = time_in_seconds;
            minutes = seconds / 60;
            seconds = seconds % 60;
            hours = minutes / 60;
            minutes = minutes % 60;
            if (hours < 10) {
                time = "0" + hours;
            } else {
                time = "" + hours;
            }
            if (minutes < 10) {
                time = time + ":0" + minutes;
            } else {
                time = time + ":" + minutes;
            }
            if (seconds < 10) {
                time = time + ":0" + seconds;
            } else {
                time = time + ":" + seconds;
            }

            //time = hours + ":" +  minutes + ":" + seconds;// + ":" + milliseconds;
        } else {
            time = "00:00:00";//:0";
        }
        return time;
    }

    // MOVIE EVENT HANDLERS =====================================================
    public final void startRecordingAction() {
        pause_button.setEnabled(true);
        stop_button.setEnabled(true);
        rec_button.setEnabled(false);
        status.setText("<HTML><CENTER><FONT COLOR=\"RED\">RECORDING...");
        recordingAlert.setVisible(true);
        videoController.record(null, null); //graph_path parametr not needed on mac
    }

    public final void pauseRecordingAction() {
        ImageIcon img = __createImageIcon(LODEConstants.IMGS_PREFIX + "button_play.gif", "resume");
        pause_button.setIcon(img);
        pause_button.setActionCommand(ActionController.RESUME_VIDEO_RECORDING);
        //pause_button.setEnabled(false);
        //rec_button.setEnabled(true);
        status.setText("<HTML><CENTER><FONT COLOR=\"ORANGE\">Recording PAUSED");
        recordingAlert.setVisible(false);
        videoController.togglePause();
    }

    public final void resumeRecordingAction() {
        ImageIcon img = __createImageIcon(LODEConstants.IMGS_PREFIX + "button_pause.gif", "suspend");
        pause_button.setIcon(img);
        pause_button.setActionCommand(ActionController.SUSPEND_VIDEO_RECORDING);
        pause_button.setEnabled(true);
        rec_button.setEnabled(false);
        status.setText("<HTML><CENTER><FONT COLOR=\"RED\">RECORDING...");
        recordingAlert.setVisible(true);
        videoController.togglePause();
    }

    /**
     *
     * @return 0 if recording was stopped, 1 if it was suspended, 2 if command
     * was ignored (i.e. recording continues)
     */
    public final int stopRecordingAction() {
        pauseRecordingAction();
        String options[] = {"YES, stop recording", "NO, pause", "No, resume recording"};
        int choice = JOptionPane.showOptionDialog(null,
                "Are you sure you want to stop recording?",
                "Are you sure?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                LODEConstants.ICON_LODE,
                options,
                options[2]);
        System.err.print(choice);
        switch (choice) {
            case 1: // suspend recording
                return choice; // it's already suspended!
            case 2: // resume recording
                resumeRecordingAction();
                return choice;
            default: // proceed with stopping
        }
        final ProgressBar pb = ProgressBar.showIndeterminateProgressBar("Please wait while the movie is saved...");
        pause_button.setEnabled(false);
        stop_button.setEnabled(false);
        rec_button.setEnabled(false);
        status.setText("<HTML><CENTER><FONT COLOR=\"YELLOW\">STOPPING...");
        recordingAlert.setVisible(false);
        frame.repaint();

        final Lecture lecture = programState.getCurrentLecture();
        lecture.setVideoLength("" + videoController.getTime());
        Thread t = new Thread(new Runnable() {

            public void run() {
                videoController.stop();
                MenuManager.getInstance().updateMenuState();
                status.setText("<HTML><CENTER><FONT COLOR=\"BLUE\">FINISHED !");
                frame.repaint();
                lecture.save(lecture.getAcquisitionPath());
                clearInstance();
                InspectorWindow.getInstance().update();
            }
        });
        System.err.println("STARTING THREAD");
        System.err.flush();
        t.start();
        System.err.println("STARTED THREAD");
        System.err.flush();
        try {
            System.err.println("WAITING FOR THREAD");
            System.err.flush();
            t.join();
        } catch (InterruptedException ex) {
            Messanger.getInstance().log("Interrupted exception while waiting for the movie to be saved - in AcquisitionWindow.stopRecordingAction()");
            System.err.println("Interrupted exception");
            System.err.flush();
        } finally {
            System.err.println("CLOSING THREAD");
            System.err.flush();
            pb.closeWindow();
            return 0;
        }
    }

    private final void resetRecordingButtons() {
        pause_button.setEnabled(false);
        stop_button.setEnabled(false);
        rec_button.setEnabled(true);
        status.setText("<HTML><CENTER><FONT COLOR=\"GREEN\">PREVIEWING");
    }

    final Action getAction(String name) {
        //needed for the select and delete of the logPresenter
        Action action = null;
        Action[] actions = logPresenter.getActions();

        for (int i = 0; i < actions.length; i++) {
            if (name.equals(actions[i].getValue(Action.NAME).toString())) {
                action = actions[i];
                break;
            }
        }
        return action;
    }
    // ======= SLIDE EVENT HANDLERS ============================================

    public final void goToNextSlide(int k) {
        // when going back skip current slide
        if (k == -1 && next_slide_number == current_slide_number) {
            k = -2;
        }

        next_slide_number = next_slide_number + k;
        if (next_slide_number >= NUM_IMAGES - 1) {
            next_slide_number = NUM_IMAGES - 1;
        }
        if (next_slide_number < 0) {
            next_slide_number = 0;
        }
        performSlidesMove(next_slide_number);
    }

    public final void jumpToSlide() {
        int slideNumber = 0;
        try {
            slideNumber = Integer.parseInt(goto_field.getText()) - 1;
        } catch (NumberFormatException e) {
            slideNumber = next_slide_number;
        }
        if (slideNumber < 0) {
            slideNumber = 0;
        }
        if (slideNumber >= NUM_IMAGES) {
            slideNumber = NUM_IMAGES - 1;
        }
        performSlidesMove(slideNumber);
    }

    //====================================================================================================================================== 
    //
    // ===============  MOUSE MANAGEMENT =======================================
    //
    //====================================================================================================================================== 
    public void mouseClicked(MouseEvent e) {/*
         * if ( SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1)
         * selectLine.actionPerformed( null );
         */

    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        int clicks = e.getClickCount();
        String s_path = "";
        String lectureHome = programState.getCurrentLecture().getAcquisitionPath();
        Object src = e.getSource();
        if (clicks == 2) {
            if (src.equals(slide_P1) & !(p1_slide_number < 0)) {
                s_path = lectureHome + File.separator + "Slides" + File.separator + (p1_slide_number + 1) + ".JPG";
            } else if (src.equals(slide_P2) & !(p2_slide_number < 0)) {
                s_path = lectureHome + File.separator + "Slides" + File.separator + (p2_slide_number + 1) + ".JPG";
            } else if (src.equals(slide_P3) & !(p3_slide_number < 0)) {
                s_path = lectureHome + File.separator + "Slides" + File.separator + (p3_slide_number + 1) + ".JPG";
            } else if (src.equals(slide_N) & !(next_slide_number < 0)) {
                s_path = lectureHome + File.separator + "Slides" + File.separator + (next_slide_number + 1) + ".JPG";
            } else if (src.equals(slide_N2a) & !(n2a_slide_number < 0)) {
                s_path = lectureHome + File.separator + "Slides" + File.separator + (n2a_slide_number + 1) + ".JPG";
            } else if (src.equals(slide_N2b) & !(n2b_slide_number < 0)) {
                s_path = lectureHome + File.separator + "Slides" + File.separator + (n2b_slide_number + 1) + ".JPG";
            }
            if ((new File(s_path)).exists()) {
                SlideWindow slidewindow = new SlideWindow(this);
                slidewindow.setSlidePath(s_path);
                slidewindow.setVisible(true);
                slidewindow.pack();
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    //====================================================================================================================================== 
    //
    // ===============  KEYBOARD MANAGEMENT ====================================
    //
    //====================================================================================================================================== 
    public void keyTyped(KeyEvent e) {
        //System.out.println((new Date())+" keyTyped "+KeyEvent.getKeyText(e.getKeyCode()));
    }

    public void keyPressed(KeyEvent e) {
        //System.out.println((new Date())+" keyPressed "+KeyEvent.getKeyText(e.getKeyCode()));
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                goToNextSlide(-1);
                break;
            case KeyEvent.VK_RIGHT:
                goToNextSlide(1);
                break;
            case KeyEvent.VK_ENTER:
                addSlide();
                break;
            default:
            // do nothing
        }
    }

    public void keyReleased(KeyEvent e) {
        //System.out.println((new Date())+" keyReleased "+KeyEvent.getKeyText(e.getKeyCode()));
    }

    //====================================================================================================================================== 
    //
    // ===============  WINDOW MANAGEMENT ======================================
    //
    //====================================================================================================================================== 
    public final void closeWindow() {
        int retval = 1;
        if (frame.isVisible()) {
            if (videoController.isRecording() || videoController.isPausing()) {
                retval = stopRecordingAction();
            }
            if (retval == 0) {
                // stop recording was confirmed
                //videoController.disposeVideoChannel(jPVideo);
                clearInstance();
                MenuManager.getInstance().updateMenuState();
            }
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.out.println("window closing");
        closeWindow();
    }

    public void windowClosed(WindowEvent e) {
        System.out.println("window closed");
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    //***************************************************************************
    //******************* FUNCTIONS CALLED BY INITPANEL *************************
    //***************************************************************************
    //============= SLIDE BUTTONS  =============================================
    private final void _addSlideSlots(GridBagConstraints c) {
        //CURRENT SLIDE RELATED ITEMS
        slide_C_label = new JLabel("Current Slide:");
        slide_C = __defineSlideImages(c, 21, 430, slide_C_label,
                LODEConstants.IMGS_PREFIX + "slidebig.gif", "The Current slide frame",
                10, 415, 32, 417,
                LODEConstants.IMGS_PREFIX + "image_empty_resize.gif", "Empty picture");

        //FIRST SMALL SLIDE RELATED ITEMS
        slide_P1_label = new JLabel("");
        slide_P1 = __defineSlideImages(c, 562, 26, slide_P1_label,
                LODEConstants.IMGS_PREFIX + "slidesmall.gif", "The small slide frame 1",
                495, 20, 496, 21,
                LODEConstants.IMGS_PREFIX + "image_empty_resize.gif", "Empty picture");

        //SECOND SMALL SLIDE RELATED ITEMS
        slide_P2_label = new JLabel("");
        slide_P2 = __defineSlideImages(c, 562, 156, slide_P2_label,
                LODEConstants.IMGS_PREFIX + "slidesmall.gif", "The small slide frame 2",
                495, 150, 496, 151,
                LODEConstants.IMGS_PREFIX + "image_empty_resize.gif", "Empty picture");

        //THIRD SMALL SLIDE RELATED ITEMS
        slide_P3_label = new JLabel("");
        slide_P3 = __defineSlideImages(c, 562, 286, slide_P3_label,
                LODEConstants.IMGS_PREFIX + "slidesmall.gif", "The small slide frame 3",
                495, 280, 496, 281,
                LODEConstants.IMGS_PREFIX + "image_empty_resize.gif", "Empty picture");

        //NEXT SLIDE RELATED ITEMS
        slide_N_label = new JLabel("Next Slide: ");
        slide_N = __defineSlideImages(c, 336, 430, slide_N_label,
                LODEConstants.IMGS_PREFIX + "slidebig.gif", "The Next slide frame",
                325, 415, 347, 417,
                LODEConstants.IMGS_PREFIX + "image_empty.gif", "Empty picture");

        //(After Next) SMALL SLIDE RELATED ICONS
        slide_N2a_label = new JLabel("");
        slide_N2a = __defineSlideImages(c, 562, 756, slide_N2a_label,
                LODEConstants.IMGS_PREFIX + "slidesmall.gif", "The small slide frame 5",
                495, 750, 496, 751,
                LODEConstants.IMGS_PREFIX + "image_empty_resize.gif", "Empty picture");

        //(After Next 2) SMALL SLIDE RELATED ICONS
        slide_N2b_label = new JLabel("");
        slide_N2b = __defineSlideImages(c, 562, 886, slide_N2b_label,
                LODEConstants.IMGS_PREFIX + "slidesmall.gif", "The small slide frame 6",
                495, 880, 496, 881,
                LODEConstants.IMGS_PREFIX + "image_empty_resize.gif", "Empty picture");

    }

    private final JLabel __defineSlideImages(GridBagConstraints c,
            int x1, int y1,
            JLabel slideLabel,
            String iconFileName1, String iconComment1,
            int x2, int y2, int x3, int y3,
            String iconFileName2, String iconComment2) {
        c.insets = new Insets(x1, y1, 0, 0);
        add(slideLabel, c);
        JLabel label1 = new JLabel(__createImageIcon(iconFileName1, iconComment1));
        c.insets = new Insets(x2, y2, 0, 0);
        add(label1, c);
        c.insets = new Insets(x3, y3, 0, 0);
        JLabel label2 = new JLabel(__createImageIcon(iconFileName2, iconComment2));
        label2.addMouseListener(this);
        add(label2, c);
        return label2;
    }

    private final void _addSlideButtons(GridBagConstraints c) {
        go_button = __makeTextButton("Go",
                ActionController.GO_TO_SLIDE,
                418, 227, c);
        go_button.setMnemonic(KeyEvent.VK_I);

        goto_field = new JTextField();
        goto_field.setOpaque(false);
        goto_field.setPreferredSize(new Dimension(50, 20));
        c.insets = new Insets(422, 175, 0, 0);
        add(goto_field, c);

        JLabel goto_frame = new JLabel(__createImageIcon(LODEConstants.IMGS_PREFIX + "gotoframe.gif", "Goto frame picture"));
        c.insets = new Insets(410, 85, 0, 0);
        add(goto_frame, c);
    }

    private final void _addTimeLogger(GridBagConstraints c) {
        //TIME FRAME RELATED ITEMS
        ImageIcon time = __createImageIcon(LODEConstants.IMGS_PREFIX + "timeframe.gif", "Adding slides at concrete time frame");
        JLabel time_frame = new JLabel(time);
        c.insets = new Insets(10, 740, 0, 0);
        add(time_frame, c);

        logPresenter = new JTextArea("");
        logPresenter.setEditable(false);
        logPresenter.addMouseListener(this);
        c.insets = new Insets(45, 750, 0, 0);
        JScrollPane areaScrollPane = new JScrollPane(logPresenter);
        areaScrollPane.setPreferredSize(new Dimension(236, 404));
        add(areaScrollPane, c);
        selectLine = getAction(DefaultEditorKit.selectLineAction);


        add_button = __makeTextButton("   Add   ",
                ActionController.ADD_SLIDE,
                277, 535, c);

        b_add_at_time = __makeTextButton("    Edit  Slide    ",
                ActionController.EDIT_SLIDE,
                444, 748, c);

        b_delete_at_time = __makeTextButton("  Delete Slide  ",
                ActionController.DELETE_SLIDE_AT_TIME,
                444, 870, c);
    }

    private final void _addSlider(GridBagConstraints c) {
        //SLIDER RELATED ITEMS ARE ON ROW 3
        slider = new JSlider();
        slider.setMinimum(0);
        //slider.setMaximum(NUM_IMAGES-1);
        //slider.setValue(next_slide_number);
        slider.setOpaque(false);
        slider.setPreferredSize(new Dimension(845, 15));
        c.insets = new Insets(605, 80, 0, 0);
        slider.addChangeListener(this);
        slider.setEnabled(false);
        add(slider, c);

        slide_down_button = __makeTextButton("<",
                ActionController.GO_TO_PREVIOUS_SLIDE,
                600, 0, c);
        slide_down_button.setEnabled(false);

        slide_up_button = __makeTextButton(">",
                ActionController.GO_TO_NEXT_SLIDE,
                600, 930, c);
        slide_up_button.setEnabled(false);
    }

    private final void _addVideo(GridBagConstraints c) {
        //VIDEO RELATED ITEMS
        c.insets = new Insets(336, 150, 0, 0);

        status = new JLabel("<HTML><CENTER><FONT COLOR=\"GREEN\">PREVIEWING");
        add(status, c);

        rec_button = __makeGraphicButton(
                LODEConstants.IMGS_PREFIX + "button_rec.gif",
                "The rec button",
                ActionController.START_VIDEO_RECORDING,
                328, 265, c);
        //rec_button.setMnemonic(KeyEvent.VK_I);
        rec_button.setPreferredSize(new Dimension(30, 30));

        stop_button = __makeGraphicButton(
                LODEConstants.IMGS_PREFIX + "button_stop.gif",
                "The stop button",
                ActionController.STOP_VIDEO_RECORDING,
                328, 300, c);
        //stop_button.setMnemonic(KeyEvent.VK_I);
        stop_button.setPreferredSize(new Dimension(30, 30));
        stop_button.setEnabled(false);

        pause_button = __makeGraphicButton(
                LODEConstants.IMGS_PREFIX + "button_pause.gif",
                "The pause button",
                ActionController.SUSPEND_VIDEO_RECORDING,
                328, 335, c);
        //pause_button.setMnemonic(KeyEvent.VK_I);
        pause_button.setPreferredSize(new Dimension(30, 30));
        pause_button.setEnabled(false);

        ImageIcon video = __createImageIcon(LODEConstants.IMGS_PREFIX + "videoframe.gif", "The Video frame");
        JLabel video_frame = new JLabel(video);
        c.insets = new Insets(10, 10, 0, 0);
        add(video_frame, c);

        jPVideo = new JPanel();
        jPVideo.setFocusable(false);
        jPVideo.setPreferredSize(new Dimension(363, 295));
        //jPVideo.setPreferredSize(new Dimension(500, 500));
        c.insets = new Insets(24, 24, 0, 0);
        add(jPVideo, c);

        //aggiunge al JPVideo l'oggetto contenente il video
        videoController = VideoControllerFactory.getInstance();
        jPVideo.setLayout(null);
        //videoController.startPreview(jPVideo);
        videoAsComponent = videoController.getVideoAsGraphicComponent();
        videoAsComponent.setBounds(0, 0, 363, 295);
        videoAsComponent.setSize(363, 295);
        //videoAsComponent.setSize(new Dimension(19000,100));
        if (videoAsComponent != null) {
            System.out.println("TRACE>" + new Exception().getStackTrace()[0].getMethodName()); //TRACING
            //jPVideo.add("Center", videoAsComponent);
            c.insets = new Insets(0, 0, 200, 200);
            jPVideo.add(videoAsComponent, c);
            jPVideo.setEnabled(false);
            jPVideo.setFocusable(false);
            videoAsComponent.setEnabled(false);
            videoAsComponent.setFocusable(false);
        }
        //videoController.startPreview(jPVideo);
    }

    private void _addSRecordingAlert(GridBagConstraints c) {
        //audioPanel = new SoundPanel();
        c.insets = new Insets(370, 85, 0, 0);
        recordingAlert.setPreferredSize(new Dimension(222, 45));
        recordingAlert.setFocusable(false);
        add(recordingAlert, c);
    }

    private void _addSoundPanel(GridBagConstraints c) {
        audioPanel = new SoundPanel();
        c.insets = new Insets(350, 15, 0, 0);
        audioPanel.setPreferredSize(new Dimension(85, 20));
        audioPanel.setFocusable(false);
        add(audioPanel, c);
    }
    /*
     * int numberOfZeroSounds=0; public void setSoundColor(Color c){
     * audioPanel.setColor(c); }
    }
     */
    //========================== BUTTON GENERATION ==============================

    /**
     * Utility function to create, initilize and add a button with an associated
     * icon
     *
     * @param filename file containing the icon
     * @param description icon description
     * @param actionName name of the actioncommand associated to the button
     * @param x Inset position
     * @param y Inset position
     * @param c constraints
     * @return the generated JButton
     */
    private final JButton __makeGraphicButton(String filename,
            String description,
            String actionName,
            int x, int y,
            GridBagConstraints c) {
        ImageIcon img = __createImageIcon(filename, description);
        JButton button = new JButton(img);
        //button.setPreferredSize(new Dimension(30,30));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setActionCommand(actionName);
        c.insets = new Insets(x, y, 0, 0);
        add(button, c);
        button.addActionListener(actionController);
        button.addKeyListener(this);
        return button;
    }

    /**
     * Utility function to create, initilize and add a button with a text label
     *
     * @param text
     * @param description
     * @param actionName
     * @param x Inset position
     * @param y Inset position
     * @param c constraints
     * @return the generated JButton
     */
    private final JButton __makeTextButton(String text,
            String actionName,
            int x, int y,
            GridBagConstraints c) {
        JButton button = new JButton(text);
        button.setActionCommand(actionName);
        c.insets = new Insets(x, y, 0, 0);
        add(button, c);
        button.addActionListener(actionController);
        button.addKeyListener(this);
        return button;
    }
    // =============== MANAGE IMAGE ICONS ======================================

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    private final ImageIcon __createImageIcon(String path, String description) {
        java.net.URL imgURL = AcquisitionWindow.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            Messanger m = Messanger.getInstance();
            m.w("Error creating icon: " + path, LODEConstants.MSG_LOG);
            m.log("Error creating icon: " + path);
            return null;
        }
    }

    public final ImageIcon __createScaledImageIcon(String path, int width, int height) {
        Image img = (new ImageIcon(path)).getImage().getScaledInstance(width, height, 0);
        ImageIcon ii = new ImageIcon(img);
        return ii;
    }
}
