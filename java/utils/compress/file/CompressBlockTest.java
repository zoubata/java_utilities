package com.zoubworld.java.utils.compress.file;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.Number;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zoubworld.java.utils.compress.AdaptativeHuffmanCode;
import com.zoubworld.java.utils.compress.CodeNumberSet;
import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.algo.RLE;

class CompressBlockTest {

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
	final void testCompressBlockReader() {
		List<ISymbol> ls=Symbol.from("test1111111111111222222222333333333");
		List<IAlgoCompress> S2S_algo=new ArrayList<IAlgoCompress>();
		ICodingRule coding=new CodingSet(CodingSet.NOCOMPRESS16);
		
		testCompressBlock( ls, S2S_algo,coding);
	}
		final void testCompressBlock(List<ISymbol> ls,List<IAlgoCompress> S2S_algo,ICodingRule coding) {	
		IBinaryWriter fifo=new BinaryFinFout();
		CompressBlockWriter cb=new CompressBlockWriter(fifo,S2S_algo,coding);
		cb.write(ls);
		fifo.flush();
		CompressBlockReader cbr=new CompressBlockReader((IBinaryReader) fifo);
		List<ISymbol> lsr = cbr.getSymbols();
		assertEquals(cb.getNbISymbols(), cbr.getNbISymbols());
		assertEquals(cb.getS2B_algo(), cbr.getS2B_algo());
		assertEquals(cb.getS2B_param(), cbr.getS2B_param());
		assertEquals(cb.getS2S_algo(), cbr.getS2S_algo());
		assertEquals(cb.getS2S_param(), cbr.getS2S_param());
		assertEquals(ls, lsr);
	}

	@Test
	final void testGetposBinStream() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetposend() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetSymbols() {
	
		List<ISymbol> ls=Symbol.from("test1111111111111222222222333333333");
		List<IAlgoCompress> S2S_algo=new ArrayList<IAlgoCompress>();
		S2S_algo.add(new RLE(2L));
		
		ICodingRule coding=new CodingSet(CodingSet.NOCOMPRESS);
		//=new AdaptativeHuffmanCode();		
		testCompressBlock( ls, S2S_algo,coding);
	}
	@Test
	final void testcase1() {
		long d2[]={8591L, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1024, 
				1024, 1024, 0, 1024, 1024, 1024, 0, 17408, 0, 0, 0,
				0, 0, 512, 512, 0, 0};
		long d[]= {33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 31};
	
		List<ISymbol> ls=Number.from(d);
		List<IAlgoCompress> S2S_algo=new ArrayList<IAlgoCompress>();
	//	S2S_algo.add(new RLE(2L));
		
		ICodingRule coding=new CodeNumberSet(ls);
		//=new AdaptativeHuffmanCode();		
		testCompressBlock( ls, S2S_algo,coding);
		 ls=Number.from(d2);
		testCompressBlock( ls, S2S_algo,coding);
		
	}
	
}
