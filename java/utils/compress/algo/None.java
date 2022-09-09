/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;

/**
 * @author Pierre
 * 
 *class that do nothing
 */
public class None implements IAlgoCompress {

	/**
	 * 
	 */
	public None() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#decodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		List<ISymbol> ldec=new ArrayList<ISymbol> ();
		ldec.addAll(lenc);
		return ldec;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#encodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lse=new ArrayList<ISymbol> ();
		lse.addAll(ldec);
		return lse;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#getName()
	 */
	@Override
	public String getName() {
		return "None";
	}

}
