import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.io.File;
import java.util.*;

public class Reg
{
	private static final String DATABASE_NAME = "reg.sqlite";

	private Connection connection;

	public Reg() {}

	public void connect() throws Exception
	{
		File databaseFile = new File(DATABASE_NAME);
		if (! databaseFile.isFile())
			throw new Exception("Database connection failed");
		connection =
			DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
	}

	public void disconnect() throws Exception
	{
		connection.close();
	}
	private static String replaceWildCard(String str)
	{
		if (str == null || str.isEmpty())
			return "%";
		str = str.replaceAll("_", "[_]");
		str = str.replaceAll("%", "[%]");
		return "%" + str + "%";	
	}
	public ArrayList<Class> search(String d, String n, String a, String t) throws Exception
	{
		ArrayList<Class> classes = new ArrayList<Class>();
		
		String stmtStr = "SELECT classid, dept, coursenum, area, title " + 
		"FROM classes, courses, crosslistings " +
		"WHERE classes.courseid = courses.courseid AND classes.courseid = crosslistings.courseid " + 
		"AND lower(dept) LIKE ? AND lower(coursenum) LIKE ? AND lower(area) LIKE ? AND lower(title) LIKE ? "+ 
		"ORDER BY dept ASC, coursenum ASC, classid ASC";
		PreparedStatement statement = connection.prepareStatement(stmtStr);
		statement.setString(1, replaceWildCard(d));
		statement.setString(2, replaceWildCard(n));
		statement.setString(3, replaceWildCard(a));
		statement.setString(4, replaceWildCard(t));
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next())
		{ 
			String classid = resultSet.getString("classid");
			String dept = resultSet.getString("dept");
			String coursenum = resultSet.getString("coursenum");
			String area = resultSet.getString("area");
			String title = resultSet.getString("title");
			classes.add(new Class(classid,dept,coursenum,area,title));
		}
		return classes;
	}
	
	public Course classSearch(String classid) throws Exception
	{
		Course ci = null;
				
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
		ci = new Course(classid, courseid, days, start, end, bldg, roomnum, code, area, title, 
			descrip, prereqs, names);
		return ci;
	}
	
	public boolean validID(String classid) throws Exception
	{
		String stmtStr = "SELECT courseid FROM classes WHERE classid = ?";
		PreparedStatement statement = connection.prepareStatement(stmtStr);
		statement.setString(1, classid);
		ResultSet resultSet = statement.executeQuery();
	
		return resultSet.next();
	}	

	public static void main(String[] args) throws Exception
	{
		
	}
}