
package com.zoubworld.electronic.logic.gates;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.Igate;
/**
 * in0=CLOCK
 * IN1=Din
 * OUT=DOut
 * */
public class FlopD extends Agate  implements Igate {

	public FlopD(List<Bit> Inputs) {
		super(Inputs);/*this.Inputs=Inputs;
		output=new Bit();*/
	}

	int in0state=-1;
	public void refresh() {

		if(in0state==0 &&Inputs.get(0).Value())
			nextValue=(Inputs.get(1).Value());
		in0state=Inputs.get(0).Value()?1:0;
	}

	boolean nextValue;
	@Override
	public void apply() {
		getOutput() .setValue(nextValue);
	}

	
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("Clk");
		lo.add("Din");
		return lo;
	}
	@Override
	public List<String> getOutputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("Dout");
		return lo;
	}
}
