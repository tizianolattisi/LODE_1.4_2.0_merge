package it.unitn.LODE.gui;

import java.awt.*;
import javax.swing.*;
import it.unitn.LODE.MP.constants.LODEConstants;
import java.awt.event.WindowEvent;

public class ProgressBar extends JFrame
 {
	private	JProgressBar    progress;
	private	JLabel          label1;
	private	JPanel          topPanel;


	public ProgressBar()
	{
		setTitle( "Progress" );
		//setSize( 500, 70 );
		//setBackground( Color.gray );

		topPanel = new JPanel();
		topPanel.setPreferredSize( new Dimension( 500, 70 ) );
        topPanel.setOpaque(true);
        getContentPane().add( topPanel );

		// Create a label and progress bar
		label1 = new JLabel( "Waiting to start tasks..." );
		label1.setPreferredSize( new Dimension( 450, 24 ) );
		topPanel.add( label1 );

		progress = new JProgressBar();
		progress.setPreferredSize( new Dimension( 500, 20 ) );
		progress.setMinimum( 0 );
		progress.setValue( 0 );
		//progress.setBounds( 20, 35, 295, 20 );
		topPanel.add( progress );
                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    }

     public void showProgress(String msg, int currentValue, int maxValue) {
        progress.setMaximum( maxValue );
        this.setLocation(LODEConstants.XPOS, LODEConstants.YPOS);

        // Update the progress indicator and label
        label1.setText( "STEP " + currentValue + " of " + maxValue+ " - "+msg);
        Rectangle labelRect = label1.getBounds();
        labelRect.x = 0;
        labelRect.y = 0;
        label1.paintImmediately( labelRect );

        progress.setValue( currentValue );
        Rectangle progressRect = progress.getBounds();
        progressRect.x = 0;
        progressRect.y = 0;
        progress.paintImmediately( progressRect );
     }

     public void showIndeterminateProgress(String label_text) {
        progress.setIndeterminate(true);
        this.setLocation(LODEConstants.XPOS, LODEConstants.YPOS);
        // Update the progress indicator and label
        label1.setText(label_text);
        Rectangle labelRect = label1.getBounds();
        labelRect.x = 0;
        labelRect.y = 0;
        label1.paintImmediately( labelRect );
     }

     public void closeWindow(){
         WindowEvent closingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
         Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);

         this.setVisible(false);
     }

     public void putOnTop() {
         this.setAlwaysOnTop(true);
     }
     public static ProgressBar showIndeterminateProgressBar(String msg){
        ProgressBar progressBarFrame = new ProgressBar();
        progressBarFrame.setVisible( true );  
        progressBarFrame.putOnTop();  
        progressBarFrame.pack();
        progressBarFrame.showIndeterminateProgress(msg);
        return progressBarFrame;
    }     
     public static ProgressBar getProgressBar(){
        ProgressBar progressBarFrame = new ProgressBar();
        progressBarFrame.setVisible( true );  
        progressBarFrame.putOnTop();  
        progressBarFrame.pack();
        return progressBarFrame;
    }

    public static void main( String args[] )
	{
		// Create an instance of the test application
		ProgressBar mainFrame	= new ProgressBar();
		mainFrame.setVisible( true );
		mainFrame.pack();
	}
}

