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


    public static void processUrl(PrintWriter out, Element activity) throws JDOMException, IOException {
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
        out.println("<dt><a href=\"" + externalurl + "\">" + title + "</a></dt>");
    }

    public static void processLabel(PrintWriter out, Element activity) throws JDOMException, IOException {
        String modulename = activity.getChildText("modulename");
        String moduleid = activity.getChildText("moduleid");
        String title = activity.getChildText("title");
        out.println("<dt>" + title + "</dt>");
    }


    public static void processAssign(PrintWriter out, Element activity) throws JDOMException, IOException {
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


        PrintWriter assignout = new PrintWriter(new FileWriter("output/assigns/assign" + moduleid + ".html"));

        Scanner template = new Scanner(new File("assigntemplate.html"));

        while (template.hasNextLine()) {
            String line = template.nextLine();
            if (line.equals("|||TITLE|||"))
                assignout.println(title);
            else if (line.equals("|||TEXT|||"))
                assignout.println(intro);
            else
                assignout.println(line);
        }
        assignout.close();

        //System.out.println(modulename + "/" + moduleid + ": " + title);
        //System.out.println(new Date(Long.parseLong(duedate)*1000));
        //System.out.println(intro);
        //System.out.println();


        out.println("<dt><a href=\"assigns/assign" + moduleid + ".html\">" + title + "</a></dt>");
        out.println("<dd>Due " + new Date(Long.parseLong(duedate)*1000) + "</dd>");
    }



    public static void main(String[] args) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        File file = new File("dump/moodle_backup.xml");
        Document doc = builder.build(file);
        Element moodle_backup = doc.getRootElement();
        Element information = moodle_backup.getChild("information");

        String shortname = information.getChildText("original_course_shortname");
        String fullname = information.getChildText("original_course_fullname");
        System.out.println("Short name: "+fullname);

        Element contents = information.getChild("contents");
        Element activities = contents.getChild("activities");

        // for (Element activity : activities.getChildren()) {
        //     String modulename = activity.getChildText("modulename");
            
        //     if (!visible(activity))
        //         // I don't want invisible activies appearing
        //         continue;

        //     else if (modulename.equals("forum"))
        //         // forums don't go on my public webpage
        //         continue;
        //     else if (modulename.equals("resource"))
        //         // resources are files I put in Moodle, I hardly ever
        //         // use them, and not worth the trouble
        //         continue;
        //     else if (modulename.equals("url"))
        //         //processUrl(activity);
        //         continue;
        //     else if (modulename.equals("assign"))
        //         processAssign(activity);
        //     else if (modulename.equals("label"))
        //         processLabel(activity);
        //     else {
        //         System.out.println("Error: " + modulename);
        //         System.exit(1);
        //     }
        // }

        String prevSectionid = "";
        int weekNum = 0;
        Scanner template = new Scanner(new File("template.html"));
        PrintWriter out = new PrintWriter(new FileWriter("output/index.html"));
        while (template.hasNextLine()) {
            String line = template.nextLine();
            if (line.equals("|||COURSETITLE|||"))
                out.println(shortname + ": " + fullname);
            else if (line.equals("|||ASSIGNMENTS|||")) {
                for (Element activity : activities.getChildren()) {
                    String modulename = activity.getChildText("modulename");
                    String sectionid = activity.getChildText("sectionid");
                    if (!sectionid.equals(prevSectionid)) {
                        prevSectionid = sectionid;
                        if (weekNum == 0) {
                            out.println("<h1>General materials</h1>");
                            weekNum++;
                        } else
                            out.println("<h1>Week " + weekNum++ +"</h1>");
                    }
                    
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
                        processUrl(out,activity);
                    else if (modulename.equals("assign"))
                        processAssign(out,activity);
                    else if (modulename.equals("label"))
                        processLabel(out,activity);
                    else {
                        System.out.println("Error: " + modulename);
                        System.exit(1);
                    }
                }
            } else
                out.println(line);
        }
        out.close();
    }
}

