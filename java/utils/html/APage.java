/**
 * 
 */
package com.zoubworld.java.utils.html;

import java.util.ArrayList;
import java.util.List;

/**
 * @author M43507
 *
 */
public class APage extends HtmlObject{

	
	/**
	 * 
	 */
	public APage() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IHtmlObject o=new APage();
		o.setTitle("tiltle");
		o.setContain("the contains");
		IHtmlObject ac=new AChapter();
		ac.setTitle("tilte1");
		ac.setContain("blabla");
		o.add(ac);
		System.out.println(o.toHtml());

	}
	@Override
	public String toHtml() {
		String tmp= " <!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<body>\r\n"
				+ "\r\n"
				+(getContain()!=null?getContain():"");
				//+ "<h1>My First Heading</h1>\r\n"
				//+ "<p>My first paragraph.</p>\r\n"
		for(IHtmlObject a:getElements())
			tmp+=a.toHtml()+ "\r\n";
				tmp+=	 "\r\n"
				+ "</body>\r\n"
				+ "</html> ";
		return tmp;
	}

}
