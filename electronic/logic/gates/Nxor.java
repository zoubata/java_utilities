package com.zoubworld.electronic.logic.gates;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.Igate;

public class Nxor extends Agate  implements Igate {

	public Nxor(List<Bit> Inputs) {
		super(Inputs);/*this.Inputs=Inputs;
		output=new Bit();*/
	}
	public void refresh() {
		try {
		nextValue=false;
		for(Bit b: Inputs)
			nextValue^=b.Value();
		nextValue=!nextValue;
	}
	catch (java.lang.NullPointerException e) {
		nextValue=null;
	}
	}
	Boolean nextValue;
	@Override
	public void apply() {
		getOutput().setValue(nextValue);
	}


	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		for(int i=0;i<Math.max(Inputs.size(),2);i++)
		lo.add("In"+i);
		return lo;
	}
	@Override
	public List<String> getOutputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		lo.add("OutN"+1);
		return lo;
	}
}
