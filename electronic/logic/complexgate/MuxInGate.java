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
import com.zoubworld.electronic.logic.gates.Not;
import com.zoubworld.electronic.logic.gates.Or;

/**
 * @author Pierre V
 * based on  AND-OR  :
 * https://en.wikipedia.org/wiki/Multiplexer
 *
 */
public class MuxInGate extends GateInGates {
	
	public static void main(String[] args) {
		List<Bit> inputs=new ArrayList();
		inputs.add((new Bit("I1")));
		inputs.add((new Bit("I2")));
		inputs.add((new Bit("S")));
		GateInGates gg=new MuxInGate(inputs);
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
		
		for(String line:gg.toWave(50,4))
			System.out.println(line);
		
	}
	/**
	 * @param Inputs
	 */
	public MuxInGate(List<Bit> Inputs) {
		super(Inputs);
		gates=new ArrayList<Igate>();
		
		gates.add(new Not(Inputs.get(0)));
		gates.add(new And(Inputs.get(1),gates.get(0).getOutput()));
		gates.add(new And(Inputs.get(0),Inputs.get(2)));

		gates.add(new Or(gates.get(1).getOutput(),gates.get(2).getOutput()));
		Outputs.clear();
		Outputs.add(gates.get(3).getOutput());
		
		gates.get(0).getOutput().setName("sb");
		gates.get(1).getOutput().setName("s0");
		gates.get(2).getOutput().setName("s1");
	}
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("S");
		lo.add("I1");
		lo.add("I2");
		return lo;
	}
	@Override
	public List<String> getOutputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		
		lo.add("Q");
		return lo;
	}
}
