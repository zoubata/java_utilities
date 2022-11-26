/**
 * 
 */
package com.zoubworld.java.utils.compress.blockSorting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.file.SymbolBloc;

/**
 * @author zoubata
 *
 */
public class DecomposeSymbol implements IAlgoCompress {

	/**
	 * 
	 */
	public DecomposeSymbol() {
		SymbolBloc bc=new SymbolBloc(null);
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#decodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#encodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ls) {
		Set<ISymbol> ss = CompositeSymbols.getCompositeSymbols(ls);
		List<ISymbol> lse=new ArrayList<ISymbol> ();
	
		for(ISymbol sref:ss)
		{
			CompositeSymbols cs=CompositeSymbols.findFirst(ls,sref);
			List<List<ISymbol>>  ln = CompositeSymbols.flatter(ls, cs);
			ls=ln.get(0);
			
		//	FLATTER:sref,ln.size(),ln.get(1).size(),ln.get(n)..ln.get(1),ln.get(0)
			
		}
		return lse;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
