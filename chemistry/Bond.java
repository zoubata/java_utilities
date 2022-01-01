/**
 * 
 */
package com.zoubworld.chemistry;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 
 *
 * Bond collection between atoms
 * 
 * see https://en.wikipedia.org/wiki/Skeletal_formula
 */
public class Bond {

	/**
	 * @return the a
	 */
	public Atom getA() {
		return a;
	}
	public String toString()
	{
		String s="";
		if (getA()!=null)
			s+=getA().getSymbol();
		s+="-";
		if (getB()!=null)
			s+=getB().getSymbol();
		
		return s;
	}
	/**
	 * @return the b
	 */
	public Atom getB() {
		return b;
	}

	/**
	 * @return the electrons
	 */
	public int getElectrons() {
		return electrons;
	}
	/**
	 * @return the atoms
	 */
	public Set<Atom> getAtoms() {
		 Set<Atom> atoms=new HashSet<Atom>();
		 if (a!=null)
		 atoms.add(a);
		 if (b!=null)
		 atoms.add(b);
		 
		return atoms;
	}
	Atom a;
	Atom b;
	int electrons=0;
	
	/**
	 * 
	 */
	public Bond(Atom a1,Atom b1,int electrons1) {
		a=a1;
		b=b1;
		electrons=electrons1;
	}
	public Bond(Atom a1,Atom b1) {
		a=a1;
		b=b1;
		electrons=1;
	}

}
