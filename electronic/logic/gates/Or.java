package com.zoubworld.electronic.logic.gates;

import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.Igate;

public class Or extends Agate  implements Igate {

	public Or(List<Bit> Inputs) {
		super(Inputs);	
	}

	public Or(Bit bit, Bit bit2) {
		super(bit,bit2);
	}

	public void refresh() {
		nextValue=false;
	for(Bit b: Inputs)
		if (b.Value()==null)
			{if(nextValue!=null && !nextValue)
			nextValue=null;}
			else
				if (b.Value())
		nextValue=true;
	/*
		try {
		nextValue=false;
		for(Bit b: Inputs)
			nextValue|=b.Value();
		}
		catch (java.lang.NullPointerException e) {
			nextValue=null;
		}*/
	}


	

}
