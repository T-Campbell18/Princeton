/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: takes a command-line integer k; reads in a sequence of strings 
 * from standard input and prints out exactly k of them, uniformly at random
 * (item from the sequence can be printed out at most once)
 *
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Subset 
{
  public static void main(String[] args) 
  {
    // read from command-line
    int k = Integer.parseInt(args[0]);
    int x = 1;
    RandomizedQueue<String> random = new RandomizedQueue<String>();
    // read from StdIn
    while (!StdIn.isEmpty())
    {
      String next = StdIn.readString();
      if (x - 1 < k)
        random.enqueue(next);
      else
      {
        int ranIndex = StdRandom.uniform(x);
        if (ranIndex < k)
        {
          random.dequeue();
          random.enqueue(next);
        }
      }
      x++;
    }
    // print the random items
    for (x = 0; x < k; x++) 
      StdOut.println(random.dequeue());
  }
}