import java.io.*;
import java.util.*;

/** program to find compression ratio using LZW compression
 */
public class Main {

	public static void main(String[] args) throws IOException {

		long start = System.currentTimeMillis();
		// String inputFileName = args[0];
		String inputFileName = "small.txt";
		FileReader reader = new FileReader(inputFileName);
		Scanner in = new Scanner(reader);

		long fileSize = new File(inputFileName).length() * 8;

		Trie trie = new Trie();

		StringBuilder sb = new StringBuilder();

		while (in.hasNextLine()) {
			String[] words = in.nextLine().split(" ");
			for (String w : words) {
				trie.insert(w);
			}
			trie.insert("\n");
		}

		int compressedSize = trie.getSize();

		System.out.println("Original file length in bits = " + fileSize);
		System.out.println("Compressed file length in bits = " + compressedSize);
		System.out.println("Compression ratio = " + (float) compressedSize/fileSize);

		reader.close();

		// output the results here

		// end timer and print elapsed time as last line of output
		long end = System.currentTimeMillis();
		System.out.println("Elapsed time: " + (end - start) + " milliseconds");
	}

}
