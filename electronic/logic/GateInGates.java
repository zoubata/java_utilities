package com.zoubworld.electronic.logic;

import java.util.ArrayList;
import java.util.List;

public class GateInGates extends Agate {

	/**
	 * list of internal gate
	 */
	protected List<Igate> gates = new ArrayList<Igate>();

	public GateInGates(List<Bit> Inputs) {
		super(Inputs);
	}

	public void refresh() {
		for (Igate g : gates)
			g.refresh();
	}

	public String toGraphviz() {
		String s = "";
		int count = 0;
		s += "digraph "+this.getName()+" {\n";// subgraph cluster_0 { label=\""+this.getName()+"\";
		s+="  // rankdir is a graph-level attribute\r\n" + 
				"  rankdir=\"LR\"\r\n";
		for (Igate g : gates)
			s += g.getName() + "[shape=box]\n";// diamond]box]circle]
		for (Igate g : gates) {
			for (Bit in : g.getInputs()) {
				Igate g2 = getgateOfOutput(in);
				if (g2 != null)
					s += g2.getName() + " -> " + g.getName() + " [label=\"" + in.getName() + ":" + in.value() + "\"]\n";

			}

		}

		for (Bit in : getInputs()) {
			for (Igate g : gates)
				if (g.getInputs().contains(in)) {
					s += "in" + count + " -> " + g.getName() + " [label=\"" + in.getName() + ":" + in.value() + "\"]\n";
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
							+ "\"]\n";
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

		s += "}\n";
		return s;
	}

	private Igate getgateOfOutput(Bit in) {
		for (Igate g : gates) {
			if (g.getOutputs().contains(in))
				return g;
		}
		return null;
	}

	@Override
	public void apply() {
		for (Igate g : gates)
			g.apply();
	}

}