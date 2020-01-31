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
 */
public class RsFlopSyncInGate extends GateInGates {

	/**
	 * @param Inputs
	 */
	public RsFlopSyncInGate(List<Bit> Inputs) {
		super(Inputs);
		Outputs.add(new Bit());
		gates=new ArrayList();	
		gates.add(new NAnd(Inputs.get(0),Inputs.get(1)));
		gates.add(new NAnd(Inputs.get(2),Inputs.get(1)));			
		gates.add(new NAnd(gates.get(0).getOutput()/*nR*/,Outputs.get(1)));
		gates.add(new NAnd(gates.get(1).getOutput()/*nS*/,Outputs.get(0)));
		Outputs.clear();
		Outputs.add(gates.get(3).getOutput());
		Outputs.add(gates.get(4).getOutput());
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
