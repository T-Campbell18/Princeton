import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.File;
import java.util.*;

public class reg 
{
	public static void humanReadable(String d, String n, String a, String t)
	{
		final String DATABASE_NAME = "reg.sqlite";
		try
		{ 
			File databaseFile = new File(DATABASE_NAME);
			if (! databaseFile.isFile())
				throw new Exception("Database connection failed");
			
			Connection connection =
				DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
					 
			String stmtStr = "SELECT classid, dept, coursenum, area, title " + 
				"FROM classes, courses, crosslistings " +
				"WHERE classes.courseid = courses.courseid AND classes.courseid = crosslistings.courseid " + 
				"AND lower(dept) LIKE ? AND lower(coursenum) LIKE ? AND lower(area) LIKE ? AND lower(title) LIKE ?"+ 
				" ORDER BY dept ASC, coursenum ASC, classid ASC";
			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1, d);
			statement.setString(2, n);
			statement.setString(3, a);
			statement.setString(4, t);
			ResultSet resultSet = statement.executeQuery();
			// top of table with column names
			for (int i = 0; i < 72; i++)
				System.out.print("=");
			System.out.println();	
			System.out.printf("%-8s|%-4s|%-4s|%-4s|%-48s\n","ClassID", "Dept", "Num", "Area", "Title");
			for (int i = 0; i < 72; i++)
				System.out.print("=");
			System.out.println();
			while (resultSet.next())
			{  
				int classid = resultSet.getInt("classid");
				String dept = resultSet.getString("dept");
				String coursenum = resultSet.getString("coursenum");
				String area = resultSet.getString("area");
				
				String title = resultSet.getString("title");
				StringBuilder sb = new StringBuilder(title);
				int j = 0;
				while ((j = sb.indexOf(" ", j + 34)) != -1)
					sb.replace(j, j + 1, "\n");
				String[] lines = sb.toString().split("\n");
				
				System.out.printf("%-8s|%-4s|%-4s|%-4s|%-48s\n",classid, dept, coursenum, area, lines[0]);
				
				for (int x = 1; x < lines.length; x++)
				{
					System.out.printf("%-8s|%-4s|%-4s|%-4s|%-48s\n","", "", "", "", lines[x]);
				}
				for (int i = 0; i < 72; i++)
					System.out.print("-");
				System.out.println();
			}
			connection.close();
		}
		catch (Exception e) {System.err.println(e);System.exit(1);}
	}
	public static void printInfo(String d, String n, String a, String t)
	{
		final String DATABASE_NAME = "reg.sqlite";
		try
		{ 
			File databaseFile = new File(DATABASE_NAME);
			if (! databaseFile.isFile())
				throw new Exception("Database connection failed");
			
			Connection connection =
				DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
					 
			String stmtStr = "SELECT classid, dept, coursenum, area, title " + 
				"FROM classes, courses, crosslistings " +
				"WHERE classes.courseid = courses.courseid AND classes.courseid = crosslistings.courseid " + 
				"AND lower(dept) LIKE ? AND lower(coursenum) LIKE ? AND lower(area) LIKE ? AND lower(title) LIKE ? "+ 
				"ORDER BY dept ASC, coursenum ASC, classid ASC";
			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1, d);
			statement.setString(2, n);
			statement.setString(3, a);
			statement.setString(4, t);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{  
				int classid = resultSet.getInt("classid");
				System.out.print(classid + "\t");
				String dept = resultSet.getString("dept");
				System.out.print(dept + "\t");
				String coursenum = resultSet.getString("coursenum");
				System.out.print(coursenum + "\t");
				String area = resultSet.getString("area");
				System.out.print(area + "\t");
				String title = resultSet.getString("title");
				System.out.println(title);
			}
			connection.close();
		}
		catch (Exception e) { System.err.println(e); System.exit(1);}
	}

	public static boolean vaildKey(String str)
	{
		switch(str)
		{
			case "-dept":
				return true;
			case "-coursenum":
				return true;
			case "-area":
				return true;
			case "-title":
				return true;
			default:
				return false;
		}
	}
	
	public static String replaceWildCard(String str)
	{
		str = str.replace("_", "[_]");
		str = str.replaceAll("%", "[%]");	
		return str;	
	}
	
	public static void main(String args[]) {
		int n = args.length;
		
		if (n == 0)
		{
			printInfo("%","%","%","%");
			System.exit(0);
		}
		
		if (n == 1 && args[0].equals("-h"))
		{
			humanReadable("%","%","%","%");
			System.exit(0);
		}
		
		HashMap<String,String> info = new HashMap<String,String>();
		boolean humanRead = false;
		
		if (args[0].equals("-h"))
			humanRead = true;
		
		int i = humanRead ? 1 : 0;
		// read key value from args print err if there is one
		for (; i < n; i++)
		{
			String key = args[i];
			if (!vaildKey(key))
			{
				System.err.println("reg: invalid key");
				System.exit(1);
			}
			if (info.containsKey(key))
			{
				System.err.println("reg: duplicate key");
				System.exit(1);
			}
			if (++i >= n)
			{
				System.err.println("reg: missing value");
				System.exit(1);
			}
			String val = "%" + replaceWildCard(args[i]) + "%";
			
			info.put(key, val.toLowerCase());	
		}
		String dept = info.containsKey("-dept") ? info.get("-dept"): "%";
		String coursenum = info.containsKey("-coursenum") ? info.get("-coursenum"): "%";
		String area = info.containsKey("-area") ? info.get("-area"): "%";
		String title = info.containsKey("-title") ? info.get("-title"): "%";
		
		if (humanRead)
			humanReadable(dept, coursenum, area, title);//human readble version
		else	
			printInfo(dept, coursenum, area, title);
	}
}