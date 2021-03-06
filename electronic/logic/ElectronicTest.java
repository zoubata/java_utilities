package com.zoubworld.electronic.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.electronic.logic.complexgate.Adder;
import com.zoubworld.electronic.logic.complexgate.DFlipFlopInGate;
import com.zoubworld.electronic.logic.complexgate.DFlopInGate;
import com.zoubworld.electronic.logic.complexgate.DLatchInGate;
import com.zoubworld.electronic.logic.complexgate.JKFlipFlopInGate;
import com.zoubworld.electronic.logic.complexgate.RsFlopInGate;
import com.zoubworld.electronic.logic.complexgate.RsFlopSyncInGate;
import com.zoubworld.electronic.logic.complexgate.RsLatchInGate;
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
		public final void DFlipFlopInGatetest() {
			List<Bit> inputs=new ArrayList<Bit>();
			Bit in0=new Bit();
			Bit in1=new Bit();
			inputs.add(in0);
			inputs.add(in1);
			DFlipFlopInGate not=new DFlipFlopInGate(inputs);
		in0.setName(not.getInputsNomenclature().get(0));
		in1.setName(not.getInputsNomenclature().get(1));
		Bit out=not.getOutput();
		out.setName(not.getOutputsNomenclature().get(0));
		assertEquals("[Din, CLK]", not.getInputsNomenclature().toString());
		assertEquals("[Q, NQ]",not.getOutputsNomenclature().toString());
		in0.setValue(false);
		in1.setValue(false);
		assertEquals(null, out.Value());
		assertEquals("x", out.value());	
		inputs.get(0).setValue(true);
		inputs.get(1).setValue(false);
		not.refresh();not.apply();not.refresh();not.apply();not.refresh();not.apply();
		assertEquals(null, out.Value());
		assertEquals(null, not.getOutputs().get(1).Value());
		inputs.get(0).setValue(true);
		inputs.get(1).setValue(true);
		not.refresh();not.apply();not.refresh();not.apply();not.refresh();not.apply();
		assertEquals(true, out.Value());
		assertEquals(false, not.getOutputs().get(1).Value());
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		not.refresh();not.apply();not.refresh();not.apply();not.refresh();not.apply();
		assertEquals(true, out.Value());
		assertEquals(false, not.getOutputs().get(1).Value());
		inputs.get(0).setValue(false);//Din
		inputs.get(1).setValue(true);//CK
		not.refresh();not.apply();not.refresh();not.apply();not.refresh();not.apply();
		assertEquals(false, out.Value());
		assertEquals(true, not.getOutputs().get(1).Value());
		int count=0;
		for(Igate g:not.getGates())
			g.setName("g"+count++);
		assertEquals("module DFlipFlopInGate(Q, nQ, Din, CLK);\r\n" + 
				"input Din, CLK;\r\n" + 
				"output Q, nQ;\r\n" + 
				"wire a, b, nR, nS;\r\n" + 
				"NAnd g0(Q,nQ, nS);\r\n" + 
				"NAnd g1(nQ,Q, nR);\r\n" + 
				"NAnd g2(a,b, nS);\r\n" + 
				"NAnd g3(nS,a, CLK);\r\n" + 
				"NAnd g4(nR,nS, CLK, b);\r\n" + 
				"NAnd g5(b,nR, Din);\r\n" + 
				"endmodule\r\n",not.toVerilog());
		not.setName(not.getClass().getSimpleName());
		assertEquals("digraph DFlipFlopInGate {\r\n" + 
				"  // rankdir is a graph-level attribute\r\n" + 
				"  rankdir=\"LR\"\r\n" + 
				"g0[shape=box]\r\n" + 
				"g1[shape=box]\r\n" + 
				"g2[shape=box]\r\n" + 
				"g3[shape=box]\r\n" + 
				"g4[shape=box]\r\n" + 
				"g5[shape=box]\r\n" + 
				"g1 -> g0 [label=\"nQ:1\"]\r\n" + 
				"g3 -> g0 [label=\"nS:1\"]\r\n" + 
				"g0 -> g1 [label=\"Q:0\"]\r\n" + 
				"g4 -> g1 [label=\"nR:0\"]\r\n" + 
				"g5 -> g2 [label=\"b:1\"]\r\n" + 
				"g3 -> g2 [label=\"nS:1\"]\r\n" + 
				"g2 -> g3 [label=\"a:0\"]\r\n" + 
				"g3 -> g4 [label=\"nS:1\"]\r\n" + 
				"g5 -> g4 [label=\"b:1\"]\r\n" + 
				"g4 -> g5 [label=\"nR:0\"]\r\n" + 
				"in0 -> g5 [label=\"Din:0\"]\r\n" + 
				"in1 -> g3 [label=\"CLK:1\"]\r\n" + 
				"in1 -> g4 [label=\"CLK:1\"]\r\n" + 
				" subgraph subsin {\r\n" + 
				"    rank=\"same\"\r\n" + 
				"in0\r\n" + 
				"in1\r\n" + 
				"  }g0 -> out0 [label=\"Q:0\"]\r\n" + 
				"g1 -> out1 [label=\"nQ:1\"]\r\n" + 
				" subgraph subsout {\r\n" + 
				"    rank=\"same\"\r\n" + 
				"out0\r\n" + 
				"out1\r\n" + 
				"  }\r\n" + 
				"}\r\n",not.toGraphviz());
	}
	@Test
	public final void Nottest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit in0=new Bit();
		in0.setName("in0");
		inputs.add(in0);
		Igate not=new Not(inputs);
		assertEquals("[In0]", not.getInputsNomenclature().toString());
		assertEquals("[OutN0]",not.getOutputsNomenclature().toString());
		Bit out=not.getOutput();
		assertEquals(null, out.Value());		
		inputs.get(0).setValue(false);
	//	inputs.get(1).setValue(false);
		not.refresh();not.apply();
		
		assertEquals(true, out.Value());
		assertEquals("1", out.value());	
		inputs.get(0).setValue(true);
		//	inputs.get(1).setValue(false);
		not.refresh();not.apply();
			assertEquals(false, out.Value());	
			assertEquals("0", out.value());	
			assertEquals("in0",in0.getName());
			assertEquals(true,out.getName().startsWith("@"));
			assertEquals(inputs,not.getInputs());
			System.out.print(not.getName()+"\r\n"+not.toTruthTable());
	
	}

	@Test
	public final void onetest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit in0=new Bit();
		in0.setName("in0");
		inputs.add(in0);
		Igate not=new One(inputs);
		assertEquals("[In0]", not.getInputsNomenclature().toString());
		assertEquals("[Out0]",not.getOutputsNomenclature().toString());
		
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
	//	inputs.get(1).setValue(false);
		assertEquals(null, out.Value());	
		not.refresh();
		assertEquals(null, out.Value());	
		not.apply();
		not.setInputs(inputs);
		assertEquals(false, out.Value());
		inputs.get(0).setValue(true);
		//	inputs.get(1).setValue(false);
		assertEquals(false, out.Value());
		not.setInputs(in0);
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
				System.out.print(not.getName()+"\r\n"+not.toTruthTable());

	}
	@Test
	public final void Bustest() {
	Bus b=new Bus();
	Bit in0=new Bit();
	in0.setName("in0");
	Bit in1=new Bit();
	in1.setName("in1");
	Bit in2=new Bit("in2");
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
	assertEquals("in0in1in2", b.toString());
	assertEquals("xxx", b.value());
	assertEquals(inputs, b.getInputs());
	Igate g1=new Not(in0) ;
	Igate g2=new Not(inputs) ;
	b.add(g1.getOutput());
	b.add(g2.getOutput());
	Igate g3=new One(g1.getOutput()) ;
	Igate g4=new One(inputs) ;
	b.add(g3.getOutput());
	b.add(g4.getOutput());
	assertEquals("xxxxxxx", b.value());
	g1.refresh();g1.apply();
	g2.refresh();g2.apply();
	g3.refresh();g3.apply();
	g4.refresh();g4.apply();
	assertEquals("xxxxxxx", b.value());
	assertEquals(null, b.Value());
	assertEquals("x",in2.value());
	in0.setValue(false);
	in1.setValue(false);
	in2.setValue(false);
	g1.refresh();g1.apply();
	g2.refresh();g2.apply();
	g3.refresh();g3.apply();
	g4.refresh();g4.apply();
	
	assertEquals("0001110", b.value());
	assertEquals(14, b.Value()+0);
	in0.setValue(false);
	in1.setValue(true);
	in2.setValue(false);
	g1.refresh();g1.apply();
	g2.refresh();g2.apply();
	g3.refresh();g3.apply();
	g4.refresh();g4.apply();
	
	assertEquals("0101110", b.value());
	assertEquals(14+32, b.Value()+0);
	in0.setValue(true);
	in1.setValue(true);
	in2.setValue(true);
	g1.refresh();g1.apply();
	g2.refresh();g2.apply();
	g3.refresh();g3.apply();
	g4.refresh();g4.apply();
	
	assertEquals("1110001", b.value());
	assertEquals("1",in2.value());
	assertEquals(1+(7<<4), b.Value()+0);
	b.setValue(15);
	assertEquals("0001111", b.value());
	assertEquals(15, b.Value()+0);
	b=new Bus(b.getInputs());
	assertEquals("0001111", b.value());
	assertEquals(15, b.Value()+0);
	Bus b1 = new Bus();
	b1.addAll(b.getInputs());
	assertEquals("0001111", b1.value());
	assertEquals(15, b1.Value()+0);
	assertEquals("0",in2.value());
	assertEquals("0",in1.value());
	assertEquals("0",in0.value());
	assertEquals("in0",in0.getName());
	assertEquals("in2",in2.getName());
	
	
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
		assertEquals("[In0, In1]", not.getInputsNomenclature().toString());
		assertEquals("[Out0]",not.getOutputsNomenclature().toString());
		assertEquals(null, out.Value());
		
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
		in0.setValue(true);
		in1.setValue(true);
		not.refresh();not.apply();
		assertEquals(true, not.getOutput().Value());
		in0.setValue(null);
		assertEquals(true, not.getOutput().Value());
		not.refresh();not.apply();
		assertEquals(null, not.getOutput().Value());
		System.out.print(not.getName()+"\r\n"+not.toTruthTable());

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
		Igate not=new NOr(in0,in1);
		assertEquals("[In0, In1]", not.getInputsNomenclature().toString());
		assertEquals("[OutN0]",not.getOutputsNomenclature().toString());
		
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		assertEquals(null, out.Value());
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		not=new NOr(inputs);
		out=not.getOutput();
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
		not=new NOr(in0,in1);
		in0.setValue(false);
		in1.setValue(false);
		not.refresh();not.apply();
		in0.setValue(null);
		assertEquals(true, not.getOutput().Value());
		not.refresh();not.apply();
		assertEquals(null, not.getOutput().Value());
		System.out.print(not.getName()+"\r\n"+not.toTruthTable());
		System.out.print(not.getName()+"\r\n"+not.toTruthTable());

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
		assertEquals("[In0, In1]", not.getInputsNomenclature().toString());
		assertEquals("[Out0]",not.getOutputsNomenclature().toString());
		
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		assertEquals(null, out.Value());
		
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
		System.out.print(not.getName()+"\r\n"+not.toTruthTable());

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
		assertEquals("[In0, In1]", not.getInputsNomenclature().toString());
		assertEquals("[OutN0]",not.getOutputsNomenclature().toString());
		
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		assertEquals(null, out.Value());
		
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
		System.out.print(not.getName()+"\r\n"+not.toTruthTable());

	}
	
	@Test
	public final void testChronos() {
		ArrayList<Bit> Input2s = new ArrayList<Bit>();
		Input2s.add(new Bit("in0"));
		Input2s.add(new Bit("in1"));
		//Inputs.add(new Bit("I2"));	
		ArrayList<Bit> Input3s = new ArrayList<Bit>();
		Input3s.add(new Bit("in0"));
		Input3s.add(new Bit("in1"));
		Input3s.add(new Bit("in2"));	
		DFlipFlopInGate g=new DFlipFlopInGate(Input2s);
		
		List<Integer> l=new ArrayList<Integer>();
		l.add(0);
		l.add(1);
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(2);
		l.add(7);
		l.add(1);
		l.add(0);
		l.add(2);
		l.add(3);
		l.add(0);
		l.add(1);
		l.add(0);
		l.add(1);
		l.add(7);
		l.add(1);
		l.add(0);
		l.add(2);
		l.add(0);	
		List<Integer> l2=new ArrayList<Integer>();
		l2.add(0);	
		l2.add(1);	
		l2.add(0);	
		l2.add(5);	
		l2.add(0);	
		l2.add(3);	
		l2.add(0);	
		l2.add(2);	
		l2.add(6);	
		l2.add(2);	
		l2.add(0);	
		l2.add(2);	
		l2.add(3);	
		l2.add(2);	
		l2.add(0);	
		l2.add(0);	
		
	
   assertEquals("        in0(Din):x0000000000000000000001111111111111111111100000000001111111111000000000000000000001111100000000001111100000\r\n" + 
   		"        in1(CLK):x0000001111100000111110000011111000001111111111000000000011111000001111100000111111111111111000000000000000\r\n" + 
   		"            Q(Q):xxxxxxxxx00000000000000000001111111111111111111111111111111111111111100000000000000000000000000000000000000\r\n" + 
   		"          nQ(NQ):xxxxxxxx111111111111111111111000000000000000000000000000000000000000111111111111111111111111111111111111111\r\n" + 
   		"",Chronos.display(g,l));
		System.out.println(g.toGraphviz());
		
		
		DFlopInGate g2=new DFlopInGate(Input2s);
		DLatchInGate g3=new DLatchInGate(Input2s);
		RsFlopInGate g5=new RsFlopInGate(Input2s);
		RsLatchInGate g7=new RsLatchInGate(Input2s);
		JKFlipFlopInGate g4=new JKFlipFlopInGate(Input3s);
		RsFlopSyncInGate g6=new RsFlopSyncInGate(Input3s);
   assertEquals("          in0(E):00000000000000000000001111111111111111111100000000001111111111000000000000000000001111100000000001111100000\r\n" + 
   		"          in1(D):00000001111100000111110000011111000001111111111000000000011111000001111100000111111111111111000000000000000\r\n" + 
   		"            Q(Q):xxxxxxxxx00000000000000000001111111111111111100000000000001111111111100000000000000111111100000000000000000\r\n" + 
   		"          nQ(NQ):xxxxxxxx111111111111111111111000000000000000111111111111111000000000111111111111111100000111111111111111111\r\n" + 
   		"",Chronos.display(g2,l));
   assertEquals("          in0(J):x0000000000000000000000000000000000001111100000000000000000000000000000000000000001111100000000000000000000\r\n" + 
   		"        in1(CLK):x0000000000000000000001111111111111111111100000000001111111111000000000000000000001111100000000001111100000\r\n" + 
   		"          in2(K):x0000001111100000111110000011111000001111111111000000000011111000001111100000111111111111111000000000000000\r\n" + 
   		"            Q(Q):xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n" + 
   		"          nQ(NQ):xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n" + 
   		"",Chronos.display(g4,l));
		   
   assertEquals("          in0(R):00000000000000000000001111111111111111111100000000001111111111000000000000000000001111100000000001111100000\r\n" + 
   		"          in1(S):00000001111100000111110000011111000001111111111000000000011111000001111100000111111111111111000000000000000\r\n" + 
   		"            Q(Q):xxxxxxxx111111111111110000000000000000000011111111110000000000101010111111111111110000011111111110000000000\r\n" + 
   		"          nQ(QN):xxxxxxx0000000000000000111100000111110000000000000000111100000101010000000000000000000000000000000111111111\r\n" + 
   		"",Chronos.display(g5,l));
		
   assertEquals("          in0(S):00000000000000000000000000000000000001111100000000000000000000000000000000000000001111100000000000000000000\r\n" + 
   		"          in1(E):00000000000000000000001111111111111111111100000000001111111111000000000000000000001111100000000001111100000\r\n" + 
   		"          in2(R):00000001111100000111110000011111000001111111111000000000011111000001111100000111111111111111000000000000000\r\n" + 
   		"            Q(Q):xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx11111xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx11111xxxxxxxxxxxxxxxxxxx\r\n" + 
   		"          nQ(QN):xxxxxxxxxxxxxxxxxxxxxxxxxxxx11111xxxxx11111xxxxxxxxxxxxxxx11111xxxxxxxxxxxxxxxxxxxx11111xxxxxxxxxxxxxxxxxxx\r\n" + 
   		"",Chronos.display(g6,l));//l2
		   
   assertEquals("          in0(R):00000000000000000000001111111111111111111100000000001111111111000000000000000000001111100000000001111100000\r\n" + 
   		"          in1(S):00000001111100000111110000011111000001111111111000000000011111000001111100000111111111111111000000000000000\r\n" + 
   		"            Q(Q):xxxxxxxx111111111111111000000000000000000001111111111000000000000000111111111111111000001111111111000000000\r\n" + 
   		"",Chronos.display(g7,l));
		   
   assertEquals("          in0(D):00000000000000000000001111111111111111111100000000001111111111000000000000000000001111100000000001111100000\r\n" + 
   		"        in1(CLK):00000001111100000111110000011111000001111111111000000000011111000001111100000111111111111111000000000000000\r\n" + 
   		"            Q(Q):xxx00000000000000000000000001111100000111110000000000000001111100000000000000000000001110000000000000000000\r\n" + 
   		"          nQ(NQ):xxx00000111110000011111000000000000000000000111100000000000000000000111110000011111100000111100000000000000\r\n" + 
   		"",Chronos.display(g3,l));
		
		
		
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
		assertEquals("[In0, In1]", not.getInputsNomenclature().toString());
		assertEquals("[Out0]",not.getOutputsNomenclature().toString());
		
		Bit out=not.getOutput();
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		assertEquals(null, out.Value());
		
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
		not=new Or(in0,in1);
		in0.setValue(false);
		in1.setValue(false);
		not.refresh();not.apply();
		in0.setValue(null);
		assertEquals(false, not.getOutput().Value());
		not.refresh();not.apply();
		assertEquals(null, not.getOutput().Value());
		System.out.print(not.getName()+"\r\n"+not.toTruthTable());

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
		Bit in2=new Bit();
		in2.setName("in2");
		Igate not=new NAnd(inputs);
		Bit out=not.getOutput();
		assertEquals("[In0, In1]", not.getInputsNomenclature().toString());
		assertEquals("[OutN0]",not.getOutputsNomenclature().toString());
		assertEquals(null, out.Value());
		inputs.get(0).setValue(false);
		inputs.get(1).setValue(false);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		inputs.get(0).setValue(true);
		not.refresh();not.apply();
		assertEquals(true, out.Value());	
		inputs.get(1).setValue(true);
		not.setInputs(in0, in1);
		not.refresh();not.apply();
		assertEquals(false, out.Value());
		inputs.get(0).setValue(false);
		not.refresh();not.apply();
		assertEquals(true, out.Value());
		assertEquals(inputs,not.getInputs());
		assertTrue(not.getName().startsWith("NAnd_"));
		not=new NAnd(in0,in1);
		assertEquals(null, not.getOutput().Value());
		not.refresh();not.apply();
		assertEquals(true, not.getOutput().Value());
		
		not=new NAnd(in0,in1,in2);
		assertEquals(null, not.getOutput().Value());
		not.refresh();not.apply();
		assertEquals(true, not.getOutput().Value());
		in2.setValue(true);in0.setValue(true);
		assertEquals(true, not.getOutput().Value());
		not.refresh();not.apply();
		assertEquals(false, not.getOutput().Value());
		in0.setValue(null);
		assertEquals(false, not.getOutput().Value());
		not.refresh();not.apply();
		assertEquals(null, not.getOutput().Value());
		System.out.print(not.getName()+"\r\n"+not.toTruthTable());

		
		
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
		assertEquals("[Clk, Din]", not.getInputsNomenclature().toString());
		assertEquals("[Dout]",not.getOutputsNomenclature().toString());
		
		Bit out=not.getOutput();
		clk.setValue(false);
		Din.setValue(false);
		assertEquals(null, out.Value());
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
//		System.out.print(not.getName()+"\r\n"+not.toTruthTable());

	}
	@Test
	public final void Addertest() {
		List<Bit> inputs=new ArrayList<Bit>();
		Bit clk=new Bit();
		clk.setName("in0");
		inputs.add(clk);
		Bit in1=new Bit();
		in1.setName("in1");
		Bit in2=new Bit();
		in2.setName("in2");
		inputs.add(in1);
		inputs.add(in2);
		Igate not=new Adder(inputs);
		System.out.print(not.getName()+"\r\n"+not.toTruthTable());
		assertEquals("|   Cin|     x|     y||   sum|  Cout|\r\n" + 
				"-------------------------------------\r\n" + 
				"|     0|     0|     0||     0|     0|\r\n" + 
				"|     0|     0|     1||     1|     0|\r\n" + 
				"|     0|     1|     0||     1|     0|\r\n" + 
				"|     0|     1|     1||     0|     1|\r\n" + 
				"|     1|     0|     0||     1|     0|\r\n" + 
				"|     1|     0|     1||     0|     1|\r\n" + 
				"|     1|     1|     0||     0|     1|\r\n" + 
				"|     1|     1|     1||     1|     1|\r\n", not.toTruthTable());
	}
}
