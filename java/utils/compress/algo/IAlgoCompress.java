package com.zoubworld.java.utils.compress.algo;

import java.util.List;

import com.zoubworld.java.utils.compress.*;
import com.zoubworld.java.utils.compress.blockSorting.*;
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
				TxtDiffInc.class,
				TupleEncoding.class,
				TreeEncoding.class,
				FifoAlgo.class,
				HashTable.class,
				DecomposeSymbol.class
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
	/** return the name of the algo
	 * */
	public String getName();
	/** return the parameter of the algo
	 * */
	public default Long getParam() {return 0L;}

	/** build internal optimization before the process of the stream
	 * */
	public default void init(List<ISymbol> ls) {}
	/** reset internal optimization after the process of the stream and before a new process
	 * */
	public default void reset() {}
	
	
}