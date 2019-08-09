/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: maintains an ordered sequence of the characters in the alphabet, 
 * and repeatedly read in a character from the input message, print out the 
 * position in which that character appears, and move that character to the 
 * front of the sequence.
 *
 ******************************************************************************/
import java.util.LinkedList;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront 
{
  private static final int R = 256; // extended ASCII alphabet size
  
  // apply move-to-front encoding, 
  // reading from standard input and writing to standard output
  public static void encode()
  {
    LinkedList<Character> chars = new LinkedList<Character>();
    for (int x = 0; x < R; x++)
      chars.add((char) x);
    String s = BinaryStdIn.readString();
    char[] input = s.toCharArray();
    
    for (int x = 0; x < input.length; x++) 
    {
      int i = chars.indexOf(input[x]);
      BinaryStdOut.write((char) i);
      chars.addFirst(chars.remove(i));
    }
    
    BinaryStdOut.close();
  }

  // apply move-to-front decoding, 
  // reading from standard input and writing to standard output
  public static void decode()
  {
    LinkedList<Character> chars = new LinkedList<Character>();
    for (int x = 0; x < R; x++)
      chars.add((char) x);
    String s = BinaryStdIn.readString();
    char[] input = s.toCharArray();
    for (int x = 0; x < input.length; x++)
    {
      char c = chars.remove(input[x]);
      chars.addFirst(c);
      BinaryStdOut.write(c);
    }
    
    BinaryStdOut.close();
  }

  // if args[0] is '-', apply move-to-front encoding
  // if args[0] is '+', apply move-to-front decoding
  public static void main(String[] args)
  {
    if (args[0].equals("-"))
      encode();
    else if (args[0].equals("+"))
      decode();
    else
      throw new IllegalArgumentException();
  }
}