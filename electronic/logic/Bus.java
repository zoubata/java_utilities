package com.zoubworld.electronic.logic;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.utils.JavaUtils;

public class Bus {

	List<Bit> bus;
	public Bus(List<Bit> bus) {
this.bus=bus;
	}
	public Bus() {
		// TODO Auto-generated constructor stub
	}
	public String toString()
	{
String s="";
for(Bit b:bus)
	s+=b.toString();
return s;
	}
	public String toHeader()
	{
String s="";
for(Bit b:bus)
	s+=b.getName()+"\n";

return JavaUtils.transpose(s,"\n");
	}
	public List<Bit> getInputs() {
		if (bus==null)
			bus=new ArrayList<Bit>();
		return bus;
	}
	public void add(Bit bit) {
		getInputs().add(bit);
		
	}
}
