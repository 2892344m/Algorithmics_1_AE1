import java.io.*;
import java.util.*;

/** program to find compression ratio using Huffman coding
 */
public class Main {


	private static class Node {
		int weight;
		char c;
		Node left;
		Node right;

		public Node(Node left, Node right) {
			this.weight = left.weight + right.weight;
			this.left = left;
			this.right = right;
		}

		public Node(char c) {
			this.c = c;
			this.weight = 0;
			this.left = null;
			this.right = null;
		} 
	}

	private static int calculateWPL(Node root, int dist) {
		int WPL = 0;
		if (root.left == null && root.right == null) return root.weight * dist;
		WPL += calculateWPL(root.left, dist+1);
		WPL += calculateWPL(root.right, dist+1);
		return WPL;
	}

	public static void main(String[] args) throws IOException {

		long start = System.currentTimeMillis();
		String inputFileName = args[0];
		FileReader reader = new FileReader(inputFileName);
		Scanner in = new Scanner(reader);
		String line;

		//Prepare hash map for assembling parentless nodes
		Map<Character, Node> nodeMap = new HashMap<>();
		char newLine = '\n';
		Node newLineNode = new Node(newLine);
		nodeMap.put(newLine, newLineNode);
		
		//Read file size and convert from bytes to bits
		long fileSize = new File(inputFileName).length() * 8;

		//Read each character and increment appropriate map keys
		while (in.hasNextLine()) {
			line = in.nextLine();
			for (char c : line.toCharArray()) {
				Node n = nodeMap.getOrDefault(c, new Node(c));
				n.weight++;
				nodeMap.put(c, n);
			}
			newLineNode.weight++;
		}

		//Place all nodes in a priority queue of ascending order
		PriorityQueue<Node> q = new PriorityQueue<>((n1, n2) -> Integer.compare(n1.weight, n2.weight));
		q.addAll(nodeMap.values());

		//Generate parent nodes
		while (q.size() > 1) {
			Node n1 = q.poll();
			Node n2 = q.poll();
			Node parent = new Node(n1, n2);
			q.offer(parent);
		}

		//Get root node
		Node root = q.poll();
		int compressedSize = calculateWPL(root, 0);
		
		// read in the data and do the work here
        // read a line at a time to enable newlines to be detected and included in the compression

		System.out.println("Large.txt");
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
