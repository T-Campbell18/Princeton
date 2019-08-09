/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: creates imutable data type that provides autocomplete 
 * functionality for a given set of string and weights, using Term and 
 * BinarySearchDeluxe
 *
 ******************************************************************************/
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Autocomplete 
{
  private final Term[] terms; // array of the terms
  
  // Initializes the data structure from the given array of terms.
  public Autocomplete(Term[] terms)
  {
    if (terms == null) 
      throw new NullPointerException();
    this.terms = new Term[terms.length];
    for (int x = 0; x < terms.length; x++)
    {
      if (terms[x] == null)
        throw new NullPointerException();
      this.terms[x] = terms[x];  
    }
    Arrays.sort(this.terms);
  }

  // Returns all terms that start with the given prefix,
  // in descending order of weight.
  public Term[] allMatches(String prefix)
  {
    if (prefix == null)
      throw new NullPointerException();
    Term temp = new Term(prefix, 0);
    int length = prefix.length();
    int first = BinarySearchDeluxe.
                            firstIndexOf(terms, temp, Term.byPrefixOrder(length));
    if (first == -1) 
      return new Term[0];
    int last  = BinarySearchDeluxe.
                           lastIndexOf(terms, temp, Term.byPrefixOrder(length));
    Term[] match = new Term[1 + last - first];
    for (int x = 0; x < match.length; x++)
      match[x] = terms[first++];
    Arrays.sort(match, Term.byReverseWeightOrder()); 
    return match;
  }

  // Returns the number of terms that start with the given prefix.
  public int numberOfMatches(String prefix)
  {
    if (prefix == null)
      throw new NullPointerException();
    Term temp = new Term(prefix, 0);
    int length = prefix.length();
    int first = BinarySearchDeluxe.
                            firstIndexOf(terms, temp, Term.byPrefixOrder(length));
    int last  = BinarySearchDeluxe.
                            lastIndexOf(terms, temp, Term.byPrefixOrder(length));
    if (last == -1 || first == -1)
      return 0;
    return last - first + 1;
  }

  // unit testing (required)
  public static void main(String[] args) 
  {

    // read in the terms from a file
    String filename = args[0];
    In in = new In(filename);
    int N = in.readInt();
    Term[] terms = new Term[N];
    for (int i = 0; i < N; i++) 
    {
      long weight = in.readLong();           // read the next weight
      in.readChar();                         // scan past the tab
      String query = in.readLine();          // read the next query
      terms[i] = new Term(query, weight);    // construct the term
    }

    // read in queries from standard input and print out the top k matching terms
    int k = Integer.parseInt(args[1]);
    Autocomplete autocomplete = new Autocomplete(terms);
    while (StdIn.hasNextLine()) 
    {
      String prefix = StdIn.readLine();
      Term[] results = autocomplete.allMatches(prefix);
      int matches = autocomplete.numberOfMatches(prefix);
      StdOut.println("Number of mathes: " + matches);
      for (int i = 0; i < Math.min(k, results.length); i++)
        StdOut.println(results[i]);
    }
  }   
}