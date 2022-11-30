package arbitraryAlphabet_L3;

import java.util.Random;

public class ArbitraryAlphabet {
	public static String generateRandomSequence(char[] alphabet, float[] weights, int length) throws Exception
	{
		// check alphabet and weight length
		if (alphabet.length != weights.length) throw new Exception("weights and alphabet must be of equal length!");
		else if (alphabet.length >= 0) // check length >=0
		{
			// check to see that the sum of weights is within round-off error of 1
			float sum = 0;
			for (int i=0; i < weights.length; i++)
				sum += weights[i];
			if (Math.round(sum*1.0/1.0) != 1f) throw new Exception("Weights must sum to 1!"); // check rounding by multiplying and dividing by 1
		}
		else throw new Exception("weights and alphabet must be at least length 0!");
			
		// Return a random string of characters sampled with replacement from alphabet[] with relative frequencies set by weights[]
		Random random = new Random();
		String outSequence = ""; // initialize output String
		
		for (int i=0; i<length; i++) // for every desired component of alphabet
		{
			float num = random.nextFloat(); //yield a value between 0 and 1 and apply distribution to generate character
			float floor = 0; // initialize a minimum probability
			for (int j=0; j<weights.length; j++) // for each desired possible value
			{
				if (floor <= num & num < floor+weights[j]) // check random value exists in current range based on distribution
				{
					outSequence += alphabet[j]; // if yes, append corresponding character
					break; // move to next character slot
				}
				else { floor += weights[j]; } // if no, move into the next range and try again
			}
		}
		
		return outSequence;
	}


	public static void main(String[] args) throws Exception
	{
		float[] dnaWeights = { .3f, .3f, .2f, .2f };
		char[] dnaChars = { 'A', 'C', 'G', 'T'  };
		
		// a random DNA 30 mer
		System.out.println(generateRandomSequence(dnaChars, dnaWeights,30));
		
		// background rate of residues from https://www.science.org/doi/abs/10.1126/science.286.5438.295
		float proteinBackground[] =
			{0.072658f, 0.024692f, 0.050007f, 0.061087f,
		        0.041774f, 0.071589f, 0.023392f, 0.052691f, 0.063923f,
		        0.089093f, 0.023150f, 0.042931f, 0.052228f, 0.039871f,
		        0.052012f, 0.073087f, 0.055606f, 0.063321f, 0.012720f,
		        0.032955f}; 
			

		char[] proteinResidues = 
				new char[] { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
							 'V', 'W', 'Y' };
		
		// a random protein with 30 residues
		System.out.println(generateRandomSequence(proteinResidues, proteinBackground, 30));
		
	}
}
