 /******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: takes two integer command-line arguments n and trials,
 * each trial(independent) simulates a random walk of n steps and 
 * computes the squared distance. Prints the mean squared distance 
 * (the average of the trials squared distances).
 *
 ******************************************************************************/

public class RandomWalkers 
{
  public static void main(String[] args) 
  {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    double sum = 0.0;
    for (int z = 0; z < trials; z++)
    {
      int x = 0;
      int y = 0;    
      for (int a = 0; a < n; a++)
      {
        int direction = (int) (Math.random()*4);
        if (direction == 0)
          x++;
        if (direction == 1)
          y++;
        if (direction == 2)
          x--;
        if (direction == 3)
          y--;
      }
      int squaredDistance = (x * x + y * y);
      sum += squaredDistance;
    }
    double mean = sum / trials;
    System.out.println("mean squared distance = " + mean);
  }
}