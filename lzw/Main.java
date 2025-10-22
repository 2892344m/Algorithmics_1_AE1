import java.io.*;
import java.util.*;

/** program to find compression ratio using LZW compression
 */
public class Main {

	public static void main(String[] args) throws IOException {

		long start = System.currentTimeMillis();
		// String inputFileName = args[0];
		FileReader reader = new FileReader("small.txt");
		Scanner in = new Scanner(reader);
		
		Map<String, String> dict = new HashMap<>();
		int codewordLength = 8;
		int curCode = 0;

		//Build the initial single char dictionary
		for (int i = 0; i < 32 * codewordLength; i++) {
			char c = (char) i;
			dict.put(Character.toString(c), Integer.toBinaryString(i));
			curCode++;
		}

		StringBuilder sb = new StringBuilder();

		while (in.hasNextLine()) {
			sb.append(in.nextLine());
			sb.append("\n");
		}
		
		int i = 0;
		String t = sb.toString();

		while (i < t.length()-1) {
			int r = i;
			String s = t.substring(i, r);
			while (r < t.length() && dict.containsKey(t.substring(i, r+1))) {
				s = t.substring(i, r+1);
				r++;
			}
			String code = dict.get(s);

			i += s.length();
			i = Math.min(i, t.length()-1);
			char c = t.charAt(i);

			dict.put(s+c, Integer.toBinaryString(curCode));
			curCode++;
			if (Integer.toBinaryString(curCode).length() > codewordLength) codewordLength++;

		}

		Trie trie = new Trie();
		for(String w : dict.keySet()) {
			trie.insert(w);
		}
		// read in the data and do the work here
        // read a line at a time to enable newlines to be detected and included in the compression

		System.out.println(trie.extract());

		reader.close();

		// output the results here

		// end timer and print elapsed time as last line of output
		long end = System.currentTimeMillis();
		System.out.println("Elapsed time: " + (end - start) + " milliseconds");
	}

}
