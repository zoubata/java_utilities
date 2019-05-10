/**
 * 
 */
package com.zoubworld.java.utils.compress.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.Symbol;

/**
 * @author Pierre Valleau
 *
 */
public class CompositeCodeTest {



	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.CompositeCode#length()}.
	 */
	@Test
	public 	void testLength() {
		Symbol s1=new Symbol((char) 0x12);
		s1.setCode(new Code((char) 0x12));
		Symbol s2=new Symbol((char) 0x34);	
		s2.setCode(new Code((char) 0x34));
		CompositeSymbol cs=new CompositeSymbol(s1,s2);
	    assertEquals(cs.getCode().length(), s2.getCode().length()+s1.getCode().length());	
	    
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.CompositeCode#getSymbol()}.
	 */
	@Test
	public 	void testGetSymbol() {
		Symbol s1=new Symbol((char) 0x12);
		s1.setCode(new Code((char) 0x12));
		Symbol s2=new Symbol((char) 0x34);	
		s2.setCode(new Code((char) 0x34));
		CompositeSymbol cs=new CompositeSymbol(s1,s2);
	    assertEquals(cs, cs.getCode().getSymbol());	
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.CompositeCode#setSymbol(com.zoubwolrd.java.utils.compress.ISymbol)}.
	 */
	@Test
	public 	void testSetSymbol() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.CompositeCode#toCode()}.
	 */
	@Test
	public 	void testToCode() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.CompositeCode#toRaw()}.
	 */
	@Test
	public 	void testToRaw() {
		
		Symbol s1=new Symbol((char) 0x12);
		s1.setCode(new Code((char) 0x12));
		Symbol s2=new Symbol((char) 0x34);	
		s2.setCode(new Code((char) 0x34));
		CompositeSymbol cs=new CompositeSymbol(s1,s2);
		assertEquals(cs.getCode().toRaw(), s1.getCode().toRaw()+s2.getCode().toRaw());
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.CompositeCode#getMsb(int)}.
	 */
	@Test
	public 	void testGetMsb() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.CompositeCode#write(net.zoubwolrd.java.utils.compress.BinaryStdOut)}.
	 */
	@Test
	public 	void testWrite() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.CompositeCode#getLong()}.
	 */
	@Test
	public 	void testGetLong() {
		Symbol s1=new Symbol((char) 0x12);
		s1.setCode(new Code((char) 0x12));
		Symbol s2=new Symbol((char) 0x34);	
		s2.setCode(new Code((char) 0x34));	
		CompositeSymbol cs=new CompositeSymbol(s1,s2);
		assertEquals(cs.getCode().getLong().longValue(), 0x1234);
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.CompositeCode#huffmanAddBit(char)}.
	 */
	@Test
	public 	void testHuffmanAddBit() {
		Symbol s1=new Symbol((char) 0x12);
		s1.setCode(new Code((char) 0x12));
		Symbol s2=new Symbol((char) 0x34);	
		s2.setCode(new Code((char) 0x34));	
		CompositeSymbol cs=new CompositeSymbol(s1,s2);
		String s=s1.getCode().toRaw()+s2.getCode().toRaw();
		cs.getCode().huffmanAddBit('0');
		assertEquals(cs.getCode().toRaw(),s+"0" );
	}

}
