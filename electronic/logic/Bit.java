package com.zoubworld.electronic.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Bit implements Comparable<Bit> {

	Boolean value=null;
	String name=null;
	public void toggle()
	{
		if(value!=null)
			value=!value;
	}
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
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (Bit.class.isInstance(obj))
		{Bit b=(Bit)obj;
		if (b.getName().equals(this.getName()))
			if (this.Value()!=null)
				return this.Value().equals(b.Value());
			else
			return b.Value()==this.Value();
		
		}
		return super.equals(obj);
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
	public int compareTo(Bit o) {
		if( o==null)
			return 1;
		if (Bit.class.isInstance(o))
			return getName().compareTo(((Bit)o).getName());
		return 1;
	}
	
	public void init() {
		value=null;		
	}
	/** create a copy with same name and value, but unlink to any sub object
	 * */
	public static Bit copy(Bit b) {
		Bit n=new Bit(b.getName());
		n.setValue(b.Value());
		n.map=b.map;
		return n;
	}

}
