package com.zoubworld.electronic.logic;

import java.util.List;
/**
 * Igate is the interface of Agate,a basic gate, the behaviour is define in java code/
 * also Igate is the interface of GateInGate, a complex gate composed by several ones.
 * the behaviour of GateInGate, is define by a structure of IGate.
 * the input and output are Bit, that can be collected into a Bus.
 * 
 * */
public interface Igate {

	public Bit getOutput();
	public List<Bit> getInputs();
	public List<Bit> getOutputs();
	public List<String> getInputsNomenclature();
	public List<String> getOutputsNomenclature();
	public void setName(String name);
	/** return the max number of tp to wait(refresh();apply();
	 * */
	public int getMaxTp();
	/** indicate if there is a comming change on apply.
	 * */
	public boolean willChange();
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
	public String toTruthTable();
	/** this reset internal state to come back as a just created gate :  uninitialised memory point
	 * */
	public void init();
}
