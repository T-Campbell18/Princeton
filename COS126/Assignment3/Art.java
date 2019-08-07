/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: draws my custom pattern of differnt colored squares
 * using recursion
 *
 ******************************************************************************/

import java.awt.Color;

public class Art 
{
  // array of different colors
  private static final Color[] COLORS = {Color.BLUE, Color.RED, Color.PINK, 
  Color.CYAN, Color.ORANGE, Color.GREEN};
  
  // Draws a square with the given length centered at (x,y) and color of the
  // colorIndex of the colors array
  private static void draw(double x, double y, double length, int colorIndex)
  {
    double size = length/2;
    double[] xP = {x - size, x - size, x + size, x + size};
    double[] yP = {y + size, y - size, y - size, y + size};
    StdDraw.setPenColor(COLORS[colorIndex]);
    StdDraw.filledPolygon(xP, yP);
  }
  
  // draws the square pattern of n order, by drawing 8 squares a 1/3 length of the 
  // previous square length. Changes color after every n order. Largest square 
  // centered at the middle of screen. 
  private static void repeat(int n, double x, double y, double length, int cIndex)
  {
    if (n == 0)
      return;
    draw(x, y, length, cIndex);
    // changes color
    cIndex++;
    if (cIndex == COLORS.length)
      cIndex = 0;
    repeat(n-1, x-length, y+length, length/3, cIndex);
    repeat(n-1, x, y+length, length/3, cIndex);
    repeat(n-1, x+length, y+length, length/3, cIndex);
    repeat(n-1, x+length, y, length/3, cIndex);
    repeat(n-1, x+length, y-length, length/3, cIndex);
    repeat(n-1, x, y-length, length/3, cIndex);
    repeat(n-1, x-length, y-length, length/3, cIndex);
    repeat(n-1, x-length, y, length/3, cIndex);
  }
  
  // Takes command line integer n.
  // Draws a black square that covers the entire screen
  // starts n order repeat that draws the pattern starting in the center.`
  public static void main(String[] args) 
  {
    int n = Integer.parseInt(args[0]);
    double[] x = {0, 1, 1, 0};
    double[] y = {0, 0, 1, 1};
    StdDraw.enableDoubleBuffering();
    StdDraw.filledPolygon(x, y);
    repeat(n, 0.5, 0.5, 1.0/3, 0);
    StdDraw.show();
  }
}