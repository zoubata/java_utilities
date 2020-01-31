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
import com.zoubworld.electronic.logic.gates.NOr;
import com.zoubworld.electronic.logic.gates.Not;

/**
 * @author Pierre V
 * Gated D latch
 * https://en.wikipedia.org/wiki/Flip-flop_(electronics)#Gated_D_latch
 */
public class DLatchInGate extends GateInGates {
	public List<Bit> internal=new ArrayList<Bit>();
	
	/**
	 * @param Inputs
	 */
	public DLatchInGate(List<Bit> Inputs) {
		super(Inputs);
		
		gates=new ArrayList();	
		gates.add(new Not(Inputs.get(0)));
		gates.add(new NAnd(Inputs.get(0),Inputs.get(1)));
		gates.add(new NAnd(gates.get(0).getOutput(),Inputs.get(1)));			
		
		gates.add(new NOr(null));
		gates.add(new NOr(null));
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

	
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("D");
		lo.add("CLK");
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
