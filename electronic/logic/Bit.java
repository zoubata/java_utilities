package com.zoubworld.electronic.logic;

import java.util.HashMap;
import java.util.Map;

public class Bit {

	boolean value;
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
		return value?"1":"0";
	}
	static Map<String,Bit> map=new HashMap();
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
	public boolean Value() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(boolean value) {
		this.value = value;
	}
	public Bit() {
		// TODO Auto-generated constructor stub
	}

}
