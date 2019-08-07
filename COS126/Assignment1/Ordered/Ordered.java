/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: Takes 3 Integer command line arguments strictly 
 * ascending order (x < y < z) or in strictly descending order 
 * (x > y > z), and false otherwise. 
 *
 ******************************************************************************/

public class Ordered 
{
  public static void main(String[] args) 
  {
    int x = Integer.parseInt(args[0]);
    int y = Integer.parseInt(args[1]);
    int z = Integer.parseInt(args[2]);
    boolean ascend = (x < y && y < z);
    b
    boolean inOrder = (x > y && y > z) || (x < y && y < z);
    System.out.println(inOrder);
  }
}