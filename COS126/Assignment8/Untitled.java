import java.util.*;

public class Untitled 
{
	public static void main(String[] args) 
	{
		Scanner scan = new Scanner(System.in);
		
		ArrayList<Integer> w = new ArrayList<Integer>();
		ArrayList<Integer> g = new ArrayList<Integer>();
		
		while (sum(w) != 100)
		{
			w.add(scan.nextInt());
			g.add(scan.nextInt());
		}
		double s = 0;
		for (int i = 0; i < w.size(); i++)
		{
			s += (w.get(i)/100.0) * g.get(i);
		}
		System.out.println("Grade: " + s);
	}
	
	static int sum(ArrayList<Integer> x)
	{
		int s = 0;
		for (int i = 0; i < x.size(); i++)
		{
			s += x.get(i);
		}
		return s;
	}
}