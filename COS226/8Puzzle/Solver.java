/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: creates an immutable data type that finds the solution the
 * to the initial board puzzle 
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;

public class Solver 
{
  private SearchNode solve; // search node for the solved board
  
  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial)
  {
    if (initial == null)
      throw new NullPointerException("Board is null");
    if (!initial.isSolvable())
      throw new IllegalArgumentException("Not solvable");
    
    MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
    pq.insert(new SearchNode(initial, 0, null));
    
    while (true) 
    {
      SearchNode current = pq.delMin();
      if (current.board.isGoal()) 
      {
        solve = current;
        break;
      }
      for (Board board : current.board.neighbors()) 
      {
        if (current.parent == null || !board.equals(current.parent.board)) 
          pq.insert(new SearchNode(board, current.moves + 1, current));  
      }
    }  
  }
  
  // min number of moves to solve initial board           
  public int moves()
  {
    return solve.moves;
  }
         
  // sequence of boards in a shortest solution              
  public Iterable<Board> solution()
  {
    Stack<Board> sList = new Stack<Board>();
    SearchNode temp = solve;
    sList.push(temp.board);
    while (temp.parent != null) 
    {  
      sList.push(temp.parent.board);
      temp = temp.parent;
    }
    return sList;
  }
  
  private class SearchNode implements Comparable<SearchNode> 
  {
    private Board board; // the board
    private int moves; // number of moves 
    private int priority; // prioirity number {moves + mahattan}
    private SearchNode parent; // the previous searchnode
    // contructs node
    public SearchNode(Board b, int m, SearchNode p) 
    {
      board = b;
      moves = m;
      parent = p;
      priority = board.manhattan() + m;
    }
    // compare 2 nodes based on proirity
    public int compareTo(SearchNode that) 
    {
      return this.priority - that.priority;
    }
  }
  
  // unit testing     
  public static void main(String[] args)
  {
   // int[][] a = {{8, 6, 7}, {2, 5, 4}, {3, 0, 1}};
   // Board tc = new Board(a);
   // Solver test = new Solver(tc);
  In in = new In(args[0]);
  int N = in.readInt();
  int[][] blocks = new int[N][N];
  for (int i = 0; i < N; i++)
    for (int j = 0; j < N; j++)
      blocks[i][j] = in.readInt();
  Board initial = new Board(blocks);
  Solver test = new Solver(initial);
  System.out.println("Number of moves: " + test.moves());
    for (Board t : test.solution())
      System.out.println(t);
  }
   
}