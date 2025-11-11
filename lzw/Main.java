import java.io.*;
import java.util.*;

/** program to find compression ratio using LZW compression
 */
public class Main {

	public static void main(String[] args) throws IOException {

		long start = System.currentTimeMillis();
		String inputFileName = args[0];
		FileReader reader = new FileReader(inputFileName);
		Scanner in = new Scanner(reader);

		long fileSize = new File(inputFileName).length() * 8;

		Trie trie = new Trie();
		StringBuilder sb = new StringBuilder();

		while (in.hasNextLine()) {
			sb.append(in.nextLine());
			sb.append("\n");
		}
		String t = sb.toString();

        Map<String, String> codeMap = new HashMap<>();
        for (int i = 0; i < 127; i++) {
            String code = trie.search(Character.toString((char) i ));
            codeMap.put(Character.toString((char) i), code);
        }

        int i = 0;
		sb = new StringBuilder();
        // t = "GACGATACGATACG"; //Test data

        // This one works
        // while (i < t.length()-1) {
        //     int j = i + 1;
        //     String node = "";
        //     String n = Character.toString(t.charAt(i));

        //     while (j < t.length() && codeMap.containsKey(n)) {
        //         node = n;
        //         n += t.charAt(j);
        //         j++;
        //     }

        //     i += (node.length());
        //     sb.append(trie.getCode(codeMap.get(node)));

        //     if (i < t.length()) {
        //         char c = t.charAt(i);
        //         node += c;
        //         codeMap.put(node, trie.insert(node));
        //     }
        // }
        // int compressedSize = sb.toString().length();

        while (i < t.length()-1) {
            i += trie.codeInsert(t, i);
        }
		int compressedSize = trie.getSize();

		// output the results here

		System.out.println("Original file length in bits = " + fileSize);
		System.out.println("Compressed file length in bits = " + compressedSize);
		System.out.println("Compression ratio = " + (float) compressedSize/fileSize);

		reader.close();

		// end timer and print elapsed time as last line of output
		long end = System.currentTimeMillis();
		System.out.println("Elapsed time: " + (end - start) + " milliseconds");
	}

}
