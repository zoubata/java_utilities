package com.zoubworld.electronic.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.electronic.logic.gates.And;
import com.zoubworld.electronic.logic.gates.FlopD;
import com.zoubworld.electronic.logic.gates.NAnd;
import com.zoubworld.electronic.logic.gates.NOr;
import com.zoubworld.electronic.logic.gates.Not;
import com.zoubworld.electronic.logic.gates.Nxor;
import com.zoubworld.electronic.logic.gates.One;
import com.zoubworld.electronic.logic.gates.Or;
import com.zoubworld.electronic.logic.gates.Xor;

public class ElectronicTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void Nottest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit in0=new Bit();
		in0.setName("in0");
		inputs.add(in0);
		Igate not=new Not(inputs);
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
	//	inputs.get(1).setValue(false);
		not.refresh();not.apply();
		
		assertEquals(true, out.Value());
		assertEquals("1", out.toString());	
		inputs.get(0).setValue(true);
		//	inputs.get(1).setValue(false);
		not.refresh();not.apply();
			assertEquals(false, out.Value());	
			assertEquals("0", out.toString());	
			assertEquals("in0",in0.getName());
			assertEquals(true,out.getName().startsWith("@"));
			assertEquals(inputs,not.getInputs());
			
	}

	@Test
	public final void onetest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit in0=new Bit();
		in0.setName("in0");
		inputs.add(in0);
		Igate not=new One(inputs);
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
	//	inputs.get(1).setValue(false);
		assertEquals(false, out.Value());	
		not.refresh();
		assertEquals(false, out.Value());	
		not.apply();
		
		assertEquals(false, out.Value());
		inputs.get(0).setValue(true);
		//	inputs.get(1).setValue(false);
		assertEquals(false, out.Value());	
		not.refresh();
		assertEquals(false, out.Value());	
		not.apply();
			assertEquals(true, out.Value());	
			assertEquals("in0",in0.getName());
			assertEquals(inputs,not.getInputs());
			
			
			inputs.get(0).setValue(false);
			//	inputs.get(1).setValue(false);
				assertEquals(true, out.Value());	
				not.refresh();
				assertEquals(true, out.Value());	
				not.apply();
				
				assertEquals(false, out.Value());
			
	}
	@Test
	public final void Bustest() {
	Bus b=new Bus();
	Bit in0=new Bit();
	in0.setName("in0");
	Bit in1=new Bit();
	in1.setName("in1");
	Bit in2=new Bit();
	in2.setName("in2");
	b.add(Bit.find("in0"));
	b.add(Bit.find("in1"));
	b.add(Bit.find("in2"));
	List<Bit> inputs=new ArrayList<Bit>();
	inputs.add(in0);
	inputs.add(in1);
	inputs.add(in2);
	
	assertEquals("iii\n" + 
			"nnn\n" + 
			"012", b.toHeader());
	assertEquals("000", b.toString());
	assertEquals(inputs, b.getInputs());
	Igate g1=new Not(in0) ;
	Igate g2=new Not(inputs) ;
	b.add(g1.getOutput());
	b.add(g2.getOutput());
	Igate g3=new One(g1.getOutput()) ;
	Igate g4=new One(inputs) ;
	b.add(g3.getOutput());
	b.add(g4.getOutput());
	assertEquals("0000000", b.toString());
	g1.refresh();g1.apply();
	g2.refresh();g2.apply();
	g3.refresh();g3.apply();
	g4.refresh();g4.apply();
	
	assertEquals("0001110", b.toString());
	}
	@Test
	public final void Andtest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit in0=new Bit();
		in0.setName("in0");
		inputs.add(in0);
		Bit in1=new Bit();
		in1.setName("in1");
		inputs.add(in1);
		Igate not=new And(inputs);
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		inputs.get(0).setValue(true);
		not.refresh();not.apply();
		assertEquals(false, out.Value());		
		inputs.get(1).setValue(true);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		inputs.get(0).setValue(false);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		assertEquals(inputs,not.getInputs());
	}
	@Test
	public final void NOrtest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit in0=new Bit();
		in0.setName("in0");
		inputs.add(in0);
		Bit in1=new Bit();
		in1.setName("in1");
		inputs.add(in1);
		Igate not=new NOr(inputs);
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		inputs.get(0).setValue(true);
		not.refresh();not.apply();
		assertEquals(false, out.Value());		
		inputs.get(1).setValue(true);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		inputs.get(0).setValue(false);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		assertEquals(inputs,not.getInputs());
	}
	@Test
	public final void XOrtest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit in0=new Bit();
		in0.setName("in0");
		inputs.add(in0);
		Bit in1=new Bit();
		in1.setName("in1");
		inputs.add(in1);
		Igate not=new Xor(inputs);
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		inputs.get(0).setValue(true);
		not.refresh();not.apply();
		assertEquals(true, out.Value());		
		inputs.get(1).setValue(true);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		inputs.get(0).setValue(false);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		assertEquals(inputs,not.getInputs());
	}@Test
	public final void NXOrtest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit in0=new Bit();
		in0.setName("in0");
		inputs.add(in0);
		Bit in1=new Bit();
		in1.setName("in1");
		inputs.add(in1);
		Igate not=new Nxor(inputs);
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		inputs.get(0).setValue(true);
		not.refresh();not.apply();
		assertEquals(false, out.Value());		
		inputs.get(1).setValue(true);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		inputs.get(0).setValue(false);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		assertEquals(inputs,not.getInputs());
	}
	@Test
	public final void Ortest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit in0=new Bit();
		in0.setName("in0");
		inputs.add(in0);
		Bit in1=new Bit();
		in1.setName("in1");
		inputs.add(in1);
		Igate not=new Or(inputs);
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		inputs.get(0).setValue(true);
		not.refresh();not.apply();
		assertEquals(true, out.Value());		
		inputs.get(1).setValue(true);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		inputs.get(0).setValue(false);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		assertEquals(inputs,not.getInputs());
	}
	@Test
	public final void NAndtest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit in0=new Bit();
		in0.setName("in0");
		inputs.add(in0);
		Bit in1=new Bit();
		in1.setName("in1");
		inputs.add(in1);
		Igate not=new NAnd(inputs);
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		inputs.get(0).setValue(true);
		not.refresh();not.apply();
		assertEquals(true, out.Value());	
		inputs.get(1).setValue(true);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		inputs.get(0).setValue(false);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		assertEquals(inputs,not.getInputs());
	}
	
	
	@Test
	public final void Dfloptest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit clk=new Bit();
		clk.setName("in0");
		inputs.add(clk);
		Bit Din=new Bit();
		Din.setName("in1");
		inputs.add(Din);
		Igate not=new FlopD(inputs);
		Bit out=not.getOutput();
		clk.setValue(false);
		Din.setValue(false);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		clk.setValue(true);
		not.refresh();not.apply();
		assertEquals(false, out.Value());		
		Din.setValue(true);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		clk.setValue(false);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		assertEquals(inputs,not.getInputs());
		clk.setValue(true);
		assertEquals(false, out.Value());
		not.refresh();
		assertEquals(false, out.Value());
		not.apply();
		assertEquals(true, out.Value());
	}

}
