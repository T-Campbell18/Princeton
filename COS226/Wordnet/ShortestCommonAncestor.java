/******************************************************************************
  * Name: Tyler Campbell
  * NetID: tylercc
  * Precept: P04
  *
  * Partner Name: Lorraine Cliff 
  * Partner NetID: lcliff
  * Partner Precept: P04B
  * 
  * Description: This program implements an immutable data type 
  * ShortestCommonAncestor that has the API specified. It uses a DAG as an 
  * argument and can determine based off of this DAG the shortest anestral 
  * distance between two vertices or subsets and it can determine the 
  * ancesestor that gives this shortest distance.
  *
  ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.ArrayList;

public class ShortestCommonAncestor {
    // declares instance variables
    private Digraph graph; // diagraph representing the argument of the constructor
    
    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        graph = new Digraph(G); // intializes instance variable
        // data type that decides if a graph has a directed cycle or not
        DirectedCycle cycle = new DirectedCycle(G); 
        if (cycle.hasCycle()) // if there is a directed cycle throw an exception
            throw new IllegalArgumentException();
        int root = 0;
        for (int x = 0; x < G.V(); x++) {
            // increments root if there are no more edges adjacent to x 
            if (!G.adj(x).iterator().hasNext()) 
                root++;
        }
        if (root != 1) // if the diagraph isn't a rooted DAG
            throw new IllegalArgumentException();
    }
    
    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        // throws exceptions if any argument vertex is invalid
        if (v >= graph.V() || v < 0) 
            throw new IndexOutOfBoundsException();
        if (w >= graph.V() || w < 0) 
            throw new IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        int min = Integer.MAX_VALUE;
        for (int x = 0; x < graph.V(); x++) {
            // if both BreadthFirstSearchs have a path to x then we 
            // compute the distance between x in each 
            if (bfs1.hasPathTo(x) && bfs2.hasPathTo(x)) {
                int d = bfs1.distTo(x) + bfs2.distTo(x);
                // update the distance to be the min only if it is smaller than 
                // the previous minimum distance
                if (d < min) 
                    min = d;
            }
        }
        return min;
    }
    
    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        // throws exceptions if any argument vertex is invalid
        if (v >= graph.V() || v < 0) 
            throw new IndexOutOfBoundsException();
        if (w >= graph.V() || w < 0) 
            throw new IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        // sets the minimum distance to infinite intially
        int min = Integer.MAX_VALUE; 
        int ant = -1;
        for (int x = 0; x < graph.V(); x++) {
            // if there is a path to x for both BreadthFirstSearchs then 
            // calculate the ancestrial path between v and w
            if (bfs1.hasPathTo(x) && bfs2.hasPathTo(x)) {
                int d = bfs1.distTo(x) + bfs2.distTo(x);
                // only update the minimum if the distance found is less than 
                // the previous distance, if this is the case also save the vertex
                // where this occurs
                if (d < min) {
                    ant = x;
                    min = d;
                } 
            }
        }
        return ant;
    }
    
    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        // throws exceptions if any argument is null
        if (subsetA == null || subsetB == null)
            throw new NullPointerException();
        // throws exception if any of the iterable arguments have zero vertices
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext())
            throw new IllegalArgumentException();
        // throws exceptions if any argument vertex is invalid
        for (int x : subsetA) {
            if (x >= graph.V() || x < 0) 
                throw new IndexOutOfBoundsException();
        }
        for (int x : subsetB) {
            if (x >= graph.V() || x < 0) 
                throw new IndexOutOfBoundsException();
        }   
        BreadthFirstDirectedPaths bfs1 = new 
                            BreadthFirstDirectedPaths(graph, subsetA);
        BreadthFirstDirectedPaths bfs2 = new 
                            BreadthFirstDirectedPaths(graph, subsetB);
        int min = Integer.MAX_VALUE;
        for (int x = 0; x < graph.V(); x++) {
            // if there is a path to x from both subset BreadthFirstSearchs 
            // then calculate the ancestral distance
            if (bfs1.hasPathTo(x) && bfs2.hasPathTo(x)) {
                int d = bfs1.distTo(x) + bfs2.distTo(x);
                // only update the minimum if the distance is smaller 
                // than the previous minimum
                if (d < min) 
                    min = d;
            }
        }
        return min;
    }
    
    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        // throws exceptions if any argument is null
        if (subsetA == null || subsetB == null)
            throw new NullPointerException();
        // throws exception if any of the iterable arguments have zero vertices
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext())
            throw new IllegalArgumentException();
        // throws exceptions if any argument vertex is invalid
        for (int x : subsetA) {
            if (x >= graph.V() || x < 0) 
                throw new IndexOutOfBoundsException();
        }
        for (int x : subsetB) {
            if (x >= graph.V() || x < 0) 
                throw new IndexOutOfBoundsException();
        } 
        BreadthFirstDirectedPaths bfs1 = new
                            BreadthFirstDirectedPaths(graph, subsetA);
        BreadthFirstDirectedPaths bfs2 = new 
                            BreadthFirstDirectedPaths(graph, subsetB);
        int min = Integer.MAX_VALUE; // sets the distanc eto infinity
        int ant = -1;
        for (int x = 0; x < graph.V(); x++) {
            // if there is a path to x from both subset BreadthFirstSearchs 
            // then calculate the ancestral distance
            if (bfs1.hasPathTo(x) && bfs2.hasPathTo(x)) {
                int d = bfs1.distTo(x) + bfs2.distTo(x);
                // only update the minimum if the distance found is less than 
                // the previous distance, if this is the case also save the vertex
                // where this occurs
                if (d < min) {
                    ant = x;
                    min = d;
                } 
            }
        }
        return ant;
    }

    // do unit testing of this class
    public static void main(String[] args) 
    {
      In in = new In(args[0]);
      Digraph G = new Digraph(in);
      ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
      ArrayList<Integer> a = new ArrayList<Integer>();
      ArrayList<Integer> b = new ArrayList<Integer>();
      a.add(2);
      a.add(3);
      b.add(4);
      b.add(8);
      int length   = sca.length(a, b);
      int ancestor = sca.ancestor(a, b);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
      while (!StdIn.isEmpty()) 
      {
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        length   = sca.length(v, w);
        ancestor = sca.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
      }
    }
}