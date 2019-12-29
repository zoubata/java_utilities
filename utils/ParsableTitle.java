/**
 * 
 */
package com.zoubworld.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pierre Valleau
 *
 */
public class ParsableTitle implements IParsable {

	/**
	 * @param mySymbol
	 * @param mykey
	 */
	public ParsableTitle(String myMatch) {
		super();
		// TODO Auto-generated constructor stub
		match=myMatch;
	}
	/**
	 * select item from ps that match this object.
	*/
	public Set<IParsable> select( Set<IParsable> ps)
	{
		Set<IParsable> set=new HashSet<IParsable>();
		for(IParsable s:ps)
			if ( s instanceof ParsableTitle) 
			if (((ParsableTitle)s).match==match) 
				set.add(s);
		return set;
		
	}
	protected String match=null;//"\\s*[(.*)]\\s*";
	public String toString()
	{
		return "ParsableTitle("+match+")";
	}
	/**
	 * @return the p0
	 */
	public Pattern getP0() {
		if (p0==null)
		p0=Pattern.compile(match);
		return p0;
	}

	String value=null;
	@SuppressWarnings("unused")
	private ParsableTitle()
	{
		super();
	}
	
	protected Pattern p0=null;

		public static List<String> GeList(List<IParsable> classList)
		{
			if (classList==null)
				return null;
			List<String> map=new ArrayList<String>();
			for(IParsable ps:classList)
				map.add( ( ps).get());
			return map;
			
		}
	public IParsable Parse(String line) {
		
			
		Matcher m=null;
		 if ((m=getP0().matcher(line))!=null)
		 if ((m).find())    		 
	 {
			 ParsableTitle tc; 	
			 if (this instanceof ParsableSymbol) 
			 {
				 ParsableSymbol new_name = (ParsableSymbol) this;
				tc= new ParsableSymbol(new_name.symbol,new_name.key);
				
			}
			 else tc= new ParsableTitle(this.match); 
			 tc.value=m.group(1);
			
		return tc;
		}
		return null;
	}

	public String get() {
		return value;
	}
	@Override
	/** return [value]
	 * */
	public List<String> getList() {
		List<String> l=new ArrayList<String>();
		
		l.add(get());		
		return l;
	}	

}
