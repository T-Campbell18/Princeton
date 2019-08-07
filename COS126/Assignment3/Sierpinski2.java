/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: drawshv ubibibibib
 *
 ******************************************************************************/

public class Sierpinski2 
{
  // Height of an equilateral triangle whose sides are of the specified length.
  public static double height(double length)
  {
	  return length * Math.sqrt(3) / 2;
	}
	
	// Draws a filled equilateral triangle whose bottom vertex is (x,y)
	// of the specified side length.
	public static void filledTriangle(double x, double y, double length)
	{
		double[] xPoints = {x, x -  (length / 2), x +  (length / 2)};
		double[] yPoints = {y, y + Sierpinski.height(length), 
		y + Sierpinski.height(length)};
		StdDraw.filledPolygon(xPoints, yPoints);
	}

	// Draws a Sierpinski triangle of order n, such that the largest filled
	// triangle has bottom vertex (x, y) and sides of the specified length.
	public static void sierpinski(int n, double x, double y, double length)
	{
		if (n == 0)
			return;
		
		filledTriangle(x, y, length);
		sierpinski(n-1, x, y + height(length), length/2);
		sierpinski(n-1, x - length/2, y, length/2);
		sierpinski(n-1, length/2 + x, y, length/2);

	}

	// Takes an integer command-line argument n;
	// draws the outline of an equilateral triangle (pointed upwards) of length 1;
	// whose bottom-left vertex is (0, 0) and bottom-right vertex is (1, 0); and
	// draws a Sierpinski triangle of order n that fits snugly inside the outline.
	public static void main(String[] args)
	{
		int n = Integer.parseInt(args[0]);
		double[] x = {0, 1, 0.5};
		double[] y = {0, 0, Sierpinski.height(1)};
		sierpinski(n, 0.5, 0, 0.5);
		StdDraw.polygon(x, y);
	}
	
	
}