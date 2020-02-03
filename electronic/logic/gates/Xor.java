package com.zoubworld.electronic.logic.gates;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.Igate;

public class Xor extends Agate  implements Igate {

	public Xor(List<Bit> Inputs) {
		super(Inputs);		
		
	}


	public Xor(Bit bit, Bit bit2) {
	super(bit,bit2);
	}


	public void refresh() {
		try {
		nextValue=false;
		for(Bit b: Inputs)
			nextValue^=b.Value();
		}
		catch (java.lang.NullPointerException e) {
			nextValue=null;
		}
	}
	Boolean nextValue;
	@Override
	public void apply() {
		getOutput().setValue(nextValue);
	}


}
