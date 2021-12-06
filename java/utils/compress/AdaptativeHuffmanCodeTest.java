package com.zoubworld.java.utils.compress;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zoubworld.java.utils.compress.file.BinaryFinFout;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;

class AdaptativeHuffmanCodeTest {

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
	final void testGetISymbol()
	{
		for(int i=0;i<CodeNumber.MaxCodingIndex;i++)
		{
		ICodingRule code=new CodeNumberSet(i);
		testGetISymbol( code);
		}
		ICodingRule code=new ShannonFanoEliasCode(Number.from(d));
		
		assertEquals(new Code("1110"),code.get(new Number(123)));
		
	}
	 void testGetISymbol(ICodingRule code) {
		assertEquals(""+code,CodeNumber.getCode(code.getParam().intValue(),(123L)),
				code.get(new Number(123)));
		
	}

	@Test
	final void testGetICode()
	{
		for(int i=0;i<CodeNumber.MaxCodingIndex;i++)
		{
		ICodingRule code=new CodeNumberSet(i);
		testGetICode( code);
		}
		ICodingRule code=new ShannonFanoEliasCode(Number.from(d)) ;
		assertEquals(new Number(123),code.get(new Code("1110")));
	}
	long[] d={12L,123L,123L,12L,10L,12L,12L,13L,10L,11L,1L,0L};
		
	 void testGetICode(ICodingRule code) {
		
		assertEquals(""+code,new Number(123),
				code.get(CodeNumber.getCode(code.getParam().intValue(), 123L)));
	}
/*
	@Test
	final void testGetCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetGenericCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetSymbol() {
		fail("Not yet implemented"); // TODO
	}
*/
	@Test
	final void testWriteCodingRule() {
	for(int i=0;i<CodeNumber.MaxCodingIndex;i++)
	{
	ICodingRule code=new CodeNumberSet(i);
	testWriteCodingRule( code);
	}
	ICodingRule code=new ShannonFanoEliasCode(Number.from(d));
	testWriteCodingRule( code);
	}
	final void testWriteCodingRule(ICodingRule codeset) {
		//ICodingRule codeset=new CodeNumberSet(CodeNumber.FibonacciCoding);
		ICode code;
		Number sym;
		BinaryFinFout fifo=new BinaryFinFout();
		fifo.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS16));
		//codeset.writeCodingRule(binaryStdOut);
		codeset.writeCodingRule(fifo);
		fifo.flush();
		assertEquals(codeset,ICodingRule.ReadCodingRule(fifo));
		
	}

	@Test
	final void testWritecode()
	{/*
		for(int i=0;i<CodeNumber.MaxCodingIndex;i++)
		{
		ICodingRule code=new CodeNumberSet(i);
		testWritecode( code);
		}*/
		ShannonFanoEliasCode code=new ShannonFanoEliasCode(Number.from(d));
		/*code.setSprout(new Number());*/
		code.build();
		code.buildTree();
		testWritecode( code);
	}
	 void testWritecode(ICodingRule codeset) {
		ICode code;
		Number sym;
		BinaryFinFout fifo=new BinaryFinFout();
		//codeset.writeCodingRule(binaryStdOut);
		
		fifo.write(code=codeset.get(new Number(10)));
		fifo.setCodingRule(codeset);
		fifo.write(sym=new Number(10));
		fifo.write((int)12);
		code.write(fifo);
		fifo.flush();
		assertEquals(code, fifo.readCode());
		assertEquals(sym, fifo.readSymbol());
		assertEquals((int)12, fifo.readInt());
		assertEquals(code, fifo.readCode());
		
		 fifo=new BinaryFinFout();
		//codeset.writeCodingRule(binaryStdOut);
		
		fifo.write(code=codeset.get(new Number(10)));
		fifo.setCodingRule(codeset);
		fifo.write(sym=new Number(10));
		fifo.write((int)11);
		code.write(fifo);
		
		fifo.flush();
		assertEquals(code.getSymbol(), fifo.readSymbol());
		assertEquals(sym, fifo.readCode().getSymbol());
		assertEquals((int)11, fifo.readInt());
		assertEquals(code, fifo.readCode());
		
		//check that a bit stream writen in code from Number can be read to build Symbol
		 fifo=new BinaryFinFout();
			//codeset.writeCodingRule(binaryStdOut);
		    codeset.setSprout(new Number(0));
			
			fifo.write(code=codeset.get(new Number(13)));
			fifo.setCodingRule(codeset);
			fifo.write(sym=new Number(10));
			fifo.write((int)11);
			code.write(fifo);
			codeset.setSprout(new Symbol());
			fifo.setCodingRule(codeset);
			fifo.flush();
			assertEquals(Symbol.findId(13), fifo.readSymbol());
			assertEquals(Symbol.findId(10), fifo.readCode().getSymbol());
			assertEquals((int)11, fifo.readInt());
			assertEquals(code, fifo.readCode());
			
			
	}

}
