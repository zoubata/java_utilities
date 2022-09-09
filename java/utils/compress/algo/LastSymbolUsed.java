/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.SymbolComplex.Sym_LSn;

/**
 * @author Pierre Valleau
 * 
 *         replace a symbol to the index on a FIFO list where symbol exist just 1 time as a set.
 *
 */
public class LastSymbolUsed implements IAlgoCompress {
	

	/**
	 * 
	 */
	public LastSymbolUsed() {
	
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.algo.IalgoCompress#decodeSymbol(java.util.
	 * List)
	 */
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
	List<ISymbol> ldec = new ArrayList<ISymbol>();
		
		List<ISymbol> table = new ArrayList<ISymbol>();
		ISymbol previous = null;
		int count = 1;
		for (ISymbol e : lenc) {
			int index;
			if( e.getId()==Symbol.LSn.getId())
			{
				Sym_LSn s=(Sym_LSn)e;
				index=(int) s.getIndex();
				e=table.get(index);}
			ldec.add(e);
			
			if ((index=table.indexOf(e))>=0)
			{
			
				table.remove(index);							
			}
			else
			{
					
			}
			table.add(0,e);
			if (table.size()>=(16+64+256+4096))
				table.remove((16+64+256+4096)-1);
		}
		return ldec;
	}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.algo.IalgoCompress#encodeSymbol(java.util.
	 * List)
	 */
	@Override

	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lenc = new ArrayList<ISymbol>();
		
		List<ISymbol> table = new ArrayList<ISymbol>();
		ISymbol previous = null;
		int count = 1;
		for (ISymbol e : ldec) {
			int index;
			if ((index=table.indexOf(e))>=0)
			{
				lenc.add(new Sym_LSn(index));
				table.remove(index);
							
			}
			else
			{
				lenc.add(e);	
			}
			table.add(0,e);
			if (table.size()>=(16+64+256+4096))
				table.remove((16+64+256+4096)-1);
		}
		return lenc;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
	}

	@Override
	public String getName() {
		
		return "RLE()";
	}
}
