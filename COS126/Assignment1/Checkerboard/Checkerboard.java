/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: takes an integer command-line argument n, prints an 
 * n-by-n checkerboard pattern of asterisks (alternating between 
 * asterisks and spaces)
 *
 ******************************************************************************/

public class Checkerboard
{
  public static void main(String[] args) 
  {
    int n = Integer.parseInt(args[0]);
    for (int x = 0; x < n; x++)
    {
      for (int y = 0; y < n; y++)
      {
        if (x % 2 == 0)
          System.out.print("* ");
         else 
          System.out.print(" *");
      }
      System.out.println();
    }
  }
}