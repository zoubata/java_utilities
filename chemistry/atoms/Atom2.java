/**
 * 
 */
package com.zoubworld.chemistry.atoms;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.chemistry.IAtom;
import com.zoubworld.chemistry.PeriodicElementTable;

/**
 * @author Pierre Valleau
 *
 */
public class Atom2 extends com.zoubworld.chemistry.Atom implements IOpenScad {

	List<Electron> electrons;
	List<Proton> protons;
	List<Neutron> neutrons;
	public void build()
	{
		electrons= new ArrayList<Electron> ();
		protons= new ArrayList<Proton> ();
		neutrons= new ArrayList<Neutron> ();
	
		for( int i=getNumberOfElectron();i>0;i--)
			electrons.add(new Electron());
		
		for( int i=getNumberOfProton();i>0;i--)
			protons.add(new Proton());
		if(getNumberOfNeutron()!=null)
		for( int i=getNumberOfNeutron();i>0;i--)
			neutrons.add(new Neutron());
		
	}
	
	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(Electron e) {
		return electrons.add(e);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public Electron remove(int index) {
		return electrons.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Electron o) {
		return electrons.remove(o);
	}

	/**
	 * @return the electrons
	 */
	public List<Electron> getElectrons() {
		return electrons;
	}

	/**
	 * @return the protons
	 */
	public List<Proton> getProtons() {
		return protons;
	}

	/**
	 * @return the neutrons
	 */
	public List<Neutron> getNeutrons() {
		return neutrons;
	}

	/**
	 * 
	 */
	public Atom2() {
		build();
	}

	/**
	 * @param at
	 */
	public Atom2(com.zoubworld.chemistry.Atom at) {
		super(at);
		build();
	}

	/**
	 * @param name2
	 * @param sym
	 * @param num
	 */
	public Atom2(String name2, String sym, int num) {
		super(name2, sym, num);
		build();
	}

	/**
	 * @param sym
	 */
	public Atom2(String sym) {
		super(sym);
		build();
	}

	/**
	 * @param A
	 * @param Z
	 */
	public Atom2(int A, int Z) {
		super(A, Z);
		build();
	}

	/**
	 * @param Z
	 */
	public Atom2(int Z) {
		super(Z);
		build();
	}

	public Atom2(IAtom atom) {
		super(atom);
		build();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		Atom2 a=new Atom2("Cl");
		System.out.println(a.toOpenSCad());
		
		System.out.println(a.toOpenSCadDetail());
		

	}

	@Override
	public String getColor() {
		if ("H".equals(getSymbol()))
			return "White";
		if ("O".equals(getSymbol()))
			return "Red";
		if ("C".equals(getSymbol()))
			return "Black";
		if ("N".equals(getSymbol()))
			return "Blue";
		return "";
	}
	
	@Override
	 public String toOpenSCad()
		{
		return "// "+getSymbol()+"\r\n" + 
		" color( c = \""+getColor()+"\" ) {\r\n"
		+
			"\t"+"sphere( r = "+Factor*getRadius()+");\r\n"
			+ "}\r\n";
		}
	@Override
	public Double getRadius() {
		
		return this.getCovalentRadius();
	}
	/** this function return the polar coordinate of point n°i on a case where 
	 *  N points, on a sphere to be all at an equal distance,
	 * 
	 * 
	 * this is a very hard math problem, so this is  a very approximative answer
	 * */
	static public String getPolCoordNpointi(int N,int i)
	{
		switch(N)
		{
		case 1:
		{
			String t[]={"[0,0,0]"};
			return t[i%N];
			//break;
		}
		case 2:{
			String t[]={"[0,0,0]","[0,180,0]"};
			return t[i%N];
			//	break;
			}
		case 3:
		{
			String t[]={"[0,0,0]","[0,120,0]","[0,240,0]"};
			return t[i%N];
			//	break;
			}
		case 4:
		{
			String t[]={"[15,0,0]","[15,120,0]","[-15,240,0]","[90,0,0]"};
			return t[i%N];
			//	break;
			}
		case 5:
		case 6:

		{
			String t[]={"[0,0,0]","[90,0,0]","[180,0,0]","[270,0,0]","[0,90,0]","[0,270,0]"};
			return t[i%N];
			//break;
			}
		case 7:
		case 8:

		{
			String t[]={"[60,0,0]","[60,90,0]","[60,180,0]","[60,270,0]","[-60,45,0]","[-60,135,0]","[-60,225,0]","[-60,315,0]"};
			return t[i%N];
			//	break;
		}

		default : 
			
			return "["+(i*97*360/N)%360+","+(i*-7*360/N)%360+","+(i*23*360/N)%360+"]";
		}
		}
	
	

	 public String toOpenSCadDetail()
		{
		String s="";
		int i=0;
		int N=this.getProtons().size();
		
		for(Proton e:this.getProtons())
			s+="rotate("+getPolCoordNpointi(N,i++)+") { \r\n"
			+ "		    translate(["+Factor*e.getRadius()*2+",0,0]) {\r\n"
			+e.toOpenSCad()
			+"}}\r\n";
		
		
		N=this.getNeutrons().size();
		i=0;
		s+="rotate([-5,+10,+35]) ";
		for(Neutron e:this.getNeutrons())
			s+="rotate("+getPolCoordNpointi(N,i++)+") { \r\n"
			+ "		    translate(["+Factor*e.getRadius()*2+",0,0]) {\r\n"
			+e.toOpenSCad()
			+"}}\r\n";
		
		N=this.getElectrons().size();
		i=0;
		for(Electron e:this.getElectrons())
			s+="rotate("+getPolCoordNpointi(N,i++)+") { \r\n"
			+ "		    translate(["+Factor*this.getRadius()+",0,0]) {\r\n"
			+e.toOpenSCad()
			+"}}\r\n";
	
		return s;
		}
}
