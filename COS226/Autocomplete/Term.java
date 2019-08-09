/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: creates immutable data type that represents an autocomplete 
 * term: a query string and an associated integer weight.
 *
 ******************************************************************************/
import java.util.Comparator;
import java.util.Arrays;

public class Term implements Comparable<Term>
{
    private final String query; // represents the string query
    private final long weight; // represents the associated integer weight 
    // Initializes a term with the given query string and weight.
    public Term(String query, long weight)
    {
      if (query == null)
        throw new NullPointerException();
      if (weight < 0)
        throw new IllegalArgumentException();
      this.query = query;
      this.weight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder()
    {
      return new ComparatorByReverseOrderWeight();
    }

    // Compares the two terms in lexicographic order but using only
    // the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r)
    {
      if (r < 0)
        throw new IllegalArgumentException();
      return new ComparatorByPrefixOrderQuery(r);  
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that)
    {
      return this.query.compareTo(that.query);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString()
    {
      return weight + "\t" + query;
    }
    
    // Comparator of Term in descending order by weight
    private static class ComparatorByReverseOrderWeight implements Comparator<Term> 
    {
      // compares 2 terms by the weight
      public int compare(Term term1, Term term2) 
      {
        if (term1.weight == term2.weight) 
          return 0;
        if (term1.weight > term2.weight) 
          return -1;
        return 1;
      }
    }
    
    // lexicographic order by query string but using only the first r characters
    private static class ComparatorByPrefixOrderQuery implements Comparator<Term> 
    {
      private int r; // integer for first r characters
      
      // Initializes r
      private ComparatorByPrefixOrderQuery(int r) 
      {
        this.r = r;
      }
      // compare 2 terms by the first r characters in lexicographic order
      public int compare(Term term1, Term term2) 
      {
        String pre1 = "";
        String pre2 = "";
        
        if (term1.query.length() < r) 
          pre1 = term1.query;
        else 
          pre1 = term1.query.substring(0, r);
        
        if (term2.query.length() < r) 
          pre2 = term2.query;
        else 
          pre2 = term2.query.substring(0, r);

        return pre1.compareTo(pre2);
      }
    }
    
    // unit testing (required)
    public static void main(String[] args)
    {
      Term[] terms = new Term[5];
      terms[0] = new Term("Tyler", 18);
      terms[3] = new Term("Biraj", 18);
      terms[2] = new Term("Samara", 23);
      terms[1] = new Term("Kayla", 17);
      terms[4] = new Term("Ty", 13);
      // print orginal
      for (Term term : terms) 
        System.out.println(term);
      System.out.println();
      // print after different sorts
      Arrays.sort(terms, Term.byReverseWeightOrder());
      for (Term term : terms) 
        System.out.println(term);
      System.out.println();
      Arrays.sort(terms, Term.byPrefixOrder(2));
      for (Term term : terms) 
        System.out.println(term);
      System.out.println();
      Arrays.sort(terms);
      for (Term term : terms) 
        System.out.println(term);
    }
}