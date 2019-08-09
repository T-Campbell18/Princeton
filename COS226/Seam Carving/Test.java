import edu.princeton.cs.algs4.*;
public class Test
{
  public static void main(String[] args) 
  {
    Picture p = SCUtility.randomPicture(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    double t = 0;
    for (int x = 0; x < 1; x++)
    {
      SeamCarver sc = new SeamCarver(p);
      Stopwatch s = new Stopwatch();
      int[] seam = sc.findHorizontalSeam();
      sc.removeHorizontalSeam(seam);
      t += s.elapsedTime();
    }
    System.out.printf("row: %.3f\n", t/1);
    t = 0;
    for (int x = 0; x < 1; x++)
    {
      SeamCarver sc = new SeamCarver(p);
      Stopwatch s = new Stopwatch();
      int[] seam = sc.findVerticalSeam();
      sc.removeVerticalSeam(seam);
      t += s.elapsedTime();
    }
    System.out.printf("column: %.3f\n", t/1);
  }
}