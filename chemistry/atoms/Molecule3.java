/**
 * 
 */
package com.zoubworld.chemistry.atoms;

import com.zoubworld.chemistry.IAtom;
import com.zoubworld.chemistry.Molecule2;
import com.zoubworld.chemistry.PeriodicElementTable;

/**
 * @author Pierre Valleau
 *
 */
public class Molecule3 extends Molecule2 implements IOpenScad {

	/**
	 * 
	 */
	public Molecule3() {
		// TODO Auto-generated constructor stub
	}

	public Molecule3(Molecule2 m) {
		super(m);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Molecule3 m= new Molecule3(Molecule3.parseAsciiMath("H_2O"));
		System.out.println(m.toOpenSCad());
		
		
		m= new Molecule3(Molecule3.parseAsciiMath("CO_2"));
		System.out.println(m.toOpenSCad());
		
		m= new Molecule3(Molecule3.parseAsciiMath("CH_3CH_2CH_2OH"));
		System.out.println(m.toOpenSCad());
		
	}

	@Override
	public String getColor() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Double getRadius() {
		
		return null;
	}
	@Override
	 public String toOpenSCad()
		{
		String s="";
		int i=0;
		int N=this.getAtoms().size();
		for(IAtom e:this.getAtoms())
		s+="rotate("+Atom2.getPolCoordNpointi(N,i++)+") { \r\n"
			+ "		    translate(["+Factor*(new Atom2(e)).getRadius()+",0,0]) {\r\n"
			+(new Atom2(e)).toOpenSCad()
			+"}}\r\n";
		return s;
		}
}
