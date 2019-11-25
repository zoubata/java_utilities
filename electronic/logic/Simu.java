package com.zoubworld.electronic.logic;

import java.util.ArrayList;
import java.util.List;

import org.jruby.javasupport.JavaUtil;

import com.zoubworld.utils.JavaUtilList;
import com.zoubworld.utils.JavaUtils;

public class Simu {

	public Simu() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		List<Bit> inputs=new ArrayList();
		Igate not=new Not(inputs);
		Igate next=not;
		int N=6;
		List<Igate> l=new ArrayList();
		 l.add(0,next);
		next.getOutput().setName("O"+0);
		for(int i=1;i<N;i++)
		{
		 next=new One(next.getOutput());
		 l.add(0,next);
		 next.getOutput().setName("O"+i);
		}
		inputs.add(next.getOutput());//loop it
		next.getOutput().setValue(false);
	
		
		List<Bit> Ixor=new ArrayList();
		Ixor.add(Bit.find("O2"));
		Ixor.add(Bit.find("O5"));
		Igate go=new Xor(Ixor);
		l.add(0,go);go.getOutput().setName("XOR2");

		Ixor=new ArrayList();
		Ixor.add(Bit.find("O0"));
		Ixor.add(Bit.find("O2"));
		Igate g01=new Xor(Ixor);
		g01.getOutput().setName("XOR02");
		l.add(0,g01);
		

		Ixor=new ArrayList();
		Ixor.add(Bit.find("O2"));
		Ixor.add(Bit.find("O4"));
		Igate g23=new Xor(Ixor);
		g23.getOutput().setName("XOR24");
		l.add(0,g23);

		Ixor=new ArrayList();
		Ixor.add(Bit.find("O4"));
		Ixor.add(Bit.find("O0"));
		g23=new Nxor(Ixor);
		g23.getOutput().setName("NXOR46");
		l.add(0,g23);
		
		Ixor=new ArrayList();
		Ixor.add(Bit.find("O0"));
		Ixor.add(Bit.find("O1"));
		g01=new Xor(Ixor);
		g01.getOutput().setName("XOR01");
		l.add(0,g01);
		
		Ixor=new ArrayList();
		Ixor.add(Bit.find("O1"));
		Ixor.add(Bit.find("O2"));
		g23=new Xor(Ixor);
		g23.getOutput().setName("XOR12");
		l.add(0,g23);
		
		Ixor=new ArrayList();
		Ixor.add(Bit.find("O2"));
		Ixor.add(Bit.find("O3"));
		g23=new Xor(Ixor);
		g23.getOutput().setName("XOR23");
		l.add(0,g23);
		
		Ixor=new ArrayList();
		Ixor.add(Bit.find("O3"));
		Ixor.add(Bit.find("O4"));
		g23=new Xor(Ixor);
		g23.getOutput().setName("XOR34");
		l.add(0,g23);
		

		Ixor=new ArrayList();
		Ixor.add(Bit.find("O4"));
		Ixor.add(Bit.find("O5"));
		Igate g45=new Xor(Ixor);
		g45.getOutput().setName("XOR45");
		l.add(0,g45);
		
		Ixor=new ArrayList();
		Ixor.add(Bit.find("O5"));
		Ixor.add(Bit.find("O0"));
		g45=new Nxor(Ixor);
		g45.getOutput().setName("NXOR56");
		l.add(0,g45);
		
		Ixor=new ArrayList();
		Ixor.add(Bit.find("XOR01"));
		Ixor.add(Bit.find("XOR23"));
		Ixor.add(Bit.find("XOR45"));
		Igate gg=new Or(Ixor);
		gg.getOutput().setName("OR6");
		l.add(0,gg);
		
		
		//XOR2/OR6/O0
		inputs=new ArrayList();
		gg=new Not(inputs);
		Ixor=new ArrayList();
		Ixor.add(Bit.find("OR6"));
		Ixor.add(gg.getOutput());
		gg.getOutput().setName("NFlopD3");
		l.add(0,gg);
		gg=new FlopD(Ixor);
		gg.getOutput().setName("FlopD3");
		inputs.add(gg.getOutput());		
		l.add(0,gg);
	
				inputs=new ArrayList();
				gg=new Not(inputs);
				Ixor=new ArrayList();
				Ixor.add(Bit.find("FlopD3"));//CLK
				Ixor.add(gg.getOutput());//DIN
				gg.getOutput().setName("NFlopD4");
				l.add(0,gg);
				gg=new FlopD(Ixor);
				gg.getOutput().setName("FlopD4");
				inputs.add(gg.getOutput());		
				l.add(0,gg);
			
				
		//core:
// 1..128 /2 div 
// 1,2,3,6 * Fringosc=133Mhz/1Ghz
//out: 1Ghz...1Mhz		
//DFLL:div=trimfine,div=trimcoarse; MUL:1..65536 : 64Mhz/1Khz 800Mhz/32Khz
		
		int count=0;
		Bus b=new Bus();
		b.add(Bit.find("OR6"));
		b.add(Bit.find("FlopD3"));
		b.add(Bit.find("XOR2"));
		b.add(Bit.find("FlopD4"));
		b.add(Bit.find("O1"));
		for(Igate g:l)
		{
			//g.getOutput().setName(""+(char)('A'+count++));
			b.add(g.getOutput());
		}
		//		Bit out=not.getOutput();
		Bit out=(go).getOutput();
		System.out.println(b.toHeader());		
		for(int i=0;i<24;i++)
		{
			for(Igate g:JavaUtilList.listToSet(l))
				g.refresh();
			for(Igate g:JavaUtilList.listToSet(l))
				g.apply();
				
	/*		for(Igate g:l)
				System.out.print(g.getOutput().value?"1":"0");
			System.out.print(out.value?"1":"0");
			System.out.print((g01).getOutput().value?"1":"0");
			System.out.print((g23).getOutput().value?"1":"0");
			System.out.print();*/
			System.out.print(b.toString());
			System.out.println();
		}
	}

}

