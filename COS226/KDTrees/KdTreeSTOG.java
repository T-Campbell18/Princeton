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
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.LinkedList;

public class KdTreeSTOG<Value> 
{
  public static final boolean VERTICAL = true;
  public static final boolean HORIZONTAL = false;
  private Node root;
  private int size;
  // construct an empty symbol table of points
  public KdTreeSTOG()
  {
    size = 0;
  }
  // is the symbol table empty?                                
  public boolean isEmpty()
  {
    return size == 0;
  }
  // number of points                       
  public int size()
  {
    return size;
  }
  // associate the value val with point p                        
  public void put(Point2D p, Value val)
  {
    if (p == null)
      throw new NullPointerException("Point2D can not be null");
    if (val == null)
      throw new NullPointerException("Value can not be null");
    RectHV temp = new RectHV(-Double.MAX_VALUE, -Double.MAX_VALUE, 
                                          Double.MAX_VALUE, Double.MAX_VALUE);
    root = put(root, p, val, VERTICAL, temp);
  }
  // !!!
  private Node put(Node node, Point2D p, Value val, boolean level, RectHV rect)
  {
    if (node == null) 
    {
      size++;
      return new Node(p, val, rect);
    }
    if (p.equals(node.point))
    {
      node.value = val;
      return node;
    }
    if (level == VERTICAL)
    {
      double compare = p.x() - node.point.x();
      if (compare < 0)
      {
        RectHV temp = new RectHV(node.rect.xmin(), node.rect.ymin(), 
                                          node.point.x(), node.rect.ymax());
        node.left = put(node.left, p, val, !level, temp);
      } 
      else
      {
        RectHV temp = new RectHV(node.point.x(), node.rect.ymin(), 
                                          node.rect.xmax(), node.rect.ymax());
        node.right = put(node.right, p, val, !level, temp);
      }
    }
    else
    {
      double compare = p.y() - node.point.y();
      if (compare < 0)
      {
        RectHV temp = new RectHV(node.rect.xmin(), node.rect.ymin(), 
                                          node.rect.xmax(), node.point.y());
        node.left = put(node.left, p, val, !level, temp);
      } 
      else
      {
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
    return get(root, p, VERTICAL);
  }
  
  private Value get(Node node, Point2D p, boolean level)
  {
    if (node == null)
      return null;
    if (node.point.equals(p))
        return node.value;
    double compare;    
    if (level == VERTICAL)
      compare = p.x() - node.point.x();
    else
      compare = p.y() - node.point.y();
    
    if (compare < 0)
      return get(node.left, p, !level);
    else
      return get(node.right, p, !level);
  }
  
  // does the symbol table contain point p?                 
  public boolean contains(Point2D p)
  {
    return get(p) != null;
  }
  // all points in the symbol table             
  public Iterable<Point2D> points()
  {
    ArrayList<Point2D> list = new ArrayList<Point2D>();
    LinkedList<Node> nodes = new LinkedList<Node>();
    if (root == null)
      return null;
    nodes.add(root);  
    while (!nodes.isEmpty())
    {
      Node temp = nodes.removeFirst();
      list.add(temp.point);
      if (temp.left != null)
        nodes.add(temp.left);
      if (temp.right != null)
        nodes.add(temp.right);  
    }
    return list;
  }
  
  // all points that are inside the rectangle                       
  public Iterable<Point2D> range(RectHV rect)
  {
    if (rect == null)
      throw new NullPointerException("RectHV can not be null");
    ArrayList<Point2D> list = new ArrayList<Point2D>();
    range(root, rect, list);
    return list;
  }
  
  private void range(Node node, RectHV rect, ArrayList<Point2D> list)
  {
    if (node == null || !rect.intersects(node.rect))
      return;
    if (rect.contains(node.point))
      list.add(node.point);
    range(node.left, rect, list);
    range(node.right, rect, list);
  }
  
  // a nearest neighbor to point p; null if the symbol table is empty              
  public Point2D nearest(Point2D p)
  {
    if (p == null)
      throw new NullPointerException("Point2D can not be null");
    if (isEmpty())
      return null;  
    return nearest(root, p, root.point, VERTICAL);
  }
  
  private Point2D nearest(Node node, Point2D p, Point2D c, boolean level)
  {
    if (node == null)
      return c;
    if (node.point.distanceSquaredTo(p) < c.distanceSquaredTo(p))
      c = node.point;
    if (node.rect.distanceSquaredTo(p) < c.distanceSquaredTo(p))
    {
      Node close;
      Node far;
      if (level == VERTICAL)
      {
        if (p.x() < node.point.x())
        {
          close = node.left;
          far = node.right;
        }
        else
        {
          close = node.right;
          far = node.left;
        }
      }
      else
      {
        if (p.y() < node.point.y())
        {
          close = node.left;
          far = node.right;
        }
        else
        {
          close = node.right;
          far = node.left;
        }
      }
      c = nearest(close, p, c, !level);
      c = nearest(far, p, c, !level);
    }
    return c;
  }
  private class Node
  {
    private Node left;
    private Node right;
    private RectHV rect;
    private Point2D point;
    private Value value;
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
    String filename = args[0];
    In in = new In(filename);
    PointST<Integer> brute = new PointST<Integer>();
    KdTreeST<Integer> kdtree = new KdTreeST<Integer>();
    for (int i = 0; !in.isEmpty(); i++) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      kdtree.put(p, i);
      brute.put(p, i);
    }
    Stopwatch t = new Stopwatch();
    for (int x = 0; x < 1000; x++)
    {
      Point2D p = new Point2D(StdRandom.uniform(), StdRandom.uniform());
      brute.nearest(p);
    }
    System.out.println(t.elapsedTime());
    
    
    
//    KdTreeST<Integer> tc = new KdTreeST<Integer>();
//    System.out.println("Is empty? " + tc.isEmpty());
//    // add random points
//    for (int x = 0; x < 10000000; x++)
//    {
//      Point2D p = new Point2D(StdRandom.uniform(), StdRandom.uniform());
//      tc.put(p, x);
//    }
//    Point2D lc = new Point2D(0.5, 0.5);
//    tc.put(lc, 18);
//    System.out.println("Size: " + tc.size());
//    // print all points
//    for (Point2D p : tc.points())
//      System.out.println(p);
//    System.out.println("Is empty? " + tc.isEmpty());
//    System.out.println("Contains (0.5, 0.5)? " + tc.contains(lc));
//    System.out.println("Value with (0.5, 0.5): " + tc.get(lc));
//    // random RectHV
//    double a1 = StdRandom.uniform();
//    double a2 = StdRandom.uniform();
//    double a3 = StdRandom.uniform();
//    double a4 = StdRandom.uniform();
//    double xMin = Math.min(a1, a2);
//    double xMax = Math.max(a1, a2);
//    double yMin = Math.min(a3, a4);
//    double yMax = Math.max(a3, a4);
//    RectHV rect = new RectHV(xMin, yMin, xMax, yMax);
//    System.out.println(rect);
//    // print points in RectHV
//    System.out.println("Points in RectHV: ");
//    for (Point2D p : tc.range(rect))
//      System.out.println(p);
//    // draw points
//    StdDraw.setPenColor(StdDraw.BLACK);
//    StdDraw.setPenRadius(0.01);
//    for (Point2D p : tc.points())
//     // p.draw();
//    // draw rectangle 
//    StdDraw.setPenColor(StdDraw.BLACK);
//    StdDraw.setPenRadius();
//    //rect.draw();
//    // draw points in rectangle
//    StdDraw.setPenRadius(0.02);
//    StdDraw.setPenColor(StdDraw.RED);
//    for (Point2D p : tc.range(rect))
//      //p.draw();
//    StdDraw.setPenRadius(0.02);
//    StdDraw.setPenColor(StdDraw.GREEN);
//    // random point
//    Point2D p = new Point2D(StdRandom.uniform(), StdRandom.uniform());
//    //p.draw();
//    System.out.println("Random point: " + p);
//    // closest to random point
//    StdDraw.setPenRadius(0.015);
//    StdDraw.setPenColor(StdDraw.BLUE);
//    Stopwatch time = new Stopwatch();
//    Point2D n = tc.nearest(p);
//    System.out.println(tc.c / time.elapsedTime());
//    //n.draw();
//    System.out.println("Closest to random point: " + n);
  }                   
}