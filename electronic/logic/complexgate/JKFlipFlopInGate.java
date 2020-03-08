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
 * JK flip-flop
 * https://en.wikipedia.org/wiki/Flip-flop_(electronics)#JK_flip-flop
 *
 */
public class JKFlipFlopInGate extends GateInGates {
	public List<Bit> internal=new ArrayList<Bit>();
	
	/**
	 * @param Inputs
	 */
	public JKFlipFlopInGate(List<Bit> Inputs) {
		super(Inputs);
		
		gates=new ArrayList<Igate>();	
		gates.add(new NAnd(null));
		gates.add(new NAnd(null));			
		Outputs.clear();
		Outputs.add(gates.get(0).getOutput());
		Outputs.add(gates.get(1).getOutput());
		
		gates.add(new NAnd(Inputs.get(0),Inputs.get(1),Outputs.get(1)));//2
		gates.add(new NAnd(Inputs.get(3),Inputs.get(1),Outputs.get(0)));//3	
		
		
		
		internal.add(gates.get(2).getOutput());
		internal.add(gates.get(3).getOutput());
	    
		
		
		gates.get(0).setInputs(gates.get(1).getOutput(),gates.get(2).getOutput());
		gates.get(1).setInputs(gates.get(0).getOutput(),gates.get(3).getOutput());
		
	
		gates.get(0).getOutput().setName("Q");
		gates.get(1).getOutput().setName("nQ");
		gates.get(2).getOutput().setName("nS");
		gates.get(3).getOutput().setName("nR");
		
	}

	
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("J");
		lo.add("CLK");
		lo.add("K");
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
