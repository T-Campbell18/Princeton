/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: represents a percolation system for a nxn grid of opened and
 * blocked sites, a full site is an open site that can be connected to an 
 * open site in the top row via a chain of neighboring (left, right, up, down) 
 * open sites, the system percolates if there is a full site in the bottom row
 * (has many applications such as conducting materials and water flow)
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation 
{
  // represents if the site is open
  private static final int OPEN = 1; 
  // represents if the site is connected to the virtial top
  private static final int CONNECT_START = 2;
  // represents if the site is connected to the virtual bottom
  private static final int CONNECT_END = 4;
   
  private byte[] grid; // the 2D grid represented  as a 1d array of bytes
  // Weighted Quick Union object that represents the grid
  private WeightedQuickUnionUF uf; 
  private int size; // length of NxN grid
  private int count; // number of open sites
  private boolean perc; // does the system peroclate? true if it does

  // create n-by-n grid, with all sites initially blocked
  public Percolation(int n) 
  {
    if (n <= 0)
      throw new IllegalArgumentException("n must be greater than 0");
    size = n;
    grid = new byte[n * n];
    uf = new WeightedQuickUnionUF(n * n);
    perc = false;
    for (int x = 0; x < n; x++)
    {
      int start = to1D(0, x);
      grid[start] = CONNECT_START;
      int end = to1D(n-1, x);
      grid[end] = CONNECT_END;
    }
  }
  
  // convert the postion to the 1D array index
  private int to1D(int row, int col) 
  {
    outOfBounds(row, col);
    return size * row + col;
  }
    
  // open the site (row, col) if it is not open already             
  public void open(int row, int col) 
  {
    outOfBounds(row, col);
    if (isOpen(row, col))
      return;
    count++;  
    int current = to1D(row, col);
    grid[current] = (byte) (grid[current] | OPEN);
    if ((grid[current] & CONNECT_START) != 0 && (grid[current] & CONNECT_END) != 0)
      perc = true;
    // up
    if (row > 0 && isOpen(row - 1, col)) 
    {
      int neighbor = to1D(row - 1, col);
      connect(current, neighbor);
    }
    // right
    if (col < size - 1 && isOpen(row, col + 1))
    {
      int neighbor = to1D(row, col + 1);
      connect(current, neighbor);
    }
    // down
    if (row < size - 1 && isOpen(row + 1, col)) 
    {
      int neighbor = to1D(row+1, col);
      connect(current, neighbor);
    }
    // left
    if (col > 0 && isOpen(row, col - 1)) 
    {
      int neighbor = to1D(row, col - 1);
      connect(current, neighbor);
    }
  }
  
  // connect the two postions and checks if system percolates
  private void connect(int pos1, int pos2)
  {
    int root1 = uf.find(pos1);
    int root2 = uf.find(pos2);
    uf.union(pos1, pos2);
    int root = uf.find(pos1);
    if ((grid[root1] & CONNECT_START) != 0 || (grid[root2] & CONNECT_START) != 0)
      grid[root] = (byte) (grid[root] | CONNECT_START);
    if ((grid[root1] & CONNECT_END) != 0 || (grid[root2] & CONNECT_END) != 0)
      grid[root] = (byte) (grid[root] | CONNECT_END);
    if ((grid[root] & CONNECT_START) != 0 && (grid[root] & CONNECT_END) != 0) 
      perc = true;
  }
    
  // is the site (row, col) open?
  public boolean isOpen(int row, int col) 
  {
    outOfBounds(row, col);
    return (grid[to1D(row, col)] & OPEN) == OPEN;
  }
  
  // is the site (row, col) full?
  public boolean isFull(int row, int col) 
  {
    outOfBounds(row, col);
    if (isOpen(row, col))
    {
      if (size == 1)
        return true;
      else
        return (grid[uf.find(to1D(row, col))] & CONNECT_START) != 0;
    }
    return false;
  }
  
  // number of open sites 
  public int numberOfOpenSites() 
  {
    return count;
  }
  
  // does the system percolate?       
  public boolean percolates() 
  {
    if (size == 1)
      return isFull(0, 0);
    else
      return perc;
  }
  
  // is the point vaild in the n by n grid
  private void outOfBounds(int row, int col)
  {
    if (row < 0 || col < 0 || row >= size || col >= size)
      throw new IndexOutOfBoundsException("Site is not a valid location in grid");
  }
  
  // unit testing 
  public static void main(String[] args) 
  {
    Percolation p = new Percolation(1);
    System.out.println("Does it Percolate? " + p.percolates());
    p.open(0, 0);
    System.out.println("Does it Percolate? " + p.percolates());
    p = new Percolation(3);
    System.out.println("Does it Percolate? " + p.percolates());
    p.open(0, 0);
    p.open(1, 0);
    p.open(1, 1);
    System.out.println("Isfull? " + p.isFull(1, 1));
    p.open(1, 2);
    p.open(2, 2);
    System.out.println("Does it Percolate? " + p.percolates());
    System.out.println("Number of open sites: " + p.numberOfOpenSites());
  }
}