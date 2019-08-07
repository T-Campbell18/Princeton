public class NoonSnooze
{
  public static void main(String[] args) 
  {
    int s = Integer.parseInt(args[0]);
    int h = s / 60 + 12;
    int m = s % 60;
    int t1 = 12;
    String a = "pm";
    int x = 0;
    while (h >= 12)
    {
      if (h == 12)
      {
        x++;
        break;
      }
      h -= 12;
      x++;
    }
//  Posible implementation    
//    while (h > 0)
//    {
//      t1++;
//      if (t1 == 12 && a.equals("pm")) // if (t1 == 12) x++;
//        a = "am";
//      else if (t1 == 12 && a.equals("am"))
//        a = "pm";
//      if (t1 == 13)
//        t1 = 1;
//      h--;
//    }
    if (x % 2 == 0)
      a = "am";
    
    System.out.printf("%d:%02d%s\n", h, m, a);
  }
}