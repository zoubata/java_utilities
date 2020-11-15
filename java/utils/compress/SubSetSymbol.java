package com.zoubworld.java.utils.compress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zoubworld.java.utils.compress.file.IBinaryReader;

/** try to optimize symbol representation.
SubSetSymbol : from an Alphabet N numbered 0 to N-1, sub select n symbol, renumber it from 0 to n-1.
Bit Stream : N[32] bit[N] as :
table of symbol present : sym=3 sym=6 present , transcode 0 and 1, bit stream is :  
	0x2;0b00010010....
	for 256 symbol : 32+256=288=36 octet
	for 300 symbol : 41.5 octets
	
This is used as the last step before converting symbol to code, with predefined number coding, see CodeNumber
no interest with Huffman/Shannon/Arithmetic, and other using probability.

	*/
class SubSetSymbol
{
	
	public SubSetSymbol(int id, IBinaryReader bin) {
		build( bin);
	}
	void build(Map<ISymbol, Long> freq)
	{
		int j=0;
		 tableSym2SymTrunc= new HashMap<ISymbol,ISymbol> ();
		for(int i=0;i<Symbol.getNbSymbol();i++)
			if ((freq.get(Symbol.findId(i))!=null) && (freq.get(Symbol.findId(i))>0))
				tableSym2SymTrunc.put(Symbol.findId(i),Symbol.findId(j++));
		//table=(ISymbol[]) l.toArray();
		
	}
	Map<ISymbol,ISymbol> buildReverted()
	{
		Map<ISymbol,ISymbol> tmp= new HashMap<ISymbol,ISymbol> ();
		for(Entry<ISymbol, ISymbol> e:tableSym2SymTrunc.entrySet())
		tmp.put(e.getValue(), e.getKey());
		return tmp;
	}
	
	void build(IBinaryReader bin)
	{
		int nb=bin.readInt();//bin.readNumber();
		tableSym2SymTrunc= new HashMap<ISymbol,ISymbol> ();
		int j=0;
		for(int i=0;i<nb;i++)
			if(bin.readBoolean())
				tableSym2SymTrunc.put(Symbol.findId(i),Symbol.findId(j++));
		//table=(ISymbol[]) l.toArray();
	}
	ICode getCode()
	{
		String s="";
		Code c=new Code(tableSym2SymTrunc.size(),32);
		for(int i=0;i<Symbol.getNbSymbol();i++)
			if(tableSym2SymTrunc.get(Symbol.findId(i))!=null)
					s+="1";
			else
				s+="0";
			return new Code(c,new Code(s));
	}
	/**
	 * normal symbol to trunked symbol
	 * */
	Map<ISymbol,ISymbol> tableSym2SymTrunc;
	
	List<ISymbol>  encode(List<ISymbol> l)
	{
		List<ISymbol> lo=new ArrayList<ISymbol> (l.size());
		for(ISymbol s:l)
			lo.add(tableSym2SymTrunc.get(s));
		return lo;
		
	}
	List<ISymbol>  decode(List<ISymbol> l)
	{
		Map<ISymbol,ISymbol> decode=buildReverted();
		List<ISymbol> lo=new ArrayList<ISymbol> (l.size());
		for(ISymbol s:l)
			lo.add(decode.get(s));
		return lo;
	}
	
}