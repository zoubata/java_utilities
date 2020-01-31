package com.zoubworld.electronic.logic;

import java.util.List;

public interface Igate {

	public Bit getOutput();
	public List<Bit> getInputs();
	public List<Bit> getOutputs();
	public List<String> getInputsNomenclature();
	public List<String> getOutputsNomenclature();
	/** this refresh compute the next output based on input.
	 * 
	 * */
	public void refresh();

	/** this apply the compute the next output based on input.
	 * 
	 * */
	public void apply();
	public void setInputs(Bit in0, Bit in1);
	public void setInputs(Bit in0, Bit in1, Bit in2);
	public void setInputs(Bit in0);
	public void setInputs(List<Bit> ins);
	public String getName();
	
}
