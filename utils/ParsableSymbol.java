package com.zoubworld.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zoubworld.utils.IParsable;
import com.zoubworld.utils.ParsableSymbol;
import com.zoubworld.utils.ParsableTitle;


/** generics parsable class
 * 
 * used with ParseFile(List<IParsable> classList, String filename)
 *  the synthaxe is "key Symbol value"
 *  example "Total : 100"
 * */
public class ParsableSymbol extends ParsableTitle {

	public ParsableSymbol(String mySymbol,String  mykey) {
		super(null);
		match="^\\s*"+mykey+"\\s*"+mySymbol+"\\s*(.*)\\s*$";
		symbol=mySymbol;
		key=mykey;	
		
	}
	private ParsableSymbol(String myMatch) {
		super(myMatch);
		
	}
public static Double MapKeyToDouble(Map<String,String> map, String key)
{
	String value=map.get(key);
	try
	{
	return Double.parseDouble(value);
	}
	catch(Exception e)
	{
		return null;
	}
	}
public static Integer MapKeyToInteger(Map<String,String> map, String key)
{
	String value=map.get(key);
	try
	{
	return Integer.parseInt(value);
	}
	catch(Exception e)
	{
		return null;
	}
	}
	public static Map<String,String> GetMap(List<IParsable> classList)
	{
		if (classList==null)
			return null;
		Map<String,String> map=new HashMap<String,String>();
		for(IParsable ps:classList)
			if ( ps instanceof ParsableSymbol) 
				if(((ParsableSymbol)ps).key!=null)
					if(((ParsableSymbol)ps).value!=null)
						map.put(((ParsableSymbol)ps).key, ((ParsableSymbol)ps).value);
		return map;
		
	}
	
	public String toString()
	{
		return "ParsableSymbol("+key+symbol+".*)";
	}	


	String symbol=null;
	String key=null;
	
	
	protected Pattern p0=null;

    public String getSymbol() {
		return symbol;
	}
	public String getKey() {
		return key;
	}



}
