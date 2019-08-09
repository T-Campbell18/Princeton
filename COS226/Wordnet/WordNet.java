/******************************************************************************
  * Name: Tyler Campbell
  * NetID: tylercc
  * Precept: P04
  *
  * Partner Name: Lorraine Cliff 
  * Partner NetID: lcliff
  * Partner Precept: P04B
  * 
  * Description: This program builds an immutable data type WordNet. Here, WordNet
  * is a digraph where every vertex v is an integer that represents a synset and 
  * every directed edge v→w represents that w is a hypernym of v. For this program
  * the WordNet diagragh happens to be a rooted DAG. This means that that the 
  * diagraph is acyclic and that it has one vertex the root, that is an ancestor 
  * of every other vertex.
  *
  ******************************************************************************/
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.HashMap;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    // declare instance variables
    private ShortestCommonAncestor sca; // shortest common ancester object
    // maps integers to strings for synset representation
    private HashMap<Integer, String> synsets; 
    // maps strings to an array list of integers for hypernyms representation
    private HashMap<String, ArrayList<Integer>> nouns;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        // throws exception if any of the arguments are null
        if (synsets == null || hypernyms == null)
            throw new NullPointerException();
        // intializes instances variables
        this.synsets = new HashMap<Integer, String>();
        this.nouns = new HashMap<String, ArrayList<Integer>>();
        // calls helper functions to read in text files
        readSynsets(synsets);
        readHypernyms(hypernyms);
    }
    
    // helper function to read in synset.txt files
    private void readSynsets(String synsetsTxt) {
        In in = new In(synsetsTxt);
        while (in.hasNextLine()) {
            // splits the line at any commas and puts the results of the split 
            // into an array of strings
            String[] line = in.readLine().split(","); 
            int id = Integer.parseInt(line[0]);
            synsets.put(id, line[1]);
            String[] noun = line[1].split(" ");
            for (String n : noun) {
                if (isNoun(n)) 
                    nouns.get(n).add(id);
                else {
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    list.add(id);
                    nouns.put(n, list);
                } 
            }
        }
    }
    
    // helper meethod that reads in Hypernyms from a hypernyms.txt file
    private void readHypernyms(String hypernymsTxt) {
        Digraph digraph = new Digraph(synsets.size());
        In in = new In(hypernymsTxt);
        while (in.hasNextLine()) {
            // splits the line at any commas and puts the results of the 
            // splits in an array of strings
            String[] line = in.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            // adds an edge from the id to the diagraph for every index value in
            // the line array
            for (int x = 1, y = line.length; x < y; x++) {
                digraph.addEdge(id, Integer.parseInt(line[x]));
            }
        }
        // creates a ShortestCommonAncester object based on the diagraph 
        // created above
        sca = new ShortestCommonAncestor(digraph); 
    }
    
    // all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet(); // returns the set of strings in the nouns hashmap
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        // throws exception if the word argument is null
        if (word == null)
            throw new NullPointerException();
        return nouns.containsKey(word); // returns true if nouns has a key of word
    }
    
    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        // throws a nullPointerException if either of the arguments are null
        if (noun1 == null || noun2 == null)
            throw new NullPointerException();
        // throws an exception if either of the arguments aren't WordNet nouns
        if (!isNoun(noun1) || !isNoun(noun2))
            throw new IllegalArgumentException();
        // finds the shortest common ancestor of the two arguments
        return synsets.get(sca.ancestor(nouns.get(noun1), nouns.get(noun2)));
    }
    
    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        // throws exception if either of the arguments are null
        if (noun1 == null || noun2 == null)
            throw new NullPointerException();
        // throws an exception if either of the arguments aren't WordNet nouns
        if (!isNoun(noun1) || !isNoun(noun2))
            throw new IllegalArgumentException();  
        // calls the length function on a shortest common ancester object 
        // to determine the distance between the two nouns
        return sca.length(nouns.get(noun1), nouns.get(noun2));
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        // cretes a WordNet object (tests constructor)
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        
        // tests the nouns method, should print a list of the first 10 nouns 
        // in the wordnet
        int x = 0;
        for (String noun : wordnet.nouns()) {
            if (x < 10)
                StdOut.println(noun);
            x++;
        }
        
        // tests the isNoun function, should return true for AIDS and false 
        // for PERRY
        StdOut.println("AIDS is a word: " + wordnet.isNoun("AIDS"));
        StdOut.println("PERRY is a word: " + wordnet.isNoun("PERRY"));
        
        // tests the sca method. From the small subgraph shown its clear that the
        // least common ancestor of "jump" and "dash" is 
        // "Motion movement move"
        StdOut.println("shortest common ancestor of jump and dash: " + 
                       wordnet.sca("jump", "dash"));
        
        // tests the distance method. From the assignment pg the distance btw
        // "white_marlin" and "mileage" should be 23
        StdOut.println("distance between white_marlin and mileage: " + 
                       wordnet.distance("white_marlin", "mileage"));
        
    }
}