package com.zoubworld.electronic.logic.gates;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.Igate;

public class NOr extends Agate  implements Igate {

	public NOr(List<Bit> Inputs) {
		super(Inputs);/*
		this.Inputs=Inputs;
		output=new Bit();*/
	}
public NOr(Bit bit, Bit bit2) {
	super(bit,bit2);/*
	this.Inputs=new ArrayList();
	Inputs.add(bit);
	Inputs.add(bit2);	
	output=new Bit();*/
	}

	public void refresh() {
	/*	try {
		nextValue=false;
		for(Bit b: Inputs)
			nextValue|=b.Value();
		nextValue=!nextValue;
		}
		catch (java.lang.NullPointerException e) {
			if (!nextValue)
			nextValue=null;
		}*/
		nextValue=false;
		for(Bit b: Inputs)
			if (b.Value()==null)
				{if(nextValue!=null && !nextValue)
				nextValue=null;}
				else
					if (b.Value())
			nextValue=true;
		
		
		if(nextValue!=null)
		nextValue=!nextValue;
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
		lo.add("OutN"+0);
		return lo;
	}
}
