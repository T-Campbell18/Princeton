/******************************************************************************
  * Name: Tyler Campbell
  * NetID: tylercc
  * Precept: P04
  *
  * Partner Name: Lorraine Cliff 
  * Partner NetID: lcliff
  * Partner Precept: P04B
  * 
  * Description: This proram implements an immutable data type Outcast with the 
  * API provided. When given a specific set of WordNet nouns, this program 
  * determines which noun is the least related to the others and determines that 
  * to be the outcast. 
  ******************************************************************************/
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Outcast {
    // declare instance variables
    private WordNet wordnet; // WordNet representing the argument of the constructor
    
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet; // intializes instance variable
    }
    
    // given an array of WordNet nouns, return an outcast         
    public String outcast(String[] nouns) {
        String outcast = "";
        int max = Integer.MIN_VALUE;
        // computes the sum of the distance between each noun in nouns to all the 
        // other nouns in nouns
        for (String n1 : nouns) {
            int d = 0;
            for (String n2 : nouns) {
                d += wordnet.distance(n1, n2);
            }
            // only updates the max if d is greater than the previous max
            if (d > max) {
                max = d;
                // save the noun associated with this particular sum of 
                // distnace to it
                outcast = n1; 
            }
        }
        return outcast;
    }
    
    // see test client below   
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        // cretes a Outcast object (tests constructor)
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            // tests the outcast method
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    } 
}