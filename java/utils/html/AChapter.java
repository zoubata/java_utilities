/**
 * 
 */
package com.zoubworld.java.utils.html;

/**
 * @author zoubata
 *
 */
public class AChapter  extends HtmlObject {
	
	/**
	 * 
	 */
	public AChapter() {
		// TODO Auto-generated constructor stub
	}
	public AChapter(String title, String contain) {
		this.Title=title;
		this.contain=contain;
	}
	public AChapter( String contain) {

		this.contain=contain;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IHtmlObject o=new AChapter();
		o.setTitle("tiltle");
		o.setContain("the contains");
		
		System.out.println(o.toHtml());

	}

	@Override
	public String toHtml() {
		String tmp= "<p>"				
				+ "\r\n";
		if (getTitle()!=null)
			tmp+="<h1>"+getTitle()+"</h1>"+"\r\n";
		if (getContain()!=null)
			tmp+=""+getContain()+"\r\n";
		
		/*
		 *  <ul>
  <li>Coffee</li>
  <li>Tea</li>
  <li>Milk</li>
</ul> 
*/
		for(IHtmlObject a:getElements())
			tmp+=a.toHtml()+ "\r\n";
		tmp+="</p>";
		return tmp;
	}

}
