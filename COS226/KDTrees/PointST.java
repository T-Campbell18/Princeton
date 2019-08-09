/******************************************************************************
  * Name: Tyler Campbell 
  * NetID: tylercc
  * Precept: P04
  *
  * Partner Name:   Lorraine Cliff
  * Partner NetID:   lcliff
  * Partner Precept: P04B
  * 
  * Description: This is a mutable data type PointST.java that actually is symbol
  *              table with Point2D. It implements API specified by using using 
  *              a red-black BST implemented through a TreeMap.
  *
  ******************************************************************************/
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import java.util.TreeMap;
import java.util.ArrayList;

public class PointST<Value> 
{
    
    // declare instance variables
    private TreeMap<Point2D, Value> pointMap; // TreeMap to represent the ST
    
    // construct an empty symbol table of points
    public PointST() 
    {
        pointMap = new TreeMap<Point2D, Value>(); // intiates TreeMap
    }
    
    // is the symbol table empty?                                
    public boolean isEmpty() 
    {
        return pointMap.isEmpty();
    }
    
    // number of points                       
    public int size() 
    {
        return pointMap.size();
    }
    
    // associate the value val with point p                        
    public void put(Point2D p, Value val) 
    {
        // throws exceptions if any argument is null
        if (p == null)
            throw new NullPointerException("Point2D can not be null");
        if (val == null)
            throw new NullPointerException("Value can not be null");
        pointMap.put(p, val);
    }
    
    // value associated with point p      
    public Value get(Point2D p) 
    {
        if (p == null)
            throw new NullPointerException("Point2D can not be null");
        return pointMap.get(p);
    }
    
    // does the symbol table contain point p?                 
    public boolean contains(Point2D p) 
    {
        if (p == null)
            throw new NullPointerException("Point2D can not be null");
        return pointMap.containsKey(p);
    }
    
    // all points in the symbol table             
    public Iterable<Point2D> points() 
    {
        return pointMap.keySet();
    }
    
    // all points that are inside the rectangle                       
    public Iterable<Point2D> range(RectHV rect) 
    {
        if (rect == null)
            throw new NullPointerException("RectHV can not be null");
        // creates an ArrayList to to hold the Point2D
        ArrayList<Point2D> list = new ArrayList<Point2D>();
        for (Point2D p : points()) 
        {
            // only adds the points contained in the rectangle
            if (rect.contains(p)) 
                list.add(p);
        }    
        return list;
    }
    
    // a nearest neighbor to point p; null if the symbol table is empty              
    public Point2D nearest(Point2D p) 
    {
        if (p == null)
            throw new NullPointerException("Point2D can not be null");
        if (isEmpty())
            return null;
        Point2D near = pointMap.firstKey();
        for (Point2D p1 : points()) 
        {
            // keeps track of the point that is the shortest distance away
            if (p1.distanceTo(p) < near.distanceTo(p)) 
                near = p1;
        }
        return near;
    }
    
    // unit testing (required)             
    public static void main(String[] args) 
    {
        // intializes the PointSt data type
        PointST<Integer> map = new PointST<Integer>();
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
        for (Point2D p : map.points()) // tests the points method
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