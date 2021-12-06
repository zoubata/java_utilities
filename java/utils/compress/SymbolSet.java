package com.zoubworld.java.utils.compress;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zoubworld.utils.JavaUtils;

public class SymbolSet {

		//default alphabet is the list of Symbol
	public SymbolSet() {		
		List<ISymbol> alphabet=new ArrayList<ISymbol> ();
		for(long l=0;l<Symbol.getNbSymbol();l++)
			
			alphabet.add(Symbol.findId((int)l));
	}
	public SymbolSet(long begin,long end) {		
		List<ISymbol> alphabet=new ArrayList<ISymbol> ();
		for(long l=begin;l<end;l++)
			
			alphabet.add(new Number(l));
	}
	public SymbolSet(long begin,long end, BigInteger mask) {		
		List<ISymbol> alphabet=new ArrayList<ISymbol> ();
		for(long l=begin;l<end;l++)
			if (mask==null || mask.shiftLeft((int)l).and(BigInteger.ONE).equals(BigInteger.ONE))
			alphabet.add(new Number(l));
	}
	public SymbolSet(Set<ISymbol> set) {		
		alphabet=JavaUtils.asSortedSet(set,ISymbol.ComparatorbyId);
}
	public SymbolSet(List<ISymbol> list) {		
		Set<ISymbol> set=new HashSet<ISymbol> ();
		set.addAll(list);
		alphabet=JavaUtils.asSortedSet(set,ISymbol.ComparatorbyId);
}
	List<ISymbol> alphabet=null;
	public List<ISymbol> getAlphabet()
		{
			return alphabet;
		}
	public ISymbol getSymbol()
	{
		return new CompositeSymbols(Symbol.Alphabet, 
				getAlphabet());
	}

}
