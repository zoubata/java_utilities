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
	public void addAll(List<Bit> bus) {
		getInputs().addAll(bus);
		
	}
	public void setValue(int v) {
		int i=0;
		for(Bit b:bus)
			b.setValue((v&(1<<(i++)))!=0);
		
	}
}
