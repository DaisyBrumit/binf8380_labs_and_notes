package aminoAcidQuiz_L2;

import java.util.Random;

public class AminoAcidQuiz {
	public static void main(String[] args) {
		// start by generating hashmaps with amino acid info
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
		
		// next initialize empty values before starting the quiz
		Random random = new Random();
		int score = 0;
		long START_TIME = System.currentTimeMillis();
		boolean goodAnswer = true;
		
		// keep the quiz running for up to 30s as long as answers are right
		while (System.currentTimeMillis() - START_TIME <= 30000 & goodAnswer == true)
		{
			int index = random.nextInt(19); // generate random index value
				
			System.out.println("Amino Acid: " + FULL_NAMES[index]);
			String response = System.console().readLine().toUpperCase();
			
			if (response.equals(SHORT_NAMES[index]))
			{
				score++;
				System.out.println("Correct! Score = " + score);
			}
			else
			{
				System.out.println("Incorrect! Answer = " + SHORT_NAMES[index]);
				System.out.println("Final score = " + score);
				goodAnswer = false;
			}
		}
	}
}
