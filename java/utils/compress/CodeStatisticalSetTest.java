package com.zoubworld.java.utils.compress;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodeStatisticalSetTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testN() {
		CodeStatisticalSet cr=new CodeStatisticalSet(0,32,1,new Number(),0.0,0.0,0.0,0.0);
		Map<ISymbol, Double> fm = cr.freq();
		assertEquals(cr.N(), 32);
		assertEquals( 5.0,ISymbol.getEntropied(fm),0.0001);
		cr.build();
		
	}

}
