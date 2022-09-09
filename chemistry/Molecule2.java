/**
 * 
 */
package com.zoubworld.chemistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author M43507
 *
 */
public class Molecule2 implements IMolecule {

	/**
	 * 
	 */
	public Molecule2() {
		compositions= new HashMap<IAtom,Integer> ();
	}
	public Molecule2(Molecule2 m) {
		compositions= new HashMap<IAtom,Integer> ();
		compositions.putAll(m.compositions);
		charge=m.charge;
	}
	static Pattern p__AsciiMath = Pattern.compile("([A-Z][a-z]?)(_\\d+)?(\\^\\d*[\\+|\\-])?");
	static Pattern p__AsciiMath2 = Pattern.compile("(([A-Z][a-z]?)(_\\d+)?(\\^\\d*[\\+|\\-]))+");

	static public Molecule2 parseAsciiMath  (String AsciiMath) {
		Molecule2 m=new Molecule2();
		Matcher ma = p__AsciiMath.matcher(AsciiMath);
		while (ma.find()) {
		//	System.out.println(ma.group()+":"+ma.toString() +":"+ma.groupCount()+":"+ma.group(0));
			String sym = ma.group(1).trim();
			
			String nb =null;
			if(ma.group().contains("_")||ma.group().contains("^"))
			nb= ma.group(2).trim();
			String e =null;
			if(ma.group().contains("^")&&ma.group().contains("_"))
				e= ma.group(3).trim();	
			if (nb!=null && nb.startsWith("^"))
				{e=nb;nb=null;}
			if (nb==null)
				nb="_1";
			if (e==null)
				e="^0+";
			Atom a = Atom.Factory(sym);
			int N=Integer.parseInt(nb.substring(1,nb.length()));
			if (m.compositions.get(a)!=null)
				N+=	m.compositions.get(a);
			m.compositions.put(a,N);
			
			String s=e.substring(e.length()-1);
			if ("+".equals(s))
			m.charge=Integer.parseInt(e.substring(1,e.length()-1));
			if ("-".equals(s))
				m.charge=-Integer.parseInt(e.substring(1,e.length()-1));
			
			}
		return m;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.chemistry.IMolecule#getAtoms()
	 */
	@Override
	public Collection<IAtom> getAtoms() {
		Collection<IAtom> compo=new ArrayList<IAtom>();
		for(IAtom a : compositions.keySet())
			for(int i=0;i<compositions.get(a);i++)
			compo.add(a);
		return compo;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.chemistry.IMolecule#toDot()
	 */
	@Override
	public String toDot() {
		// TODO Auto-generated method stub
		return null;
	}
	Map<IAtom,Integer> compositions;
	//e<sup>x</sup> =>e^x
	//https://fr.acervolima.com/html-balises-en-indice-et-en-exposant/
	//https://codebeautify.org/htmlviewer
	public String toHtml() {
		String s="";
		for(IAtom a:compositions.keySet())
			s+=a.getSymbol()+(compositions.get(a)==1?"":"<sub>"+compositions.get(a)+"</sub>");
		s+=(getCharge()==0?"":"<sup>"+Math.abs(getCharge())+(getCharge()>0?"+":"-")+"</sup>");
		return s;
	}
	
	//https://codepen.io/bqlou/pen/yOgbmb
	public String toAsciiMathML() {
		String s="";
		for(IAtom a:compositions.keySet())
			s+=a.getSymbol()+(compositions.get(a)==1?"":"_"+compositions.get(a)+"");
		s+=(getCharge()==0?"":"^"+Math.abs(getCharge())+(getCharge()>0?"+":"-")+"");
		return s;
	}
	//https://codepen.io/bqlou/pen/yOgbmb
	public String toLatex() {
		String s="";
		for(IAtom a:compositions.keySet())
			s+=a.getSymbol()+(compositions.get(a)==1?"":"_{"+compositions.get(a)+"}");
		s+=(getCharge()==0?"":"^{"+Math.abs(getCharge())+(getCharge()>0?"+":"-")+"}");
		return s;
	}
	/** charge of ion */
	int charge=0;
	public int getCharge() {		
		return charge;
	}

	//https://developer.mozilla.org/en-US/docs/Web/MathML/Element/mmultiscripts
	//https://codepen.io/bqlou/pen/yOgbmb
	public String toMathML() {
		String s="<math>\r\n" + 
				"\r\n" + 
				"    <mmultiscripts>\r\n" + 
				"\r\n" ; 
		
		
			 
				
				
			
		for(IAtom a:compositions.keySet())
			s+=
					"        <mi>"+a.getSymbol()+"</mi>      <!-- base expression -->\r\n" 
			+				"\r\n"  
							+ 	"        <mi>"+(compositions.get(a)==1?"":""+compositions.get(a)+"")+"</mi>      <!-- postsubscript -->\r\n" + 
			"        <mi>"+(getCharge()==0?"":""+Math.abs(getCharge())+(getCharge()>0?"+":"-")+"")+"</mi>      <!-- postsuperscript -->\r\n" + 
			"\r\n" + 
			"        <mprescripts />\r\n" + 
			"        <mi></mi>      <!-- presubscript -->\r\n" + 
			"        <mi></mi>      <!-- presuperscript -->\r\n" 	;
		
		s+=	"\r\n" + 
		"    </mmultiscripts>\r\n" + 
				"\r\n" + 
				"</math>\r\n" ;
		return s;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Molecule2 m = Molecule2.parseAsciiMath("SO_4^2-");
		System.out.print(m.toAsciiMathML());
		System.out.print(m.toLatex());
		System.out.print(m.toMathML());
		
		

	}

}
