/**
 * 
 */
package com.zoubwolrd.chemistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata	
 *
 */
public class Atom {

	int electron=0;
	/**
	 * @return the electron
	 */
	public int getElectron() {
		return electron;
	}

	/**
	 * @param electron the electron to set
	 */
	public void setElectron(int electron) {
		this.electron = electron;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the proton
	 */
	public int getProton() {
		return proton;
	}

	/**
	 * @return the neutron
	 */
	public int getNeutron() {
		return neutron;
	}

	int proton=0;
	int neutron=0;
	String symbol="";
	String name="";
	/**
	 * 
	 */
	public Atom() {
		// TODO Auto-generated constructor stub
	}

	Map<String,String> property;
	/**
	 * @return the property
	 */
	public Map<String, String> getProperty() {
		if (property==null)
			property=new HashMap<String,String>();
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(Map<String, String> property) {
		this.property = property;
	}

	public Atom(String name2, String sym, int num) {
		name=name2;
		symbol=sym;
		proton=electron=num;
	}
	public String toString()
	{
		String s="";
		s+=getSymbol()+ " : "+ getName()+" ("+getElectron()+")\r\n";
		s+="{\r\n";
		
		for(String key:JavaUtils.asSortedSet(getProperty().keySet()))
			if (!key.equals(""))
			s+="\t"+key+" : "+  getProperty().get(key)+"\r\n";
				
		s+="}\r\n";
		return s;
	}

	public String toDot()
	{
	String s="";

	s+="digraph dot  {\r\n"
			+ "graph [layout = circo,  fixedsize=true ]\r\n"
			+ ""+
		 
			getId()+" [shape = circle,\r\n" + 
			"      style = filled,\r\n" + 
			"      color = red,\r\n"+
			"      label = \""+getSymbol()+"\"];\r\n"+  
			"\r\n" +				
			"node [shape = circle,\r\n" + 
			"			      style = filled,\r\n" + 
			"			      color = black,]\r\n" ;
	for(int i=1;i<getElectron()+1;i++)
		s+=" "+i;
	s+=
			";\r\n" + 
			"\tedge [style = filled, color = \"transparent\"]\r\n" + 
			"\t"+getId()+" -> {";
			for(int i=1;i<getElectron()+1;i++)
			s+= " "+i;
			s+= "}\r\n" 

			 ;

	
	s+="}\r\n";
	return s;
}

	int eletrons[]= {2,8,18,32,50};
	
	public Integer getEletrons(int Shell)
	{
		int e=getElectron();
		for(int i=1;i<=Math.min(eletrons.length,Shell);i++)
		if (e<eletrons[i-1])
			return e;
		else
			e-=eletrons[i];
		if (e>eletrons[Shell-1])
		return eletrons[Shell-1];
		else
			return e;		
	}
	// https://simple.wikipedia.org/wiki/Electron_shell
	public Integer getEletronsLastShell( )
	{
		int e=getElectron();
		for(int i=1;i<=eletrons.length;i++)
		if (e<eletrons[i-1])
			return e;
		else
			e-=eletrons[i-1];
		return e;
	}
	/** clone everything
	 * */
	public Atom clone()
	{
		Atom a=new Atom();
		a.electron=this.electron;
		a.proton=this.proton;
		a.name=""+this.name;
		a.neutron=this.neutron;
		a.symbol=""+this.symbol;
		a.getProperty().putAll(this.getProperty());
		return a;
	}
	// https://simple.wikipedia.org/wiki/Electron_shell
		public Integer getMissingEletronsLastShell( )
		{
			int e=getElectron();
			for(int i=1;i<=eletrons.length;i++)
			if (e<eletrons[i-1])
				return eletrons[i-1]-e;
			else
				e-=eletrons[i-1];
			return eletrons[eletrons.length-1]-e;
		}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PeriodicElementTable t=new PeriodicElementTable();
		Atom h=t.getAtom("H");
		Atom c=t.getAtom("C");
		Atom o=t.getAtom("O");
		System.out.println("H : "+h.getEletronsLastShell() + " -"+h.getMissingEletronsLastShell() );
		System.out.println("C : "+c.getEletronsLastShell() + " -"+c.getMissingEletronsLastShell() );
		System.out.println("O : "+o.getEletronsLastShell() + " -"+o.getMissingEletronsLastShell() );
	}

	public String getId() {
		// TODO Auto-generated method stub
		return ""+this.hashCode();
	}

}
