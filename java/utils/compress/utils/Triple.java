/**
 * 
 */
package com.zoubworld.java.utils.compress.utils;

import com.zoubworld.java.utils.compress.ISymbol;

/**
 * @author Pierre Valleau
 *
 */
public class Triple extends Pair implements Comparable<Pair> {
	private ISymbol c;		
	public Triple(ISymbol a, ISymbol b, ISymbol c) {
		super(a,b);		
		this.c = c;
	}
	/**
	 * @return the c
	 */
	public ISymbol getC() {
		return c;
	}
	/**
	 * @param a
	 * @param b
	 */
	public Triple(ISymbol a, ISymbol b) {
		super(a, b);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public String toString()
	{
		return super.toString()+"+"+c;
	}
	@Override
	public boolean equals(Object obj) {
		if(Triple.class.isInstance(obj))
		{
			Triple c1=(Triple)obj;			
			Pair p1=(Pair)obj;			
			return super.equals(p1) && c.equals(c1.c);			
		}
		return super.equals(obj);
	}
	
	public int compareTo(Triple o) {
		if(Triple.class.isInstance(o))
		{
			Triple c1=(Triple)o;	
			if(super.compareTo(c1)!=0)
			return super.compareTo(c1);
			else
				return c1.c.compareTo(c);			
		}
		if(Pair.class.isInstance(o))
		{
			Pair p1=(Pair)o;	
			if(super.compareTo(p1)!=0)
			return super.compareTo(p1);
			else
				return 1;			
		}
		return -1;
	}
	
	@Override
	public int hashCode() {
		int i=super.hashCode();		
		i^=c.hashCode();
		return i;
	}
}
