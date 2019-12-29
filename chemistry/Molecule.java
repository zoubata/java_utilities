package com.zoubworld.chemistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Molecule {

	public Molecule() {
		// TODO Auto-generated constructor stub
	}
	List<Bond> structures=null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PeriodicElementTable t=new PeriodicElementTable();
		Atom h1=t.getAtom("H").clone();
		Atom h2=h1.clone();
		Atom o=t.getAtom("O");
		System.out.println(h1.toDot());
		System.out.println(o.toDot());
		Bond b1=new Bond(h1,o,1);
		Bond b2=new Bond(h2,o,1);
		List<Bond> lb=new ArrayList<Bond>();
		lb.add(b1);
		lb.add(b2);
		
		Molecule m=Molecule.buildb(lb);
		System.out.println(m.toString());
		System.out.println(m.toDot());
		
	}

 
	/**
 * @return the structures
 */
public List<Bond> getStructures() {
	return structures;
}

/**
 * @return the atoms
 */
public Collection<Atom> getAtoms() {
	 List<Atom> atoms=new ArrayList<Atom>();
	 for(Bond b:structures)
		 atoms.addAll(b.getAtoms());
	return atoms;
}

	public static Molecule buildb(List<Bond> lb)
	{
		Molecule m=new Molecule();	
		m.structures=	new ArrayList<Bond>();
		m.structures.addAll(lb);
		return m;
	}
	
	/** generate a graphviz string
	 * 
	 * it is compliant with The DOT Language - Graphviz
	 * https://www.graphviz.org/doc/info/lang.html
	 * save string on  file.viz
	 * run dot -ot.svg -v -Tsvg file.viz
	 * */

	public String toDot2()
	{
	String s="";

	s+="digraph dot  {\r\n"
			+ "graph [layout = circo]\r\n"

			 ;
	Set<Atom> atoms=new HashSet<>();

	if (structures!=null)
	for(Bond b:structures)
		{
			atoms.add(b.getA());
			atoms.add(b.getB());
		}
	for(Atom a: atoms)
	s+=	"\tnode "+a.getId()+" [label=\""+a.getSymbol()+"\" fillcolor = green]\r\n"   ;
	
	s+="edge [style = filled, color = grey]\r\n";
			for(Bond d: structures)
				for(int i=0;i<d.getElectrons();i++)
		s+=	"\t\t"+d.getA().getId()+" -> "+d.getB().getId()+" [style=bold,label=\""+(i+1)+"\"];\r\n" ;
			
			/*
			2 [label="Ni!"]
			3 [label="Ni!" shape=egg]
			
			"	    b -- c;\r\n" + 
			"	    a -- c;\r\n" + 
			"	    d -- c;\r\n" + 
			"	    e -- c;\r\n" + 
			"	    e -- a;\r\n" +*/
			s+=
			"	}\r\n";
	return s;
	}

	public String toDot()
	{
	String s="";

	s+="graph {\r\n" ;
	Set<Atom> atoms=new HashSet<>();

	if (structures!=null)
	for(Bond b:structures)
		{
			atoms.add(b.getA());
			atoms.add(b.getB());
		}
	for(Atom a: atoms)
	s+=	"\t"+a.getId()+" [label=\""+a.getSymbol()+"\"]\r\n"   ; 
	
			for(Bond d: structures)
				for(int i=0;i<d.getElectrons();i++)
		s+=	"\t\t"+d.getA().getId()+" -- "+d.getB().getId()+" [style=bold,label=\""+(i+1)+"\"];\r\n" ;
			
			/*
			2 [label="Ni!"]
			3 [label="Ni!" shape=egg]
			
			"	    b -- c;\r\n" + 
			"	    a -- c;\r\n" + 
			"	    d -- c;\r\n" + 
			"	    e -- c;\r\n" + 
			"	    e -- a;\r\n" +*/
			s+=
			"	}\r\n";
	return s;
	}
	public static Molecule build(List<Atom> atoms)
	{
		Map<Atom,List<Bond>> reaction=new HashMap<Atom,List<Bond>> ();
		for(Atom a:atoms)
			reaction.put(a, new ArrayList<Bond>());
		
		build(reaction);
	
		List<Bond> atomConnections=null;
		/*
		Atom a=atoms.get(0);
				Atom b=atoms.get(1);
				int n=a.getMissingEletronsLastShell()
						new Bond(a,bi,1..n)
		a -1 
		b -2  
		*/
		return buildb(atomConnections);		
	}
	private static Set<Map<Atom, List<Bond>>> build(Map<Atom, List<Bond>> reaction) {
/*		8H1 8O2
		2C4
		
		4H2+4O2+C2
		8H2O+C2
		2CH4+4O2
		2H4O4C
		H1  C4 O2 =>8        C-O-H    H-C=0
		
		
		
				H11C21O
				H11C22O
				H11C31O
				H11C32O
				H11C41O
				H11C42O
				
				*/
for(Atom a:reaction.keySet())
{

	if ((a.getMissingEletronsLastShell()-reaction.get(a).size())>0)
	{
//		reaction.get(a).add(e);
	}
}
return null;		
	}

	public String toString()
	{
		//Set<Atom> atoms=new HashSet<Atom>();
		List<Atom> atoml=new ArrayList<Atom>();
		String s="";
		if (structures!=null)
		for(Bond b:structures)
			{
			/*	atoms.add(b.getA());
				atoms.add(b.getB());*/
				if(!atoml.contains(b.getB()))
				atoml.add(b.getB());
				if(!atoml.contains(b.getA()))
				atoml.add(b.getA());
			}
		
		for(Atom a: atoml)
			s+=a.getSymbol();
			return s;
			
	}

}
