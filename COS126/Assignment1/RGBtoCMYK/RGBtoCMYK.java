/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: Converts from RGB format to CMYK format. Reads 
 * three integer command-line arugments red, green, and blue; 
 * prints the RGB values; then prints the equivalent CMYK 
 *
 ******************************************************************************/

public class RGBtoCMYK 
{
  public static void main(String[] args) 
  {
    final double RANGE = 255.0;
    int red = Integer.parseInt(args[0]);
    int green = Integer.parseInt(args[1]);
    int blue = Integer.parseInt(args[2]);
    System.out.println("red = " + red);
    System.out.println("green = " + green);
    System.out.println("blue = " + blue);
    // if the color is black
    if (red == 0 && green == 0 && blue == 0)
    {
        System.out.println("cyan = " + 0.0);
        System.out.println("magenta = " + 0.0);
        System.out.println("yellow = " + 0.0);
        System.out.println("black = " + 1.0);
    }
    else 
    {
      // calculate white 
      double white = Math.max(green/RANGE, blue/RANGE);
      white = Math.max(white , red/RANGE);
      // calculate cyan
      double cyan = (white - red / RANGE) / white;
      System.out.println("cyan = " + cyan);
      // calculate magenta
      double magenta = (white - green / RANGE) / white;
      System.out.println("magenta = " + magenta);
      // calculate yellow
      double yellow = (white - blue / RANGE) / white;
      System.out.println("yellow = " + yellow);
      // calculate black
      double black = 1 - white;  
      System.out.println("black = " + black);
    } 
    
  }
}