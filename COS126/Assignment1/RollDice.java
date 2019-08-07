public class RollDice 
{
  public static void main(String[] args) 
  {
    int n = Integer.parseInt(args[0]);
    int[] a = new int[100];
    for (int x = 0; x < n; x++)
    {
      int t = 0;
      for (int y = 0; y < 10; y++)
      {
        int d = StdRandom.uniform(6) + 1;
        t += d;
      }
      a[t]++;
    }
    for (int x = 10; x < 61; x++)
    {
      System.out.print(x + ": ");
      int z = a[x];
      System.out.print(z);
      while (z > 0)
      {
        System.out.print("*");
        z--;
      }
      System.out.println();  
    }
  }
}