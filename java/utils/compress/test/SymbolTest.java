/**
 * 
 */
package com.zoubworld.java.utils.compress.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CodeComparator;
import com.zoubworld.java.utils.compress.CodeComparatorInteger;
import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;


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
	public void testlength()
	{
		List<ISymbol> ls = null;
		ls= Symbol.factoryCharSeq("0123456789");
		ICodingRule cs =new CodingSet(CodingSet.NOCOMPRESS);
	assertEquals(10*9, Symbol.length(ls,cs).longValue());
	
	cs =new CodingSet(CodingSet.NOCOMPRESS16);
	assertEquals(10*16, Symbol.length(ls,cs).longValue());
	
	cs =new CodingSet(CodingSet.NOCOMPRESS32);
	assertEquals(10*32, Symbol.length(ls,cs).longValue());
	
	cs =new CodingSet(CodingSet.UNCOMPRESS);
	assertEquals(10*8, Symbol.length(ls,cs).longValue());
	
	
	}
	@Test
	public void testCode()
	{
		{
	

		Code c = new Code();
	System.out.println(c.toString());
	c.huffmanAddBit('0');	System.out.println(c.toString());
	assertEquals(c.toString(), "(0x0 	,1),0b0	");
	c.huffmanAddBit('1');	System.out.println(c.toString());
	assertEquals(c.toString(), "(0x1 	,2),0b01	");
	c.huffmanAddBit('0');	System.out.println(c.toString());
	assertEquals(c.toString(), "(0x2 	,3),0b010	");
	c.huffmanAddBit('1');	System.out.println(c.toString());
	assertEquals(c.toString(), "(0x5 	,4),0b0101	");
	c.huffmanAddBit('0');	System.out.println(c.toString());
	assertEquals(c.toString(), "(0xa 	,5),0b01010	");
	c.huffmanAddBit('1');	System.out.println(c.toString());
	assertEquals(c.toString(), "(0x15 	,6),0b010101	");
	c.huffmanAddBit('1');	System.out.println(c.toString());
	assertEquals(c.toString(), "(0x2b 	,7),0b0101011	");
	c.huffmanAddBit('1');	System.out.println(c.toString());
	assertEquals(c.toString(), "(0x57 	,8),0b01010111	");
	
	
	ICode a = new Code((char) 1);
	System.out.println("(char) 1 : "+a.toString()+": "+a.toRaw());
	assertEquals(a.toString(), "(0x1 	,8),0b00000001	");
	assertEquals(a.toRaw(), "00000001");
	ICode b = new Code((short) 2);
	System.out.println("(short) 2 : "+b.toString()+": "+b.toRaw());
	assertEquals(b.toString(), "(0x2 	,16),0b0000000000000010	");
	assertEquals(b.toRaw(), "0000000000000010");
	assertEquals(c.toString(), "(0x57 	,8),0b01010111	");
	assertEquals(c.toRaw(), "01010111");
	ICode d = new Code((int) 4);
	System.out.println("(int) 4 : "+d.toString()+": "+c.toRaw());
	assertEquals(d.toString(), "(0x4 	,32),0b00000000000000000000000000000100	");
	assertEquals(d.toRaw(), "00000000000000000000000000000100");
	c = new Code((long) 0x123456789L);
	System.out.println("(long) 0x123456789 : "+c.toString() +": "+c.toRaw());
	System.out.print("0b");
	assertEquals(c.toString(), "(0x123456789 	,64),0b0000000000000000000000000000000100100011010001010110011110001001	");
	assertEquals(c.toRaw(), "0000000000000000000000000000000100100011010001010110011110001001");
/*	for(int i=c.lenbit-1;i>=0;i--)
		System.out.print(c.getMsb(i));
		System.out.println();
*/}
	
	{
		Code a = new Code((char)0x80);
		System.out.println("() 0x80 : \t"+a.toString()+"\t: "+a.toRaw());
		Code b = new Code((char)0x8F);
		System.out.println("() 0x8F : \t"+b.toString()+"\t: "+b.toRaw());
		Code c = new Code(0x1007F);
		System.out.println("() 0x1007F : \t"+c.toString() +"\t: "+c.toRaw());
		Code d = new Code((short)0x1);
		System.out.println("() 0x1 : \t"+d.toString() +"\t: "+d.toRaw());

		System.out.println(a.toString()+".compareToCode("+b.toString()+")="+a.compareToCode(b));
		assertEquals(a.compareToCode(b),-1);
		assertEquals(b.compareToCode(c),1);
		assertEquals(c.compareToCode(a),-1);
			
		System.out.println(b.toString()+".compareToCode("+c.toString()+")="+b.compareToCode(c));
		System.out.println(c.toString()+".compareToCode("+a.toString()+")="+c.compareToCode(a));
		System.out.println(c.toString()+".compareToCode("+d.toString()+")="+c.compareToCode(d));
	}
	{// test sort
		List<Code> lc=new  ArrayList<Code>();
		for (int i=1; i<25;i+=5)
			lc.add(new Code((char)i));
		for (int i=1; i<25;i+=5)
			lc.add(new Code((short)(i)));
		for (int i=1; i<25;i+=5)
			lc.add(new Code((short)(i*256)));
		for (int i=1; i<30;i+=5)
			lc.add(new Code((short)(i*256+i)));
		
			  Collections.sort(lc, new CodeComparator());
			  System.out.println("\nCodeComparator\n=========");
			  for(ICode n:lc)
			  {
				    System.out.println(n.toString() + "\t=\t" +n.toString() );
					
			  }
			  
			  Collections.sort(lc, new CodeComparatorInteger());
			  System.out.println("\nCodeComparatorInteger\n=========");
			  for(ICode n:lc)
			  {
				    System.out.println(n.toString() + "\t=\t" +n.toString() );
					
			  }
			  

}
	
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
	assertEquals("INT4(255)", s.toString());
	
	
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
