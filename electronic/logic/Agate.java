/**
 * 
 */
package com.zoubworld.electronic.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pierre V
 *
 */
public class Agate implements Igate {

	/** list of inputs
	 * */
	protected List<Bit> Inputs;
	/** list of outputs
	 * */
	protected List<Bit> Outputs;
	
	@Override
	public List<Bit> getInputs() {
		return Inputs;
	}
	@Override
	public Bit getOutput() {
	if(Outputs!=null && Outputs.size()>0)
		return Outputs.get(0);
	return null;
	}
	@Override
	public List<Bit> getOutputs() {

		return Outputs;
	}
	public void refresh() {
		//@todo
	}

	boolean nextValue;
	@Override
	public void apply() {
		//@todo
	}
	/**
	 * 
	 */
	public Agate(List<Bit> myInputs) {
		this.Inputs=myInputs;
		Outputs=new ArrayList<Bit>();
		Outputs.add(new Bit());
	}
	public Agate(Bit bit, Bit bit2) {
		this.Inputs=new ArrayList();
		Inputs.add(bit);
		Inputs.add(bit2);		
		Outputs=new ArrayList<Bit>();
		Outputs.add(new Bit());
	}
	public Agate(Bit bit) {
		this.Inputs=new ArrayList();
		Inputs.add(bit);		
		Outputs=new ArrayList<Bit>();
		Outputs.add(new Bit());
	}
	
	@Override
	public List<String> getInputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		for(int i=0;i<Math.max(Inputs.size(),1);i++)
		lo.add("In"+i);
		return lo;
	}
	@Override
	public List<String> getOutputsNomenclature() {
		List<String> lo=new ArrayList<String>();
		for(int i=0;i<Math.max(Outputs.size(),1);i++)
		lo.add("Out"+i);
		return lo;
	}
	@Override
	public void setInputs(Bit in0, Bit in1) {
		Inputs=new ArrayList<Bit>();
		Inputs.add(in0);
		Inputs.add(in1);		
	}
	@Override
	public void setInputs(Bit in0, Bit in1, Bit in2) {
		Inputs=new ArrayList<Bit>();
		Inputs.add(in0);
		Inputs.add(in1);
		Inputs.add(in2);
	}
	@Override
	public void setInputs(Bit in0) {
		Inputs=new ArrayList<Bit>();
		Inputs.add(in0);
		
	}
	@Override
	public void setInputs(List<Bit> ins) {
		Inputs=ins;
		
	}
	String name=null;
	@Override
	public String getName() {
		if (name!=null)
		return name;
		return this.getClass().getSimpleName()+"_"+hashCode();
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	

}
