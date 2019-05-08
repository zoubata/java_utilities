package com.zoubworld.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zoubworld.utils.ExtractList;
import com.zoubworld.utils.IParsable;


/** generics parsable class
 * 
 * used with ParseFile(List<IParsable> classList, String filename)
 * 
 * example it can parse : 
$var wire 1 # RESET $end


 * */
public class ExtractList implements IParsable {

	public ExtractList(String mymatch) {
		super();
		match=mymatch;
	}
	public String toString()
	{
		return "ExtractList("+match+")";
	}
	/**
	 * @return the p0
	 */
	public Pattern getP0() {
		if (p0==null)
		p0=Pattern.compile(match);
		return p0;
	}
String match="";
//key index
	List<String> value=null;
	
	private ExtractList()
	{
		super();
	}
	
	private Pattern p0=null;
	/**
	 * select item from ps that match this object.
	*/
	public Set<IParsable> select( List<IParsable> ps)
	{
		Set<IParsable> set=new HashSet<IParsable>();
		for(IParsable s:ps)
			if ( s instanceof ExtractList) 
			if ( (((ExtractList)s).match==match))
				set.add(s);
		return set;		
	}

	/*
		public static Map<String,List<String>> GetMap(List<IParsable> classList)
		{
			if (classList==null)
				return null;
			Map<String,List<String>> map=new HashMap<String,List<String>>();
			for(IParsable ps:classList)
				if ( ps instanceof ExtractList) 
					if(((ExtractList)ps).match!=null)
						if(((ExtractList)ps).value!=null)
							if(map.containsKey(((ExtractList)ps).key))
							{
								List<String> l=new ArrayList<String>();
								l.addAll(((ExtractList)ps).value);
								l.addAll(map.get(((ExtractList)ps).key));
								map.put(((ExtractList)ps).key, l);	
							}
							else
				map.put(((ExtractList)ps).key, ((ExtractList)ps).value);
			return map;
			
		}*/
	public IParsable Parse(String line) {
		
			
		Matcher m=null;

		 if ((m=getP0().matcher(line))!=null)
		 if ((m).find())    		 
	 {

			 ExtractList tc= new ExtractList(match); 	
			 if (tc.value==null)
			 	tc.value=new ArrayList<String>();
			
			 for(int i=1;i<m.groupCount();i++)
				 tc.value.add(m.group(i).trim());
			
		return tc;
		}
		return null;
	}

	public List<String> getValue() {
		return value;
	}
/*	public String getSymbol() {
		return symbol;
	}*/
	public String getKey() {
		return match;
	}
	@Override
	public String get() {
		// TODO Auto-generated method stub
		return getValue().toString();
	}




}