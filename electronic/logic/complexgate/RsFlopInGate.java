/**
 * 
 */
package com.zoubworld.electronic.logic.complexgate;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.GateInGates;
import com.zoubworld.electronic.logic.gates.NOr;

import com.zoubworld.electronic.logic.Igate;
/**
 * @author Pierre V
 *
 * SR NOR latch
 *https://en.wikipedia.org/wiki/Flip-flop_(electronics)#SR_NOR_latch
 */
public class RsFlopInGate extends GateInGates {
	/**
	 * @param Inputs
	 */
	public RsFlopInGate(List<Bit> Inputs) {
		super(Inputs);
		gates=new ArrayList<Igate>();		
		gates.add(new NOr(null));
		gates.add(new NOr(null));
		Outputs.clear();
		Outputs.add(gates.get(0).getOutput());
		Outputs.add(gates.get(1).getOutput());
		gates.get(0).setInputs(gates.get(1).getOutput(),Inputs.get(0));
		gates.get(1).setInputs(gates.get(0).getOutput(),Inputs.get(1));
		
		gates.get(0).getOutput().setName("Q");
		gates.get(1).getOutput().setName("nQ");
	}
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("R");
		//lo.add("G");
		lo.add("S");
		return lo;
	}
	@Override
	public List<String> getOutputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		
		lo.add("Q");
		lo.add("QN");
		return lo;
	}
}
