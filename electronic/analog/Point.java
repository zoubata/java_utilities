/**
 * 
 */
package com.zoubworld.electronic.analog;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Pierre Valleau
 * it is where several thing are connected.
 * The sum of current of connected thing is equal to 0 A.
 *
 */
public class Point {

	Set<Iconnect> thing=new HashSet<Iconnect> ();
	String name="";
	/**
	 * @param arg0
	 * @return
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(Iconnect arg0) {
		return thing.add(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends Iconnect> arg0) {
		return thing.addAll(arg0);
	}

	/**
	 * 
	 */
	public Point() {
		// TODO Auto-generated constructor stub
	}
	public Point(String name) {
		this.name=name;
	}
	public String toString()
	{ return name;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public Double getPotential(Point ref) throws Exception
	{
		for(Iconnect cmp: thing	)
			if (cmp.getPotential(ref,this)!=null)
			return cmp.getPotential(ref,this);
		return null;
	}
	public void vassert() throws Exception
	{
		double s=0.0;
				for(Iconnect cmp: thing	)
						s+=cmp.getCurrent(this);
									
					if (s!=0.0)
						throw new Exception("ohm law broken");
					
	}
	public Double getCurrent(Iconnect resistor) throws Exception {
		double s=0.0;
		
		for(Iconnect cmp: thing	)
			if(cmp!=resistor)
			if (cmp.getCurrent(this)!=null)
				s+=cmp.getCurrent(this);
		
		return -s;
	}

}
