package com.zoubworld.electronic.logic;

import java.util.List;

public interface Igate {

	public Bit getOutput();
	public List<Bit> getInputs();
	
	/** this refresh compute the next output based on input.
	 * 
	 * */
	public void refresh();

	/** this apply the compute the next output based on input.
	 * 
	 * */
	public void apply();
}
