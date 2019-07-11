import java.util.*;
import java.io.*;

public class Untitled 
{
	public static void main(String[] args) throws IOException
	{
		Scanner in = new Scanner(new File("t.txt"));
		int x = 0;
		int o = 0;
		String a ="";
		while(in.hasNext())
		{
			a = in.next();
			if (a.contains("x"))
				x++;
			if (a.contains
			("o"))
				o++;	
		}
		System.out.println(x-o);
	}
}