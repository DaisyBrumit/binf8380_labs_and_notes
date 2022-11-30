package aminoAcidQuizGUI_L5;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class AAApplication extends JFrame {
	JLabel full = new JLabel();
	JLabel clip = new JLabel();
	private static final long serialVersionUID = -5465077034163085045L;
	
	// amino acid arrays
	String[] SHORT_NAMES = 
		{ "A","R", "N", "D", "C", "Q", "E", 
		"G",  "H", "I", "L", "K", "M", "F", 
		"P", "S", "T", "W", "Y", "V" };

	String[] FULL_NAMES = 
		{
		"alanine","arginine", "asparagine", 
		"aspartic acid", "cysteine",
		"glutamine",  "glutamic acid",
		"glycine" ,"histidine","isoleucine",
		"leucine",  "lysine", "methionine", 
		"phenylalanine", "proline", 
		"serine","threonine","tryptophan", 
		"tyrosine", "valine"};
	
	// initialize variables for scoring during the quiz
	Random random = new Random();
	//private boolean firstRun = true;
	private int seconds = 30;
	private int index = 100;
	public int correct = 0;
	public int incorrect = 0;
	
	private Timer timer = new Timer(1000, new timerListener());
	
	// SOUTH positioned panel w/ start and cancel buttons
	private JPanel startStopPanel = new JPanel();
	private JButton startButton = new JButton("Start");
	private JButton cancelButton = new JButton("Cancel");
	
	// WEST positioned panel w/ timer and score keeping
	private JPanel timeScorePanel = new JPanel();
	private JLabel timerLabel = new JLabel();
	private JLabel scoreLabel = new JLabel();
	
	// CENTER position panel w/ full AA name prompt
	private JPanel promptPanel = new JPanel();
	private JLabel AALabel = new JLabel();
	
	// EAST positioned panel w/ textbox for user response
	private JPanel userPanel = new JPanel();
	private JTextField userText = new JTextField("", 20);
	
	public AAApplication() {
		// basic window
		super("Amino Acid Quiz");
		setSize(800,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		getContentPane().setLayout(new BorderLayout());
		
		// startStopPanel arrangement
		startStopPanel.setLayout(new GridLayout(0,2));
		startStopPanel.add(startButton);
		startStopPanel.add(cancelButton);
		getContentPane().add(startStopPanel, BorderLayout.SOUTH);
		
		// startStopPanel listeners
		startButton.addActionListener(new startListener());
		cancelButton.addActionListener(new cancelListener());
		
		// timeScorePanel arrangement
		timeScorePanel.setPreferredSize(new Dimension(200,600));
		timeScorePanel.setLayout(new GridLayout(2,0));
		timeScorePanel.add(timerLabel);
		timeScorePanel.add(scoreLabel);
		getContentPane().add(timeScorePanel, BorderLayout.WEST);
		
		// promptPanel arrangement
		promptPanel.setPreferredSize(new Dimension(200,600));
		promptPanel.add(AALabel);
		getContentPane().add(promptPanel, BorderLayout.CENTER);
		
		// userResponsePanel arrangement
		userPanel.setPreferredSize(new Dimension(200,600));
		userPanel.setLayout(new GridLayout(3,0));
		
		setVisible(true);
	}
	
	private class startListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// populate starting values
			updateScoreLabel(correct, incorrect);
			index = random.nextInt(19);
			updateAALabel(index);
			clip.setText("Clip = " + index + " " + SHORT_NAMES[index]);
			
			// let user respond
			getContentPane().add(userPanel, BorderLayout.EAST);
			userPanel.add(userText);
			userText.addActionListener(new userTextListener());
			userText.setToolTipText("Please enter single letter abbreviation and hit Enter.");
			
			// start the timer
			timer.start();
		}
	}
	
	private class cancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			seconds = 0;
			userText.setText("Game Over!");
			AALabel.setText("");
		}
	}
	
	private class timerListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        	seconds--;
            long second = TimeUnit.SECONDS.toSeconds(seconds);
            timerLabel.setText(second + "s");
            if (seconds == 0) {
                timer.stop();
            }
        }}
	
	public class userTextListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// take in the response
			String response = userText.getText().toUpperCase();
				
			// count scores accordingly
			if (response.equals(SHORT_NAMES[index])) {
				correct++;
			}
			else {
				incorrect++;
			}
					
			// reset text field and AA prompt
			updateScoreLabel(correct, incorrect);
			userText.setText("");
			index = random.nextInt(19);
			updateAALabel(index);
				
			if (seconds == 0) {
				userText.setText("Game Over!");
				AALabel.setText("");
			}	
		}
	}
	
	private void updateAALabel(int index) {
		AALabel.setText(FULL_NAMES[index]);
		full.setText("Full = " + index);
	}
	
	private void updateScoreLabel(int correct, int incorrect) {
		scoreLabel.setText("Correct: " + correct + " Incorrect: " + incorrect);
	}

	public static void main(String[] args) {
		new AAApplication();
	}
}
