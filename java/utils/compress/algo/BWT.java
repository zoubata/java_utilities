/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.bwt.CircularList;
import com.zoubworld.utils.JavaUtils;


/**
 * @author Pierre Valleau
 * implementation of Burrows–Wheeler transform
  * https://en.wikipedia.org/wiki/Burrows%E2%80%93Wheeler_transform
 * https://fr.wikipedia.org/wiki/Transform%C3%A9e_de_Burrows-Wheeler

This class didn't compress, it just change the entropie.

 */
public class BWT implements IAlgoCompress {

	/**
	 * 
	 */
	public BWT() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#decodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		CircularList<ISymbol> cl = new CircularList<ISymbol>(
				);
		ISymbol bwt = lenc.remove(0);
		ISymbol s = lenc.remove(0);
		
		 List<ISymbol> lr = cl.decode(Symbol.getINTn(s).intValue(),lenc);
		return lr;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#encodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		CircularList<ISymbol> cl = new CircularList<ISymbol>(ldec
				);
		 
		List<ISymbol> le=cl.encode(cl);
		le.add(0,Symbol.FactorySymbolINT(cl.getIndex()));
		le.add(0, Symbol.BWT);
		return le;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#getName()
	 */
	@Override
	public String getName() {
		return "BWT";
	}

}
