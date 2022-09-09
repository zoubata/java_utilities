/**
 * 
 */
package com.zoubworld.java.utils.compress.blockSorting;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;

/**
 * @author Pierre
 * based on concept of  Move to Front :
 * https://fr.wikipedia.org/wiki/Move-to-front
 * https://en.wikipedia.org/wiki/Move-to-front_transform
 * This class didn't compress, it just change the entropie.
 */
public class MTF implements IAlgoCompress {

	/**
	 * 
	 */
	public MTF() {
		 reset();
	}
	public void reset() {
		context=new ArrayList<ISymbol>();
		for(int index=0;index<Symbol.getNbSymbol();index++)
		context.add(Symbol.findId(index));
	}
	List<ISymbol> context=null;
	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#decodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		if(lenc==null) return null;
		reset();
		List<ISymbol> ldec=new ArrayList<ISymbol>();
		for(ISymbol s:lenc)
		{
			Long sn = Symbol.getINTn(s);
			ISymbol e=s;
			if (sn==null)
				ldec.add(s);
			else
			{
				int index=sn.intValue();
				ldec.add(e= context.remove(index));
				
			}
			context.add(0, e);
		}
		return ldec;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#encodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		if(ldec==null) return null;
		reset();
		List<ISymbol> lenc=new ArrayList<ISymbol>();
		for(ISymbol s:ldec)
		{
			int index=context.indexOf(s);
			if(index>=0)
			{
				lenc.add(Symbol.FactorySymbolINT(index));			
				context.remove(index);
			}
			else
				lenc.add(s);	
			context.add(0, s);
			
		}
		return lenc;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "MTF";
	}

}
