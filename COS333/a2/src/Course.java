import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.File;
import java.sql.PreparedStatement;
import java.util.*;
import javax.swing.*;

public class Course
{	
	public static String getInfo(String classid)
	{
		final String DATABASE_NAME = "reg.sqlite";
		StringBuilder info = new StringBuilder();
		try
		{  
			File databaseFile = new File(DATABASE_NAME);
			if (! databaseFile.isFile())
				throw new Exception("Database connection failed, but why");
			
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
			info.append("CourseID: " + courseid + "\n\n");
			info.append("Days: " +  days + "\n");
			info.append("Start Time: " + start + "\n");
			info.append("End Time: " + end + "\n");
			info.append("Buliding: " + bldg + "\n");
			info.append("Room Number: " + roomnum + "\n\n");
			// need work
			for (String c : code)
			{
				info.append("Dept and Number: " + c + "\n");
			}
			info.append("\n");
			info.append("Area: " + area + "\n\n");
			info.append("Title: " + title + "\n\n");
			
			StringBuilder sb = new StringBuilder(descrip);
			int j = 0;
			while ((j = sb.indexOf(" ", j + 60)) != -1)
				sb.replace(j, j + 1, "\n");
			info.append("Description: " +  sb + "\n\n");
			
			sb = new StringBuilder(prereqs);
			j = 0;
			while ((j = sb.indexOf(" ", j + 60)) != -1)
				sb.replace(j, j + 1, "\n");
			info.append("Description: " +  sb + "\n\n");
			// needs work
			info.append("Professor(s): ");
			boolean a = true;
			for (String n : names)
			{
				if (a)
					info.append(n + "\n");
				else	
					info.append("                     " + n + "\n");
				a = false;	
			}
				
			connection.close();
		}
		catch (Exception e) { System.err.println(e); System.exit(1);}
		return info.toString();
	}
	public static void main(String[] args) 
	{
		String test = getInfo(args[0]);
		JOptionPane.showMessageDialog(null, test, "Class Info", JOptionPane.INFORMATION_MESSAGE);
	}
}