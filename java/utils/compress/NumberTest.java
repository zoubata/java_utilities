package com.zoubworld.java.utils.compress;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NumberTest {

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
	final void testNumber() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testNumberInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testFrom() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testNumberLong() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testIsChar() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testIsInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testIsShort() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetChar() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetLong() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetShort() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetId() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testToSymbol() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testSetCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testEqualsObject() {
		long d[]={8589934591L, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1024, 
				1024, 1024, 0, 1024, 1024, 1024, 0, 17408, 0, 0, 0,
				0, 0, 512, 512, 0, 0};
	
		List<ISymbol> ls=Number.from(d);
		assertEquals(ls.get(1), ls.get(1));
		assertEquals(new Number(8589934591L), new Number(8589934591L));
		assertEquals(ls.get(0), new Number(8589934591L));
		assertEquals(ls.get(1), ls.get(2));
		assertNotEquals(ls.get(0), ls.get(1));
		 Map<ISymbol, Long> m = ISymbol.Freq(ls);
		 assertEquals(5, m.size());
	}

	@Test
	final void testCompareTo() {
		fail("Not yet implemented"); // TODO
	}

}
