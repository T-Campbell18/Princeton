/******************************************************************************
 *  Name: Tyler Campbell   
 *  NetID: tylercc
 *  Precept: P02 
 *
 *  Partner Name: n/a  
 *  Partner NetID: n/a 
 *  Partner Precept: n/a
 * 
 * Description: represents the BeadFinder data type. uses a recursive
 * depth-first search to identify the blobs and beads efficiently
 *  
 *
 ******************************************************************************/

public class BeadFinder 
{
  private Picture pic; // the picture
  private double tau; // threshold tau
  private boolean[][] here; // represents if the pixel has been visted
  private Stack<Blob> list; // list of blobs
  

  // finds all blobs in the specified picture using luminance threshold tau
  public BeadFinder(Picture picture, double tau)
  {
    list = new Stack<Blob>();
    pic = picture;
    this.tau = tau;
    here = new boolean[pic.width()][pic.height()];
    for (int x = 0; x < pic.width(); x++)
    {
      for (int y = 0; y < pic.height(); y++)
      {
        if (Luminance.lum(pic.get(x, y)) >= tau && !here[x][y])
        {
          Blob b = new Blob();
          dfs(x, y, b);
          list.push(b);
        }
      }
    }
  }
  
  // locates all of the foreground pixels in the same blob as the 
  // foreground pixel (x, y). using depth-first search
  private void dfs(int x, int y, Blob b)
  {
    if (x < 0 || y < 0 || x >= pic.width() || y >= pic.height() || here[x][y]) 
      return;
    if (Luminance.lum(pic.get(x, y)) < tau)
    {
      here[x][y] = true;
      return;
    }
    b.add(x, y);
    here[x][y] = true;
    dfs(x + 1, y, b);
    dfs(x - 1, y, b);
    dfs(x, y + 1, b);
    dfs(x, y - 1, b);
  }

  // returns all beads (blobs with >= min pixels)
  public Blob[] getBeads(int min)
  {
    Blob[] blobs = new Blob[countBeads(min)];
    int x = 0;
    for (Blob b : list)
    {
      if (b.mass() >= min)
      {
        blobs[x] = b;
        x++;
      }
    }
    return blobs;
  }
  
  // counts the number of beads (blobs with >= min pixels)
  private int countBeads(int min) 
  {
    int x = 0;
    for (Blob b : list) 
    {
      if (b.mass() >= min)
        x++;
    }
    return x;
  }

  // test client
  public static void main(String[] args)
  {
    int min = Integer.parseInt(args[0]);
    double tau = Double.parseDouble(args[1]);
    BeadFinder finder = new BeadFinder(picture, tau);
    Blob [] beads = finder.getBeads(min);
    for (int x = 0; x < beads.length; x++)
      System.out.println(beads[x]);
  }
}