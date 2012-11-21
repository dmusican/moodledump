// HTML blocks don't backup. Stop using them for links in the future,
// to make conversion of them easier.

import org.jdom2.*;
import org.jdom2.input.*;
import java.io.*;
import java.util.*;

public class MoodleJdom {

    public static boolean visible(Element activity) throws JDOMException, IOException {
	String modulename = activity.getChildText("modulename");
	String moduleid = activity.getChildText("moduleid");
	String title = activity.getChildText("title");
	String directory = activity.getChildText("directory");

	File file = new File("dump/" + directory + "/module.xml");
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
	Element module = doc.getRootElement();
	String visible = module.getChildText("visible");
	if (visible.equals("1"))
	    return true;
	else
	    return false;
    }


    public static void processUrl(Element activity) throws JDOMException, IOException {
	String modulename = activity.getChildText("modulename");
	String moduleid = activity.getChildText("moduleid");
	String title = activity.getChildText("title");
	String directory = activity.getChildText("directory");

	File file = new File("dump/" + directory + "/url.xml");
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
	Element subactivity = doc.getRootElement();
	Element url = subactivity.getChild("url");
	String externalurl = url.getChildText("externalurl");
	System.out.println(modulename + "/" + moduleid + ": " + title + " " + externalurl);
	System.out.println();
    }

    public static void processLabel(Element activity) throws JDOMException, IOException {
	String modulename = activity.getChildText("modulename");
	String moduleid = activity.getChildText("moduleid");
	String title = activity.getChildText("title");
	System.out.println(modulename + "/" + moduleid + ": " + title);
	System.out.println();
    }


    public static void processAssign(Element activity) throws JDOMException, IOException {
	String modulename = activity.getChildText("modulename");
	String moduleid = activity.getChildText("moduleid");
	String title = activity.getChildText("title");
	String directory = activity.getChildText("directory");

	File file = new File("dump/" + directory + "/assign.xml");
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
	Element subactivity = doc.getRootElement();
	Element assign = subactivity.getChild("assign");
	String intro = assign.getChildText("intro");
	String duedate = assign.getChildText("duedate");

	System.out.println(modulename + "/" + moduleid + ": " + title);
	System.out.println(new Date(Long.parseLong(duedate)*1000));
	System.out.println(intro);
	System.out.println();
    }



    public static void main(String[] args) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        File file = new File("dump/moodle_backup.xml");
        Document doc = builder.build(file);
	Element moodle_backup = doc.getRootElement();
	Element information = moodle_backup.getChild("information");
	Element contents = information.getChild("contents");
	Element activities = contents.getChild("activities");

	for (Element activity : activities.getChildren()) {
	    String modulename = activity.getChildText("modulename");

	    if (!visible(activity))
		// I don't want invisible activies appearing
		continue;

	    else if (modulename.equals("forum"))
		// forums don't go on my public webpage
		continue;
	    else if (modulename.equals("resource"))
		// resources are files I put in Moodle, I hardly ever
		// use them, and not worth the trouble
		continue;
	    else if (modulename.equals("url"))
		processUrl(activity);
	    else if (modulename.equals("assign"))
		processAssign(activity);
	    else if (modulename.equals("label"))
		processLabel(activity);
	    else {
		System.out.println("Error: " + modulename);
		System.exit(1);
	    }
	}

	Scanner template = new Scanner(new File("template.html"));
	
    }
}
