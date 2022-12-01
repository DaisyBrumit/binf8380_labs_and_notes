package slowJob_Multithreading_L6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class PrimerFinder implements Runnable {
	// internal use
	long min, max;
	Semaphore semaphore;
	
	// external use (w/ getters)
	private static final List<Long> primeList = Collections.synchronizedList(new ArrayList<Long>());
	private volatile static boolean isFinished = false;
	
	public PrimerFinder(long min, long max, Semaphore semaphore) {
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
	
	public List<Long> getPrimeList() {
		return primeList;
	}
	
	public static void setPrimeList() {
		primeList.removeAll(primeList);
	}
	
	public int getPrimeCount() {
		return primeList.size();
	}
	
	public boolean getStatus() {
		return isFinished;
	}
	
}
