
package com.zoubworld.electronic.logic.gates;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.GateInGates;
import com.zoubworld.electronic.logic.Igate;
import com.zoubworld.electronic.logic.complexgate.MuxInGate;
/**
 * in0=CLOCK
 * IN1=Din
 * OUT=DOut
 * */
public class FlopD extends Agate  implements Igate {

	public FlopD(List<Bit> Inputs) {
		super(Inputs);/*this.Inputs=Inputs;
		output=new Bit();*/
		this.getOutput().setValue(null);
		init();
	}

	int in0state=-1;
	public void refresh() {

		if((in0state==0) && Inputs.get(1).Value())
			nextValue=(Inputs.get(0).Value());
		in0state=Inputs.get(1).Value()?1:0;
	}

	@Override
	public void init() {
		super.init();
		in0state=-1;
	}

	
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("Din");
		lo.add("Clk");
		return lo;
	}
	@Override
	public List<String> getOutputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("Dout");
		return lo;
	}
	
	public static void main(String[] args) {
		List<Bit> inputs=new ArrayList();
		inputs.add((new Bit("I1")));
		inputs.add((new Bit("I2")));
	//	inputs.add((new Bit("S")));
		Agate gg=new FlopD(inputs);
		int i=0;
		for(String n:gg.getInputsNomenclature())
			inputs.get(i++).setName(n);
		 i=0;
		for(String n:gg.getOutputsNomenclature())
			gg.getOutputs().get(i++).setName(n);
		System.out.println(gg.toString());
		System.out.println(gg.toTruthTable());
/*		System.out.println(gg.toVerilog());
		System.out.println(gg.toGraphviz());
		*/
		for(String line:gg.toWave(50,4))
			System.out.println(line);
		
	}
}
