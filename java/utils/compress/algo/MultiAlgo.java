/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.blockSorting.BWT;
import com.zoubworld.java.utils.compress.blockSorting.MTF;

/**
 * @author Pierre Valleau
 *
 */
public class MultiAlgo implements IAlgoCompress {
	@Override
	public String getName() {
		String s="MultiAlgo(";
		
		for(IAlgoCompress e:encList)
			s+=","+e.getName();
			s+=")";
		return s;
	}
	IAlgoCompress table[] = { new RLE(), // ok
			new PIEcompress(), 
			new BytePairEncoding(), // ok
			new ByteTripleEncoding(), // ok
			new LZS(),//ok
			new LZWBasic(),
			new LZ4(),
			new None(),// ok
			new MTF(),// ok
			new BWT()// ok
			};

	/**
	 * 
	 */
	public MultiAlgo(List<IAlgoCompress> l) {
		encList = l;
	}
	public MultiAlgo(IAlgoCompress algo1,IAlgoCompress algo2) {
		encList = new ArrayList<IAlgoCompress> ();
		encList.add(algo1);
		encList.add(algo2);		
	}
	public MultiAlgo(IAlgoCompress algo1,IAlgoCompress algo2,IAlgoCompress algo3) {
		encList = new ArrayList<IAlgoCompress> ();
		encList.add(algo1);
		encList.add(algo2);		
		encList.add(algo3);		
		}
	public MultiAlgo(IAlgoCompress algo1,IAlgoCompress algo2,IAlgoCompress algo3,IAlgoCompress algo4) {
		encList = new ArrayList<IAlgoCompress> ();
		encList.add(algo1);
		encList.add(algo2);		
		encList.add(algo3);		
		encList.add(algo4);		
		}
	List<IAlgoCompress> encList;

	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		List<ISymbol> ldec = null;
		List<IAlgoCompress> decList = new ArrayList<IAlgoCompress>();
		decList.addAll(encList);
		Collections.reverse(decList);
		for (IAlgoCompress l : decList) {
			ldec = l.decodeSymbol(lenc);
			lenc = ldec;
		}
		return ldec;
	}

	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lenc = null;
		for (IAlgoCompress l : encList) {
			lenc = l.encodeSymbol(ldec);
			ldec = lenc;
		}
		return lenc;
	}

}
