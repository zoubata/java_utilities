package com.zoubworld.electronic.logic.gates;

import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.Igate;

public class One extends Agate  implements Igate {

	public One(List<Bit> Inputs) {
		super(Inputs);	
		/*
		if (Inputs.size()>1)
		{this.Inputs=new ArrayList<Bit>();
		this.Inputs.add(Inputs.get(0));
		}
		else
		this.Inputs=Inputs;
		output=new Bit();*/
	}
	public One(Bit Input) {
		super(Input);	

	}

	public void refresh() {
		/*
	try {
		nextValue=(Inputs.get(0).Value());
	}
	catch (java.lang.NullPointerException e) {
		nextValue=null;
	}*/
	if(Inputs.get(0).Value()!=null)
	nextValue=(Inputs.get(0).Value());
	else
		nextValue=null;
	}
	Boolean nextValue;
	@Override
	public void apply() {
		getOutput().setValue(nextValue);
	}


}
