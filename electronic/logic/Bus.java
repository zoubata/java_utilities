package com.zoubworld.electronic.logic;

import java.util.ArrayList;
import java.util.List;

import org.python.google.common.collect.Lists;

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
		for(Bit b:Lists.reverse(bus))
			b.setValue((v&(1<<(i++)))!=0);
		
	}
	public Integer Value() {
		int i=0;
		int r=0;
		for(Bit b:Lists.reverse(bus))
			if(b.Value()==null)
				return null;
			else
				r+=(b.Value()?1:0)<<(i++);
		return r;
	}
	public String value() {

String s="";
for(Bit b:bus)
	s+=b.value();
return s;
	}
	/** create a copy with same name and value, but unlink to any sub object
	 * */
	
	public static Bus copy(Bus bbo) {
		List<Bit> l = new ArrayList();
		for(Bit b:bbo.getInputs())
		l.add(Bit.copy(b));
		
		return new Bus(l);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj){
		if (Bus.class.isInstance(obj))
		{Bus b=(Bus)obj;
		if (b.getInputs().size()!=getInputs().size())
			return false;
		for(int i=0;i<getInputs().size();i++)
			if(!this.getInputs().get(i).equals(b.getInputs().get(i)))
				return false;
		return true;
		
		}
		return super.equals(obj);
	}
}
