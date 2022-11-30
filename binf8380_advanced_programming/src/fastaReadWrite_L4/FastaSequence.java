package fastaReadWrite_L4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FastaSequence {
	private String header;
	private String sequence;
	
	// constructor
	public FastaSequence(String header, String sequence)
	{
		this.header = header.substring(1); // header should be the incoming string minus index 0
		this.sequence = sequence; // return sequence as is
	}
	// return header of sequence
	public String getHeader()
	{
		return header;
	}
	
	// return DNA sequence
	public String getSequence()
	{
		return sequence;
	}
	
	// return G and C counts over sequence length
	public float getGCRatio()
	{
		// convert to floats for division later
		float gcCount = (float)( counter('G') + counter('C') );
		float len = (float)sequence.length();
		
		return gcCount/len;
	}
	
	public int counter(char base) // make the counter an integer function for space
	{		
		int count = 0;
		
		for(int i = 0; i<sequence.length(); i++)
		{
			char c = sequence.charAt(i);
			
			if (c == base) count++;
		}
		
		return count;
		
	}
	
	// read in fasta file
	public static List<FastaSequence> readFastaFile(String path) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		
		List<FastaSequence> fastaList = new ArrayList<FastaSequence>(); // birthplace of fastaList
		
		String tmp_header = ""; //LinkedList will let me pop off the ">" faster
		String tmp_sequence = ""; //ArrayList will generate base counts faster
		
		// read in the whole file and save into a single List<FastaSequence> object
		for (String nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine())
		{
			if (nextLine.charAt(0) == '>' && tmp_header == "") // should only run for the 1st line in the file
			{
				tmp_header = nextLine; // save first header as tmp_header
			}
			else if (nextLine.charAt(0) == '>') // all subsequent headers
			{
				fastaList.add(new FastaSequence(tmp_header, tmp_sequence)); // output previous finished item
				tmp_header = nextLine; // populate new header
				tmp_sequence = ""; // initiate new sequence
			}	
			else // in case of multi-line sequences
			{
				tmp_sequence += nextLine; // append sequence snippet to tmp_sequence
			}
		}
		
		reader.close();
		return fastaList;
	}
	
	public static void writeTableSummary( List<FastaSequence> list, File outputFile) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		
		writer.write("sequenceID\tnumA\tnumC\tnumG\tnumT\tsequence\n");
		
		// for each sequence in the fasta file
		for(FastaSequence seq : list)
		{
			// use FastaSequence counter method for each base
			int Acount = seq.counter('A');
			int Ccount = seq.counter('C');
			int Gcount = seq.counter('G');
			int Tcount = seq.counter('T');
			
			// output desired info
			writer.write(seq.header + "\t" + Acount + "\t" + Ccount + "\t" + Gcount + "\t" 
					+ Tcount + "\t" + seq.sequence);
		}
		
		writer.flush(); // failure to flush may result in incomplete output
		writer.close(); // always do
	}
	
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> fastaList = FastaSequence.readFastaFile("C:\\Users\\Perry\\Documents\\BINF8380\\Reptiles_R35_copy.fasta");
		
		for(FastaSequence fs : fastaList)
		{
			System.out.println(fs.getHeader());
			System.out.println(fs.getSequence());
			System.out.println(fs.getGCRatio());
		}
		
		File myFile = new File("C:\\Users\\Perry\\Documents\\BINF8380\\lab4OutputFile.txt");
		
		writeTableSummary(fastaList, myFile);
	}
}
