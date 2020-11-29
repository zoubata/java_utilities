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
/**
 * @author Pierre V
 * 
 * Gated SR latch
 * https://en.wikipedia.org/wiki/Flip-flop_(electronics)#Gated_SR_latch
 *
 */
public class RsFlopSyncInGate extends GateInGates {

	/**
	 * @param Inputs
	 */
	public RsFlopSyncInGate(List<Bit> Inputs) {
		super(Inputs);
		Outputs.add(new Bit());
		gates=new ArrayList<Igate>();	
		gates.add(new NAnd(Inputs.get(0),Inputs.get(1)));
		gates.add(new NAnd(Inputs.get(2),Inputs.get(1)));			
		gates.add(new NAnd(gates.get(0).getOutput()/*nR*/,Outputs.get(1)));
		gates.add(new NAnd(gates.get(1).getOutput()/*nS*/,Outputs.get(0)));
		Outputs.clear();
		Outputs.add(gates.get(2).getOutput());
		Outputs.add(gates.get(3).getOutput());
		
		gates.get(2).getOutput().setName("Q");
		gates.get(3).getOutput().setName("nQ");
		gates.get(1).getOutput().setName("nS");
		gates.get(0).getOutput().setName("nR");
	}

	
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("S");
		lo.add("E");
		lo.add("R");
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
