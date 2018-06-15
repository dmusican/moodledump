// HTML blocks don't backup. Stop using them for links in the future,
// to make conversion of them easier.

package edu.carleton.dmusicant;

import org.jdom2.*;
import org.jdom2.input.*;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import java.io.*;
import java.util.*;

public class MoodleJdom {


   private static String inputRootDirectory;
   private static String outputRootDirectory;

   public static boolean visible(Element activity) throws JDOMException, IOException {
      String modulename = activity.getChildText("modulename");
      String moduleid = activity.getChildText("moduleid");
      String title = activity.getChildText("title");
      String directory = activity.getChildText("directory");

      File file = new File(inputRootDirectory + directory + "/module.xml");
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(file);
      Element module = doc.getRootElement();
      String visible = module.getChildText("visible");
      if (visible.equals("1"))
         return true;
      else
         return false;
   }


    public static String generateHtml(String text, String format) {
      // If intro is in markdown format, reformat as HTML. Moodle uses a 4
      // to mean markdown.
      if (format.equals("4")) {
         Parser parser = Parser.builder().build();
         Node document = parser.parse(text);
         HtmlRenderer renderer = HtmlRenderer.builder().build();
         return renderer.render(document);
      } else {
          return text;
      }
    }
    
   public static void processUrl(PrintWriter out, Element activity) throws JDOMException, IOException {
      String modulename = activity.getChildText("modulename");
      String moduleid = activity.getChildText("moduleid");
      String title = activity.getChildText("title");
      String directory = activity.getChildText("directory");

      File file = new File(inputRootDirectory + directory + "/url.xml");
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
      String directory = activity.getChildText("directory");

      File file = new File(inputRootDirectory + directory + "/label.xml");
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(file);
      Element subactivity = doc.getRootElement();
      Element assign = subactivity.getChild("label");
      String intro = assign.getChildText("intro");
      String introFormat = assign.getChildText("introformat");

      intro = generateHtml(intro, introFormat);


      // String modulename = activity.getChildText("modulename");
      // String moduleid = activity.getChildText("moduleid");
      // String title = activity.getChildText("title");
      out.println("<dt>" + intro + "</dt>");
   }


   public static void processAssign(PrintWriter out, Element activity) throws JDOMException, IOException {
      String modulename = activity.getChildText("modulename");
      String moduleid = activity.getChildText("moduleid");
      String title = activity.getChildText("title");
      String directory = activity.getChildText("directory");

      File file = new File(inputRootDirectory + directory + "/assign.xml");
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(file);
      Element subactivity = doc.getRootElement();
      Element assign = subactivity.getChild("assign");
      String intro = assign.getChildText("intro");
      String duedate = assign.getChildText("duedate");
      String introFormat = assign.getChildText("introformat");

      intro = generateHtml(intro, introFormat);

      PrintWriter assignout = new PrintWriter(new FileWriter(outputRootDirectory + "assigns/assign" + moduleid + ".html"));

      InputStream inputStream = 
         Thread.currentThread().getContextClassLoader().getResourceAsStream("assigntemplate.html");
      Scanner template = new Scanner(inputStream);

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

   public static void processLongLabel(PrintWriter out, Element activity) throws JDOMException, IOException {
      String modulename = activity.getChildText("modulename");
      String moduleid = activity.getChildText("moduleid");
      String title = activity.getChildText("title");
      String directory = activity.getChildText("directory");

      File file = new File(inputRootDirectory + directory + "/label.xml");
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(file);
      Element subactivity = doc.getRootElement();
      Element assign = subactivity.getChild("label");
      //String title = assign.getChildText("name");
      String intro = assign.getChildText("intro");
      String introFormat = assign.getChildText("introformat");

      intro = generateHtml(intro, introFormat);

      PrintWriter assignout = new PrintWriter(new FileWriter(outputRootDirectory + "labels/label" + moduleid + ".html"));

      InputStream inputStream = 
         Thread.currentThread().getContextClassLoader().getResourceAsStream("labeltemplate.html");
      Scanner template = new Scanner(inputStream);

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

      out.println("<dt><a href=\"labels/label" + moduleid + ".html\">" + title + "</a></dt>");
   }


   public static void processPage(PrintWriter out, Element activity) throws JDOMException, IOException {
      String modulename = activity.getChildText("modulename");
      String moduleid = activity.getChildText("moduleid");
      String title = activity.getChildText("title");
      String directory = activity.getChildText("directory");

      File file = new File(inputRootDirectory + directory + "/page.xml");
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(file);
      Element subactivity = doc.getRootElement();
      Element assign = subactivity.getChild("page");
      String name = assign.getChildText("name");
      String content = assign.getChildText("content");
      String contentFormat = assign.getChildText("contentformat");
      content = generateHtml(content, contentFormat);

      PrintWriter assignout = new PrintWriter(new FileWriter(outputRootDirectory + "pages/page" + moduleid + ".html"));

      InputStream inputStream = 
         Thread.currentThread().getContextClassLoader().getResourceAsStream("assigntemplate.html");
      Scanner template = new Scanner(inputStream);

      while (template.hasNextLine()) {
         String line = template.nextLine();
         if (line.equals("|||TITLE|||"))
            assignout.println(name);
         else if (line.equals("|||TEXT|||"))
            assignout.println(content);
         else
            assignout.println(line);
      }
      assignout.close();

      out.println("<dt><a href=\"pages/page" + moduleid + ".html\">" + title + "</a></dt>");
   }



   public static void main(String[] args) throws JDOMException, IOException {

      if (args.length !=2) {
         System.out.println("Command-line arguments: inputDirectory outputDirectory");
         System.exit(1);
      }
      inputRootDirectory = args[0] + "/";
      outputRootDirectory = args[1] + "/";

      SAXBuilder builder = new SAXBuilder();
      File file = new File(inputRootDirectory + "moodle_backup.xml");
      Document doc = builder.build(file);
      Element moodle_backup = doc.getRootElement();
      Element information = moodle_backup.getChild("information");

      String shortname = information.getChildText("original_course_shortname");
      String fullname = information.getChildText("original_course_fullname");
      System.out.println("Short name: "+fullname);

      Element contents = information.getChild("contents");
      Element activities = contents.getChild("activities");

      String prevSectionid = "";
      int weekNum = 0;
      InputStream inputStream = 
         Thread.currentThread().getContextClassLoader().getResourceAsStream("template.html");
      Scanner template = new Scanner(inputStream);
      PrintWriter out = new PrintWriter(new FileWriter(outputRootDirectory + "index.html"));
      while (template.hasNextLine()) {
         String line = template.nextLine();
         if (line.equals("|||COURSETITLE|||"))
            out.println(shortname + ": " + fullname);
         else if (line.equals("|||ASSIGNMENTS|||")) {
            for (Element activity : activities.getChildren()) {
               String modulename = activity.getChildText("modulename");
               String sectionid = activity.getChildText("sectionid");
               String title=activity.getChildText("title");
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
               else if (modulename.equals("lti"))
                  // lti, which is used for Piazza links, don't go public
                  continue;
               else if (modulename.equals("wiki"))
                  // wiki, which is used for wiki links, ignore
                  continue;
               else if (modulename.equals("scheduler")) {
                  // scheduled appointments don't go on public webpage
                  continue;
               }
               else if (modulename.equals("choice")) {
                  // choice is a survey, which doesn't go on public webpage
                  continue;
               }
               else if (modulename.equals("resource"))
                  // resources are files I put in Moodle, I hardly ever
                  // use them, and not worth the trouble
                  continue;
               else if (modulename.equals("url"))
                  processUrl(out,activity);
               else if (modulename.equals("assign"))
                  processAssign(out,activity);
               else if (modulename.equals("label")) {
                  //System.out.println(title.substring(title.length()-3));
                  if (title.substring(title.length()-3).equals("..."))
                     processLongLabel(out,activity);
                  else
                     processLabel(out,activity);
                                
               }
               else if (modulename.equals("page")) {
                  processPage(out,activity);
               }
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

