/******************************************************************************
  * Name: Tyler Campbell
  * NetID: tylercc
  * Precept: P04
  *
  * Partner Name:  Lorraine Cliff 
  * Partner NetID:   lcliff
  * Partner Precept: P04B
  * 
  * Description: This is a mutable data type that uses a 2d-tree to implement the 
  *              same API as PointST. For this purpose a 2d-tree is a 
  *              generalization of a BST to two-dimensional keys. It's 
  *              a BST with points in the node that uses alternating x-values 
  *              and y-values, starting with x to form the BST. 
  *
  ******************************************************************************/
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;
import java.util.LinkedList;

public class KdTreeST<Value> 
{
    // declares instance variables
    private static final boolean VERTICAL = true; // is it a verticle compare
    private static final boolean HORIZONTAL = false; // is it a horizontal compare
    private Node root; // maintains the root node
    private int size; // the size of the symbol table
    
    // construct an empty symbol table of points
    public KdTreeST() 
    {
        size = 0; // intially the size is zero
    }
    
    // is the symbol table empty?                                
    public boolean isEmpty() 
    {
        return size == 0; // only empty if size equals zero
    }
    
    // number of points                       
    public int size() 
    {
        return size;
    }
    
    // associate the value val with point p                        
    public void put(Point2D p, Value val) 
    {
        // throws the necessary exceptions
        if (p == null)
            throw new NullPointerException("Point2D can not be null");
        if (val == null)
            throw new NullPointerException("Value can not be null");
        // creates the intial temporaty variable
        RectHV temp = new RectHV(-Double.MAX_VALUE, -Double.MAX_VALUE, 
                                 Double.MAX_VALUE, Double.MAX_VALUE);
        root = put(root, p, val, VERTICAL, temp); // recusively calls put
    }
    
    // recursive method for putting a point in with a given value
    private Node put(Node node, Point2D p, Value val, boolean level, RectHV rect) 
    {
        // base case for recusion
        if (node == null) 
        {
            size++; // increment size since your adding a point
            return new Node(p, val, rect);
        }
        // if the point allready exists
        if (p.equals(node.point)) 
        {
            node.value = val; // update the value
            return node;
        }
        // for verticle compare based levels (compare by x values)
        if (level == VERTICAL) 
        {
            double compare = p.x() - node.point.x(); // compares p to the node
            // update the rectangle and put in the left branch of the node and 
            // update the recusion call based on the level change
            if (compare < 0) 
            {
                RectHV temp = new RectHV(node.rect.xmin(), node.rect.ymin(), 
                                         node.point.x(), node.rect.ymax());
                node.left = put(node.left, p, val, !level, temp);
            } 
            else 
            {
                // update the rectangle and put in the right branch of the node and 
                // update the recusion call based on the level chagne
                RectHV temp = new RectHV(node.point.x(), node.rect.ymin(), 
                                         node.rect.xmax(), node.rect.ymax());
                node.right = put(node.right, p, val, !level, temp);
            }
        }
        // for horizontal compare levels (compare by y vales)
        else 
        {
            double compare = p.y() - node.point.y(); // compares p to the node
            if (compare < 0) 
            {
                // update the rectangle and put in the left branch of the node and 
                // update the recusion call based on the level change
                RectHV temp = new RectHV(node.rect.xmin(), node.rect.ymin(), 
                                         node.rect.xmax(), node.point.y());
                node.left = put(node.left, p, val, !level, temp);
            } 
            else 
            {
                // update the rectangle and put in the right branch of the node and 
                // update the recusion call based on the level change
                RectHV temp = new RectHV(node.rect.xmin(), node.point.y(), 
                                         node.rect.xmax(), node.rect.ymax());
                node.right = put(node.right, p, val, !level, temp);
            }
        }
        return node;
    }
    
    // value associated with point p      
    public Value get(Point2D p) 
    {
        if (p == null)
            throw new NullPointerException("Point2D can not be null");
        return get(root, p, VERTICAL); // calls the recursive get method
    }
    
    // recursive method that traverses the kd-tree to get the value at the point p
    private Value get(Node node, Point2D p, boolean level) 
    {
        // base case if the node is null
        if (node == null)
            return null;
        // stop if you find the node and return the value there
        if (node.point.equals(p))
            return node.value;
        double compare;  
        // compare by x values
        if (level == VERTICAL)
            compare = p.x() - node.point.x();
        else
            compare = p.y() - node.point.y(); // compare by y values
        // place the node to the left or right based on the vompare value
        if (compare < 0)
            return get(node.left, p, !level); // recursively call get, update level
        else
            return get(node.right, p, !level); // recursively call get, update level
    }
    
    // does the symbol table contain point p?                 
    public boolean contains(Point2D p) 
    {
        return get(p) != null; // if get doesn't equal null the point is there
    }
    
    // all points in the symbol table             
    public Iterable<Point2D> points() 
    {
        // array list to keep track of level order traversal
        ArrayList<Point2D> list = new ArrayList<Point2D>();
        LinkedList<Node> nodes = new LinkedList<Node>();
        if (root == null)
            return null;
        nodes.add(root);  
        while (!nodes.isEmpty()) 
        {
            Node temp = nodes.removeFirst();
            list.add(temp.point); // adds the temp point to the ArrayList
            if (temp.left != null) // add the left child if it exists
                nodes.add(temp.left);
            if (temp.right != null) // add the right child if it exists
                nodes.add(temp.right);  
        }
        return list;
    }
    
    // all points that are inside the rectangle                       
    public Iterable<Point2D> range(RectHV rect) 
    {
        if (rect == null)
            throw new NullPointerException("RectHV can not be null");
        // list will hold the points contained in the RectHV (rect)
        ArrayList<Point2D> list = new ArrayList<Point2D>();
        range(root, rect, list); // recursively call range
        return list;
    }
    
    // the recursive method that searches through the points for points in rect
    private void range(Node node, RectHV rect, ArrayList<Point2D> list) 
    {
        // base cases if the at the node equals null or rect no longer interests 
        // the rectangle in the node
        if (node == null || !rect.intersects(node.rect))
            return;
        // add the point if it is contained in rect
        if (rect.contains(node.point))
            list.add(node.point);
        range(node.left, rect, list); // recursively calls range on left child 
        range(node.right, rect, list); // recursively calls range on right child
    }
    
    // a nearest neighbor to point p; null if the symbol table is empty              
    public Point2D nearest(Point2D p) 
    {
        if (p == null)
            throw new NullPointerException("Point2D can not be null");
        if (isEmpty())
            return null;  
        // recursively calls the nearest method
        return nearest(root, p, root.point, VERTICAL); 
    }
    
    // recursive method for locating the nearest point to point p
    private Point2D nearest(Node node, Point2D p, Point2D champ, boolean level) 
    {
        // base case if the node is null
        if (node == null)
            return champ;
        if (node.point.distanceSquaredTo(p) < champ.distanceSquaredTo(p))
            champ = node.point;
        if (node.rect.distanceSquaredTo(p) < champ.distanceSquaredTo(p)) 
        {
            Node close; // the closest node
            Node far; // the farthest node
            // for comparing by x values
            if (level == VERTICAL) 
            {
                if (p.x() < node.point.x()) 
                {
                    far = node.right; // right child is farthest
                    close = node.left; // left child is closest
                }
                else 
                { 
                    far = node.left; // left child is farthest
                    close = node.right; // right child is closest
                }
            }
            // for comparing by y values
            else 
            {
                if (p.y() < node.point.y()) 
                {
                    far = node.right; // right child is farthest
                    close = node.left; // left child is closest
                }
                else 
                {
                    far = node.left; // left child is clests
                    close = node.right; // right child is closest
                }
            }
            // recursively call nearest based on the close node
            champ = nearest(close, p, champ, !level);
            // recusively call nearest based on the far node
            champ = nearest(far, p, champ, !level);
        }
        // return the overall closest node
        return champ;
    }
    
    // node representaion of a point in the KD-tree
    private class Node 
    {
        private Node left; // left subtree
        private Node right; // right subtree
        private RectHV rect; // the RectHV used
        private Point2D point; // the point
        private Value value; // the value associated with the give point
        
        // constructor that instantiates the private node class
        public Node(Point2D point, Value value, RectHV rect) 
        {
            this.point = point;
            this.value = value;
            this.rect = rect;
        }
    }
    
    // unit testing (required)             
    public static void main(String[] args) 
    {
        // intializes the PointSt data type
        KdTreeST<Integer> map = new KdTreeST<Integer>();
        // tests the isEmpty method
        System.out.println("Is empty? " + map.isEmpty()); // should be empty
        // add random points and tests the put method
        for (int x = 0; x < 10; x++) 
        {
            Point2D p = new Point2D(StdRandom.uniform(), StdRandom.uniform());
            map.put(p, x);
        }
        Point2D testPoint = new Point2D(0.5, 0.5);
        map.put(testPoint, 18);
        System.out.println("Size: " + map.size()); // should be 11
        
        // print all points in the map
        for (Point2D p : map.points())
            System.out.println(p);
        
        System.out.println("Is empty? " + map.isEmpty()); // should return false
        // tests the contains method, and should return true
        System.out.println("Contains (0.5, 0.5)? " + map.contains(testPoint));
        // tests the get method, should return 18
        System.out.println("Value with (0.5, 0.5): " + map.get(testPoint));
        
        
        // generates a random RectHV
        double a1 = StdRandom.uniform();
        double a2 = StdRandom.uniform();
        double a3 = StdRandom.uniform();
        double a4 = StdRandom.uniform();
        double xMin = Math.min(a1, a2);
        double xMax = Math.max(a1, a2);
        double yMin = Math.min(a3, a4);
        double yMax = Math.max(a3, a4);
        // creates the RectHV object, based off of the random values
        RectHV rect = new RectHV(xMin, yMin, xMax, yMax);
        System.out.println(rect);
        
        // print points in range of RectHV
        System.out.println("Points in RectHV: ");
        for (Point2D p : map.range(rect)) // tests the range method
            System.out.println(p);
        
        // draws points the points
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : map.points()) // tests the point method
            p.draw();
        
        // draw rectangle 
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        rect.draw();
        
        // draw points in rectangle in red
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.RED);
        // tests the range method
        for (Point2D p : map.range(rect))
            p.draw();
        
        // draws one random point in green
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.GREEN);
        Point2D p = new Point2D(StdRandom.uniform(), StdRandom.uniform());
        p.draw();
        System.out.println("Random point: " + p);
        
        // draws the point closest to random point in blue
        StdDraw.setPenRadius(0.015);
        StdDraw.setPenColor(StdDraw.BLUE); 
        Point2D n = map.nearest(p); // tests the nearest method
        n.draw();
        System.out.println("Closest to random point: " + n);
    }                   
}
    