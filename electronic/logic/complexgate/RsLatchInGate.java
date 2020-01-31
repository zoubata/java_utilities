/**
 * 
 */
package com.zoubworld.electronic.logic.complexgate;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.GateInGates;
import com.zoubworld.electronic.logic.gates.And;
import com.zoubworld.electronic.logic.gates.NOr;
import com.zoubworld.electronic.logic.gates.Not;
import com.zoubworld.electronic.logic.gates.Or;

/**
 * @author Pierre V
 * based on SR AND-OR latch :
 * https://en.wikipedia.org/wiki/Flip-flop_(electronics)#SR_AND-OR_latch
 *
 */
public class RsLatchInGate extends GateInGates {
	/**
	 * @param Inputs
	 */
	public RsLatchInGate(List<Bit> Inputs) {
		super(Inputs);
		gates=new ArrayList();		
		gates.add(new Not(Inputs.get(0)));
		gates.add(new And(null));
		Outputs.clear();
		Outputs.add(gates.get(1).getOutput());
		gates.add(new Or(Outputs.get(0),Inputs.get(1)));
		
		gates.get(1).setInputs(gates.get(0).getOutput(),gates.get(2).getOutput());
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
		return lo;
	}
}
