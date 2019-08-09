/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: creates an immutable data type that represents the board 
 * of the puzzle
 *
 ******************************************************************************/
import java.util.ArrayList;
import java.util.Arrays;

public class Board 
{
  private final int n; // the size of board
  private int[] board; // 1d array representation of n-by-n board
  private int x0; // row postion of blank space
  private int y0; // column postion of blank space
  
  // construct a board from an N-by-N array of tiles
  // (where tiles[i][j] = tile at row i, column j)
  public Board(int[][] tiles)
  {
    int c = 0;
    n = tiles.length;
    board = new int[n * n];
    for (int x = 0; x < n; x++)
    {
      for (int y = 0; y < n; y++)
      {
        if (tiles[x][y] == 0)
        {
          x0 = x;
          y0 = y;
        }
        board[c] = tiles[x][y];
        c++;
      }
    }
  }
  
  // construct a board from an (N*N) array of tiles
  private Board(int[] tiles)
  {
    n = (int) Math.sqrt(tiles.length);
    board = Arrays.copyOf(tiles, tiles.length);
    for (int x = 0; x < n; x++)
    {
      for (int y = 0; y < n; y++)
      {
        if (tileAt(x, y) == 0)
        {
          x0 = x;
          y0 = y;
        }
      }
    }
  }
  
  // return tile at row i, column j (or 0 if blank)        
  public int tileAt(int i, int j)
  {
    if (i < 0 || j < 0 || i >= n || j >= n)
      throw new java.lang.IndexOutOfBoundsException("Tile not in board");
    return board[n * i + j];
  }
  
  // board size N        
  public int size()
  {
    return n;
  }
  
  // number of tiles out of place                      
  public int hamming()
  {
    int c = 0;
    for (int x = 0; x < n; x++)
    {
      for (int y = 0; y < n; y++)
      {
        if (tileAt(x, y) == 0)
          continue;
        if (tileAt(x, y) != n * x + y + 1)
          c++;
      }
    }
    return c;
  }
  
  // sum of Manhattan distances between tiles and goal                   
  public int manhattan()
  {
    int c = 0;
    for (int x = 0; x < n; x++)
    {
      for (int y = 0; y < n; y++)
      {
        if (tileAt(x, y) == 0)
          continue;
        int tile = tileAt(x, y) - 1;
        int dx = tile / n;
        int dy = tile % n;
        c += Math.abs(x - dx) + Math.abs(y - dy); 
      }
    }
    return c;
  }
  
  // is this board the goal board?                 
  public boolean isGoal()
  {
    return manhattan() == 0;  
  }
  
  // the number of inversions in the board
  private int numInversions()
  {
    int inversion = 0;
    for (int x = 0; x < board.length - 1; x++)
    {
      if (board[x] == 0)
        continue;
      for (int y = x + 1; y < board.length; y++)
      {
        if (board[y] == 0)
          continue;
        if (board[x] > board[y])
          inversion++;  
      }
    }
    return inversion;
  }
  
  // is this board solvable?               
  public boolean isSolvable()
  {
    if (n % 2 == 0)
      return (numInversions() + x0) % 2 != 0;
    else
      return numInversions() % 2 == 0;
  }
  
  // does this board equal y?            
  public boolean equals(Object y)
  {
    if (y == null)
      return false;
    if (y.getClass() != this.getClass())
      return false;  
    Board that = (Board) y;
    if (this.n != that.n) 
      return false;
    for (int x = 0; x < (n * n); x++) 
    {
      if (this.board[x] != that.board[x]) 
        return false;
    }
    return true;
  }
  
  // converts the 2D array index to 1D array index
  private int to1D(int x, int y)
  {
    return n * x + y;
  }
  
  // all neighboring boards       
  public Iterable<Board> neighbors()
  {
    ArrayList<Board> neighbor = new ArrayList<Board>();
    // swap left
    if (y0 > 0) 
    {
      Board left = new Board(this.board);
      left.board[to1D(x0, y0)] = left.tileAt(x0, y0 - 1);
      left.board[to1D(x0, y0 - 1)] = 0;
      left.y0--;
      neighbor.add(left);
    }
    // swap up
    if (x0 > 0) 
    {
      Board up = new Board(this.board);
      up.board[to1D(x0, y0)] = up.tileAt(x0 - 1, y0);
      up.board[to1D(x0 - 1, y0)] = 0;
      up.x0--;
      neighbor.add(up);
    }
    // swap right
    if (y0 < n - 1) 
    {
      Board right = new Board(this.board);
      right.board[to1D(x0, y0)] = right.tileAt(x0, y0 + 1);
      right.board[to1D(x0, y0 + 1)] = 0;
      right.y0++;
      neighbor.add(right);
    }
    // swap down
    if (x0 < n - 1) 
    {
      Board down = new Board(this.board);
      down.board[to1D(x0, y0)] = down.tileAt(x0 + 1, y0);
      down.board[to1D(x0 + 1, y0)] = 0;
      down.x0++;
      neighbor.add(down);
    }
    return neighbor;
  }

  // string representation of this board (in the output format specified below)     
  public String toString() 
  {
    StringBuilder s = new StringBuilder();
    s.append(n + "\n");
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        s.append(String.format("%2d ", tileAt(i, j)));
      }
      s.append("\n");
    }
    return s.toString();
  }     
 
  // unit testing (required)
  public static void main(String[] args)
  {
    int[][] a = {{1, 2, 3}, {4, 5, 6}, {8, 0, 7}};
    Board test = new Board(a);
    Board test2 = new Board(a);
    System.out.println(test);
    System.out.println("Number of inversions: " + test.numInversions());
    System.out.println("Can it be solved? " + test.isSolvable());
    System.out.println("Is is the goal? " + test.isGoal());
    System.out.println("Mahattan: " + test.manhattan());
    System.out.println("Hamming: " + test.hamming());
    System.out.println("Size: " + test.size());
    System.out.println("Tile at 0,0: " + test.tileAt(0, 0));
    System.out.println("\n All neighbors:");
    
    for (Board tc : test.neighbors())
      System.out.println(tc);
    System.out.println(test.equals(test2));
  } 
}