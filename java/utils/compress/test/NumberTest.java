package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Number;

public class NumberTest {



/*
	@Test
	public void testNumber() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testNumberInt() {
		fail("Not yet implemented"); // TODO
	}
*/
	@Test
	public void testFrom() {
		long d[]= {33, 31,6};
		
		List<ISymbol> ls=Number.from(d);
		assertEquals(ls.size(), 3);
		assertEquals(ls.get(0), new Number(33));
		assertEquals(ls.get(1), new Number(31));
		assertEquals(ls.get(2), new Number(6));
		
	}
/*
	@Test
	public void testToString() {
		assertEquals("#6",  new Number(6));
	}

	@Test
	public void testNumberLong() {

		assertEquals("#6",  (new Number(6)).toString());
		assertEquals("#6",  (new Number((long)6)).toString());
	}

	@Test
	public void testIsChar() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testIsInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testIsShort() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetChar() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetLong() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetShort() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetId() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testToSymbol() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetCode() {
		fail("Not yet implemented"); // TODO
	}
*/
	@Test
	public void testEqualsObject() {
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
	public void testCompareTo() {
		assertEquals(0,  (new Number(6)).compareTo(new Number((long)6)));
		assertEquals(-1,  (new Number(5)).compareTo(new Number((long)6)));
		assertEquals(1,  (new Number(7)).compareTo(new Number((long)6)));
		
	}

}
