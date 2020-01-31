package com.zoubworld.electronic.logic.gates;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.Igate;

public class And extends Agate implements Igate  {

	public And(List<Bit> Inputs) {
		super(Inputs);		
		output=getOutput();
	}
Bit output;
	@Override
	public Bit getOutput() {
	
		return output;
	}
	public void refresh() {
		/*
	try {
		nextValue=true;
		for(Bit b: Inputs)
			nextValue&=b.Value();
	}
	catch (java.lang.NullPointerException e) {
		nextValue=null;
	}
		*/
		nextValue=true;
	for(Bit b: Inputs)
		if (b.Value()==null)
			if(nextValue)
			nextValue=null;
			else
				if (!b.Value())
		nextValue=false;
	}
	Boolean nextValue;
	@Override
	public void apply() {
		output.setValue(nextValue);
	}
	List<Bit> Inputs;
	@Override
	public List<Bit> getInputs() {
		return Inputs;
	}
	@Override
	public List<Bit> getOutputs() {
		List<Bit> lo=new ArrayList<Bit>();
		lo.add(output);
		return lo;
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
		lo.add("Out"+1);
		return lo;
	}
}
