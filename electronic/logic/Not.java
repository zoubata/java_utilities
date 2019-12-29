package com.zoubworld.electronic.logic;

import java.util.ArrayList;
import java.util.List;

public class Not implements Igate {

	public Not(List<Bit> Inputs) {
		if (Inputs.size()>1)
		{this.Inputs=new ArrayList<Bit>();
		this.Inputs.add(Inputs.get(0));
		}
		else
		this.Inputs=Inputs;
		output=new Bit();
	
	}
	public Not(Bit Input) {
		this.Inputs=new ArrayList<Bit>();
		this.Inputs.add(Input);
		
		output=new Bit();
		
	}
Bit output;
	@Override
	public Bit getOutput() {
	
		return output;
	}
	public void refresh() {
		 nextValue=(!Inputs.get(0).value);
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
