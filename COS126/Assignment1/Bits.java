public class Bits 
{
  public static void main(String[] args) 
  {
    int n = Integer.parseInt(args[0]);
    if (n < 0)
      System.out.println("Illegal input");
    else 
    {
      int x = 0;
      while (n >= 1)
      {
        x++;
        n /= 2;
      }
      System.out.println(x);
    }  
  }
}