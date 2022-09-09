package com.zoubworld.java.utils.compress.test;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.Number;

import com.zoubworld.java.utils.compress.AdaptativeHuffmanCode;
import com.zoubworld.java.utils.compress.CodeNumberSet;
import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.algo.RLE;
import com.zoubworld.java.utils.compress.blockSorting.FifoAlgo;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;
import com.zoubworld.java.utils.compress.file.CompressBlockReader;
import com.zoubworld.java.utils.compress.file.CompressBlockWriter;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;

public class CompressBlockTest {

	@Test
	public void testCompressBlockReader() {
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
/*
	@Test
	public void testGetposBinStream() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetposend() {
		fail("Not yet implemented"); // TODO
	}
*/
	@Test
	public void testGetSymbols() {
	
		List<ISymbol> ls=Symbol.from("test1111111111111222222222333333333");
		List<IAlgoCompress> S2S_algo=new ArrayList<IAlgoCompress>();
		S2S_algo.add(new RLE(2L));
		
		ICodingRule coding=new CodingSet(CodingSet.NOCOMPRESS);
		//=new AdaptativeHuffmanCode();		
		testCompressBlock( ls, S2S_algo,coding);
	}
	@Test
	public void testcase1() {
		long d2[]={8591, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1024, 
				1024, 1024, 0, 1024, 1024, 1024, 0, 17408, 0, 0, 0,
				0, 0, 512, 512, 0, 0};
		long d[]= {33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 31};
	
		List<ISymbol> ls=Number.from(d);
		List<IAlgoCompress> S2S_algo=new ArrayList<IAlgoCompress>();
	
		ICodingRule coding=new CodeNumberSet(ls);
		//=new AdaptativeHuffmanCode();		
		testCompressBlock( ls, S2S_algo,coding);
		 ls=Number.from(d2);
		testCompressBlock( ls, S2S_algo,coding);
		
	}
	@Test
	public void testcase2() {
		long d2[]={8591, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1024, 
				1024, 1024, 0, 1024, 1024, 1024, 0, 17408, 0, 0, 0,
				0, 0, 512, 512, 0, 0};
		long d[]= {33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 31};
	
		List<ISymbol> ls=Number.from(d);
		List<IAlgoCompress> S2S_algo=new ArrayList<IAlgoCompress>();
	//	S2S_algo.add(new RLE(2L));
		S2S_algo.add(new FifoAlgo(2L));
		ICodingRule coding=new CodeNumberSet(ls);
		//=new AdaptativeHuffmanCode();		
		testCompressBlock( ls, S2S_algo,coding);
		 ls=Number.from(d2);
		 S2S_algo.remove(0);
		 S2S_algo.add(new FifoAlgo(2L));
		testCompressBlock( ls, S2S_algo,coding);
		
	}
}
