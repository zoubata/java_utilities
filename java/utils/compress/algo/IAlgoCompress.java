package com.zoubworld.java.utils.compress.algo;

import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.blockSorting.BWT;
import com.zoubworld.java.utils.compress.blockSorting.FifoAlgo;
import com.zoubworld.java.utils.compress.blockSorting.MTF;

import sandbox.TxtDiffInc;

public interface IAlgoCompress {
	public static Class list[]= 
		{
				BWT.class,
				BytePairEncoding.class,
				ByteTripleEncoding.class,
				LZ4.class,
				LZS.class,
				LZSe.class,
				LZW.class,
				LZWBasic.class,
				MTF.class,
				None.class,
				RLE.class,				
				FifoAlgo.class,
				PIEcompress.class,
				TxtDiffInc.class
				}; 
	
	//.newInstance();
	
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc);
	/*
	 * tab[0..255][0..255] list<offset> 65355 x
	 * 
	 * 
	 * dico1 256 50% dico2 65356 25% dico3 16777216 10%
	 * 
	 * 
	 * 1 Mo range; dico 16Mo 1 + 16*8+1.6*64=260Mo
	 * 
	 * 
	 */

	public List<ISymbol> encodeSymbol(List<ISymbol> ldec);

	public String getName();

	public default Long getParam() {return 0L;}

}