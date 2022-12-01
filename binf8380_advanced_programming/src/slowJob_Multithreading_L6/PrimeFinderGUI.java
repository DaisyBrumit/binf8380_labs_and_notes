package slowJob_Multithreading_L6;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class PrimeFinderGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextArea primeDisplay = new JTextArea();
	private final JButton cancelButton = new JButton("Cancel");
	private final JButton startButton = new JButton("Look For Primes");
	private final JPanel buttonPanel = new JPanel();
	private final Timer timer = new Timer(2000, new timerListener());
	private volatile boolean cancel = false;
	private static PrimerFinder primeFinder;
	private static long startTime;
	
	private PrimeFinderGUI() {
		// basic window
		super("Welcome to Prime Finder!");
		setSize(1000,900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		getContentPane().setLayout(new BorderLayout());
		
		// set up display space
		getContentPane().add(primeDisplay, BorderLayout.CENTER);
		
		// add action listeners
		startButton.addActionListener((ActionListener) new StartListener());
		cancelButton.addActionListener(new CancelListener());
		
		// set button panel
		buttonPanel.setLayout(new GridLayout(0,2));
		buttonPanel.add(startButton);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	private class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			cancel = true;
			primeDisplay.append("Process cancelled!\n");
			haltProcess();
		}
	}
	
	private void haltProcess() {
		timer.stop();
    	float time = (System.currentTimeMillis() - startTime) / 1000f;
    	primeDisplay.append(primeFinder.getPrimeCount() + " total primes found.\n");
    	primeDisplay.append("time run: " + time + " s.\n");
    	
		startButton.setEnabled(true);
		cancelButton.setEnabled(false);
	}
	
	private class StartListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// make sure buttons are appropriately accessible
			cancel=false;
			startButton.setEnabled(false);
			cancelButton.setEnabled(true);
			
			// nuke anything that exists
			primeDisplay.setText(null);
			if(!primeFinder.getPrimeList().isEmpty()) { 
				primeFinder.setPrimeList();
				}
			
			// set parameters for the Callable
			try {
			Long maxNumber = Long.parseLong(JOptionPane.showInputDialog("Enter a large integer:"));
			Integer numThreads = Integer.parseInt(JOptionPane.showInputDialog("How many threads should I use?"));

			primeDisplay.setText("Searching for primes...\n");
			startTime = System.currentTimeMillis();
			
			new primeWorker(maxNumber, numThreads).execute();
			System.out.println("primeWorker executed");			}
			
			catch(Exception ex) {
				ex.printStackTrace();
				System.out.println("abort");
				System.exit(1);
			}
		}
	}
	
	private class primeWorker extends SwingWorker<Void, Integer>{
		long maxNumber;
		int numThreads;
		Semaphore semaphore = new Semaphore(numThreads);
		
		private primeWorker(long maxNumber, int numThreads) {
			this.maxNumber = maxNumber;
			this.numThreads = numThreads;
			this.semaphore = new Semaphore(numThreads);
			System.out.println("primeWorker constructed");
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			// break into bins
			long tmp_min = 0;
			long bin_size = maxNumber / numThreads;
			
			// start the Runnable class
			for(int x=0; x<numThreads; x++) {
				long tmp_max = tmp_min + bin_size;
				semaphore.acquire();
				primeFinder = new PrimerFinder(tmp_min, tmp_max, semaphore);
				Thread thread = new Thread (primeFinder);
				thread.start();
				tmp_min = tmp_max;
			}
			
			//
			timer.start();
			
			System.out.println("There's a snake in my boot!");
			// get licenses back
			for(int x=0; x<numThreads; x++) {
				semaphore.release();
				System.out.println("semaphore acquired");
			}
			System.out.println("Beam me up, Scotty!");
			
			return null;
		}
	}
	
	private class timerListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        	boolean isFinished = primeFinder.getStatus();
        	System.out.println(isFinished);

            if(! cancel && ! isFinished) {
            	int tmp_primeCount = primeFinder.getPrimeCount();
            	primeDisplay.append(tmp_primeCount + " primes found so far.\n");
            }
            if(isFinished) {
            	cancel=true;
            	haltProcess();
            }
        }}
	
	public static void main(String[] args) {
		new PrimeFinderGUI();
	}
}
