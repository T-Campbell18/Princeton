/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: Takes 3 Integers as command-line arguments and prints the 
 * 3 int and the sum of the 3 int in the form of an equation.
 *
 ******************************************************************************/

public class SumThree
{
  public static void main(String[] args) 
  {
    int x = Integer.parseInt(args[0]);
    int y = Integer.parseInt(args[1]);
    int z = Integer.parseInt(args[2]);
    int sum = x + y + z;
    System.out.println(x + " + "+ y + " + " + z + " = " + sum);
  }
}