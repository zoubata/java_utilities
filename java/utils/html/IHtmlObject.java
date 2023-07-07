/**
 * 
 */
package com.zoubworld.java.utils.html;

import java.util.List;

/**
 * @author M43507
 *
 */
public interface IHtmlObject {


	/**
	 * @return the title
	 */
	public String getTitle() ;
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title);
	/**
	 * @return the contain
	 */
	public String getContain();
	
	
	/**
	 * @param contain the contain to set
	 */
	public void setContain(String contain);
	 /**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(IHtmlObject e);
	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) ;
	/**
	 * @return the elements
	 */
	public List<IHtmlObject> getElements();
	
	/** return the HTML page
	 * */
	public String toHtml() ;
}
