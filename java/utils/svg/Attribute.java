package com.zoubworld.java.utils.svg;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	
	/// general commun property to a class
	public static Attribute Global=new Attribute();
	public static Attribute Basic=new Attribute();
	public static Attribute Circle=new Attribute();
	public static Attribute Line=new Attribute();
	public static Attribute ArcCircle=new Attribute();
	public static Attribute Path=new Attribute();
	public static Attribute Polygone=new Attribute();
	public static Attribute Text=new Attribute();
	public static Attribute Rectangle=new Attribute();
	
	public Attribute() {
		{
			put("stroke","black");
		}
	}
	public Attribute(Attribute attribute) {
		for(Entry<String, String> e:attribute.values.entrySet())
			values.put(e.getKey(), e.getValue());	
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
