/**
 * 
 */
package com.zoubworld.java.utils.compress.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

import junit.framework.Assert;


public class SymbolTest {



	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#Symbol(char)}.
	 */
	@Test
	public void testSymbolChar() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#Symbol(java.lang.String)}.
	 */
	@Test
	public void testSymbolString() {
	//	fail("Not yet implemented");
	}
	@Test
	public void testFactorySymbolINT() {
	CompositeSymbol s;
	
	s=(CompositeSymbol)Symbol.FactorySymbolINT(1);
	assertEquals(1, s.getS2().getId());
	assertEquals(Symbol.INT4.getId(), s.getS1().getId());
	assertEquals(Symbol.INT4.getId(), 256);
	s=(CompositeSymbol)Symbol.FactorySymbolINT(256);
	assertEquals(Symbol.INT12.getId(), s.getS1().getId());	
	assertEquals(256, s.getS2().getId());
	s=(CompositeSymbol)Symbol.FactorySymbolINT(256*126);
	assertEquals(Symbol.INT16.getId(), s.getS1().getId());	
	
	assertEquals(256*126, s.getS2().getId());
	s=(CompositeSymbol)Symbol.FactorySymbolINT(256*6);
	assertEquals(256*6, s.getS2().getId());
	
	s=(CompositeSymbol)Symbol.FactorySymbolINT(127);
	assertEquals(Symbol.INT8.getId(), s.getS1().getId());	
	
	assertEquals(127, s.getS2().getId());
	
	s=(CompositeSymbol)Symbol.FactorySymbolINT(256*256);
	assertEquals(256*256, s.getS2().getId());
	s=(CompositeSymbol)Symbol.FactorySymbolINT(256L*256L*256L);
	assertEquals(256L*256L*256L, s.getS2().getId());
	s=(CompositeSymbol)Symbol.FactorySymbolINT(256L*256L*256L*256L*256L);
	assertEquals(256L*256L*256L*256L*256L, s.getS2().getId());
	s=(CompositeSymbol)Symbol.FactorySymbolINT(256L*256L*256L*256L);
	assertEquals(256L*256L*256L*256L, s.getS2().getId());
	
	s=(CompositeSymbol)Symbol.FactorySymbolINT(256L*256L*256L*256L*256L*256L);
	assertEquals(256L*256L*256L*256L*256L*256L, s.getS2().getId());
	
	s=(CompositeSymbol)Symbol.FactorySymbolINT(256L*256L*256L*256L*256L*256L*255L);
	assertEquals(256L*256L*256L*256L*256L*256L*255L, s.getS2().getId());
	
	s=(CompositeSymbol)Symbol.FactorySymbolINT(-1);
	assertEquals(null, s);
	
	
	}
	
	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#isChar()}.
	 */
	@Test
	public void testIsChar() {
		ISymbol c=new Symbol((char) 123);
		assertTrue(c.isChar());
		
		ISymbol s=new Symbol((int) 12356);
		assertTrue(!s.isChar());
		}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#getChar()}.
	 */
	@Test
	public void testGetChar() {

		ISymbol c=new Symbol((char) 123);
		assertTrue(c.getChar()==123);
		
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#listSymbolToCharSeq(java.util.List)}.
	 */
	@Test
	public void testListSymbolToCharSeq() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#factoryCharSeq(java.lang.String)}.
	 */
	@Test
	public void testFactoryCharSeq() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#getInt()}.
	 */
	@Test
	public void testGetInt() {
		ISymbol c=new Symbol((int) 123);
		assertTrue(c.getInt()==123);
		
		ISymbol s=new Symbol((int) 12356);
		assertTrue(s.getInt()==12356);
		
		ISymbol i=new Symbol((int) 0xF654321);
		assertTrue(i.getInt()==0xF654321);
		
	}

	/**
	 * Test method for {@link net.zoubwolrd.java.utils.compress.factoryFile()}.
	 */
	@Test
	public void testFactoryFile() {
		//ClassLoader classLoader = getClass().getClassLoader();
		//File file = new File(classLoader.getResource("res/test/smallfile.txt").getFile());
		File file = new File("res/test/smallfile.txt");
		File file2 = new File("res/test/smallfile.txt"+".tmp");
		System.out.println(file.getAbsolutePath());
		
		String inputFile="";
		List<ISymbol> ls=Symbol.factoryFile( file.getAbsolutePath());
		Symbol.listSymbolToFile(ls,file2.getAbsolutePath());
		
		assertTrue(file.length()==file2.length());
		
	}
	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#Symbol(byte)}.
	 */
	@Test
	public void testSymbolByte() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#Symbol(int)}.
	 */
	@Test
	public void testSymbolInt() {
	Symbol s=new Symbol((int)256);
	assertEquals(s.getId(), 256);
	assertTrue(s.isInt());
	assertTrue(!s.isShort());
	assertTrue(!s.isChar());
	
	 s=new Symbol((char) 123);
	assertEquals(s.getId(), 123);
	assertEquals(s.getChar(),(char) 123);
	assertTrue(s.isChar());
	assertTrue(!s.isInt());
	assertTrue(!s.isShort());
	
	
	s=new Symbol((short) 123);
	assertEquals(s.getId(), 123);
	assertEquals( 123,s.getShort());
	assertTrue(s.isShort());
	assertTrue(!s.isInt());
	assertTrue(!s.isChar());
	
	
	
	s=new Symbol((short) 256);
	assertEquals(s.getId(), 256);
	assertEquals(s.getShort(),(short) 256);
			
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#toSymbol()}.
	 */
	@Test
	public void testToSymbol() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#getFromSet(java.util.Set, char)}.
	 */
	@Test
	public void testGetFromSet() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#getCode()}.
	 */
	@Test
	public void testGetCode() {
	//	fail("Not yet implemented");
	}
	 @After // tearDown()
	    public void after() throws Exception {

  //    System.out.println("Running: tearDown");


  }
@Before // setup()

  public void before() throws Exception {

  //    System.out.println("Setting it up!");


  }

}
