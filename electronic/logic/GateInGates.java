package com.zoubworld.electronic.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.zoubworld.utils.JavaUtils;

public class GateInGates extends Agate {

	/**
	 * list of internal gate
	 */
	protected List<Igate> gates = new ArrayList<Igate>();

	/**
	 * @return the gates
	 */
	public List<Igate> getGates() {
		return gates;
	}
	public GateInGates(List<Bit> Inputs) {
		super(Inputs);
	}
	
	@Override
	public void init() {
		super.init();	
		if (getGates()!=null)
		for(Igate bo:getGates())
			bo.init();
		for(Bit bo:getOutputs())
			bo.init();
		
	}
	
	public Collection<Bit> getInternalWires()
	{
		Collection<Bit> ls=new HashSet<Bit>();
		for(Igate g:gates)
		{
			ls.addAll(g.getInputs());
			ls.addAll(g.getOutputs());
			if (GateInGates.class.isInstance(g))
			ls.addAll(((GateInGates)g).getInternalWires());
			
			}
		ls.removeAll(getInputs());
		ls.removeAll(getOutputs());
		
		return JavaUtils.asSortedSet(ls);
		
	}
	public void refresh() {
		for (Igate g : gates)
			g.refresh();
	}

	public String toGraphviz() {
		String s = "";
		int count = 0;
		s += "digraph "+this.getName()+" {"
				+ "\r\n";// subgraph cluster_0 { label=\""+this.getName()+"\";
		s+="  // rankdir is a graph-level attribute\r\n" + 
				"  rankdir=\"LR\"\r\n";
		for (Igate g : gates)
			s += g.getName() + "[shape=box]\r\n";// diamond]box]circle]
		for (Igate g : gates) {
			for (Bit in : g.getInputs()) {
				Igate g2 = getgateOfOutput(in);
				if (g2 != null)
					s += g2.getName() + " -> " + g.getName() + " [label=\"" + in.getName() + ":" + in.value() + "\"]\r\n";

			}

		}

		for (Bit in : getInputs()) {
			for (Igate g : gates)
				if (g.getInputs().contains(in)) {
					s += "in" + count + " -> " + g.getName() + " [label=\"" + in.getName() + ":" + in.value() + "\"]\r\n";
				}
			count++;
		}
		/*
		s += "{ rank=same, ";
		for (int i = 0; i < count; i++)
			s += ("in" + i + "; ");
		s += " }";*/
		{s+=" subgraph subsin {\r\n" + 
				"    rank=\"same\"\r\n" ; 
		for (int i = 0; i < count; i++)
			s += ("in" + i + "\r\n");
				s+=	"  }";
			}
		count = 0;
		for (Bit in : getOutputs()) {
			for (Igate g : gates)
				if (g.getOutputs().contains(in))
					s += g.getName() + " -> " + "out" + count + " [label=\"" + in.getName() + ":" + in.value()
							+ "\"]\r\n";
			count++;
		}
	/*	s += "{ rank=same, ";
		for (int i = 0; i < count; i++)
			s += ("out" + i + "; ");
		s += " }";*/
{s+=" subgraph subsout {\r\n" + 
		"    rank=\"same\"\r\n" ; 
for (int i = 0; i < count; i++)
	s += ("out" + i + "\r\n");
		s+=	"  }\r\n";
	}
		// { rank=same; out0, out1 }
		// { rank=same; in0, in1, in2 }

		s += "}\r\n";
		return s;
	}
	/** return the gate of this out wire
	 * 	*/
	private Igate getgateOfOutput(Bit out) {
		for (Igate g : gates) {
			if (g.getOutputs().contains(out))
				return g;
		}
		return null;
	}

	@Override
	public void apply() {
		for (Igate g : gates)
			g.apply();
		
	}

	/**
	 * indicate if there is a comming change on apply.
	 */
	@Override
	public boolean willChange() {
		for (Igate g : gates)
			if(g.willChange())
				return true;
		return false;
	}
public int getMaxTp() {
	int i=0;
	for (Igate g : gates)
		i+=g.getMaxTp();
		return i*4;
	}
	public String toVerilog()
	{
		String in="";
		for(Bit b:getInputs())
			in+=b.getName()+", ";
		String out="";
		for(Bit b:getOutputs())
			out+=b.getName()+", ";
		out=out.substring(0,out.length()-2);
		in=in.substring(0,in.length()-2);
		String wire="";
		for(Bit b:getInternalWires())
			wire+=b.getName()+", ";
		wire=wire.substring(0,wire.length()-2);
		String s="module "+getClass().getSimpleName()+"("+out+", "+in+");\r\n";
		//for(Bit b:getInputs())
			s+="input "+in+";\r\n";
	//	for(Bit b:getOutputs())
			s+="output "+out+";\r\n";
			s+="wire "+wire+";\r\n";
			for(Igate g: gates)
			{s+=
				g.getClass().getSimpleName()+" "+g.getName()+"("+g.getOutputs().toString().replace("[", "").replace("]", "")+","+g.getInputs().toString().replace("[", "").replace("]", "")+");\r\n";
			}
		s+="";
		s+="endmodule\r\n";		
		return s;
	}
	public boolean isCombinatory()
	{ 
	//	return true/false;compute loop
	return true;	
		
	}
}