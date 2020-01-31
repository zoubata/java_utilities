package com.zoubworld.electronic.logic.gates;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.Igate;

public class Not extends Agate  implements Igate {

	public Not(List<Bit> Inputs) {
		super(Inputs);
		/*if (Inputs.size()>1)
		{this.Inputs=new ArrayList<Bit>();
		this.Inputs.add(Inputs.get(0));
		}
		else
		this.Inputs=Inputs;
		output=new Bit();
	*/
	}
	public Not(Bit Input) {
		super(Input);
		/*
		this.Inputs=new ArrayList<Bit>();
		this.Inputs.add(Input);
		
		output=new Bit();*/
		
	}

	public void refresh() {
		/*
		try {
		 nextValue=(!Inputs.get(0).Value());
		}
		catch (java.lang.NullPointerException e) {
			nextValue=null;
		}*/
		if(Inputs.get(0).Value()!=null)
		nextValue=(!Inputs.get(0).Value());
		else
			nextValue=null;
			
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
