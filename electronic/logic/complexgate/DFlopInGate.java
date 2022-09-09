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
 * https://en.wikipedia.org/wiki/Flip-flop_(electronics)#Gated_D_latch
 */
public class DFlopInGate extends GateInGates {
	public static void main(String[] args) {
		List<Bit> inputs=new ArrayList();
		inputs.add((new Bit("I1")));
		inputs.add((new Bit("I2")));
		GateInGates gg=new DFlopInGate(inputs);
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
	public DFlopInGate(List<Bit> Inputs) {
		super(Inputs);
		//SR Nor Latch
		
		gates=new ArrayList<Igate>();	
		gates.add(new Not(Inputs.get(0)));
		gates.add(new NAnd(Inputs.get(0),Inputs.get(1)));
		gates.add(new NAnd(gates.get(0).getOutput(),Inputs.get(1)));			
		
		gates.add(new NAnd(null));
		gates.add(new NAnd(null));
		Outputs.clear();
		Outputs.add(gates.get(3).getOutput());
		Outputs.add(gates.get(4).getOutput());
		gates.get(3).setInputs(gates.get(1).getOutput(),Outputs.get(1));
		gates.get(4).setInputs(gates.get(2).getOutput(),Outputs.get(0));
		
		gates.get(0).getOutput().setName("Dn");
		gates.get(1).getOutput().setName("Dc");
		gates.get(2).getOutput().setName("Dnc");
		gates.get(3).getOutput().setName("Q");
		gates.get(4).getOutput().setName("nQ");
		
		internal.add(gates.get(0).getOutput());
		internal.add(gates.get(1).getOutput());
	    internal.add(gates.get(2).getOutput());
	
	}

	public DFlopInGate(List<Bit> Inputs,String h) {
		super(Inputs);
		//SbRb Nand Latch
		gates=new ArrayList<Igate>();	
		gates.add(new NAnd(Inputs.get(0),Inputs.get(1)));
		gates.add(new NAnd(gates.get(0).getOutput(),Inputs.get(1)));			
		
		gates.add(new NAnd(null));
		gates.add(new NAnd(null));
		Outputs.clear();
		Outputs.add(gates.get(3).getOutput());
		Outputs.add(gates.get(4).getOutput());
		gates.get(2).setInputs(gates.get(0).getOutput(),Outputs.get(3));
		gates.get(3).setInputs(gates.get(2).getOutput(),Outputs.get(1));
		
		gates.get(0).getOutput().setName("Sn");
		gates.get(1).getOutput().setName("Rn");
		gates.get(2).getOutput().setName("Q");
		gates.get(3).getOutput().setName("nQ");
		
		internal.add(gates.get(0).getOutput());
		internal.add(gates.get(1).getOutput());
	
	}
	
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("E");
		lo.add("D");
		return lo;
	}
	@Override
	public List<String> getOutputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		
		lo.add("Q");
		lo.add("NQ");
		return lo;
	}
}
