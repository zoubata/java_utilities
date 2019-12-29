package com.zoubworld.electronic.logic;

import java.util.List;

public class Nxor implements Igate {

	public Nxor(List<Bit> Inputs) {
		this.Inputs=Inputs;
		output=new Bit();
	}
Bit output;
	@Override
	public Bit getOutput() {
	
		return output;
	}
	public void refresh() {
		nextValue=false;
		for(Bit b: Inputs)
			nextValue^=b.Value();
		nextValue=!nextValue;
	}
	boolean nextValue;
	@Override
	public void apply() {
		output.setValue(nextValue);
	}
	List<Bit> Inputs;
	@Override
	public List<Bit> getInputs() {
		return Inputs;
	}

}
