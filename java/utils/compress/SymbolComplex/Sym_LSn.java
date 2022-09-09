package com.zoubworld.java.utils.compress.SymbolComplex;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.Symbol;
// Symbol LSN+l[2]+n[L]
import com.zoubworld.java.utils.compress.file.IBinaryReader;

public class Sym_LSn extends CompositeSymbols {

	public Sym_LSn(int index) {
		super(Symbol.LSn);
		int l,L;
		int indexval=index;
		if (index<16)
		{
			l=0;
			L=4;
			}
		else if (index<16+64)
		{
			l=1;
			indexval-=16;
			L=6;
		}else if (index<16+64+256)
		{
			l=2;
			indexval-=16+64;
			L=8;
		}else if (index<16+64+256+4096)
		{
			l=3;
			indexval-=16+64+256;
			L=12;
		}
		else  throw new IllegalArgumentException("LS can't be created : "+index);
		this.add(new Symbol(L,new Code(l,2)));
		this.add(new Symbol(index,new Code(indexval,L)));
	
		
	}
	public Sym_LSn(IBinaryReader binaryStdIn) {
		super(Symbol.LSn);
		int l=binaryStdIn.readUnsignedInt(2);
		int L=4+l*2;
		if (l==3)
			L=12;
		this.add(new Symbol(L,new Code(l,2)));
		
		
		int index=binaryStdIn.readUnsignedInt(L);
		int codeval=index;
		if (l==1)
			index+=16;
		else
			if (l==2)
				index+=64+16;
			else
				if (l==3)
					index+=256+16+64;
				else	
		if (l==0)
			index+=0;
		else
			throw new IllegalArgumentException("LS can't be read : "+index+"+"+l);
		
		this.add(new Symbol(index,new Code(codeval,L)));
		
		
	}
	public int getIndex() {
	int index=(int) getS2().getId();
	/*
	int l=(int)getLength();
			
			if (l==1)
				index+=16;
			else
				if (l==2)
					index+=64;
				else
					if (l==3)
						index+=256;
					
			if (l==0)
				index+=0;
			else
				throw new IllegalArgumentException("LS can't be read : "+index+"+"+l);
			*/
		return index;
	}
	
	public long getLength() {
		return Symbol.getINTn(getS1());
	}
}
