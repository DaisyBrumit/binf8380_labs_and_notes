package slowJob_Multithreading_L6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class PrimeFinder_Draft implements Runnable {
	// for the main method
	static long absMax;
	static int numThreads;
	
	// for the actual class
	private static final List<Long> primeList = Collections.synchronizedList(new ArrayList<Long>());
	private volatile static boolean isFinished = false;
	long min, max;
	Semaphore semaphore;
	
	public PrimeFinder_Draft(long min, long max, Semaphore semaphore) {
		this.min = min;
		this.max = max;
		this.semaphore = semaphore;
	}
	
	public void run() {
		try {	
			long n = min;
			
			while(n <= max){ 
				boolean prime = isPrime(n);
				if(prime) primeList.add(n);
				n++;
				System.out.println("counted");
				}
			System.out.println("primes: " + primeList.size());
			}
		catch(Exception e) { // print out trace and message in case of Exception
			e.printStackTrace();
			System.out.println("abort");
			System.exit(1);
		}
		isFinished = true;
	}
		
	private boolean isPrime(long n) {
		boolean primeStatus = true;
		for(long i=2; i< n/2+1; i++) { // for each n up to half of desired value
			if(n % i == 0) { 
				primeStatus = false;
				break;
			}	
		}
		return primeStatus;
	}
	
	public static List<Long> getPrimeList() {
		return primeList;
	}
	
	public static int getPrimeCount() {
		return primeList.size();
	}
	
	public static boolean getStatus() {
		return isFinished;
	}
	
	public static void callMainMethod(long absMax_incoming, int numThreads_incoming) throws Exception {
		absMax = absMax_incoming;
		numThreads = numThreads_incoming;
		main(null);
	}
	
	public static void main(String[] args) throws Exception {
		Semaphore semaphore = new Semaphore(numThreads);
		
		long tmp_min = 0;
		long bin_size = absMax / numThreads;
		
		// start the worker thread
		for(int x=0; x<numThreads; x++) {
			long tmp_max = tmp_min + bin_size;
			semaphore.acquire();
			new Thread (new PrimeFinder_Draft(tmp_min, tmp_max, semaphore)).start();
			tmp_min = tmp_max;
		}
		
		// get licenses back
		for(int x=0; x<numThreads; x++) {
			semaphore.acquire();
		}
	}
}
