 /*******************************************************************************
 *  Name: Tyler Campbell
 *  NetID: tylercc 
 *  Precept: P02
 *
 * Description: encrypts and decrypts pictures based on the given LFSR
 *
 ******************************************************************************/
import java.awt.Color;

public class PhotoMagic
{
  // Returns a transformed copy of the specified picture, using the specified LFSR
  public static Picture transform(Picture picture, LFSR lfsr)
  {
    Picture pic = new Picture(picture);
    for (int y = 0; y < pic.width(); y++)
    {
      for (int x = 0; x < pic.height(); x++)
      {
        Color col = pic.get(y, x);
        int r = col.getRed();
        int b = col.getBlue();
        int g = col.getGreen();
        r = r ^ lfsr.generate(8);
        g = g ^ lfsr.generate(8);
        b = b ^ lfsr.generate(8);
        col = new Color(r, g, b);
        pic.set(y, x, col);
      } 
    }
    return pic;
  }
  
  // Takes the name of an image file and
  // a description of an LFSR as command-line arguments;
  // displays a copy of the image that is "encrypted" using the LFSR.
  public static void main(String[] args) 
  {
    Picture pic = new Picture(args[0]);
    LFSR a = new LFSR(args[1], Integer.parseInt(args[2]));
    Picture transformer = transform(pic, a);
    transformer.show();
  }
}