/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: Burrows-Wheeler transform does not compress a message, but 
 * rather to transforms it into a form that is more amenable to compression
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler 
{
  private static final int R = 256; // extended ASCII alphabet size
  
  // apply Burrows-Wheeler transform, 
  // reading from standard input and writing to standard output
  public static void transform()
  {
    String s = BinaryStdIn.readString();
    CircularSuffixArray csa = new CircularSuffixArray(s);
    int first = -1;
    for (int x = 0; x < csa.length(); x++) 
    {
      if (csa.index(x) == 0) 
      {
        first = x;
        break;
      }
    }
    BinaryStdOut.write(first);
    for (int x = 0; x < csa.length(); x++)
      BinaryStdOut.write(s.charAt((csa.index(x) + s.length() - 1) % s.length()));
    BinaryStdOut.close();
  }

  // apply Burrows-Wheeler inverse transform, 
  // reading from standard input and writing to standard output
  public static void inverseTransform()
  {
    int first = BinaryStdIn.readInt();
    String s = BinaryStdIn.readString();
    int n = s.length();
    char[] t = s.toCharArray();
    int[] next = new int[n];
    int[] count = new int[R + 1];

    for (int x = 0; x < n; x++)
      count[t[x] + 1]++;
      
    for (int r = 0; r < R; r++)
      count[r + 1] += count[r];
      
    for (int x = 0; x < n; x++)
      next[count[t[x]]++] = x; 
      
    int i = next[first];
    for (int x = 0; x < n; x++)
    {
      BinaryStdOut.write(t[i]);
      i = next[i];
    }
    BinaryStdOut.close();
  }

  // if args[0] is '-', apply Burrows-Wheeler transform
  // if args[0] is '+', apply Burrows-Wheeler inverse transform
  public static void main(String[] args)
  {
    if (args[0].equals("-"))
      transform();
    else if (args[0].equals("+"))
      inverseTransform();
    else
      throw new IllegalArgumentException();
  }
}