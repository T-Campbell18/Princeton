import java.util.*;

public class Course
{
	private String classid;
	private String courseid;
	private String days;
	private String start;
	private String end;
	private String bldg;
	private String roomnum;
	
	private TreeSet<String> code;
	private String area;
	private String title;
	private String descrip;
	private String prereqs;
	private TreeSet<String> profs;
	
	TreeSet<String> names = new TreeSet<String>();
	
	public Course(String classid, String courseid, String days, String start, String end, 
	String bldg, String roomnum, TreeSet<String> code, String area, String title, 
	String descrip, String prereqs, TreeSet<String> profs)
	{
		this.classid = classid;
		this.courseid = courseid;
		this.days = days;
		this.start = start;
		this.end = end;
		this.bldg = bldg;
		this.roomnum = roomnum;
		this.code = code;
		this.title = title;
		this.area = area;
		this.descrip = descrip;
		this.prereqs = prereqs;
		this.profs = profs;
	}
	
	public String getClassID(){return classid;}
	public String getCourseID(){return courseid;}
	public String getDays(){return days;}
	public String getStart(){return start;}
	public String getEnd(){return end;}
	public String getBldg(){return bldg;}
	public String getRoomnum(){return roomnum;}
	public TreeSet<String> getCode(){return code;}
	public String getTitle(){return title;}
	public String getArea(){return area;}
	public String getDescrip(){return descrip;}
	public String getPrereqs(){return prereqs;}
	public String getProfs(){
		StringBuilder sb = new StringBuilder();
		for (String p : profs)
		{
			sb.append(p);
			sb.append(", ");
		}
		return sb.toString().substring(0, sb.length() -2);
	}
	
	
	public static void main(String[] args) 
	{
	}
}