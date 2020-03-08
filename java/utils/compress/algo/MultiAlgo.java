/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;

/**
 * @author Pierre Valleau
 *
 */
public class MultiAlgo implements IAlgoCompress{

	IAlgoCompress table[]= {
			new RLE(),//ok
			new PIEcompress(),
			new BytePairEncoding(),//ok
			new ByteTripleEncoding(),//ok
			new LZWBasic(),
			new LZ4()
	};
	/**
	 * 
	 */
	public MultiAlgo(List<IAlgoCompress> l) {
		encList=l;	
	}
	List<IAlgoCompress> encList;
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		List<ISymbol> ldec=null;
		List<IAlgoCompress> decList = new ArrayList<IAlgoCompress>();
		decList.addAll(encList);
		Collections.reverse(decList);
		for(IAlgoCompress l:decList)
		{
			ldec=l.decodeSymbol(lenc);
			lenc=ldec;
		}
		return ldec;
	}
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lenc=null;
		for(IAlgoCompress l:encList)
		{
			lenc=l.encodeSymbol(ldec);
			ldec=lenc;
		}
		return lenc;
	}

}
