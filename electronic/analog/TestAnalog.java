package com.zoubworld.electronic.analog;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAnalog {

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
	public final void test() throws Exception {
		Point gnd=new Point("gnd");
		 Point a=new Point("a");
		 Point b=new Point("b");
		 Point c=new Point("c");
		 Point d=new Point("d");
			    Iconnect cmp=new VoltageGen(1.0,gnd,a);
	    assertEquals(1.0,a.getPotential(gnd),0.001);
	    Iconnect cmp2=new CurrentGen(1.0,gnd,b);
	    assertEquals(null,b.getPotential(gnd));
	    Iconnect r=new Resistor(1000.0,gnd,b);
	    assertEquals(1000.0,b.getPotential(gnd),0.01);
	    Iconnect r2=new Resistor(1000.0,gnd,a);
	    assertEquals(1/1000.0,r2.getCurrent(gnd),0.01);
	        
		    
						
	}

}
