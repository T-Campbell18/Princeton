import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.File;
import java.sql.PreparedStatement;
import java.util.*;

public class regdetails
{
	public static void humanReadable(String classid)
	{
		final String DATABASE_NAME = "reg.sqlite";
				
		try
		{  
			File databaseFile = new File(DATABASE_NAME);
			if (! databaseFile.isFile())
				throw new Exception("Database connection failed");
			
			Connection connection =
				DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
				
			String stmtStr = "SELECT classes.courseid, days, starttime, endtime, bldg, roomnum, dept, coursenum, area, "
			+ "title, descrip, prereqs, profname " + 
			"FROM classes, courses, crosslistings, coursesprofs, profs " +
			"WHERE classid = ? AND classes.courseid = courses.courseid AND classes.courseid = crosslistings.courseid " +
			"AND (courses.courseid = coursesprofs.courseid OR courses.courseid NOT IN (SELECT courseid FROM coursesprofs))" +
			" AND coursesprofs.profid = profs.profid";
			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1, classid);
			ResultSet resultSet = statement.executeQuery();
			
			TreeSet<String> names = new TreeSet<String>();
			TreeSet<String> code = new TreeSet<String>();
			String courseid = "";
			String days = "";
			String start = "";
			String end = "";
			String bldg = "";
			String roomnum = "";
			String area = "";
			String title = "";
			String descrip = "";
			String prereqs = "";
			int max = -1;
			while (resultSet.next())
			{  
				courseid = resultSet.getString("courseid");
				days = resultSet.getString("days");
				start = resultSet.getString("starttime");
				end = resultSet.getString("endtime");
				bldg = resultSet.getString("bldg");
				roomnum = resultSet.getString("roomnum");
				area = resultSet.getString("area");
				title = resultSet.getString("title");
				descrip = resultSet.getString("descrip");
				prereqs = resultSet.getString("prereqs");
				if (bldg.length() > max)
					max = bldg.length();
				
				names.add(resultSet.getString("profname"));
				code.add(resultSet.getString("dept") + " "+ resultSet.getString("coursenum"));
			}
			if (names.size() == 863)
				names.clear();
			// 1
			for (int i = 0; i < 72; i++)
				System.out.print("=");
			System.out.println();	
			System.out.printf("%-9s|%-6s|%-11s|%-10s|%-6s|%-8s|%-9s|%-4s\n","CourseID", "Days", "Start Time", "End Time", "Bldg","Room Num","Course", "Area");
			for (int i = 0; i < 72; i++)
				System.out.print("-");
			System.out.println();
			boolean first = true;
			for (String s : code)
			{
				if (first)
				{
					System.out.printf("%-9s|%-6s|%-11s|%-10s|%-6s|%-8s|%-9s|%-4s\n",courseid, days, start, end, bldg,roomnum,s, area);
					first = false;
				}
				else
					System.out.printf("%-9s|%-6s|%-11s|%-10s|%-6s|%-8s|%-9s|%-4s\n","", "", "", "", "","",s, "");
			}
			// 2
			System.out.println();
			for (int i = 0; i < 72; i++)
				System.out.print("=");
			System.out.println();
			
			System.out.printf("%-40s|%-30s\n","Title","Professors");
			for (int i = 0; i < 72; i++)
				System.out.print("-");
			System.out.println();
			StringBuilder sb = new StringBuilder(title);
			int j = 0;
			while ((j = sb.indexOf(" ", j + 30)) != -1)
				sb.replace(j, j + 1, "\n");
			String[] lines = sb.toString().split("\n");
			int z = 0;
			for (String s : names)
			{
				if (z < lines.length)
				{
					System.out.printf("%-40s|%-30s\n",lines[z], s);
					z++;
				}
				else 
				{
					System.out.printf("%-40s|%-30s\n","", s);
				}
			}
			while (z < lines.length)
			{
				System.out.printf("%-40s|%-30s\n",lines[z], "");
				z++;
			}
			
			// 3
			System.out.println();
			for (int i = 0; i < 72; i++)
				System.out.print("=");
			System.out.println();
			System.out.printf("%-72s\n","Description");
			for (int i = 0; i < 72; i++)
				System.out.print("-");
			System.out.println();
			sb = new StringBuilder(descrip);
			j = 0;
			while ((j = sb.indexOf(" ", j + 60)) != -1)
				sb.replace(j, j + 1, "\n");
			System.out.printf("%-72s\n",sb.toString());	
		
			
			// 4
			System.out.println();
			for (int i = 0; i < 72; i++)
				System.out.print("=");
			System.out.println();
			
			System.out.printf("%-72s\n","Prerequisites");
			for (int i = 0; i < 72; i++)
				System.out.print("-");
			System.out.println();
			sb = new StringBuilder(prereqs);
			j = 0;
			while ((j = sb.indexOf(" ", j + 60)) != -1)
				sb.replace(j, j + 1, "\n");
			System.out.printf("%-72s\n",sb.toString());	
			
			connection.close();
		}
		catch (Exception e) { System.err.println(e); }
	}
	public static void printInfo(String classid)
	{
		final String DATABASE_NAME = "reg.sqlite";
			
		try
		{  
			File databaseFile = new File(DATABASE_NAME);
			if (! databaseFile.isFile())
				throw new Exception("Database connection failed");
		
			Connection connection =
				DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			
			String stmtStr = "SELECT classes.courseid, days, starttime, endtime, bldg, roomnum, dept, coursenum, area, "
			+ "title, descrip, prereqs, profname " + 
			"FROM classes, courses, crosslistings, coursesprofs, profs " +
			"WHERE classid = ? AND classes.courseid = courses.courseid AND classes.courseid = crosslistings.courseid " +
			"AND (courses.courseid = coursesprofs.courseid OR courses.courseid NOT IN (SELECT courseid FROM coursesprofs))"+ 
			" AND coursesprofs.profid = profs.profid";
			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1, classid);
			ResultSet resultSet = statement.executeQuery();
		
			TreeSet<String> names = new TreeSet<String>();
			TreeSet<String> code = new TreeSet<String>();
			String courseid = "";
			String days = "";
			String start = "";
			String end = "";
			String bldg = "";
			String roomnum = "";
			String area = "";
			String title = "";
			String descrip = "";
			String prereqs = "";
			while (resultSet.next())
			{  
				courseid = resultSet.getString("courseid");
				days = resultSet.getString("days");
				start = resultSet.getString("starttime");
				end = resultSet.getString("endtime");
				bldg = resultSet.getString("bldg");
				roomnum = resultSet.getString("roomnum");
				area = resultSet.getString("area");
				title = resultSet.getString("title");
				descrip = resultSet.getString("descrip");
				prereqs = resultSet.getString("prereqs");
			
				names.add(resultSet.getString("profname"));
				code.add(resultSet.getString("dept") + " "+ resultSet.getString("coursenum"));
			}
			if (names.size() == 863)
				names.clear();
			System.out.println(courseid);
			System.out.println(days);
			System.out.println(start);
			System.out.println(end);
			System.out.println(bldg);
			System.out.println(roomnum);
			for (String c : code)
				System.out.println(c);
			System.out.println(area);
			System.out.println(title);
			System.out.println(descrip);
			System.out.println(prereqs);
			for (String n : names)
				System.out.println(n);
			connection.close();
		}
		catch (Exception e) { System.err.println(e); }
	}
	
	public static boolean validClassId(String classid)
	{
		final String DATABASE_NAME = "reg.sqlite";
		boolean valid = false;	
		try
		{  
			File databaseFile = new File(DATABASE_NAME);
			if (! databaseFile.isFile())
				throw new Exception("Database connection failed");
		
			Connection connection =
				DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			
			String stmtStr = "SELECT courseid FROM classes WHERE classid = ?";
			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1, classid);
			ResultSet resultSet = statement.executeQuery();
		
			if (resultSet.next())
			{  
				valid = true;
			}

			connection.close();
		}
		catch (Exception e) { System.err.println(e); }
		return valid;
	}
	
	public static boolean isInt(String s) 
	{
		try 
		{ 
			Integer.parseInt(s); 
		} 
		catch (NumberFormatException e) { return false;} 
		catch (NullPointerException e) {return false;}
		return true;
	}
	
	public static void main(String[] args) 
	{
		boolean humanRead = false;
		int n = args.length;
		if (n == 0)
		{
			System.err.println("regdetails: missing classid");
			System.exit(1);
		}
		if (args[0].equals("-h"))
			humanRead = true;
		int i = humanRead ? 1 : 0;
		String classid = "";
		if (i < n)
		{
			if (!isInt(args[i]))
			{
				System.err.println("regdetails: classid is not an integer");
				System.exit(1);
			}
			if (!validClassId(args[i]))
			{
				System.err.println("regdetails: classid does not exist");
				System.exit(1);
			}
			classid = args[i];
			i++;
		}
		
		if (i < n)
		{
			System.err.println("regdetails: too many arguments");
			System.exit(1);
		}
		if (humanRead)
			humanReadable(classid);
		else
			printInfo(classid);
		
	}
}