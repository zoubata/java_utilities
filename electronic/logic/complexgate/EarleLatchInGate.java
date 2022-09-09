/**
 * 
 */
package com.zoubworld.electronic.logic.complexgate;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.GateInGates;
import com.zoubworld.electronic.logic.Igate;
import com.zoubworld.electronic.logic.gates.NAnd;
import com.zoubworld.electronic.logic.gates.Not;

/**
 * @author Pierre V
 *
 * A gated D latch based on an SR NOR latch
 * https://en.wikipedia.org/wiki/Flip-flop_(electronics)#Earle_latch
 */
public class EarleLatchInGate extends GateInGates {
	public static void main(String[] args) {
		List<Bit> inputs=new ArrayList();
		inputs.add((new Bit("I1")));
		inputs.add((new Bit("I2")));
		inputs.add((new Bit("I3")));
		
		GateInGates gg=new EarleLatchInGate(inputs);
		int i=0;
		for(String n:gg.getInputsNomenclature())
			inputs.get(i++).setName(n);
		 i=0;
		for(String n:gg.getOutputsNomenclature())
			gg.getOutputs().get(i++).setName(n);
		System.out.println(gg.toString());
		System.out.println(gg.toTruthTable());
		System.out.println(gg.toVerilog());
		System.out.println(gg.toGraphviz());
		
	}
	public List<Bit> internal=new ArrayList<Bit>();
	
	/**
	 * @param Inputs
	 */
	public EarleLatchInGate(List<Bit> Inputs) {
		super(Inputs);
		
		gates=new ArrayList<Igate>();	
		gates.add(new NAnd(null));
		
		gates.add(new NAnd(Inputs.get(0),Inputs.get(1)));
		gates.add(new NAnd(Inputs.get(1),gates.get(0).getOutput()));
		gates.add(new NAnd(Inputs.get(2),gates.get(0).getOutput()));			
		
		
		Outputs.clear();
		Outputs.add(gates.get(0).getOutput());
		
		gates.get(0).setInputs(gates.get(1).getOutput(),
				gates.get(2).getOutput(),
				gates.get(3).getOutput());
		
		gates.get(0).getOutput().setName("Q");
		gates.get(1).getOutput().setName("Hi");
		gates.get(2).getOutput().setName("Di");
		gates.get(3).getOutput().setName("Li");
		
		internal.add(gates.get(0).getOutput());
		internal.add(gates.get(1).getOutput());
	    internal.add(gates.get(2).getOutput());
	
	}

	
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("E_H");
		lo.add("D");
		lo.add("E_L");
		return lo;
	}
	@Override
	public List<String> getOutputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		
		lo.add("Q");
		return lo;
	}
}
