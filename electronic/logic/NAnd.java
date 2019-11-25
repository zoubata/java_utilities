package com.zoubworld.electronic.logic;

import java.util.List;

import jnr.ffi.Struct.in_addr_t;

public class NAnd implements Igate {

	public NAnd(List<Bit> Inputs) {
		this.Inputs=Inputs;
		output=new Bit();
	}
Bit output;
	@Override
	public Bit getOutput() {
	
		return output;
	}
	public void refresh() {
		nextValue=true;
		for(Bit b: Inputs)
			nextValue&=b.Value();
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
