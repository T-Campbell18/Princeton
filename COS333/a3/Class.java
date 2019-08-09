public class Class
{
	private String classid;
	private String dept;
	private String coursenum;
	private String area;
	private String title;
	
	public Class(String c, String d, String n, String a, String t)
	{
		classid = c;
		dept = d;
		coursenum = n;
		area = a;
		title = t;
	}
	
	public String getId() {return classid;}
	public String getDept(){return dept;}
	public String getNum() {return coursenum;}
	public String getArea() {return area;}
	public String getTitle() {return title;}

}