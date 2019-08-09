import spark.Spark;
import spark.Request;
import spark.Response;
import spark.template.velocity.VelocityTemplateEngine;
import spark.ModelAndView;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

public class Web
{
	private static String search(Request req, Response res)
			{
				Map<String, Object> model = new HashMap<>();
				ModelAndView mv = new ModelAndView(model, "temp.vtl");
				return new VelocityTemplateEngine().render(mv);
			}

	private static String searchClasses(Request req, Response res)
	{
		String errorMsg = null;
		String ret = "";
		String dept = req.queryParams("dept");
		String coursenum = req.queryParams("num");
		String area = req.queryParams("area");
		String title = req.queryParams("title");



		ArrayList<Class> classes = null;
		try
		{
			Reg database = new Reg();
			database.connect();
			classes = database.search(dept,coursenum,area,title);
			database.disconnect();
		}
		catch (Exception e)
		{
			errorMsg = e.toString();
			System.err.println(e);
		}

		Map<String, Object> model = new HashMap<>();
		model.put("classes", classes);
		if (dept == null)
			model.put("dept", "");
		else
			model.put("dept", dept);

		if (coursenum == null)
			model.put("num", "");
		else
			model.put("num", coursenum);

		if (area == null)
			model.put("area", "");
		else
			model.put("area", area);

		if (title == null)
			model.put("title","");
		else
			model.put("title", title);


		if (errorMsg != null)
			model.put("errorMsg", errorMsg);

		ret +=
		"<table class=\"w3-table w3-hoverable w3-pale-blue\">" +
				"<tr >" +
					"<th align=\"left\" >ClassId</th>" +
					"<th align=\"left\">Dept</th>" +
					"<th align=\"left\">Num</th>" +
					"<th align=\"left\">Area</th>" +
					"<th align=\"left\">Title</th>" +
				"</tr>";

			for (Class c : classes)
			{
				ret += "<tr  class=\"w3-card\">";
					ret += "<td><a href=\"classInfo?classid=" + c.getId()  + "\" target = \" _blank\">"+ c.getId() +"</a></td>";
					ret += "<td>" + c.getDept()  + "</td>";
					ret += "<td>" + c.getNum()  + "</td>";
					ret += "<td>" + c.getArea()  + "</td>";
					ret += "<td>" + c.getTitle()  + "</td>";
					ret += "</tr>";
			}

		ret += "</table>";
		return ret;
	}

	private static String classInfo(Request req, Response res)
		{
			String errorMsg = null;

			String classid = req.queryParams("classid");

			if ((classid == null) || (classid.trim().equals("")))
			{
				errorMsg = "Missing class id";
				System.err.println("Missing class id");
			}
			else if (!isInt(classid))
			{
				errorMsg = "Class id is not numeric";
				System.err.println("Classid is not numeric");
			}

			Course ci = null;
			if (errorMsg == null)
			{
				try
				{
					Reg database = new Reg();
					database.connect();
					if (!database.validID(classid))
					{
						errorMsg = "Class id " + classid + " does not exist";
						System.err.println("Class id " + classid + " does not exist");
					}
					ci = database.classSearch(classid);
					database.disconnect();
				}
				catch (Exception e)
				{
					errorMsg = e.toString();
					System.err.println(e);
				}
			}


			String prevDept = req.cookie("prevDept");
			String prevNum = req.cookie("prevNum");
			String prevArea = req.cookie("prevArea");
			String prevTitle = req.cookie("prevTitle");

			Map<String, Object> model = new HashMap<>();
			model.put("course", ci);
			if (prevDept == null)
				prevDept = "";
			model.put("prevDept", prevDept);

			if (prevNum == null)
				prevNum = "";
			model.put("prevNum", prevNum);

			if (prevNum == null)
				prevNum = "";
			model.put("prevArea", prevArea);

			if (prevTitle == null)
				prevTitle = "";
			model.put("prevTitle", prevTitle);
			if (errorMsg != null)
				model.put("errorMsg", errorMsg);
			ModelAndView mv = new ModelAndView(model, "classinfo.vtl");
			return new VelocityTemplateEngine().render(mv);
		}


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
			System.err.println("Usage: runserver port");
			System.exit(1);
		}
		if (!isInt(args[0]))
		{
			System.err.println("port must be an integer");
			System.exit(1);
		}

		Spark.port(Integer.parseInt(args[0]));

		Spark.get("/",
			(req, res) -> search(req, res)
		);

		Spark.get("/searchClasses",
			(req, res) -> searchClasses(req, res)
		);

		Spark.get("/classInfo",
			(req, res) -> classInfo(req, res)
		);

	}
}