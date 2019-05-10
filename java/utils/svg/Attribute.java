package com.zoubwolrd.java.utils.svg;

import java.util.HashMap;
import java.util.Map;

public class Attribute implements ISvgObject{

	final public static  String fill="fill";
	final public static  String stroke="stroke";
	
	
	
	final public static  String black="black";
	final public static  String white="white";
	final public static String blue="blue";
	final public static String gray="gray";
	final public static  String red="red";
	final public static  String green="green";
	public static final String none = "none";
	public static final String stroke_width = "stroke-width";
	
	public Attribute() {
		
	}
	Map<String,String> values=new HashMap<String,String>();
	public String toSVG()
	{
		String s="";
		 
		for(String k:values.keySet())
			s+=k+"=\""+values.get(k)+"\" ";
		  
		return s;
		
	
	}
	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public String put(String key, String value) {
		return values.put(key, value);
	}
	@Override
	public void move(Double x, Double y) {
	
		
	}

}
