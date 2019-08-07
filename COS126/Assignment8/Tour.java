/******************************************************************************
 *  Name: Tyler Campbell   
 *  NetID: tylercc
 *  Precept: P02 
 *
 *  Partner Name: n/a  
 *  Partner NetID: n/a 
 *  Partner Precept: n/a
 * 
 * Description: data type that represents the sequence of points visited in
 * a TSP tour. Represents the tour as a circularly linked list of nodes, one
 * for each point in the tour. Implements 2 greedy heuristics
 * (Nearest neighbor heuristic) (Smallest increase heuristic) 
 *  
 *
 ******************************************************************************/

public class Tour 
{
  // instance variable 
  private Node first; // refers to first node of ciruclar linked-list
  
  //  creates an empty tour
  public Tour()
  {
    
  }
  
  //  creates the 4-point tour a->b->c->d->a (for debugging)
  public Tour(Point a, Point b, Point c, Point d)
  {
    first = new Node(a);
    Node nodeB = new Node(b);
    Node nodeC = new Node(c);
    Node nodeD = new Node(d);
    first.next = nodeB;
    nodeB.next = nodeC;
    nodeC.next = nodeD;
    nodeD.next = first;
  }
  
  //  returns the number of points in this tour
  public int size()
  {
    // list is empty
    if (first == null)
      return 0;
    int size = 0;
    Node temp = first;
    // traverse list increase size by 1 every node
    do
    {
      size++;
      temp = temp.next;
    }
    while (temp != first);
    return size;  
  }
  
  //  returns the length of this tour
  public double length()
  {
    // list is empty
    if (first == null)
      return 0.0;
    double distance = 0.0;
    Node temp = first;
    // traverse list calculate distance between current node point
    // and next node point
    do
    {
      distance += temp.p.distanceTo(temp.next.p);
      temp = temp.next;
    }
    while (temp != first);
    return distance;
  }
  
  //  draws this tour to standard drawing
  public void draw()
  {
    // empty list
    if (first == null)
      return;
    Node temp = first;
    // traverse list draw line from current node point to next node point
    do
    {
      temp.p.drawTo(temp.next.p);
      temp = temp.next;
    }
    while (temp != first);
  }
  
  //  prints this tour to standard output
  public void show()
  {
    // empty list
    if (first == null)
      return;
    Node temp = first;
    // traverse list print the node point
    do
    {
      StdOut.println(temp.p.toString());
      temp = temp.next;
    }
    while (temp != first);
  }
  
  //  inserts p using the nearest neighbor heuristic
  public void insertNearest(Point p)
  {
    double near = Double.POSITIVE_INFINITY;
    Node temp = first;
    Node close = null;
    // empty list
    if (first == null)
    {
      first = new Node(p);
      first.next = first;
      return;
    }
    // traverse list calculate what node point is closest to point p
    do
    {
      if (temp.p.distanceTo(p) < near)
      {
        close = temp;
        near = temp.p.distanceTo(p);
      }
      temp = temp.next;
    }
    while (temp != first);
    // create node from point p and insert
    Node insert = new Node(p);
    insert.next = close.next;
    close.next = insert;
  }
  
  //  inserts p using the smallest increase heuristic
  public void insertSmallest(Point p)
  {
    double tiny = Double.POSITIVE_INFINITY;
    Node temp = first;
    Node small = null;
    // empty list
    if (first == null)
    {
      first = new Node(p);
      first.next = first;
      return;
    }
    // traverse list calculate what node point p should be inserted
    // after that would increase the total length ths least
    do
    {
      double newDistance = (temp.p.distanceTo(p) + p.distanceTo(temp.next.p)); 
      double currentDistance = temp.p.distanceTo(temp.next.p);
      double deltaDistance = newDistance - currentDistance;
      
      if (deltaDistance < tiny)
      {
        tiny = deltaDistance;
        small = temp;
      }
      temp = temp.next;
    }
    while(temp != first);
    // create node from point p and insert
    Node insert = new Node(p);
    insert.next = small.next;
    small.next = insert;
  }
  
  public static void main(String[] args) 
  {
    // define 4 points that are the corners of a square
    Point a = new Point(100.0, 100.0);
    Point b = new Point(500.0, 100.0);
    Point c = new Point(500.0, 500.0);
    Point d = new Point(100.0, 500.0);

    // create the tour a -> b -> c -> d -> a
    Tour squareTour = new Tour(a, b, c, d);

    // print the tour to standard output
    squareTour.show();
    
    // print the size and length of tour
    StdOut.println(squareTour.size() + " " + squareTour.length());
    
    // draw the tour
    StdDraw.setXscale(0, 600);
    StdDraw.setYscale(0, 600);
    squareTour.draw();
    
    // add 10 random points to 2 tours
    Tour tourA = new Tour();
    Tour tourB = new Tour();
    for (int i = 0; i < 10; i++)
    {
      double x = StdRandom.uniform() * 600;
      double y = StdRandom.uniform() * 600;
      Point p = new Point(x, y);
      tourA.insertNearest(p);
      tourB.insertSmallest(p);
    }
    // print the length
    StdOut.println(tourA.length() + " " + tourB.length());
  }
  
  // nested class
  private class Node 
  {
    // instance varibles 
    private Point p; // ponint of the node
    private Node next; // refers to next node
   
    // constrctor that sets the node's point
    public Node(Point p)
    {
      this.p = p;
    }
  }
}