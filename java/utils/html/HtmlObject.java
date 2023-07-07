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
public abstract class HtmlObject implements IHtmlObject {

	List<IHtmlObject> elements=new ArrayList<IHtmlObject>();
	
	 /**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(IHtmlObject e) {
		return elements.add(e);
	}
	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return elements.remove(o);
	}
	/**
	 * @return the elements
	 */
	public List<IHtmlObject> getElements() {
		return elements;
	}
	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		elements.clear();
	}
	String Title;
	 String contain;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return Title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		Title = title;
	}
	/**
	 * @return the contain
	 */
	public String getContain() {
		return contain;
	}
	/**
	 * @param contain the contain to set
	 */
	public void setContain(String contain) {
		this.contain = contain;
	}
	abstract public String toHtml();
	/**
	 * 
	 */
	public HtmlObject() {
		// TODO Auto-generated constructor stub
	}
	public String toString() {
		
		return toHtml();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
