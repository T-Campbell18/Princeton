/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    
 * Partner NetID:   
 * Partner Precept: 
 * 
 * Description: !!! 
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.TreeMap;
import java.util.ArrayList;

public class PointSTOG<Value> 
{
  private TreeMap<Point2D, Value> pointMap;
  // construct an empty symbol table of points
  public PointSTOG()
  {
    pointMap = new TreeMap<Point2D, Value>();
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
    ArrayList<Point2D> list = new ArrayList<Point2D>();
    for (Point2D p : points())
    {
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
      if (p1.distanceTo(p) < near.distanceTo(p)) 
            near = p1;
    }
    return near;
  }
  // unit testing (required)             
  public static void main(String[] args)
  {
    PointST<Integer> tc = new PointST<Integer>();
    System.out.println("Is empty? " + tc.isEmpty());
    // add random points
    for (int x = 0; x < 1000000; x++)
    {
      Point2D p = new Point2D(StdRandom.uniform(), StdRandom.uniform());
      tc.put(p, x);
    }
    Point2D lc = new Point2D(0.5, 0.5);
    tc.put(lc, 18);
    System.out.println("Size: " + tc.size());
    // print all points
    for (Point2D p : tc.points())
      System.out.println(p);
    System.out.println("Is empty? " + tc.isEmpty());
    System.out.println("Contains (0.5, 0.5)? " + tc.contains(lc));
    System.out.println("Value with (0.5, 0.5): " + tc.get(lc));
    // random RectHV
    double a1 = StdRandom.uniform();
    double a2 = StdRandom.uniform();
    double a3 = StdRandom.uniform();
    double a4 = StdRandom.uniform();
    double xMin = Math.min(a1, a2);
    double xMax = Math.max(a1, a2);
    double yMin = Math.min(a3, a4);
    double yMax = Math.max(a3, a4);
    RectHV rect = new RectHV(xMin, yMin, xMax, yMax);
    System.out.println(rect);
    // print points in RectHV
    System.out.println("Points in RectHV: ");
    for (Point2D p : tc.range(rect))
      System.out.println(p);
    // draw points
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    for (Point2D p : tc.points())
      //p.draw();
    // draw rectangle 
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius();
    //rect.draw();
    // draw points in rectangle
    StdDraw.setPenRadius(0.02);
    StdDraw.setPenColor(StdDraw.RED);
    for (Point2D p : tc.range(rect))
      //p.draw();
    StdDraw.setPenRadius(0.02);
    StdDraw.setPenColor(StdDraw.GREEN);
    // random point
    Point2D p = new Point2D(StdRandom.uniform(), StdRandom.uniform());
    //p.draw();
    System.out.println("Random point: " + p);
    // closest to random point
    StdDraw.setPenRadius(0.015);
    StdDraw.setPenColor(StdDraw.BLUE); 
    Stopwatch time = new Stopwatch();
    Point2D n = tc.nearest(p);
    System.out.println(time.elapsedTime());
    //n.draw();
    System.out.println("Closest to random point: " + n);
  }                   
}