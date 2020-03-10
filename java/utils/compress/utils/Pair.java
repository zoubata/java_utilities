package com.zoubworld.java.utils.compress.utils;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

public class Pair implements Comparable<Pair>{
	private ISymbol a;
	private ISymbol b;		
	public Pair(ISymbol a, ISymbol b) {
		super();
		this.a = a;
		this.b = b;
	}
	/**
	 * @return the a
	 */
	public ISymbol getA() {
		return a;
	}
	/**
	 * @return the b
	 */
	public ISymbol getB() {
		return b;
	}
	public String toString()
	{
		return a+"+"+b;
	}
	@Override
	public boolean equals(Object obj) {
		if(Pair.class.isInstance(obj))
		{
			Pair c1=(Pair)obj;			
			return c1.a.equals(a) && c1.b.equals(b);			
		}
		return super.equals(obj);
	}
	@Override
	public int compareTo(Pair o) {
		if(Pair.class.isInstance(o))
		{
			Pair c=(Pair)o;	
			if(c.a.compareTo(a)!=0)
			return c.a.compareTo(a);
			else
				return c.b.compareTo(b);			
		}
		return -1;
	}
	
	@Override
	public int hashCode() {
		int i=a.hashCode();		
		i^=b.hashCode();
		return i;
	}
}