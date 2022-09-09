package com.zoubworld.electronic.logic.gates;

import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.Igate;

public class And extends Agate implements Igate  {

	public And(List<Bit> myInputs) {
		super(myInputs);		
		
	}
	public And(Bit bit, Bit bit2) {
		super(bit,bit2);
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
		{	if(nextValue!=null && nextValue)
			nextValue=null;}
			else
				if (!b.Value())
		nextValue=false;
	}

	
}
