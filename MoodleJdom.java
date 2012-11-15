import org.jdom2.*;
import org.jdom2.input.*;
import java.io.*;
import java.util.*;

public class MoodleJdom {

    public static void processUrl(Element activity) throws JDOMException, IOException {
	String modulename = activity.getChildText("modulename");
	String title = activity.getChildText("title");
	String directory = activity.getChildText("directory");

	File file = new File("dump/" + directory + "/url.xml");
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
	Element subactivity = doc.getRootElement();
	Element url = subactivity.getChild("url");
	String externalurl = url.getChildText("externalurl");
	System.out.println(modulename + ": " + title + " " + externalurl);
    }


    public static void processAssign(Element activity) throws JDOMException, IOException {
	String modulename = activity.getChildText("modulename");
	String title = activity.getChildText("title");
	String directory = activity.getChildText("directory");

	File file = new File("dump/" + directory + "/assign.xml");
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
	Element subactivity = doc.getRootElement();
	Element assign = subactivity.getChild("assign");
	String name = assign.getChildText("name");
	String intro = assign.getChildText("intro");
	String duedate = assign.getChildText("duedate");

	System.out.println(modulename + ": " + title + " " + name);
	System.out.println(new Date(Long.parseLong(duedate)*1000));
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
	    if (modulename.equals("forum"))
		// forums don't go on my public webpage
		continue;
	    else if (modulename.equals("url"))
		processUrl(activity);
	    else if (modulename.equals("assign"))
		processAssign(activity);
	}
    }
}
