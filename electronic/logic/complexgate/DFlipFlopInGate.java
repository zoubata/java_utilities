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
 * positive-edge-triggered D flip-flop
 * https://en.wikipedia.org/wiki/Flip-flop_(electronics)#Classical_positive-edge-triggered_D_flip-flop
 *
 */
public class DFlipFlopInGate extends GateInGates {
	public List<Bit> internal=new ArrayList<Bit>();
	
	/**
	 * @param Inputs
	 */
	public DFlipFlopInGate(List<Bit> Inputs) {
		super(Inputs);
		
		gates=new ArrayList<Igate>();	
		gates.add(new NAnd(null));
		gates.add(new NAnd(null));			
		Outputs.clear();
		Outputs.add(gates.get(0).getOutput());
		Outputs.add(gates.get(1).getOutput());
		
		
		
		gates.add(new NAnd(null));//2
		gates.add(new NAnd(null));//3	
		gates.add(new NAnd(null));//4
		gates.add(new NAnd(null));//5	
		
		
		
		gates.get(2).setInputs(gates.get(5).getOutput(),gates.get(3).getOutput());
		gates.get(3).setInputs(gates.get(2).getOutput(),Inputs.get(1));
		gates.get(4).setInputs(gates.get(3).getOutput(),Inputs.get(1),gates.get(5).getOutput());
		gates.get(5).setInputs(gates.get(4).getOutput(),Inputs.get(0));
		

		internal.add(gates.get(2).getOutput());
		internal.add(gates.get(3).getOutput());
		internal.add(gates.get(4).getOutput());
		internal.add(gates.get(5).getOutput());
	    
		
		
		gates.get(0).setInputs(gates.get(1).getOutput(),gates.get(3).getOutput());
		gates.get(1).setInputs(gates.get(0).getOutput(),gates.get(4).getOutput());
		
	
		gates.get(0).getOutput().setName("Q");
		gates.get(1).getOutput().setName("nQ");
		gates.get(2).getOutput().setName("a");
		gates.get(3).getOutput().setName("nS");
		gates.get(4).getOutput().setName("nR");
		gates.get(5).getOutput().setName("b");
		
	}

	
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("Din");
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
