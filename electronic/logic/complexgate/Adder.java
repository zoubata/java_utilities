/**
 * 
 */
package com.zoubworld.electronic.logic.complexgate;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.GateInGates;
import com.zoubworld.electronic.logic.Igate;
import com.zoubworld.electronic.logic.gates.And;
import com.zoubworld.electronic.logic.gates.NAnd;
import com.zoubworld.electronic.logic.gates.NOr;
import com.zoubworld.electronic.logic.gates.Not;
import com.zoubworld.electronic.logic.gates.Or;
import com.zoubworld.electronic.logic.gates.Xor;

/**
 * @author Pierre V
 *
 */
public class Adder extends GateInGates {
	public List<Bit> internal=new ArrayList<Bit>();
	
	/**
	 * @param Inputs
	 */
	public Adder(List<Bit> Inputs) {
		super(Inputs);
		//Cin x y 
		
		gates=new ArrayList();	
		gates.add(new Xor(Inputs.get(1),Inputs.get(2)));
		gates.add(new And(Inputs.get(1),Inputs.get(2)));			
		
		gates.add(new Xor(Inputs.get(0),gates.get(0).getOutput()));
		gates.add(new And(Inputs.get(0),gates.get(0).getOutput()));			
		gates.add(new Or(gates.get(1).getOutput(),gates.get(3).getOutput()));			
		
		
		Outputs.clear();
	
		Outputs.add(gates.get(2).getOutput());
		Outputs.add(gates.get(4).getOutput());
		
		
		gates.get(0).getOutput().setName("S1");
		gates.get(1).getOutput().setName("c1");
		gates.get(3).getOutput().setName("c2");
		
		gates.get(2).getOutput().setName("sum");
		gates.get(4).getOutput().setName("cout");
		
	}

	
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("Cin");
		lo.add("x");
		lo.add("y");
		return lo;
	}
	@Override
	public List<String> getOutputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		
		lo.add("sum");
		lo.add("Cout");
		return lo;
	}
}
