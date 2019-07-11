import java.util.Arrays;
import java.io.PrintWriter;
import java.io.*;

public class Untitled2 
{
	public static void main(String[] args) throws FileNotFoundException
	{
		PrintWriter writer = new PrintWriter("mywc7.txt");
		String c = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\t\n";
		String[] a = c.split("");
		for (int x = 0; x < 30000; x++)
		{
			int d = (int) (Math.random() * 97);
			double e = Math.random();
			if (e < .01)
				writer.print("\n");
			writer.print(a[d]);
		}
		writer.close();
	}
}