import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.File;
import java.util.LinkedList;

public class GetByArg 
{
	//static Connection connection;
	static String DATABASE_NAME;
	static File databaseFile;

	GetByArg() {
		DATABASE_NAME = "reg.sqlite";
		databaseFile = new File (DATABASE_NAME);
	}
	/*
	private void getAllClassDetails(String classid, Connection connection) {
		try {
			String courseidString = ("SELECT courseid FROM classes WHERE classid = ?");
			PreparedStatement courseStatement = connection.prepareStatement(courseidString);
			courseStatement.setString(1, classid);
			ResultSet resultSetCourseId = courseStatement.executeQuery();
			String courseid = resultSetCourseId.getString("courseid");
			
			String profidString = ("SELECT profid FROM courseprofs WHERE courseid = ?");
			PreparedStatement profStatement = connection.prepareStatement(profidString);
			profStatement.setString(1, courseid);
			ResultSet resultSetProfId = profStatement.executeQuery();
			LinkedList<String> profList = new LinkedList<String>; 
			while (resultSetProfId.next()) {
				profList.add(resultSetProfId.getString("profid"));
			}
			
			// profList contains profids for all relevant professors
			
			
			// fix this statement, see if i can just add the profids with AND shit, if not do same as ^ for
			// relevant fields
			String stmtStr = 
				("SELECT courseid, days, starttime, endtime, bldg, roomnum, dept, coursenum, area, title,"
				+ "descrip, prereqs, profname " + 
				"FROM classes, courses, courseprofs, crosslistings, profs "
				+ "WHERE classes.classid = ? AND courses.classid = ? AND courseprofs.classid = ? AND "
				+ "crosslistings.classid = ? AND profs.profid = ?");
			
			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1, classid);
			statement.setString(2, classid);
			statement.setString(3, classid);
			statement.setString(4, classid);
			statement.setString(5, profid);
		}
	}
*/
	private void getClassDataByCourseId(String courseid, Connection connection) {
		try {
			
			String stmtStr = ("SELECT classid, area, title, coursenum, dept " + 
				"FROM classes, courses, crosslistings " +
				"WHERE classes.courseid = ? AND courses.courseid = ? AND "
				+ "crosslistings.courseid = ?"); 
		
			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1,  courseid);
			statement.setString(2,  courseid);
			statement.setString(3,  courseid);
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String classid = resultSet.getString("classid");
				String area = resultSet.getString("area");
				String title = resultSet.getString("title");
				String coursenum = resultSet.getString("coursenum");
				String dept = resultSet.getString("dept");

				// FORMAT AND SORT CORRECTLY
				System.out.print(classid + " " + dept + " "+ coursenum + " " 
					+ area + " " + title +"\n" );
			}
		}
		catch (Exception e) { System.err.println(e); }
		
	}
	
	private void getByArea(String areaString) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			String stmtStr = ("SELECT courseid " + 
				"FROM courses " + 
				"WHERE area = ?");
			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1, areaString.toUpperCase());
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String courseid = resultSet.getString("courseid");
				getClassDataByCourseId(courseid, connection);
			}
			connection.close();
		}
		catch (Exception e) { System.err.println(e); }
	}
	
	private void getByCourseNum(String courseNumString) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			String stmtStr = ("SELECT courseid " +
				"FROM crosslistings " + 
				"WHERE crosslistings.coursenum LIKE ?");

			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1, "%" + courseNumString + "%");

			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String courseid = resultSet.getString("courseid");
				getClassDataByCourseId(courseid, connection);
			}
			connection.close();
		}
		catch (Exception e) { System.err.println(e); }
	}
	
	private void getByTitle(String titleString) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			String stmtStr = ("SELECT courseid " +
				"FROM courses " + 
				"WHERE title LIKE ? COLLATE NOCASE");

			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1, "%" + titleString + "%");

			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String courseid = resultSet.getString("courseid");
				getClassDataByCourseId(courseid, connection);
			}
			connection.close();
		}
		catch (Exception e) { System.err.println(e); }
	}
	
	private void getByDepartment(String departmentString) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			String stmtStr = ("SELECT courseid " +
				"FROM crosslistings " + 
				"WHERE dept = ?");

			PreparedStatement statement = connection.prepareStatement(stmtStr);
			statement.setString(1, departmentString);

			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String courseid = resultSet.getString("courseid");
				getClassDataByCourseId(courseid, connection);
			}
			connection.close();
		}
		catch (Exception e) { System.err.println(e); }
	}

	public static void main(String args[]) {
		GetByArg argThing = new GetByArg();
		argThing.getByDepartment("COS");
		System.out.print("\n \n \nArea \n");
		argThing.getByArea("ST");
		System.out.print("\n \n \nTitle \n");
		argThing.getByTitle("Alg");
		System.out.print("\n \n \nCourseNum \n");
		argThing.getByCourseNum("333");
	}
}