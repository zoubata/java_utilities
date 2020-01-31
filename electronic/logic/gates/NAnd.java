package com.zoubworld.electronic.logic.gates;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.electronic.logic.Agate;
import com.zoubworld.electronic.logic.Bit;
import com.zoubworld.electronic.logic.Igate;

public class NAnd extends Agate  implements Igate {

	public NAnd(List<Bit> Inputs) {
		super(Inputs);/*this.Inputs=Inputs;
		output=new Bit();*/
	}
public NAnd(Bit bit, Bit bit2) {
	super(bit,bit2);/*this.Inputs=new ArrayList();
	Inputs.add(bit);
	Inputs.add(bit2);	
	output=new Bit();*/
	}

	
	public NAnd(Bit bit1, Bit bit2, Bit bit3) {
		super((List<Bit>)null);
		setInputs(bit1,bit2,bit3);
	}
	public void refresh() {
		
			nextValue=true;
		for(Bit b: Inputs)
			if (b.Value()==null)
				{if(nextValue!=null && nextValue)
				nextValue=null;}
				else
					if (!b.Value())
			nextValue=false;
		if(nextValue!=null)
		nextValue=!nextValue;
		
	
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
