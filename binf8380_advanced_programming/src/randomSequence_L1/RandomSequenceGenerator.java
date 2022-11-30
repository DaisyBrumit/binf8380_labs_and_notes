package randomSequence_L1;

import java.util.Random;

public class RandomSequenceGenerator {
	public static void uniformGen() {
		//initialize variables
		Random random = new Random();
		int aaaCount = 0;
		int aCount = 0;
		int tCount = 0;
		int cCount = 0;
		int gCount = 0;
				
		// generate 1000 total codons
		for(int x=0; x<1000; x++)
		{
			String codon = ""; //start with nothing
			
			// generate a codon from 3 characters
			for(int y=0; y<3; y++) 
			{
				int num = random.nextInt(4); // get some random value from 0:3
				
				//append letters to codon and add to letter counts
				if(num == 0) {
					codon += "A"; 
					aCount++;
				}
				else if(num == 1) {
					codon += "T";
					tCount++;
				}
				else if(num == 2) {
					codon += "C";
					cCount++;
				}
				else if(num == 3) {
					codon += "G";
					gCount++;
				}
			}
			
			// print resulting codon and add to AAA codon count
			System.out.print(codon);
			if (codon.equals("AAA"))
				aaaCount++;
		}
		
		System.out.println("\nAAA:" + aaaCount);
		//System.out.println("A: " + aCount);
		//System.out.println("T: " + tCount);
		//System.out.println("C: " + cCount);
		//System.out.println("G: " + gCount);
		
		//calculate percentages for display
		float pctA = aCount*100/3000;
		float pctT = tCount*100/3000;
		float pctC = cCount*100/3000;
		float pctG = gCount*100/3000;
		
		System.out.println("% A: " + pctA);
		System.out.println("% T: " + pctT);
		System.out.println("% C: " + pctC);
		System.out.println("% G: " + pctG);	
	}
	
	public static void customGen() {
		//initialize variables
		Random random = new Random();
		int aaaCount = 0;
		int aCount = 0;
		int tCount = 0;
		int cCount = 0;
		int gCount = 0;
		
		// generate 1000 total codons
		for(int x=0; x<1000; x++)
		{
			String codon = ""; //start with nothing
			
			// generate a codon from 3 characters
			for(int y=0; y<3; y++) 
			{
				//yield a value between 0 and 1 and apply distribution to generate character
				float num = random.nextFloat();
				if (0 <= num & num < 0.12) {
					codon += "A";
					aCount++;
				}
				else if (0.12 <= num & num < 0.50) {
					codon += "C";
					cCount++;
				}
				else if (0.50 <= num & num < 0.89) {
					codon += "G";
					gCount++;
				}
				else if (0.89 <= num & num <= 1.00) {
					codon += "T";
					tCount++;
				}
			}
			
			// print resulting codon and add to AAA codon count
			System.out.println(codon);
			if (codon == "AAA")
				aaaCount++;
		}
		
		System.out.println("AAA:" + aaaCount);
		
		//calculate percentages for display
		float pctA = aCount*100/3000;
		float pctT = tCount*100/3000;
		float pctC = cCount*100/3000;
		float pctG = gCount*100/3000;
		
		System.out.println("% A: " + pctA);
		System.out.println("% T: " + pctT);
		System.out.println("% C: " + pctC);
		System.out.println("% G: " + pctG);	
	}
	
	public static void main(String[] args) {
		uniformGen();
		customGen();
	}
}
