/**
 * 
 */
package com.zoubworld.chemistry;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata	
 *
 */
public class Atom implements IAtom {

	int electron=0;
	/* (non-Javadoc)
	 * @see com.zoubworld.chemistry.IAtom#getElectron()
	 */
	@Override
	public int getNumberOfElectron() {
		return electron;
	}
	/* (non-Javadoc)
	 * @see com.zoubworld.chemistry.IAtom#getNucleus()
	 */
	@Override
	public int getNumberOfNucleus() {
		return getNumberOfProton()+getNumberOfNeutron();
	}
	
	
	/* (non-Javadoc)
	 * @see com.zoubworld.chemistry.IAtom#setElectron(int)
	 */
	@Override
	public void setElectron(int electron) {
		this.electron = electron;
	}
	public static Comparator<Atom> byLastShellElectrons = 
			  (Atom player1, Atom player2) -> Integer.compare(player2.getEletronsLastShell().intValue(), player1.getEletronsLastShell().intValue());
			  public static Comparator<Atom> byMissingEletronsLastShell = 
					  (Atom player1, Atom player2) -> Integer.compare(player2.getMissingEletronsLastShell( ).intValue(), player1.getMissingEletronsLastShell( ).intValue());
					  

	/* (non-Javadoc)
	 * @see com.zoubworld.chemistry.IAtom#getSymbol()
	 */
	@Override
	public String getSymbol() {
		int charge=getNumberOfElectron()-getNumberOfProton();
		String c="+";
		if(charge==0)
			c="";
		else
			if(charge<0)
					{c="-";charge=-charge;}
		for(int i=charge;i>0;i--)
		c+=c;
		
		return symbol+c;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + electron;
		result = prime * result + ((neutron == null) ? 0 : neutron.hashCode());
		result = prime * result + proton;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Atom other = (Atom) obj;
		if (electron != other.electron)
			return false;
		if (neutron == null) {
			if (other.neutron != null)
				return false;
		} else if (!neutron.equals(other.neutron))
			return false;
		if (proton != other.proton)
			return false;
		if (proton==0)
		{
			if (symbol != other.symbol)
				return false;
			if (name != other.name)
				return false;
				
		}
		return true;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.chemistry.IAtom#getProton()
	 */
	@Override
	public int getNumberOfProton() {
		return proton;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.chemistry.IAtom#getNeutron()
	 */
	@Override
	public Integer getNumberOfNeutron() {
		return neutron;
	}
	static double massenucleus=1.67e-27;//kg
	static double masseelectron=9.1e-31;//kg
	static double chargeElectron=-1.60e-19;//coulomb
	/** masse de l'atome
	 * 
	 * @return
	 */
	public double getMasse() {
		return getNumberOfNucleus()*massenucleus+getNumberOfElectron()*chargeElectron;
	}
	int proton=0;
	Integer neutron=null;
	String symbol="";
	String name="";
	/**
	 * 
	 */
	public Atom() {
		// TODO Auto-generated constructor stub
	}
	public Atom(Atom at) {
		this.electron=at.electron;
		this.name=at.name;
		this.neutron=at.neutron;
		this.property=at.property;
		this.proton=at.proton;
		this.symbol=at.symbol;
	}

	Map<String,String> property;
	/**
	 * @return the property
	 */
	@Override
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
	public Atom( String sym) {	
		symbol=sym;		
		Atom atom=PeriodicElementTable.getInstance().getAtom(sym);
		proton=electron=atom.getNumberOfElectron();
		neutron=atom.getNumberOfNeutron();
		symbol=atom.getSymbol();
		property=atom.getProperty();
		
	}
	static public Atom Factory( String sym) {	
		
		return PeriodicElementTable.getInstance().getAtom(sym);
	}
	/**   
	 * ^A_ZX
	 * 
	 * A: number of nucleon
	 * Z: number of proton
	 * */
	public Atom(int A, int Z) {
		Atom atom=PeriodicElementTable.getInstance().getAtom(Z);
		proton=electron=atom.getNumberOfElectron();
		neutron=atom.getNumberOfNeutron();
		symbol=atom.getSymbol();
		property=atom.getProperty();
		proton=electron=Z;
		neutron=A-Z;
//		IAtom a=PeriodicElementTable.getAtom(Z):
	}
	public Atom( int Z) {
		
		Atom atom=PeriodicElementTable.getInstance().getAtom(Z);
		proton=electron=atom.getNumberOfElectron();
		neutron=atom.getNumberOfNeutron();
		symbol=atom.getSymbol();
		property=atom.getProperty();
		proton=electron=Z;
			
			
	}
	public Atom(IAtom atom) {
		proton=electron=atom.getNumberOfElectron();
		neutron=atom.getNumberOfNeutron();
		symbol=atom.getSymbol();
		property=atom.getProperty();
		
	}
	public String toString()
	{
		String s="";
		s+=getSymbol()+ " : "+ getName()+" ("+getNumberOfElectron()+")\r\n";
		s+="{\r\n";
		
		for(String key:JavaUtils.asSortedSet(getProperty().keySet()))
			if (!key.equals(""))
			s+="\t"+key+" : "+  getProperty().get(key)+"\r\n";
				
		s+="}\r\n";
		return s;
	}/*
	public String toScad()
	{
	String s="";
s+="sphere(d = 20);"+"\r\n";
return s;
	}*/
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
	for(int i=1;i<getNumberOfElectron()+1;i++)
		s+=" "+i;
	s+=
			";\r\n" + 
			"\tedge [style = filled, color = \"transparent\"]\r\n" + 
			"\t"+getId()+" -> {";
			for(int i=1;i<getNumberOfElectron()+1;i++)
			s+= " "+i;
			s+= "}\r\n" 

			 ;

	
	s+="}\r\n";
	return s;
}

	public static int electrons[]= {2,8,8,18,18,32,32,50,72};

	/* (non-Javadoc)
	 * @see com.zoubworld.chemistry.IAtom#getEletronStructure()
	 */
	@Override
	public String getEletronStructure()
	{
		String s="";
		char L[]= {'K','L','M','N','O','P','Q'};
		int i=getNumberOfElectron();
		int j=0;
				
		for(int k:electrons)
		if (i>0)
		{	if (i>k)
				
			s+=L[j++]+""+k;
			else
				s+=L[j++]+""+i;
		i-=k;
		}
				
			return s;
	}
	public Integer getEletrons(int Shell)
	{
		int e=getNumberOfElectron();
		for(int i=1;i<=Math.min(electrons.length,Shell);i++)
		if (e<electrons[i-1])
			return e;
		else
			e-=electrons[i];
		if (e>electrons[Shell-1])
		return electrons[Shell-1];
		else
			return e;		
	}
	// https://simple.wikipedia.org/wiki/Electron_shell
	/* (non-Javadoc)
	 * @see com.zoubworld.chemistry.IAtom#getEletronsLastShell()
	 */
	@Override
	public Integer getEletronsLastShell( )
	{
		int e=getNumberOfElectron();
		for(int i=1;i<=electrons.length;i++)
		if (e<=electrons[i-1])
			return e;
		else
			e-=electrons[i-1];
		return e;
	}
	
	public Integer getPeriod( )
	{
		int e=getNumberOfElectron();
		int i=1;
		for(;i<=electrons.length;i++)
		if (e<=electrons[i-1])
			return i;
		else
			e-=electrons[i-1];
		return i;
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
			int e=getNumberOfElectron();
			for(int i=1;i<=electrons.length;i++)
			if (e<electrons[i-1])
				return electrons[i-1]-e;
			else
				e-=electrons[i-1];
			return electrons[electrons.length-1]-e;
		}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PeriodicElementTable t=new PeriodicElementTable();
		Atom h=t.getAtom("H");
		Atom c=t.getAtom("C");
		Atom o=t.getAtom("O");
		Atom Li=t.getAtom("Li");
		Atom a=t.getAtom("U");
				System.out.println("H : "+h.getEletronsLastShell() + " -"+h.getMissingEletronsLastShell() );
		System.out.println("C : "+c.getEletronsLastShell() + " -"+c.getMissingEletronsLastShell() );
		System.out.println("O : "+o.getEletronsLastShell() + " -"+o.getMissingEletronsLastShell() );
		System.out.println(Li.getSymbol()+" : "+Li.getEletronsLastShell() + " -"+Li.getMissingEletronsLastShell() );
		System.out.println(a.getSymbol()+" : "+a.getEletronsLastShell() + " -"+a.getMissingEletronsLastShell() );
		System.out.println(a.getSymbol()+" : electrons : "+a.getNumberOfElectron() + " Neutron "+a.getNumberOfNeutron() + " Proton "+a.getNumberOfProton() +" Structure:"+a.getEletronStructure());
		System.out.println(a.getSymbol()+" : \r\n"+a.toDot());
		
		}

	public String getId() {
		// TODO Auto-generated method stub
		return ""+this.hashCode();
	}

	public Double getAtomicRadius() {
		// TODO Auto-generated method stub
		String s= getProperty().get("Rayon atomique (calculé)");
		s=s.replace("pm", "");
		s=s.replaceAll(" ", "");
		s=s.trim();
		return Double.parseDouble(s)*1e-12;
	}
	public Double getCovalentRadius() {
		// TODO Auto-generated method stub
		String s= getProperty().get("Rayon covalent");
		s=s.replace("pm", "");
		s=s.replaceAll(" ", "");
		s=s.trim();
		return Double.parseDouble(s)*1e-12;
	}
	
	
}
