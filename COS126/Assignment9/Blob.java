/******************************************************************************
 *  Name: Tyler Campbell   
 *  NetID: tylercc
 *  Precept: P02 
 *
 *  Partner Name: n/a  
 *  Partner NetID: n/a 
 *  Partner Precept: n/a
 * 
 *  Description: represents the Blob data type
 *  
 *
 ******************************************************************************/

public class Blob 
{
  private int mass; // number of pixels
  private double x; // x-coordinate center of mass
  private double y; // y-coordinate center of mass
  
  // creates an empty blob
  public Blob()
  {
    
  }
  
  // adds pixel (x, y) to this blob                     
  public void add(int a, int b)
  {
    int m = mass;
    mass++;
    x = (x * m + a) / mass;
    y = (y * m + b) / mass;
  }
  
  // number of pixels added to this blob     
  public int mass()
  {
    return mass;
  }
   
  // Euclidean distance between the center of 
  // masses of the two blobs                  
  public double distanceTo(Blob that)
  {
    double dx = x - that.x;
    double dy = y - that.y;
    return Math.sqrt(dx * dx + dy * dy);
  }
  
  // string representation of this blob 
  public String toString()
  {
    return String.format("%2d (%8.4f, %8.4f)", mass, x, y);
  }
  
  // tests all methods in this data type                
  public static void main(String[] args)
  {
    Blob blobA = new Blob();
    Blob blobB = new Blob();
    int a = StdRandom.uniform(1, 25);
    for (int x = 0; x < a; x++)
    {
      blobA.add(StdRandom.uniform(1, 50), StdRandom.uniform(1, 50));
      blobB.add(StdRandom.uniform(1, 50), StdRandom.uniform(1, 50));
    }
    StdOut.println("Number of Blobs: " + blobA.mass());
    StdOut.println(blobA);
    StdOut.println(blobB);
    StdOut.println(blobA.distanceTo(blobB));
  }   
}