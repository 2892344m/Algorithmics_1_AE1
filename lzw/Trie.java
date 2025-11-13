import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Trie {
	
	// create root of the trie
	private Node root; 
    int compressedSize;
    int curCode;
    int bitLength;
    StringBuilder binaryString;
    Set<String> codeSet = new HashSet<>();
	
	public Trie() {
		// null character in the root
		root = new Node(Character.MIN_VALUE, ""); 
        bitLength = 8;
        compressedSize = 0;
        curCode = 0;
        binaryString = new StringBuilder();
        codeSet = new HashSet<>();

        //Initialise the ASCII characters
        Node cur = new Node('-', "");

        for (int i = 0; i < 127; i++) {
            Node next = new Node((char) i, Integer.toBinaryString(curCode));
            cur.setSibling(next);
            cur = next;
            curCode++;
            binaryString.append(Integer.toBinaryString(curCode));
            codeSet.add(cur.getCode());
            if (i == 0) root.setChild(cur);
        }
	}

    // public Trie() {
	// 	// null character in the root
	// 	root = new Node(Character.MIN_VALUE, ""); 
    //     bitLength = 2;
    //     compressedSize = 0;
    //     curCode = 3;
    //     binaryString = new StringBuilder();
    //     Node child = new Node('A', "00");
    //     root.setChild(child);
    //     Node sib = new Node('C', "01");
    //     child.setSibling(sib);
    //     child = sib;
    //     sib = new Node('G', "10");
    //     child.setSibling(sib);
    //     child = sib;
    //     sib = new Node('T', "11");
    //     child.setSibling(sib);
	// }
    
	// list to store the words in the trie when extract is called
    private LinkedList<String> words = new LinkedList<String>();

    // possible outcomes of a search
	private enum Outcomes {PRESENT, ABSENT, UNKNOWN}
	
	/** search trie for word w */
	public String search(String w) {
		
		// initially outcome unknown
		Outcomes outcome = Outcomes.UNKNOWN;
		
		// position in word so far searched (start at beginning)
		int i = 0;

		// start with first child of the root
		Node current = root.getChild();
		
		// start search
		while (outcome == Outcomes.UNKNOWN) {
			
			if (current == null) outcome = Outcomes.ABSENT; // dead-end
			else if (current.getLetter() == w.charAt(i)) { // positions match				
				if (i == w.length() - 1) {
					outcome = Outcomes.PRESENT; // matched word
				}
				else { // descend one level...
					current = current.getChild(); // in trie
					i++; // in word being searched
				}
			}	
			else { // positions not matched so try next sibling
				current = current.getSibling();
			}
		}
		// return answer
		if (outcome != Outcomes.PRESENT) return "";
		else return prependString(current.getCode().length()) + current.getCode();
	}
		
	/* now changed so insertion is performed in a lexicographical order (task 3) */
		
    /** inserting a word w into trie */
    //Modified version now returns code for insert into the map
    public String insert(String w){
        
        int i = 0; // position in word (start at beginning)
        Node current = root; // current node in trie (start at root)
        Node next = current.getChild(); // first child of current node we are testing
        
        while (i < w.length()) { // not reached end of word
            if (next !=null && next.getLetter() == w.charAt(i)) { // chars match: decend a level
                current = next; // update node to the child node
                next = current.getChild(); // update child node
                i++; // next position in the word
            }
            else if (next != null) { // try next sibling
                next = next.getSibling();
            }
            else { // no more siblings: need new node
                curCode++;
                if (curCode >= Math.pow(2, bitLength)) {
                    bitLength++;
                }
                Node x = new Node(w.charAt(i), Integer.toBinaryString(curCode)); // label with ith element of the word
                x.setSibling(current.getChild()); // sibling: first child of current node
                current.setChild(x); // make first child of current node
                current = x; // move to the new node
                next = current.getChild(); // update child node
                i++; // next position in word
            }
        }
        current.setIsWord(true); // current represents word w
        return current.getCode();
    }

    public int codeInsert(String t){ //Recieve the full text and a pointer to the words start
        Node current = root; // current node in trie (start at root)
        Node next = current.getChild(); // first child of current node we are testing
        int i = 0;
        
        while (i <= t.length()) { // not reached end of word
            if (next != null && i < t.length() && next.getLetter() == t.charAt(i)) { // chars match: decend a level
                current = next; // update node to the child node
                next = current.getChild(); // update child node
                i++; // next position in the word
            }
            else if (next != null) { // try next sibling
                next = next.getSibling();
            }
            else { // no more siblings: need new node
                String code = current.getCode();

                curCode++;
                if (curCode > Math.pow(2, bitLength)) {
                    bitLength++;
                }
                compressedSize += bitLength;

                binaryString.append(getCode(current.getCode()));
                if (i >= t.length()) break;

                Node x = new Node(t.charAt(i), code); // label with ith element of the word
                x.setSibling(current.getChild()); // sibling: first child of current node
                current.setChild(x); // make first child of current node
                next = root.getChild();
            }
        }

        current.setIsWord(true); // current represents word w
        return 0;
    }

	/* traverse trie from node t adding words to list a assuming path to t yields the string s */
	private void traverse(Node t, String s){
		if (t != null){ // not at a leaf
			
			if (t.getIsWord()) words.add(s + t.getLetter()); // if node represents a word: add word to list
			
			// continue the traversal
			traverse(t.getChild(), s + t.getLetter()); // first look at children (note string changes)
			traverse(t.getSibling(), s); // then look at siblings
		}
	}

	/** returns an List containing all the distinct words in the trie */
	public LinkedList<String> extract(){
        words.clear(); // clear the list
		String s = ""; // path to root yields empty string
		traverse(root.getChild(), s); // start traversal
		return words;
	}

    private String prependString(int binaryLength) {
        int prependSize = bitLength - binaryLength;
        return "0".repeat(prependSize);
    }

    public String getCode(String code) {
        return prependString(code.length()) + code;
    }

    public int getSize() {
        return compressedSize;
    }
}
