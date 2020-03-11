/**
 * 
 */
package com.zoubworld.electronic.logic;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.complexgate.DFlipFlopInGate;

/**
 * @author M43507
 *
 */
public class Chronos {

	/**
	 * 
	 */
	public Chronos() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	/*	ArrayList<Bit> Inputs = new ArrayList<Bit>();
		Inputs.add(new Bit("Din"));
		Inputs.add(new Bit("clk"));
		//Inputs.add(new Bit("I2"));	
		DFlipFlopInGate g=new DFlipFlopInGate(Inputs);
		
		List<Integer> l=new ArrayList<Integer>();
		l.add(0);
		l.add(1);
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(2);
		l.add(3);
		l.add(1);
		l.add(0);
		l.add(2);
		l.add(3);
		l.add(0);
		l.add(1);
		l.add(0);
		l.add(1);
		l.add(3);
		l.add(1);
		l.add(0);
		l.add(2);
		l.add(0);
		
		
		
		
		
		System.out.println(display(g,l));
		System.out.println(g.toGraphviz());*/
	}

	public static String display(GateInGates g, List<Integer> l) {
		Bus bi=new Bus();
		bi.addAll(g.getInputs());
		Bus b=new Bus();
		b.addAll(g.getInputs());
		b.addAll(g.getOutputs());
	//	b.addAll(g.internal);
		String s[]=new String[b.getInputs().size()];
		int i=0;
		int kj=0;
		for(Bit e:b.getInputs())
			s[i++]=String.format("%16s:", e.getName()+((kj<g.getInputsNomenclature().size())?("("+g.getInputsNomenclature().get(kj++)+")"):("("+g.getOutputsNomenclature().get(i-1-bi.getInputs().size())+")")));
		i=0;
		for(Bit e:b.getInputs())
			s[i++]+=e.Value()==null?"x":((e.Value())?"1":"0");
		i=0;
		bi.setValue(l.get(0));
		for(Bit e:b.getInputs())
			s[i++]+=e.Value()==null?"x":((e.Value())?"1":"0");
		for(Integer v:l)
		{
			bi.setValue(v);
			for(int j=0;j<5;j++)
			{
			g.refresh();g.apply();
			i=0;
			for(Bit e:b.getInputs())
				s[i++]+=e.Value()==null?"x":((e.Value())?"1":"0");
			}
			
			
		}
		String ss="";
		for(String e:s)
			ss+=e+"\r\n";
		return ss;
	}

}
