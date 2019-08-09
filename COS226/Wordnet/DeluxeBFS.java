import java.util.Hashtable;
import edu.princeton.cs.algs4.Digraph; 
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class DeluxeBFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    private Hashtable<Integer, Boolean> marked;
    private Hashtable<Integer, Integer> edgeTo;
    private Hashtable<Integer, Integer> distTo;
    private int size;
    
    public DeluxeBFS(Digraph G, int s) {
        marked = new Hashtable<Integer, Boolean>();
        distTo = new Hashtable<Integer, Integer>();
        edgeTo = new Hashtable<Integer, Integer>();
        size = G.V();
        for (Integer x: distTo.keySet()) 
            distTo.put(x, INFINITY);
        validateVertex(s);
        bfs(G, s);
    }

    public DeluxeBFS(Digraph G, Iterable<Integer> sources) {
        marked = new Hashtable<Integer, Boolean>();
        distTo = new Hashtable<Integer, Integer>();
        edgeTo = new Hashtable<Integer, Integer>();
        for (Integer x : distTo.keySet()) {
            distTo.put(x, INFINITY);
        }
        validateVertices(sources);
        bfs(G, sources);
    }

    // BFS from single source
    private void bfs(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        marked.put(s, true);
        distTo.put(s, 0);
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked.get(s)) {
                    edgeTo.put(w, v);
                    distTo.put(w, distTo.get(v) + 1);
                    marked.put(w, true);
                    q.enqueue(w);
                }
            }
        }
    }

    // BFS from multiple sources
    private void bfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            marked.put(s, true);
            distTo.put(s, 0);
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked.get(w)) {
                    edgeTo.put(w, v);
                    distTo.put(w, distTo.get(v) + 1);
                    marked.put(w, true);
                    q.enqueue(w);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        //validateVertex(v);
        return marked.get(v);
    }

    public int distTo(int v) {
        //validateVertex(v);
        return distTo.get(v);
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo.get(x) != 0; x = edgeTo.get(x))
            path.push(x);
        path.push(x);
        return path;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {

        if (v < 0 || v >= size)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (size-1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = marked.size();
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
            }
        }
    }


    /**
     * Unit tests the {@code BreadthFirstDirectedPaths} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        /*In in = new In(args[0]);
        Digraph G = new Digraph(in);
        // StdOut.println(G);

        int s = Integer.parseInt(args[1]);
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("->" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d (-):  not connected\n", s, v);
            }*/
        

        }
    }

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


