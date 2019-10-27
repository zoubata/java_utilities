
package com.zoubworld.electronic.logic;

import java.util.List;

import jnr.ffi.Struct.in_addr_t;
/**
 * in0=CLOCK
 * IN1=Din
 * OUT=DOut
 * */
public class FlopD implements Igate {

	public FlopD(List<Bit> Inputs) {
		this.Inputs=Inputs;
		output=new Bit();
	}
Bit output;
	@Override
	public Bit getOutput() {
	
		return output;
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
		output.setValue(nextValue);
	}
	List<Bit> Inputs;
	@Override
	public List<Bit> getInputs() {
		return Inputs;
	}
	

}
