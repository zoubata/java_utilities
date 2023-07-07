/**
 * 
 */
package com.zoubworld.Crypto.server.Http;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zoubworld.Crypto.server.Server;
import com.zoubworld.utils.JavaUtils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
/**
 * @author zoubata
 *  build a basic Html page
 */
public class APage {
	List<APage> child=new ArrayList<APage>();
	static public String BR="<br>\r\n";
	static public String Header(int i,String s)
	{return "<h"+i+">"+s+"</h"+i+">\r\n";}
	static public String FOOTER(String s)
	{return "<footer>\r\n"
			+ "	  <p>"+s+"</p>\r\n"
			+ "	</footer>";}
	
	
	
	/**
	 * 
	 */
	public APage() {
		// TODO Auto-generated constructor stub
	}

	public APage(String title, String body, String filename) {
		this.body=body;
		this.title=title;
		this.filename=filename;
	}

	protected String title="";
	protected String body="\r\n"
			+ "<h1>This is a Heading</h1>\r\n"
			+ "<p>This is a paragraph.</p>\r\n"
			+ " \r\n"
			;
	public String filename;
	static public String URL(String brief,String Url)
	{
		return " "+" <a href=\""+Url+"\">"+brief+"</a> \r\n";
	}
	public void addUrl(String brief,String Url)
	{
		body+=URL( brief, Url);
	}
	
	static public String IMAGE(String BriefOfImage,String filename)
	{
		String t="";
		if(filename.startsWith("htt"))
			t+=" "+" <img src=\""+filename+"\" alt=\""+BriefOfImage+"\">\r\n";
		else
		t+=" "+" <img src=\"image/"+filename+"\" alt=\""+BriefOfImage+"\">\r\n";
		return t;
	}
	public void addImg(String BriefOfImage,String filename)
	{	
		body+=IMAGE( BriefOfImage, filename);
	}
	
	public void addUML(String diagram)
	{
		body+= UML( diagram);
	
	}
	/**
	 * https://mermaid-js.github.io/mermaid/#/./n00b-gettingStarted*
	 * example : https://mermaid-js.github.io/mermaid/#/
	 * */
	static public String UML(String diagram)
	{
		return "<pre class=\"mermaid\">\r\n"+diagram+"\r\n</pre>\r\n";
	}
	/** katex mathematical expressions
	 * 
	 * https://katex.org/docs/supported.html#math-operators
	 * */
	public void addEQUATION(String LaTeXexpressions)
	{
		body+= EQUATION( LaTeXexpressions);
	
	}
	static public String EQUATION(String LaTeXexpressions)
	{
		return "<p>$$\r\n"+LaTeXexpressions+"\r\n$$</p>\r\n";
	}
	
	public String toString()
	{
		String s="<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<title>"+title+"</title>\r\n"
				+ getStyle()
				+ HeadKaTeXrender()
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ Bodymermaid()
				+ getBody() 
				+ FOOTER(getRev())
				
				+ "</body>\r\n"
				+ "</html> ";
		return s;
	}
	/** to support equation on KaTeX 
	 * see https://github.com/KaTeX/KaTeX
	 * */
private String HeadKaTeXrender()
{
	return  "<!-- KaTeX requires the use of the HTML5 doctype. Without it, KaTeX may not render properly -->\r\n"
			+ "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/katex@0.16.3/dist/katex.min.css\" integrity=\"sha384-Juol1FqnotbkyZUT5Z7gUPjQ9gzlwCENvUZTpQBAPxtusdwFLRy382PSDx5UUJ4/\" crossorigin=\"anonymous\">\r\n"
			+ "\r\n"
			+ "    <!-- The loading of KaTeX is deferred to speed up page rendering -->\r\n"
			+ "    <script defer src=\"https://cdn.jsdelivr.net/npm/katex@0.16.3/dist/katex.min.js\" integrity=\"sha384-97gW6UIJxnlKemYavrqDHSX3SiygeOwIZhwyOKRfSaf0JWKRVj9hLASHgFTzT+0O\" crossorigin=\"anonymous\"></script>\r\n"
			+ "\r\n"
			+ "    <!-- To automatically render math in text elements, include the auto-render extension: -->\r\n"
			+ "    <script defer src=\"https://cdn.jsdelivr.net/npm/katex@0.16.3/dist/contrib/auto-render.min.js\" integrity=\"sha384-+VBxd3r6XgURycqtZ117nYw44OOcIax56Z4dCRWbxyPt0Koah1uHoK0o4+/RRE05\" crossorigin=\"anonymous\"\r\n"
			+ "        onload=\"renderMathInElement(document.body);\"></script>\r\n"
			+ "";
	}
/** to support UML diagrams using Mermaid
 * see https://stackedit.io/app#
 * https://mermaid-js.github.io/mermaid/#/./n00b-gettingStarted
 * on md convertion : 
 * need to replace section "```mermaid ... ```" by "<pre class="mermaid"> ... </pre>"
 * and revert any processing like "&gt;" to ">" or "</p><p>" to "\n"
 * https://github.com/mermaid-js/mermaid/blob/develop/docs/misc/integrations.md
 * */
private String Bodymermaid()
{
	return "  <script type=\"module\">\r\n"
+ "    import mermaid from 'https://unpkg.com/mermaid@9/dist/mermaid.esm.min.mjs';\r\n"
+ "    mermaid.initialize({ startOnLoad: true });\r\n"
+ "  </script>";
}

	private String getStyle() {
		// TODO Auto-generated method stub
	/*	return "<style>\r\n"
				+ "body {background-color: powderblue;}\r\n"
				+ "h1   {color: blue;}\r\n"
				+ "p    {color: red;}\r\n"
				+ "</style>\r\n"
				+ "";*/
				return
		"<style>\r\n"
		+ "* {\r\n"
		+ "  box-sizing: border-box;\r\n"
		+ "}\r\n"
		+ "\r\n"
		+ "body {\r\n"
		+ "  font-family: Arial, Helvetica, sans-serif;\r\n"
		+ "}\r\n"
		+ "\r\n"
		+ "/* Style the header */\r\n"
		+ "header {\r\n"
		+ "  background-color: #666;\r\n"
		+ "  padding: 30px;\r\n"
		+ "  text-align: center;\r\n"
		+ "  font-size: 35px;\r\n"
		+ "  color: white;\r\n"
		+ "}\r\n"
		+ "\r\n"
		+ "/* Create two columns/boxes that floats next to each other */\r\n"
		+ "nav {\r\n"
		+ "  float: left;\r\n"
		+ "  width: 30%;\r\n"
		+ "  background: #5d9ab2 url(\"https://www.w3schools.com/css/img_tree.png\") no-repeat top left;\r\n"
		+ "  height: 300px; /* only for demonstration, should be removed */\r\n"
		+ "  background: #ccc;\r\n"
		+ "  padding: 20px;\r\n"
		+ "}\r\n"
		+ "\r\n"
		+ "/* Style the list inside the menu */\r\n"
		+ "nav ul {\r\n"
		+ "  list-style-type: none;\r\n"
		+ "  padding: 0;\r\n"
		+ "}\r\n"
		+ "\r\n"
		+ "article {\r\n"
		+ "  float: left;\r\n"
		+ "  padding: 20px;\r\n"
		+ "  width: 70%;\r\n"
		+ "  background-color: #f1f1f1;\r\n"
		+ "  height: 300px; /* only for demonstration, should be removed */\r\n"
		+ "}\r\n"
		+ "\r\n"
		+ "/* Clear floats after the columns */\r\n"
		+ "section::after {\r\n"
		+ "  content: \"\";\r\n"
		+ "  display: table;\r\n"
		+ "  clear: both;\r\n"
		+ "}\r\n"
		+ "\r\n"
		+ "/* Style the footer */\r\n"
		+ "footer {\r\n"
		+ "  background-color: #777;\r\n"
		+ "  padding: 10px;\r\n"
		+ "  text-align: center;\r\n"
		+ "  color: white;\r\n"
		+ "}\r\n"
		+ "\r\n"
		+ "/* Responsive layout - makes the two columns/boxes stack on top of each other instead of next to each other, on small screens */\r\n"
		+ "@media (max-width: 600px) {\r\n"
		+ "  nav, article {\r\n"
		+ "    width: 100%;\r\n"
		+ "    height: auto;\r\n"
		+ "  }\r\n"
		+ "}\r\n"
		+ "</style>";
		/*
		return "<style>\r\n"
				+ "body {\r\n"
				+ "  margin-left: 200px;\r\n"
				+ "  background: #5d9ab2 url(\"https://www.w3schools.com/css/img_tree.png\") no-repeat top left;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".center_div {\r\n"
				+ "  border: 1px solid gray;\r\n"
				+ "  margin-left: auto;\r\n"
				+ "  margin-right: auto;\r\n"
				+ "  width: 90%;\r\n"
				+ "  background-color: #d0f0f6;\r\n"
				+ "  text-align: left;\r\n"
				+ "  padding: 8px;\r\n"
				+ "}\r\n"
				+ "</style>";
		/*
		return "<style>\r\n"
				+ "h1 {\r\n"
				+ "  color: blue;\r\n"
				+ "  font-family: verdana;\r\n"
				+ "  font-size: 300%;\r\n"
				+ "}\r\n"
				+ "body {\r\n"
				+ "  background-color: powderblue;\r\n"
				+ "}\r\n"
				+ "h1 {\r\n"
				+ "  color: blue;\r\n"
				+ "}\r\n"
				+ "p {\r\n"
				+ "  color: red;\r\n"
				+ "}\r\n"
				+ "p {\r\n"
				+ "  color: red;\r\n"
				+ "  font-family: courier;\r\n"
				+ "  font-size: 160%;\r\n"
				+ "}\r\n"
				+ "</style>\r\n";
		/*
		return " <link rel=\"stylesheet\" href=\"styles.css\">\r\n";*/
	}

	public static DateTimeFormatter dateformat=DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	/** return html information about revision of the page
	 * */
	private String getRev() {
		
		LocalDateTime now = LocalDateTime.now();
		
		return "<p>page generated by "+this.getClass().getCanonicalName()+", the "+dateformat.format(now)+ " on "+Server.getthis().getAcc().getId12s()+" Server </p>";
	}

	/** return the body of the page
	 * */
	private String getBody() {
		
		return body;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String body=""; 
	APage a=new APage("title",body,"index.html");
	
	a.addUML(" graph TD \r\n"
			+ "        A[Client] --> B[Load Balancer] \r\n"
			+ "        B --> C[Server01] \r\n"
			+ "        B --> D[Server02]");
	a.addUML("graph LR\r\n"
	+ "A[Square Rect] -- Link text --> B((Circle))\r\n"
	+ "A --> C(Round Rect)\r\n"
	+ "B --> D{Rhombus}\r\n"
	+ "C --> D");
	a.addUML(
	"sequenceDiagram\r\n"
	+ "Alice ->> Bob: Hello Bob, how are you?\r\n"
	+ "Bob-->>John: How about you John?\r\n"
	+ "Bob--x Alice: I am good thanks!\r\n"
	+ "Bob-x John: I am good thanks!\r\n"
	+ "Note right of John: Bob thinks a long<br/>long time, so long<br/>that the text does<br/>not fit on a row.\r\n"
	+ "\r\n"
	+ "Bob-->Alice: Checking with John...\r\n"
	+ "Alice->John: Yes... John, how are you?");
	a.addUML(
	"gantt\r\n"
	+ "dateFormat  YYYY-MM-DD\r\n"
	+ "title Adding GANTT diagram to mermaid\r\n"
	+ "excludes weekdays 2014-01-10\r\n"
	+ "\r\n"
	+ "section A section\r\n"
	+ "Completed task            :done,    des1, 2014-01-06,2014-01-08\r\n"
	+ "Active task               :active,  des2, 2014-01-09, 3d\r\n"
	+ "Future task               :         des3, after des2, 5d\r\n"
	+ "Future task2               :         des4, after des3, 5d");
	a.addEQUATION("\\Gamma(z) = \\int_0^\\infty t^{z-1}e^{-t}dt\\,.");
	a.save("c:\\temp\\");
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public void save(String Path) {
		JavaUtils.saveAs(Path +getFilename(),toString());
		for(APage p:child)
			if (p!=this)
				   p.save(Path);	
	}
	public void add(APage page) {
		
		body+=BR;
		addUrl(page.title, page.getFilename());
		body+=BR;
		child.add(page);
	}
	public void addbody(String string) {
		body+=BR;
		body+=string;
		body+=BR;
		
	}

}
