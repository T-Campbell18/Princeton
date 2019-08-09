import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.File;
import java.sql.PreparedStatement;
import java.util.*;
import java.io.ObjectOutputStream;

class EchoThread extends Thread
{
	private Socket socket;
	private SocketAddress clientAddr;

	public EchoThread(Socket socket, SocketAddress clientAddr)
	{
		this.socket = socket;
		this.clientAddr = clientAddr;
	}

	public void run()
	{
		try
		{  
			System.out.println("Spawned thread");

			InputStream inputStream = socket.getInputStream();
			Scanner scanner = new Scanner(inputStream);

			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			

			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				String[] info = line.split("#");
				// 1 courseinfo -> info
				if (info.length > 1)
				{
					System.out.println("Command recieved: Courses");
					Vector<String> money = printInfo(info[0], info[1], info[2], info[3]);
					outputStream.writeObject(money);
					outputStream.flush();			
				}
				// 2 classid -> info
				else
				{
					System.out.println("Command recidved: ClassInfo");
					String c = classInfo(info[0]);
					printWriter.println(c);
					printWriter.flush(); 
				}	
			}
			socket.close();
			System.out.println("Closed socket");
			System.out.println("Exiting thread");
		}
		catch (Exception e) {System.err.println("Error : " +e);}
	}
	public String classInfo(String classid)
	{
		final String DATABASE_NAME = "reg.sqlite";
		StringBuilder info = new StringBuilder();
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
			info.append("CourseID: " + courseid + "\n\n");
			info.append("Days: " +  days + "\n");
			info.append("Start Time: " + start + "\n");
			info.append("End Time: " + end + "\n");
			info.append("Buliding: " + bldg + "\n");
			info.append("Room Number: " + roomnum + "\n\n");

			for (String c : code)
			{
				info.append("Dept and Number: " + c + "\n");
			}
			info.append("\n");
			info.append("Area: " + area + "\n\n");
			
			StringBuilder sb = new StringBuilder(title);
			int j = 0;
			while ((j = sb.indexOf(" ", j + 60)) != -1)
				sb.replace(j, j + 1, "\n");
			info.append("Title: " + sb + "\n\n");
			
			sb = new StringBuilder(descrip);
			j = 0;
			while ((j = sb.indexOf(" ", j + 60)) != -1)
				sb.replace(j, j + 1, "\n");
			info.append("Description: " +  sb + "\n\n");
			
			sb = new StringBuilder(prereqs);
			j = 0;
			while ((j = sb.indexOf(" ", j + 60)) != -1)
				sb.replace(j, j + 1, "\n");
			info.append("Prerequisites: " +  sb + "\n\n");

			info.append("Professor(s): ");
			boolean a = true;
			for (String n : names)
			{
				if (a)
					info.append(n + "\n");
				else	
					info.append("                        " + n + "\n");
				a = false;	
			}
			connection.close();
		}
		catch (Exception e) {
			System.err.println("Error: " + e);
			info.setLength(0);
			info.append("err");
			info.append(e);
		}
		info.append("#");
		return info.toString();
	}
	
	public Vector<String> printInfo(String d, String n, String a, String t)
	{
		final String DATABASE_NAME = "reg.sqlite";
		Vector<String> info = new Vector<String>();		
		try
		{ 
			File databaseFile = new File(DATABASE_NAME);
			if (! databaseFile.isFile())
				throw new Exception("Database connection failed");
					
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			
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
				String classid = resultSet.getString("classid");
				String dept = resultSet.getString("dept");
				String coursenum = resultSet.getString("coursenum");
				String area = resultSet.getString("area");
				String title = resultSet.getString("title");
				info.add(String.format("%5s  %4s  %4s  %3s  %s",classid, dept, coursenum, area, title));
			}
			connection.close();
		}
		catch (Exception e) {info.add("err"); info.add(e.toString()); System.err.println("Error: " + e);}
		return info;
	}
}

public class RegServer
{
	public static boolean isInt(String s) 
	{
		try 
		{ 
			Integer.parseInt(s); 
		} 
		catch (NumberFormatException e) {return false;} 
		catch (NullPointerException e) {return false;}
		return true;
	}
	
	public static void main(String[] args) 
	{
		if (args.length != 1)
		{
			System.err.println("Usage: regserver port");
			System.exit(1);
		}
		if (!isInt(args[0]))
		{
			System.err.println("Port must be an integer");
			System.exit(1);
		}
		
		try 
		{
			int port = Integer.parseInt(args[0]);
			
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Opened server socket");
			
			while (true)
			{  
				Socket socket = serverSocket.accept();
				SocketAddress clientAddr = socket.getRemoteSocketAddress();
					
				System.out.println("Accepted connection, Opened socket");
				EchoThread echoThread = new EchoThread(socket, clientAddr);
				echoThread.start();
			}
		} 
		catch (Exception e) {System.err.println(e);System.exit(1);}
	}
}