/*******************************************************************************
 *  Name: Tyler Campbell
 *  NetID: tylercc 
 *  Precept: P02
 *
 * Description: represents a linear feedback shift register (LFSR) that is a
 * register of bits that performs discrete step operations that shifts the bits 
 * one position to the left and replaces the vacated bit by the exclusive or 
 * (xor) of the bit shifted off and the bit previously at a given tap position 
 * in the register
 *
 ******************************************************************************/

public class LFSR
{
  private String seed; // represents the LFSR as a String 
  // (the intial seed and after steps)
  
  private int tap; // the tap postion of the LFSR
  
  // creates an LFSR with the specified seed and tap
  public LFSR(String seed, int tap)
  {
    this.seed = seed;
    this.tap = tap;
  }
  
  // simulates one step and return the new bit (as 0 or 1)
  public int step()
  {
    String temp = seed.substring(1);
    int index = seed.length() - tap - 1;
    int a = Integer.parseInt(seed.substring(0, 1));
    int b = Integer.parseInt(seed.substring(index, index+1));
    int c = a ^ b;
    seed = temp + c;
    return c;
  }
  
  // simulates k steps and return the k bits as a k-bit integer
  public int generate(int k)
  {
    int a = 0;
    for (int x = 0; x < k; x++)
    {
      int z = step();
      a += a;
      a += z;
    }
    return a;
  }
  
  // returns a string representation of this LFSR
  public String toString()
  {
    return seed;
  }
  
  // unit tests this class
  public static void main(String[] args) 
  {
    LFSR lfsr = new LFSR("01101000010", 8);
    StdOut.println(lfsr);
    for (int i = 0; i < 10; i++) 
    {
      int bit = lfsr.step();
      StdOut.println(lfsr + " " + bit);
    }
    for (int i = 0; i < 10; i++) 
    {
      int r = lfsr.generate(5);
      StdOut.println(lfsr + " " + r);
    }
  }
}
