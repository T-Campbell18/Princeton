/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: Takes an integer command-line argument n and 
 * simulates the motion of a random walk for n steps. Prints the 
 * location after each step (including the starting point),
 * treating the starting point as the origin (0, 0). Also, prints the
 * square of the final Euclidean distance from the origin.
 *
 ******************************************************************************/

public class RandomWalker 
{
  public static void main(String[] args) 
  {
    int x = 0;
    int y = 0;
    System.out.println("(" + x + ", " + y + ")");
    int steps = Integer.parseInt(args[0]);
    for (int a = 0; a < steps; a++)
    {
      int direction = (int) (Math.random()*4);
      // move east
      if (direction == 0)
      {
        x++;
        System.out.println("(" + x + ", " + y + ")");
      }
      // move north
      if (direction == 1)
      {
        y++;
        System.out.println("(" + x + ", " + y + ")");
      }
      // move west
      if (direction == 2)
      {
        x--;
        System.out.println("(" + x + ", " + y + ")");
      }
      // move south
      if (direction == 3)
      {
        y--;
        System.out.println("(" + x + ", " + y + ")");
      }
    }
    int squaredDistance = (x * x + y * y);
    System.out.println("squared distance = " + squaredDistance);
  }
}