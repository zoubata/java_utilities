package com.zoubworld.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** generics parsable class
 * 
 * used with ParseFile(List<IParsable> classList, String filename)
 *  the synthaxe is "key Symbol value"
 *  example "Total : 100"
 * */
public class ParsableDefine implements IParsable {
	protected String match=null;//"\\s*[(.*)]\\s*";
	public ParsableDefine() {
		super();
		match="^\\s*#define (\\S+)\\s+"+"\\s*(\\S.*)\\s*$";
		
	}
	protected Pattern p0=null;
	private ParsableDefine(String myMatch) {
		super();
		
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
	String define=null;

    public String getvalue() {
		return value;
	}
	public String getKey() {
		return define;
	}

	public static Map<String,String> GetMap(List<IParsable> classList)
	{
		if (classList==null)
			return null;
		Map<String,String> map=new HashMap<String,String>();
		for(IParsable ps:classList)
			if ( ps instanceof ParsableDefine) 
				if(((ParsableDefine)ps).define!=null)
					if(((ParsableDefine)ps).value!=null)
						map.put(((ParsableDefine)ps).define, ((ParsableDefine)ps).value);
		return map;
		
	}
	


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
		 ParsableDefine tc; 	
		 tc= new ParsableDefine(); 
		 tc.value=m.group(2);
		 tc.define=m.group(1);
		
	return tc;
	}
	return null;
}

public String get() {
	return  getKey() ;
}

	public String toString()
	{
		return "#define (.*) (.*)";
	}
	@Override
	/** return [define,value]
	 * */
	public List<String> getList() {
		List<String> l=new ArrayList<String>();
		l.add(getKey());
		l.add(get());		
		return l;
	}	


	
	



}
