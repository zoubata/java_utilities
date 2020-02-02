package com.zoubworld.electronic.logic;

import java.util.HashMap;
import java.util.Map;

public class Bit implements Comparable {

	Boolean value=null;
	String name=null;
	/**
	 * @return the name
	 */
	public String getName() {
		if (name!=null)
		return name;
		return "@"+hashCode();
	}
	public String toString()
	{
		return getName();
		/*
		if(value==null)
			return "x";
		return value?"1":"0";*/
	}
	static Map<String,Bit> map=new HashMap<String,Bit>();
	static public Bit find(String name)
	{
		return map.get(name);
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		map.put(name,this);
	}
	/**
	 * @return the value
	 */
	public Boolean Value() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Boolean value) {
		this.value = value;
	}
	public Bit() {
		// TODO Auto-generated constructor stub
	}
	public Bit(String name) {
		
		setName( name);
		
	}
	public String value() {
		if(Value()==null)
			return "x";
		return Value()?"1":"0";
	}
	@Override
	public int compareTo(Object o) {
		if( o==null)
			return 1;
		if (Bit.class.isInstance(o))
			return getName().compareTo(((Bit)o).getName());
		return 1;
	}

}
