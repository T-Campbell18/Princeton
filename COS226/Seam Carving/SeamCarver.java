   /******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: creates a data type that resizes a W-by-H image using the 
 * seam-carving technique 
 * Seam-carving is a content-aware image resizing technique where the image is 
 * reduced in size by one pixel of height (or width) at a time. A vertical seam 
 * in an image is a path of pixels connected from the top to the bottom with 
 * one pixel in each row; a horizontal seam is a path of pixels connected from 
 * the left to the right with one pixel in each column.
 * Uses the dual-gradient energy function to find vertical and horizontal seams
 * with minimum total energy
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.Arrays;

public class SeamCarver 
{
  private Picture pic; // the current Picture object

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture)
  {
    if (picture == null)
      throw new NullPointerException();
    pic = new Picture(picture);
  }
  
  // current picture                
  public Picture picture()
  {
    return new Picture(pic);
  }
  
  // width of current picture                         
  public int width()
  {
    return pic.width();
  }
  
  // height of current picture                            
  public int height()
  {
    return pic.height();
  }
  
  // energy of pixel at column x and row y                          
  public double energy(int x, int y)
  {
    if (x < 0 || x >= width() || y < 0 || y >= height())
      throw new IndexOutOfBoundsException();
    double gradX;
    double gradY;
    if (width() == 1)
      gradX = 0;
    else if (x == 0)
      gradX = calcGrad(pic.get(width() - 1, y), pic.get(x + 1, y));
    else if (x == width() - 1)
      gradX = calcGrad(pic.get(x - 1, y), pic.get(0, y));
    else
      gradX = calcGrad(pic.get(x - 1, y), pic.get(x + 1, y));
    if (height() == 1)
      gradY = 0;
    else if (y == 0)
      gradY = calcGrad(pic.get(x, height() - 1), pic.get(x, y + 1));
    else if (y == height() - 1)
      gradY = calcGrad(pic.get(x, y - 1), pic.get(x, 0));
    else
      gradY = calcGrad(pic.get(x, y - 1), pic.get(x, y + 1));
    return Math.sqrt(gradX + gradY);
  }
  
  // Calculates the gradient between 2 colors
  private double calcGrad(Color t, Color c) 
  {
    int r = t.getRed() - c.getRed();
    int g = t.getGreen() - c.getGreen();
    int b = t.getBlue() - c.getBlue();
    return r * r + g * g + b * b;
  }
  
  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam()
  {
    transposePic();
    int[] hSeam = findVerticalSeam();
    transposePic();
    return hSeam;
  }
  
  // sequence of indices for vertical seam              
  public int[] findVerticalSeam()
  {
    int[] seam = new int[height()];
    double[][] energy = new double[width()][height()];
    for (int y = 0; y < height(); y++)
      for (int x = 0; x < width(); x++)
        energy[x][y] = energy(x, y);
    
    if (width() == 1)
      return seam;
    // cummulative energy
    for (int y = 1; y < height(); y++)
    {
      for (int x = 0; x < width(); x++)
      {        
        if (x == 0)
        {
          double min = Math.min(energy[x][y-1], energy[x+1][y-1]);
          energy[x][y] = min + energy[x][y];
        }
        else if (x == width() - 1)
        {
          double min = Math.min(energy[x][y-1], energy[x-1][y-1]);
          energy[x][y] = min + energy[x][y];
        }
        else
        {
          double min = Math.min(energy[x][y-1], energy[x-1][y-1]);
          min = Math.min(min, energy[x+1][y-1]);
          energy[x][y] = min + energy[x][y];
        }
      }
    }
    // find min on bottom row 
    int in = -1;
    double min = Double.POSITIVE_INFINITY;
    for (int x = 0; x < width(); x++)
    {
      if (energy[x][height()-1] < min)
      {
        in = x;
        min = energy[x][height()-1];
      }
    }
    // backtrace
    for (int y = height() - 1; y > 0; y--) 
    {
      seam[y] = in;
      in = findNext(in, y, energy);
    }
    seam[0] = in;
    return seam;
  }
  // finds the min energy pixel from the 2/3 pixels above (used for backtracing)
  private int findNext(int x, int y, double[][] energy) 
  {
    // left column
    if (x == 0) 
    {
      if (energy[x][y - 1] <= energy[x + 1][y - 1])
        return 0;
      else
        return 1;
    }
    // right column
    if (x == width() - 1) 
    {
      if (energy[x][y - 1] <= energy[x - 1][y - 1])
        return width() - 1;
      else
        return width() - 2;
    }
    // checks 3 pixels above
    int in = x - 1;
    double min = energy[x - 1][y - 1];
    for (int z = 0; z < 3; z++) 
    {
      if (energy[x - 1 + z][y - 1] < min) 
      {
        min = energy[x - 1 + z][y - 1];
        in = x - 1 + z;
      }
    }
    return in;
  }
  
  // remove horizontal seam from current picture                
  public void removeHorizontalSeam(int[] seam)
  {
    if (seam == null)
      throw new NullPointerException();
    if (height() == 1)
      throw new IllegalArgumentException();
    transposePic();
    removeVerticalSeam(seam);
    transposePic();
  }
  
  // transposes the Picture
  private void transposePic()
  {
    Picture transpose = new Picture(pic.height(), pic.width());
    for (int w = 0; w < transpose.width(); w++) 
    {
      for (int h = 0; h < transpose.height(); h++) 
      {
        transpose.set(w, h, pic.get(h, w));
      }
    }
    pic = transpose;
  }
  
  // remove vertical seam from current picture  
  public void removeVerticalSeam(int[] seam)
  {
    if (seam == null)
      throw new NullPointerException();
    if (width() == 1)
      throw new IllegalArgumentException();
    if (seam.length != height())
      throw new IllegalArgumentException();
    if (!validSeam(seam))
      throw new IllegalArgumentException();
    Picture temp = new Picture(width() - 1, height());
    for (int y = 0; y < height(); y++)
    {
      for (int x = 0, a = 0; x < width(); x++)
      {
        if (x != seam[y])
        {
          temp.set(a, y, pic.get(x, y));
          a++;
        }
      }
    }
    pic = temp;
  }
  // checks if the seam is valid in the picture
  private boolean validSeam(int[] seam)
  {
    for (int x = 0; x < seam.length; x++) 
    {
      if (seam[x] >= width() || seam[x] < 0)
        return false;
      if (x == 0)
        continue;
      if (Math.abs(seam[x] - seam[x-1]) > 1) 
        return false;
    }
    return true;
  }
  
  // do unit testing of this class
  public static void main(String[] args)
  {
    Picture pic = new Picture(args[0]);
    SeamCarver sc = new SeamCarver(pic);
    System.out.println("width: " + sc.width());
    System.out.println("height: " + sc.height());
    sc.picture().show();
    int a = 10;
    int b = 10;
    if (a > sc.width())
      a = sc.width() / 2;
    if (b > sc.height())
      b = sc.height() / 2;
    // check enery and first vertical/horztional path from seamCarving folder
    if (sc.width() < 15 && sc.height() < 15)
    {
      int[] verticalSeam = sc.findVerticalSeam();
      System.out.println("Vertical Seam: " + Arrays.toString(verticalSeam));
      int[] horizontalSeam = sc.findHorizontalSeam();
      System.out.println("Horizontal Seam: " + Arrays.toString(horizontalSeam));
      for (int row = 0; row < sc.height(); row++) 
      {
        for (int col = 0; col < sc.width(); col++)
          System.out.printf("%9.2f ", sc.energy(col, row));
        System.out.println();
      }
    }
    for (int x = 0; x < b; x++) 
    {
      int[] horizontalSeam = sc.findHorizontalSeam();
      sc.removeHorizontalSeam(horizontalSeam);
    }
    for (int x = 0; x < a; x++) 
    {
      int[] verticalSeam = sc.findVerticalSeam();
      sc.removeVerticalSeam(verticalSeam);
    }
    System.out.println("width: " + sc.width());
    System.out.println("height: " + sc.height());
    sc.picture().show();
  }           
}