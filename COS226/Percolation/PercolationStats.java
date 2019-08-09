/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P04A
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: uses a Monte Carlo simulation to estimate the percolation 
 * threshold for muiltple trials
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats 
{
  private int trials; // number of trials
  // each index represents number of open sites 
  // when the system peroclates for each trial
  private double[] result;
  
  // perform trials independent experiments on an n-by-n grid
  public PercolationStats(int n, int trials) 
  {
    if (n <= 0)
      throw new IllegalArgumentException("n must be greater than 0");
    if (trials <= 0)
      throw new IllegalArgumentException("t must be greater than 0");  
    this.trials = trials;
    result = new double[trials];
    for (int a = 0; a < trials; a++) 
    {
      Percolation grid = new Percolation(n);
      while (!grid.percolates()) 
      {
        int x = StdRandom.uniform(n);
        int y = StdRandom.uniform(n);
        if (!grid.isOpen(x, y))
          grid.open(x, y);
      }
      result[a] = (double) grid.numberOfOpenSites() / (n*n);
    }
  } 
  // sample mean of percolation threshold
  public double mean() 
  {
    return StdStats.mean(result);
  }
  // sample standard deviation of percolation threshold                     
  public double stddev() 
  {
    return StdStats.stddev(result);
  }
  // low  endpoint of 95% confidence interval                     
  public double confidenceLow() 
  {
    return mean() - ((1.96 * stddev()) / Math.sqrt(trials));
  }
  // high endpoint of 95% confidence interval            
  public double confidenceHigh() 
  {
    return mean() + ((1.96 * stddev()) / Math.sqrt(trials));
  }
  // prints the results of percolation stats
  public static void main(String[] args) 
  {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    PercolationStats ps = new PercolationStats(n, trials);
    StdOut.printf("%-24s= %f\n", "mean()", ps.mean());
    StdOut.printf("%-24s= %f\n", "stddev()", ps.stddev());
    StdOut.printf("%-24s= %f\n", "confidenceLow()", ps.confidenceLow());
    StdOut.printf("%-24s= %f\n", "confidenceHigh()", ps.confidenceHigh());
  }
}       