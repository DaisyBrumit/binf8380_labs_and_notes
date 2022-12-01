package slowJob_Multithreading_L6;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class PrimeFinder_Contained_Draft extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextArea primeDisplay = new JTextArea();
	private final JButton cancelButton = new JButton("Cancel");
	private final JButton startButton = new JButton("Look For Primes");
	private final JPanel buttonPanel = new JPanel();
	private volatile boolean cancel = false;
	
	private PrimeFinder_Contained_Draft() {
		// basic window
		super("Welcome to Prime Finder!");
		setSize(1000,900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		getContentPane().setLayout(new BorderLayout());
		
		// set up display space
		getContentPane().add(primeDisplay, BorderLayout.CENTER);
		
		// add action listeners
		startButton.addActionListener(new StartListener());
		cancelButton.addActionListener(new CancelListener());
		
		// set button panel
		buttonPanel.setLayout(new GridLayout(0,2));
		buttonPanel.add(startButton);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	private class StartListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// make sure buttons are appropriately accessible
			cancel=false;
			startButton.setEnabled(false);
			cancelButton.setEnabled(true);
			
			// set parameters for the Runnable
			long maxNumber = Long.parseLong(JOptionPane.showInputDialog("Enter a large integer:"));
			int numThreads = Integer.parseInt(JOptionPane.showInputDialog("How many threads should I use?"));
			
			primeDisplay.setText("Searching for primes...\n");
			
			// split workload
			long binSize = (maxNumber-numThreads) / numThreads;
			long threadMin = 2;
			System.out.println("BinSize: " + binSize);
			
			for(int x=0; x<numThreads; x++) {
				long threadMax = threadMin + binSize;
				System.out.println("Thread " + x + "min = " + threadMin + " max = " + threadMax);
				new Thread(new primeFinder(threadMin, threadMax, x)).start(); // kick off the Runnable
				threadMin = threadMax + 1;
				if(threadMax > maxNumber) threadMin = maxNumber;
			}
		}
	}
	
	private class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			cancel = true;
		}
	}
	
	private class primeFinder implements Runnable {
		private final long minNumber;
		private final long maxNumber;
		private final int threadID;
		
		public primeFinder(long minNumber, long maxNumber, int threadID) {
			this.minNumber = minNumber;
			this.maxNumber = maxNumber;
			this.threadID = threadID;
		}
		public void run() {
			try {	
				final long startTime = System.currentTimeMillis();
				long time = startTime;
				long n = minNumber;
				
				int primeCount = 0;
				
				while(! cancel && n <=maxNumber){ // stop is cancelButton is pressed or maxNum is reached
					boolean prime = isPrime(n);
					if(prime) primeCount++;
					n++;
					
					if(System.currentTimeMillis() >= time + 2000){ // print primes every few seconds
						primeDisplay.append("Thread " + threadID + "primes to time: " + primeCount + "\n");
						time = System.currentTimeMillis();
					}
				}
				
				// print final values to display
				primeDisplay.append("final prime count: " + primeCount + "\n");
				primeDisplay.append("time run = " + (System.currentTimeMillis() - startTime) / 1000f + " s");
				
				// for internal use (determining efficiency increase)
				System.out.println((System.currentTimeMillis() - startTime) / 1000f);
			}
			catch(Exception e) { // print out trace and message in case of Exception
				e.printStackTrace();
				System.out.println("abort");
				System.exit(1);
			}
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								startButton.setEnabled(true);
								cancelButton.setEnabled(false);
							}
						});
				}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean isPrime(long numToCheck) { // prime checker!
		boolean prime = true;
		
		for(long i=2; i< numToCheck/2+1; i++) { // for each number up to half of desired value
			if(numToCheck % i == 0) { // is fully divisible?
				prime = false;
				break; // Not prime. Don't bother checking next i.
			}	
		}
		
		return prime;
	}
	
	public static void main(String[] args) {
		new PrimeFinder_Contained_Draft();
	}
}
