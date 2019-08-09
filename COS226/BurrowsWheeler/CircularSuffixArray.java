/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: creates a fundamental data structure known as the circular suffix
 * array, which describes the abstraction of a sorted array of the N circular 
 * suffixes of a string of length N
 *
 ******************************************************************************/
import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray 
{
  // array for index of the original suffix that appears 
  // ith in the sorted array
  private Integer[] index;
   
  // circular suffix array of s
  public CircularSuffixArray(String s)
  {
    if (s == null)
      throw new NullPointerException();
    index = new Integer[s.length()];
    for (int x = 0; x < index.length; x++) 
      index[x] = x;
    char[] chars = s.toCharArray();
    Arrays.sort(index, new Comparator<Integer>()
    {
      @Override
      public int compare(Integer a, Integer b) 
      {
        for (int x = 0; x < length(); x++) 
        {
          char charA = chars[(x + a) % length()];
          char charB = chars[(x + b) % length()];
          if (charA != charB)
            return charA - charB;
        }
        return 0;
      }
    });
  }
  
  // length of s
  public int length()
  {
    return index.length;
  }
  
  // returns index of ith sorted suffix                  
  public int index(int i)
  {
    if (i < 0 || i >= length())
      throw new IndexOutOfBoundsException();
    return index[i];
  }
  
  // unit testing (not graded)                
  public static void main(String[] args)
  {
    CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
    System.out.println("Length: " + csa.length());
    for (int x = 0; x < csa.length(); x++) 
      System.out.println(csa.index(x));
    // System.out.println(csa.index(-1));
    // System.out.println(csa.index(12));
  }
}