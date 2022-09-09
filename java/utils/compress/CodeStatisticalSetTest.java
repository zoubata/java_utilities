package com.zoubworld.java.utils.compress;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class CodeStatisticalSetTest {

	@Test
	public  void testN() {
		CodeStatisticalSet cr=new CodeStatisticalSet(0,32,1,new Number(),0.0,0.0,0.0,0.0);
		Map<ISymbol, Double> fm = cr.freq();
		assertEquals(cr.N(), 32);
		assertEquals( 5.0,ISymbol.getEntropied(fm),0.0001);
		cr.build();
		
	}

}
