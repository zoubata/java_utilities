package com.zoubworld.java.utils.compress.algo;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

import junit.framework.Assert;

public class TupleEncodingTest {

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
	public final void testDecodeSymbol() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testEncodeSymbol() {
		List<ISymbol> ls=Symbol.from("abcabcaaaaaaaacbabcabcabc");
		TupleEncoding algo=new TupleEncoding();
		algo.reset();
		algo.init(ls);
		List<ISymbol> lse=algo.encodeSymbol(ls);
		lse.addAll(0, algo.saveDictionary());
		 algo=new TupleEncoding();
		 algo.reset();
			int i=algo.readDictionary(lse);
		 
		List<ISymbol> lsdec=algo.decodeSymbol(lse.subList(i, lse.size()));
		System.out.println(lse.size()+":"+lse);
		System.out.println(lsdec.size()+":"+lsdec);
		
		assertEquals(ls, lsdec);
	}

}
