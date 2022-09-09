package com.zoubworld.java.utils.compress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zoubworld.java.utils.compress.file.IBinaryReader;

/** SubSetSymbol : try to optimize number representation.
 * 
SubSetSymbol : from an Alphabet N numbered 0 to N-1, sub select n symbol used,
 renumber it from 0 to n-1.
 
Bit Stream : h[16] k[16] bit[h] +j*bit[k] as :
where N<=k*h;h~N/k;k~(int)sqrt(N/n)  
this set of symbol of alphabet is split into h set of size k;
for each set with a used symbol(h[x]=1), j is incremented. bit[k]==1 represent the used symbol.

a symbol s of alphabet N, is converted to the number of 1 in "j*bit[k]" <n.

This is used as the last step before converting symbol to code, with predefined number coding, see CodeNumber
no interest with Huffman/Shannon/Arithmetic, and other using probability.

example alphabet 0..65335 where we use 1,2,3,8,256,257,258,259
n:0,1,2,3,4,5,6,7
after the number can be coded on 3 bit 0b000..0b111 instead of 16bits.
0b111->259
0b000->1
0b100->256

for bit stream coding of the conversion :
1/
k=64,h=1024;
bit stream:64,1024,1000100...0000,01110000100000...0,0111100000...0 : 
size=16+16+1024+64*2 bits=148 octets
2/
k=256,h=256;
bit stream:256,256,100...0000,01110000100000...00111100000...0 : 
size=16+16+256+256*1 bits=68 octets

3/ let's consider N=260 is equivalent to 65535 in the case.
K=8,h=9
bit stream:9,8,110...010..0000,01110000,10000000,01111000 :
size=16+16+9+3*8= ~8 octets


*/

class SubSetNumber
{
	long AlphabetSize=0;
	int k=1;
	public SubSetNumber(int id, IBinaryReader bin) {
		build( bin);
	}
	public SubSetNumber(long alphabetSize,int k)
	{
		AlphabetSize=alphabetSize;
		this.k=k;
	}
	void build(Map<Long, Long> freq)//symbol, count
	{
		long j=0;
		 tableSym2SymTrunc= new HashMap<Long,Long> ();
		for(long i=0;i<AlphabetSize;i++)
			if ((freq.get(i)!=null) && (freq.get((i))>0))
				tableSym2SymTrunc.put(i,j++);
		//table=(ISymbol[]) l.toArray();
		
	}
	Map<Long,Long> buildReverted()
	{
		Map<Long,Long> tmp= new HashMap<Long,Long> ();
		for(Entry<Long, Long> e:tableSym2SymTrunc.entrySet())
		tmp.put(e.getValue(), e.getKey());
		return tmp;
	}
	
	void build(IBinaryReader bin)
	{
		long l=bin.readLong();//bin.readNumber();
		tableSym2SymTrunc= new HashMap<Long,Long> ();
		long j=0;
		int i;
		long nn=0L;//new number
		long h=(int)(AlphabetSize/k+1);
		boolean K[]=new boolean[(int) h];
		for( i=0;i<h;i++)
			K[i]=bin.readBoolean();
		for( i=0;i<h;i++)
		  if (K[i])
			for( j=0;j<k;j++)
			if(bin.readBoolean())
				tableSym2SymTrunc.put((long) (((long)i)*h+j),nn++);
		//table=(ISymbol[]) l.toArray();
	}
	ICode getCode()
	{
		String s="";
		Code c=new Code(tableSym2SymTrunc.size(),64);
		long h=(int)(AlphabetSize/k+1);
		long j=0;
		int i;
		boolean K[]=new boolean[(int) h];
		for( i=0;i<h;i++)
			{K[i]=false;
				for( j=0;j<k;j++)
					if(tableSym2SymTrunc.get(i)!=null)
					K[i]=true;
				}	
		c=new Code(c,new Code(K));
		for( i=0;i<h;i++)
			  if (K[i])
				for( j=0;j<k;j++)
					if(tableSym2SymTrunc.get(i)!=null)
					s+="1";
			else
				s+="0";
			return new Code(c,new Code(s));
	}
	/**
	 * normal symbol to trunked symbol
	 * */
	Map<Long,Long> tableSym2SymTrunc;
	
	List<Long>  encode(List<Long> l)
	{
		List<Long> lo=new ArrayList<Long> (l.size());
		for(Long s:l)
			lo.add(tableSym2SymTrunc.get(s));
		return lo;
		
	}
	List<Long>  decode(List<Long> l)
	{
		Map<Long,Long> decode=buildReverted();
		List<Long> lo=new ArrayList<Long> (l.size());
		for(Long s:l)
			lo.add(decode.get(s));
		return lo;
	}
	
}