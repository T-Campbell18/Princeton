/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: Takes four first names as command-line arguments and prints 
 * a proper sentence with the names in the reverse of the order given.
 *
 ******************************************************************************/

public class HiFour 
{
  public static void main(String[] args) 
  {
    System.out.println("Hi " + args[3] + ", " + args[2] + 
                       ", " + args[1] 
                        + ", and " + args[0] + ".");
  }
}